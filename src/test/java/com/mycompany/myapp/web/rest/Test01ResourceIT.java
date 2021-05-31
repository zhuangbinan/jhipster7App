package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Test01;
import com.mycompany.myapp.repository.Test01Repository;
import com.mycompany.myapp.service.criteria.Test01Criteria;
import com.mycompany.myapp.service.dto.Test01DTO;
import com.mycompany.myapp.service.mapper.Test01Mapper;
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
 * Integration tests for the {@link Test01Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Test01ResourceIT {

    private static final String DEFAULT_JOB_CAREER_DESC = "AAAAAAAAAA";
    private static final String UPDATED_JOB_CAREER_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/test-01-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Test01Repository test01Repository;

    @Autowired
    private Test01Mapper test01Mapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTest01MockMvc;

    private Test01 test01;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Test01 createEntity(EntityManager em) {
        Test01 test01 = new Test01().jobCareerDesc(DEFAULT_JOB_CAREER_DESC);
        return test01;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Test01 createUpdatedEntity(EntityManager em) {
        Test01 test01 = new Test01().jobCareerDesc(UPDATED_JOB_CAREER_DESC);
        return test01;
    }

    @BeforeEach
    public void initTest() {
        test01 = createEntity(em);
    }

    @Test
    @Transactional
    void createTest01() throws Exception {
        int databaseSizeBeforeCreate = test01Repository.findAll().size();
        // Create the Test01
        Test01DTO test01DTO = test01Mapper.toDto(test01);
        restTest01MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(test01DTO)))
            .andExpect(status().isCreated());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeCreate + 1);
        Test01 testTest01 = test01List.get(test01List.size() - 1);
        assertThat(testTest01.getJobCareerDesc()).isEqualTo(DEFAULT_JOB_CAREER_DESC);
    }

    @Test
    @Transactional
    void createTest01WithExistingId() throws Exception {
        // Create the Test01 with an existing ID
        test01.setId(1L);
        Test01DTO test01DTO = test01Mapper.toDto(test01);

        int databaseSizeBeforeCreate = test01Repository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTest01MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(test01DTO)))
            .andExpect(status().isBadRequest());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTest01s() throws Exception {
        // Initialize the database
        test01Repository.saveAndFlush(test01);

        // Get all the test01List
        restTest01MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(test01.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobCareerDesc").value(hasItem(DEFAULT_JOB_CAREER_DESC.toString())));
    }

    @Test
    @Transactional
    void getTest01() throws Exception {
        // Initialize the database
        test01Repository.saveAndFlush(test01);

        // Get the test01
        restTest01MockMvc
            .perform(get(ENTITY_API_URL_ID, test01.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(test01.getId().intValue()))
            .andExpect(jsonPath("$.jobCareerDesc").value(DEFAULT_JOB_CAREER_DESC.toString()));
    }

    @Test
    @Transactional
    void getTest01sByIdFiltering() throws Exception {
        // Initialize the database
        test01Repository.saveAndFlush(test01);

        Long id = test01.getId();

        defaultTest01ShouldBeFound("id.equals=" + id);
        defaultTest01ShouldNotBeFound("id.notEquals=" + id);

        defaultTest01ShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTest01ShouldNotBeFound("id.greaterThan=" + id);

        defaultTest01ShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTest01ShouldNotBeFound("id.lessThan=" + id);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTest01ShouldBeFound(String filter) throws Exception {
        restTest01MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(test01.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobCareerDesc").value(hasItem(DEFAULT_JOB_CAREER_DESC.toString())));

        // Check, that the count call also returns 1
        restTest01MockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTest01ShouldNotBeFound(String filter) throws Exception {
        restTest01MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTest01MockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTest01() throws Exception {
        // Get the test01
        restTest01MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTest01() throws Exception {
        // Initialize the database
        test01Repository.saveAndFlush(test01);

        int databaseSizeBeforeUpdate = test01Repository.findAll().size();

        // Update the test01
        Test01 updatedTest01 = test01Repository.findById(test01.getId()).get();
        // Disconnect from session so that the updates on updatedTest01 are not directly saved in db
        em.detach(updatedTest01);
        updatedTest01.jobCareerDesc(UPDATED_JOB_CAREER_DESC);
        Test01DTO test01DTO = test01Mapper.toDto(updatedTest01);

        restTest01MockMvc
            .perform(
                put(ENTITY_API_URL_ID, test01DTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(test01DTO))
            )
            .andExpect(status().isOk());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
        Test01 testTest01 = test01List.get(test01List.size() - 1);
        assertThat(testTest01.getJobCareerDesc()).isEqualTo(UPDATED_JOB_CAREER_DESC);
    }

    @Test
    @Transactional
    void putNonExistingTest01() throws Exception {
        int databaseSizeBeforeUpdate = test01Repository.findAll().size();
        test01.setId(count.incrementAndGet());

        // Create the Test01
        Test01DTO test01DTO = test01Mapper.toDto(test01);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTest01MockMvc
            .perform(
                put(ENTITY_API_URL_ID, test01DTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(test01DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTest01() throws Exception {
        int databaseSizeBeforeUpdate = test01Repository.findAll().size();
        test01.setId(count.incrementAndGet());

        // Create the Test01
        Test01DTO test01DTO = test01Mapper.toDto(test01);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTest01MockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(test01DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTest01() throws Exception {
        int databaseSizeBeforeUpdate = test01Repository.findAll().size();
        test01.setId(count.incrementAndGet());

        // Create the Test01
        Test01DTO test01DTO = test01Mapper.toDto(test01);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTest01MockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(test01DTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTest01WithPatch() throws Exception {
        // Initialize the database
        test01Repository.saveAndFlush(test01);

        int databaseSizeBeforeUpdate = test01Repository.findAll().size();

        // Update the test01 using partial update
        Test01 partialUpdatedTest01 = new Test01();
        partialUpdatedTest01.setId(test01.getId());

        partialUpdatedTest01.jobCareerDesc(UPDATED_JOB_CAREER_DESC);

        restTest01MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTest01.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTest01))
            )
            .andExpect(status().isOk());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
        Test01 testTest01 = test01List.get(test01List.size() - 1);
        assertThat(testTest01.getJobCareerDesc()).isEqualTo(UPDATED_JOB_CAREER_DESC);
    }

    @Test
    @Transactional
    void fullUpdateTest01WithPatch() throws Exception {
        // Initialize the database
        test01Repository.saveAndFlush(test01);

        int databaseSizeBeforeUpdate = test01Repository.findAll().size();

        // Update the test01 using partial update
        Test01 partialUpdatedTest01 = new Test01();
        partialUpdatedTest01.setId(test01.getId());

        partialUpdatedTest01.jobCareerDesc(UPDATED_JOB_CAREER_DESC);

        restTest01MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTest01.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTest01))
            )
            .andExpect(status().isOk());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
        Test01 testTest01 = test01List.get(test01List.size() - 1);
        assertThat(testTest01.getJobCareerDesc()).isEqualTo(UPDATED_JOB_CAREER_DESC);
    }

    @Test
    @Transactional
    void patchNonExistingTest01() throws Exception {
        int databaseSizeBeforeUpdate = test01Repository.findAll().size();
        test01.setId(count.incrementAndGet());

        // Create the Test01
        Test01DTO test01DTO = test01Mapper.toDto(test01);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTest01MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, test01DTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(test01DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTest01() throws Exception {
        int databaseSizeBeforeUpdate = test01Repository.findAll().size();
        test01.setId(count.incrementAndGet());

        // Create the Test01
        Test01DTO test01DTO = test01Mapper.toDto(test01);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTest01MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(test01DTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTest01() throws Exception {
        int databaseSizeBeforeUpdate = test01Repository.findAll().size();
        test01.setId(count.incrementAndGet());

        // Create the Test01
        Test01DTO test01DTO = test01Mapper.toDto(test01);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTest01MockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(test01DTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Test01 in the database
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTest01() throws Exception {
        // Initialize the database
        test01Repository.saveAndFlush(test01);

        int databaseSizeBeforeDelete = test01Repository.findAll().size();

        // Delete the test01
        restTest01MockMvc
            .perform(delete(ENTITY_API_URL_ID, test01.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Test01> test01List = test01Repository.findAll();
        assertThat(test01List).hasSize(databaseSizeBeforeDelete - 1);
    }
}
