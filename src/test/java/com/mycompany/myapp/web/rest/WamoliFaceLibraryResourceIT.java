package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WamoliFaceLibrary;
import com.mycompany.myapp.repository.WamoliFaceLibraryRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link WamoliFaceLibraryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WamoliFaceLibraryResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wamoli-face-libraries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WamoliFaceLibraryRepository wamoliFaceLibraryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWamoliFaceLibraryMockMvc;

    private WamoliFaceLibrary wamoliFaceLibrary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WamoliFaceLibrary createEntity(EntityManager em) {
        WamoliFaceLibrary wamoliFaceLibrary = new WamoliFaceLibrary().content(DEFAULT_CONTENT);
        return wamoliFaceLibrary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WamoliFaceLibrary createUpdatedEntity(EntityManager em) {
        WamoliFaceLibrary wamoliFaceLibrary = new WamoliFaceLibrary().content(UPDATED_CONTENT);
        return wamoliFaceLibrary;
    }

    @BeforeEach
    public void initTest() {
        wamoliFaceLibrary = createEntity(em);
    }

    @Test
    @Transactional
    void createWamoliFaceLibrary() throws Exception {
        int databaseSizeBeforeCreate = wamoliFaceLibraryRepository.findAll().size();
        // Create the WamoliFaceLibrary
        restWamoliFaceLibraryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliFaceLibrary))
            )
            .andExpect(status().isCreated());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeCreate + 1);
        WamoliFaceLibrary testWamoliFaceLibrary = wamoliFaceLibraryList.get(wamoliFaceLibraryList.size() - 1);
        assertThat(testWamoliFaceLibrary.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void createWamoliFaceLibraryWithExistingId() throws Exception {
        // Create the WamoliFaceLibrary with an existing ID
        wamoliFaceLibrary.setId(1L);

        int databaseSizeBeforeCreate = wamoliFaceLibraryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWamoliFaceLibraryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliFaceLibrary))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWamoliFaceLibraries() throws Exception {
        // Initialize the database
        wamoliFaceLibraryRepository.saveAndFlush(wamoliFaceLibrary);

        // Get all the wamoliFaceLibraryList
        restWamoliFaceLibraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wamoliFaceLibrary.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    void getWamoliFaceLibrary() throws Exception {
        // Initialize the database
        wamoliFaceLibraryRepository.saveAndFlush(wamoliFaceLibrary);

        // Get the wamoliFaceLibrary
        restWamoliFaceLibraryMockMvc
            .perform(get(ENTITY_API_URL_ID, wamoliFaceLibrary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wamoliFaceLibrary.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWamoliFaceLibrary() throws Exception {
        // Get the wamoliFaceLibrary
        restWamoliFaceLibraryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWamoliFaceLibrary() throws Exception {
        // Initialize the database
        wamoliFaceLibraryRepository.saveAndFlush(wamoliFaceLibrary);

        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();

        // Update the wamoliFaceLibrary
        WamoliFaceLibrary updatedWamoliFaceLibrary = wamoliFaceLibraryRepository.findById(wamoliFaceLibrary.getId()).get();
        // Disconnect from session so that the updates on updatedWamoliFaceLibrary are not directly saved in db
        em.detach(updatedWamoliFaceLibrary);
        updatedWamoliFaceLibrary.content(UPDATED_CONTENT);

        restWamoliFaceLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWamoliFaceLibrary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWamoliFaceLibrary))
            )
            .andExpect(status().isOk());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
        WamoliFaceLibrary testWamoliFaceLibrary = wamoliFaceLibraryList.get(wamoliFaceLibraryList.size() - 1);
        assertThat(testWamoliFaceLibrary.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingWamoliFaceLibrary() throws Exception {
        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();
        wamoliFaceLibrary.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWamoliFaceLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wamoliFaceLibrary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wamoliFaceLibrary))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWamoliFaceLibrary() throws Exception {
        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();
        wamoliFaceLibrary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliFaceLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wamoliFaceLibrary))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWamoliFaceLibrary() throws Exception {
        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();
        wamoliFaceLibrary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliFaceLibraryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliFaceLibrary))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWamoliFaceLibraryWithPatch() throws Exception {
        // Initialize the database
        wamoliFaceLibraryRepository.saveAndFlush(wamoliFaceLibrary);

        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();

        // Update the wamoliFaceLibrary using partial update
        WamoliFaceLibrary partialUpdatedWamoliFaceLibrary = new WamoliFaceLibrary();
        partialUpdatedWamoliFaceLibrary.setId(wamoliFaceLibrary.getId());

        partialUpdatedWamoliFaceLibrary.content(UPDATED_CONTENT);

        restWamoliFaceLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWamoliFaceLibrary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWamoliFaceLibrary))
            )
            .andExpect(status().isOk());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
        WamoliFaceLibrary testWamoliFaceLibrary = wamoliFaceLibraryList.get(wamoliFaceLibraryList.size() - 1);
        assertThat(testWamoliFaceLibrary.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateWamoliFaceLibraryWithPatch() throws Exception {
        // Initialize the database
        wamoliFaceLibraryRepository.saveAndFlush(wamoliFaceLibrary);

        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();

        // Update the wamoliFaceLibrary using partial update
        WamoliFaceLibrary partialUpdatedWamoliFaceLibrary = new WamoliFaceLibrary();
        partialUpdatedWamoliFaceLibrary.setId(wamoliFaceLibrary.getId());

        partialUpdatedWamoliFaceLibrary.content(UPDATED_CONTENT);

        restWamoliFaceLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWamoliFaceLibrary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWamoliFaceLibrary))
            )
            .andExpect(status().isOk());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
        WamoliFaceLibrary testWamoliFaceLibrary = wamoliFaceLibraryList.get(wamoliFaceLibraryList.size() - 1);
        assertThat(testWamoliFaceLibrary.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingWamoliFaceLibrary() throws Exception {
        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();
        wamoliFaceLibrary.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWamoliFaceLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wamoliFaceLibrary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wamoliFaceLibrary))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWamoliFaceLibrary() throws Exception {
        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();
        wamoliFaceLibrary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliFaceLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wamoliFaceLibrary))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWamoliFaceLibrary() throws Exception {
        int databaseSizeBeforeUpdate = wamoliFaceLibraryRepository.findAll().size();
        wamoliFaceLibrary.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliFaceLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wamoliFaceLibrary))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WamoliFaceLibrary in the database
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWamoliFaceLibrary() throws Exception {
        // Initialize the database
        wamoliFaceLibraryRepository.saveAndFlush(wamoliFaceLibrary);

        int databaseSizeBeforeDelete = wamoliFaceLibraryRepository.findAll().size();

        // Delete the wamoliFaceLibrary
        restWamoliFaceLibraryMockMvc
            .perform(delete(ENTITY_API_URL_ID, wamoliFaceLibrary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WamoliFaceLibrary> wamoliFaceLibraryList = wamoliFaceLibraryRepository.findAll();
        assertThat(wamoliFaceLibraryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
