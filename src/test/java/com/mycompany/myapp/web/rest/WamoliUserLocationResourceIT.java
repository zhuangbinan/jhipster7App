package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.WamoliUserLocation;
import com.mycompany.myapp.repository.WamoliUserLocationRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

/**
 * Integration tests for the {@link WamoliUserLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WamoliUserLocationResourceIT {

    private static final Integer DEFAULT_STATE = 1;
    private static final Integer UPDATED_STATE = 2;

    private static final String DEFAULT_CARD_NUM = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUM = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DELAY_TIME = 1;
    private static final Integer UPDATED_DELAY_TIME = 2;

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/wamoli-user-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WamoliUserLocationRepository wamoliUserLocationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWamoliUserLocationMockMvc;

    private WamoliUserLocation wamoliUserLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WamoliUserLocation createEntity(EntityManager em) {
        WamoliUserLocation wamoliUserLocation = new WamoliUserLocation()
            .state(DEFAULT_STATE)
            .cardNum(DEFAULT_CARD_NUM)
            .expireTime(DEFAULT_EXPIRE_TIME)
            .delayTime(DEFAULT_DELAY_TIME)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return wamoliUserLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WamoliUserLocation createUpdatedEntity(EntityManager em) {
        WamoliUserLocation wamoliUserLocation = new WamoliUserLocation()
            .state(UPDATED_STATE)
            .cardNum(UPDATED_CARD_NUM)
            .expireTime(UPDATED_EXPIRE_TIME)
            .delayTime(UPDATED_DELAY_TIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return wamoliUserLocation;
    }

    @BeforeEach
    public void initTest() {
        wamoliUserLocation = createEntity(em);
    }

    @Test
    @Transactional
    void createWamoliUserLocation() throws Exception {
        int databaseSizeBeforeCreate = wamoliUserLocationRepository.findAll().size();
        // Create the WamoliUserLocation
        restWamoliUserLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isCreated());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeCreate + 1);
        WamoliUserLocation testWamoliUserLocation = wamoliUserLocationList.get(wamoliUserLocationList.size() - 1);
        assertThat(testWamoliUserLocation.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testWamoliUserLocation.getCardNum()).isEqualTo(DEFAULT_CARD_NUM);
        assertThat(testWamoliUserLocation.getExpireTime()).isEqualTo(DEFAULT_EXPIRE_TIME);
        assertThat(testWamoliUserLocation.getDelayTime()).isEqualTo(DEFAULT_DELAY_TIME);
        assertThat(testWamoliUserLocation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testWamoliUserLocation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createWamoliUserLocationWithExistingId() throws Exception {
        // Create the WamoliUserLocation with an existing ID
        wamoliUserLocation.setId(1L);

        int databaseSizeBeforeCreate = wamoliUserLocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWamoliUserLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = wamoliUserLocationRepository.findAll().size();
        // set the field null
        wamoliUserLocation.setState(null);

        // Create the WamoliUserLocation, which fails.

        restWamoliUserLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isBadRequest());

        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = wamoliUserLocationRepository.findAll().size();
        // set the field null
        wamoliUserLocation.setCardNum(null);

        // Create the WamoliUserLocation, which fails.

        restWamoliUserLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isBadRequest());

        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWamoliUserLocations() throws Exception {
        // Initialize the database
        wamoliUserLocationRepository.saveAndFlush(wamoliUserLocation);

        // Get all the wamoliUserLocationList
        restWamoliUserLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wamoliUserLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].cardNum").value(hasItem(DEFAULT_CARD_NUM)))
            .andExpect(jsonPath("$.[*].expireTime").value(hasItem(DEFAULT_EXPIRE_TIME.toString())))
            .andExpect(jsonPath("$.[*].delayTime").value(hasItem(DEFAULT_DELAY_TIME)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getWamoliUserLocation() throws Exception {
        // Initialize the database
        wamoliUserLocationRepository.saveAndFlush(wamoliUserLocation);

        // Get the wamoliUserLocation
        restWamoliUserLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, wamoliUserLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wamoliUserLocation.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.cardNum").value(DEFAULT_CARD_NUM))
            .andExpect(jsonPath("$.expireTime").value(DEFAULT_EXPIRE_TIME.toString()))
            .andExpect(jsonPath("$.delayTime").value(DEFAULT_DELAY_TIME))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWamoliUserLocation() throws Exception {
        // Get the wamoliUserLocation
        restWamoliUserLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWamoliUserLocation() throws Exception {
        // Initialize the database
        wamoliUserLocationRepository.saveAndFlush(wamoliUserLocation);

        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();

        // Update the wamoliUserLocation
        WamoliUserLocation updatedWamoliUserLocation = wamoliUserLocationRepository.findById(wamoliUserLocation.getId()).get();
        // Disconnect from session so that the updates on updatedWamoliUserLocation are not directly saved in db
        em.detach(updatedWamoliUserLocation);
        updatedWamoliUserLocation
            .state(UPDATED_STATE)
            .cardNum(UPDATED_CARD_NUM)
            .expireTime(UPDATED_EXPIRE_TIME)
            .delayTime(UPDATED_DELAY_TIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWamoliUserLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWamoliUserLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWamoliUserLocation))
            )
            .andExpect(status().isOk());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
        WamoliUserLocation testWamoliUserLocation = wamoliUserLocationList.get(wamoliUserLocationList.size() - 1);
        assertThat(testWamoliUserLocation.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testWamoliUserLocation.getCardNum()).isEqualTo(UPDATED_CARD_NUM);
        assertThat(testWamoliUserLocation.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testWamoliUserLocation.getDelayTime()).isEqualTo(UPDATED_DELAY_TIME);
        assertThat(testWamoliUserLocation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWamoliUserLocation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingWamoliUserLocation() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();
        wamoliUserLocation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWamoliUserLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wamoliUserLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWamoliUserLocation() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();
        wamoliUserLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliUserLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWamoliUserLocation() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();
        wamoliUserLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliUserLocationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWamoliUserLocationWithPatch() throws Exception {
        // Initialize the database
        wamoliUserLocationRepository.saveAndFlush(wamoliUserLocation);

        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();

        // Update the wamoliUserLocation using partial update
        WamoliUserLocation partialUpdatedWamoliUserLocation = new WamoliUserLocation();
        partialUpdatedWamoliUserLocation.setId(wamoliUserLocation.getId());

        partialUpdatedWamoliUserLocation
            .state(UPDATED_STATE)
            .expireTime(UPDATED_EXPIRE_TIME)
            .delayTime(UPDATED_DELAY_TIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWamoliUserLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWamoliUserLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWamoliUserLocation))
            )
            .andExpect(status().isOk());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
        WamoliUserLocation testWamoliUserLocation = wamoliUserLocationList.get(wamoliUserLocationList.size() - 1);
        assertThat(testWamoliUserLocation.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testWamoliUserLocation.getCardNum()).isEqualTo(DEFAULT_CARD_NUM);
        assertThat(testWamoliUserLocation.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testWamoliUserLocation.getDelayTime()).isEqualTo(UPDATED_DELAY_TIME);
        assertThat(testWamoliUserLocation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWamoliUserLocation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateWamoliUserLocationWithPatch() throws Exception {
        // Initialize the database
        wamoliUserLocationRepository.saveAndFlush(wamoliUserLocation);

        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();

        // Update the wamoliUserLocation using partial update
        WamoliUserLocation partialUpdatedWamoliUserLocation = new WamoliUserLocation();
        partialUpdatedWamoliUserLocation.setId(wamoliUserLocation.getId());

        partialUpdatedWamoliUserLocation
            .state(UPDATED_STATE)
            .cardNum(UPDATED_CARD_NUM)
            .expireTime(UPDATED_EXPIRE_TIME)
            .delayTime(UPDATED_DELAY_TIME)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWamoliUserLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWamoliUserLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWamoliUserLocation))
            )
            .andExpect(status().isOk());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
        WamoliUserLocation testWamoliUserLocation = wamoliUserLocationList.get(wamoliUserLocationList.size() - 1);
        assertThat(testWamoliUserLocation.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testWamoliUserLocation.getCardNum()).isEqualTo(UPDATED_CARD_NUM);
        assertThat(testWamoliUserLocation.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testWamoliUserLocation.getDelayTime()).isEqualTo(UPDATED_DELAY_TIME);
        assertThat(testWamoliUserLocation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWamoliUserLocation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingWamoliUserLocation() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();
        wamoliUserLocation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWamoliUserLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wamoliUserLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWamoliUserLocation() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();
        wamoliUserLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliUserLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWamoliUserLocation() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserLocationRepository.findAll().size();
        wamoliUserLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliUserLocationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUserLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WamoliUserLocation in the database
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWamoliUserLocation() throws Exception {
        // Initialize the database
        wamoliUserLocationRepository.saveAndFlush(wamoliUserLocation);

        int databaseSizeBeforeDelete = wamoliUserLocationRepository.findAll().size();

        // Delete the wamoliUserLocation
        restWamoliUserLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, wamoliUserLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WamoliUserLocation> wamoliUserLocationList = wamoliUserLocationRepository.findAll();
        assertThat(wamoliUserLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
