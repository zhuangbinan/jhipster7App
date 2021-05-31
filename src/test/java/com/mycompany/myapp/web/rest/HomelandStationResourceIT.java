package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Buildings;
import com.mycompany.myapp.domain.Community;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.HomelandStation;
import com.mycompany.myapp.repository.HomelandStationRepository;
import com.mycompany.myapp.service.criteria.HomelandStationCriteria;
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
 * Integration tests for the {@link HomelandStationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HomelandStationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LONG_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_LIVING_POPULATION = 1;
    private static final Integer UPDATED_LIVING_POPULATION = 2;
    private static final Integer SMALLER_LIVING_POPULATION = 1 - 1;

    private static final Integer DEFAULT_BUILDING_COUNT = 1;
    private static final Integer UPDATED_BUILDING_COUNT = 2;
    private static final Integer SMALLER_BUILDING_COUNT = 1 - 1;

    private static final Integer DEFAULT_ENTRANCE_COUNT = 1;
    private static final Integer UPDATED_ENTRANCE_COUNT = 2;
    private static final Integer SMALLER_ENTRANCE_COUNT = 1 - 1;

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/homeland-stations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HomelandStationRepository homelandStationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHomelandStationMockMvc;

    private HomelandStation homelandStation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomelandStation createEntity(EntityManager em) {
        HomelandStation homelandStation = new HomelandStation()
            .name(DEFAULT_NAME)
            .longCode(DEFAULT_LONG_CODE)
            .address(DEFAULT_ADDRESS)
            .livingPopulation(DEFAULT_LIVING_POPULATION)
            .buildingCount(DEFAULT_BUILDING_COUNT)
            .entranceCount(DEFAULT_ENTRANCE_COUNT)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE);
        return homelandStation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HomelandStation createUpdatedEntity(EntityManager em) {
        HomelandStation homelandStation = new HomelandStation()
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .address(UPDATED_ADDRESS)
            .livingPopulation(UPDATED_LIVING_POPULATION)
            .buildingCount(UPDATED_BUILDING_COUNT)
            .entranceCount(UPDATED_ENTRANCE_COUNT)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);
        return homelandStation;
    }

    @BeforeEach
    public void initTest() {
        homelandStation = createEntity(em);
    }

    @Test
    @Transactional
    void createHomelandStation() throws Exception {
        int databaseSizeBeforeCreate = homelandStationRepository.findAll().size();
        // Create the HomelandStation
        restHomelandStationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isCreated());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeCreate + 1);
        HomelandStation testHomelandStation = homelandStationList.get(homelandStationList.size() - 1);
        assertThat(testHomelandStation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHomelandStation.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
        assertThat(testHomelandStation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testHomelandStation.getLivingPopulation()).isEqualTo(DEFAULT_LIVING_POPULATION);
        assertThat(testHomelandStation.getBuildingCount()).isEqualTo(DEFAULT_BUILDING_COUNT);
        assertThat(testHomelandStation.getEntranceCount()).isEqualTo(DEFAULT_ENTRANCE_COUNT);
        assertThat(testHomelandStation.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testHomelandStation.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testHomelandStation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testHomelandStation.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void createHomelandStationWithExistingId() throws Exception {
        // Create the HomelandStation with an existing ID
        homelandStation.setId(1L);

        int databaseSizeBeforeCreate = homelandStationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHomelandStationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelandStationRepository.findAll().size();
        // set the field null
        homelandStation.setName(null);

        // Create the HomelandStation, which fails.

        restHomelandStationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelandStationRepository.findAll().size();
        // set the field null
        homelandStation.setLongCode(null);

        // Create the HomelandStation, which fails.

        restHomelandStationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelandStationRepository.findAll().size();
        // set the field null
        homelandStation.setLongitude(null);

        // Create the HomelandStation, which fails.

        restHomelandStationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = homelandStationRepository.findAll().size();
        // set the field null
        homelandStation.setLatitude(null);

        // Create the HomelandStation, which fails.

        restHomelandStationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHomelandStations() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList
        restHomelandStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homelandStation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].livingPopulation").value(hasItem(DEFAULT_LIVING_POPULATION)))
            .andExpect(jsonPath("$.[*].buildingCount").value(hasItem(DEFAULT_BUILDING_COUNT)))
            .andExpect(jsonPath("$.[*].entranceCount").value(hasItem(DEFAULT_ENTRANCE_COUNT)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)));
    }

    @Test
    @Transactional
    void getHomelandStation() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get the homelandStation
        restHomelandStationMockMvc
            .perform(get(ENTITY_API_URL_ID, homelandStation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(homelandStation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.longCode").value(DEFAULT_LONG_CODE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.livingPopulation").value(DEFAULT_LIVING_POPULATION))
            .andExpect(jsonPath("$.buildingCount").value(DEFAULT_BUILDING_COUNT))
            .andExpect(jsonPath("$.entranceCount").value(DEFAULT_ENTRANCE_COUNT))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE));
    }

    @Test
    @Transactional
    void getHomelandStationsByIdFiltering() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        Long id = homelandStation.getId();

        defaultHomelandStationShouldBeFound("id.equals=" + id);
        defaultHomelandStationShouldNotBeFound("id.notEquals=" + id);

        defaultHomelandStationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHomelandStationShouldNotBeFound("id.greaterThan=" + id);

        defaultHomelandStationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHomelandStationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where name equals to DEFAULT_NAME
        defaultHomelandStationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the homelandStationList where name equals to UPDATED_NAME
        defaultHomelandStationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where name not equals to DEFAULT_NAME
        defaultHomelandStationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the homelandStationList where name not equals to UPDATED_NAME
        defaultHomelandStationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHomelandStationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the homelandStationList where name equals to UPDATED_NAME
        defaultHomelandStationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where name is not null
        defaultHomelandStationShouldBeFound("name.specified=true");

        // Get all the homelandStationList where name is null
        defaultHomelandStationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllHomelandStationsByNameContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where name contains DEFAULT_NAME
        defaultHomelandStationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the homelandStationList where name contains UPDATED_NAME
        defaultHomelandStationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where name does not contain DEFAULT_NAME
        defaultHomelandStationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the homelandStationList where name does not contain UPDATED_NAME
        defaultHomelandStationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longCode equals to DEFAULT_LONG_CODE
        defaultHomelandStationShouldBeFound("longCode.equals=" + DEFAULT_LONG_CODE);

        // Get all the homelandStationList where longCode equals to UPDATED_LONG_CODE
        defaultHomelandStationShouldNotBeFound("longCode.equals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longCode not equals to DEFAULT_LONG_CODE
        defaultHomelandStationShouldNotBeFound("longCode.notEquals=" + DEFAULT_LONG_CODE);

        // Get all the homelandStationList where longCode not equals to UPDATED_LONG_CODE
        defaultHomelandStationShouldBeFound("longCode.notEquals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongCodeIsInShouldWork() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longCode in DEFAULT_LONG_CODE or UPDATED_LONG_CODE
        defaultHomelandStationShouldBeFound("longCode.in=" + DEFAULT_LONG_CODE + "," + UPDATED_LONG_CODE);

        // Get all the homelandStationList where longCode equals to UPDATED_LONG_CODE
        defaultHomelandStationShouldNotBeFound("longCode.in=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longCode is not null
        defaultHomelandStationShouldBeFound("longCode.specified=true");

        // Get all the homelandStationList where longCode is null
        defaultHomelandStationShouldNotBeFound("longCode.specified=false");
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongCodeContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longCode contains DEFAULT_LONG_CODE
        defaultHomelandStationShouldBeFound("longCode.contains=" + DEFAULT_LONG_CODE);

        // Get all the homelandStationList where longCode contains UPDATED_LONG_CODE
        defaultHomelandStationShouldNotBeFound("longCode.contains=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongCodeNotContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longCode does not contain DEFAULT_LONG_CODE
        defaultHomelandStationShouldNotBeFound("longCode.doesNotContain=" + DEFAULT_LONG_CODE);

        // Get all the homelandStationList where longCode does not contain UPDATED_LONG_CODE
        defaultHomelandStationShouldBeFound("longCode.doesNotContain=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where address equals to DEFAULT_ADDRESS
        defaultHomelandStationShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the homelandStationList where address equals to UPDATED_ADDRESS
        defaultHomelandStationShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where address not equals to DEFAULT_ADDRESS
        defaultHomelandStationShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the homelandStationList where address not equals to UPDATED_ADDRESS
        defaultHomelandStationShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultHomelandStationShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the homelandStationList where address equals to UPDATED_ADDRESS
        defaultHomelandStationShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where address is not null
        defaultHomelandStationShouldBeFound("address.specified=true");

        // Get all the homelandStationList where address is null
        defaultHomelandStationShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllHomelandStationsByAddressContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where address contains DEFAULT_ADDRESS
        defaultHomelandStationShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the homelandStationList where address contains UPDATED_ADDRESS
        defaultHomelandStationShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where address does not contain DEFAULT_ADDRESS
        defaultHomelandStationShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the homelandStationList where address does not contain UPDATED_ADDRESS
        defaultHomelandStationShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLivingPopulationIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where livingPopulation equals to DEFAULT_LIVING_POPULATION
        defaultHomelandStationShouldBeFound("livingPopulation.equals=" + DEFAULT_LIVING_POPULATION);

        // Get all the homelandStationList where livingPopulation equals to UPDATED_LIVING_POPULATION
        defaultHomelandStationShouldNotBeFound("livingPopulation.equals=" + UPDATED_LIVING_POPULATION);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLivingPopulationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where livingPopulation not equals to DEFAULT_LIVING_POPULATION
        defaultHomelandStationShouldNotBeFound("livingPopulation.notEquals=" + DEFAULT_LIVING_POPULATION);

        // Get all the homelandStationList where livingPopulation not equals to UPDATED_LIVING_POPULATION
        defaultHomelandStationShouldBeFound("livingPopulation.notEquals=" + UPDATED_LIVING_POPULATION);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLivingPopulationIsInShouldWork() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where livingPopulation in DEFAULT_LIVING_POPULATION or UPDATED_LIVING_POPULATION
        defaultHomelandStationShouldBeFound("livingPopulation.in=" + DEFAULT_LIVING_POPULATION + "," + UPDATED_LIVING_POPULATION);

        // Get all the homelandStationList where livingPopulation equals to UPDATED_LIVING_POPULATION
        defaultHomelandStationShouldNotBeFound("livingPopulation.in=" + UPDATED_LIVING_POPULATION);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLivingPopulationIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where livingPopulation is not null
        defaultHomelandStationShouldBeFound("livingPopulation.specified=true");

        // Get all the homelandStationList where livingPopulation is null
        defaultHomelandStationShouldNotBeFound("livingPopulation.specified=false");
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLivingPopulationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where livingPopulation is greater than or equal to DEFAULT_LIVING_POPULATION
        defaultHomelandStationShouldBeFound("livingPopulation.greaterThanOrEqual=" + DEFAULT_LIVING_POPULATION);

        // Get all the homelandStationList where livingPopulation is greater than or equal to UPDATED_LIVING_POPULATION
        defaultHomelandStationShouldNotBeFound("livingPopulation.greaterThanOrEqual=" + UPDATED_LIVING_POPULATION);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLivingPopulationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where livingPopulation is less than or equal to DEFAULT_LIVING_POPULATION
        defaultHomelandStationShouldBeFound("livingPopulation.lessThanOrEqual=" + DEFAULT_LIVING_POPULATION);

        // Get all the homelandStationList where livingPopulation is less than or equal to SMALLER_LIVING_POPULATION
        defaultHomelandStationShouldNotBeFound("livingPopulation.lessThanOrEqual=" + SMALLER_LIVING_POPULATION);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLivingPopulationIsLessThanSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where livingPopulation is less than DEFAULT_LIVING_POPULATION
        defaultHomelandStationShouldNotBeFound("livingPopulation.lessThan=" + DEFAULT_LIVING_POPULATION);

        // Get all the homelandStationList where livingPopulation is less than UPDATED_LIVING_POPULATION
        defaultHomelandStationShouldBeFound("livingPopulation.lessThan=" + UPDATED_LIVING_POPULATION);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLivingPopulationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where livingPopulation is greater than DEFAULT_LIVING_POPULATION
        defaultHomelandStationShouldNotBeFound("livingPopulation.greaterThan=" + DEFAULT_LIVING_POPULATION);

        // Get all the homelandStationList where livingPopulation is greater than SMALLER_LIVING_POPULATION
        defaultHomelandStationShouldBeFound("livingPopulation.greaterThan=" + SMALLER_LIVING_POPULATION);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingCountIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where buildingCount equals to DEFAULT_BUILDING_COUNT
        defaultHomelandStationShouldBeFound("buildingCount.equals=" + DEFAULT_BUILDING_COUNT);

        // Get all the homelandStationList where buildingCount equals to UPDATED_BUILDING_COUNT
        defaultHomelandStationShouldNotBeFound("buildingCount.equals=" + UPDATED_BUILDING_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where buildingCount not equals to DEFAULT_BUILDING_COUNT
        defaultHomelandStationShouldNotBeFound("buildingCount.notEquals=" + DEFAULT_BUILDING_COUNT);

        // Get all the homelandStationList where buildingCount not equals to UPDATED_BUILDING_COUNT
        defaultHomelandStationShouldBeFound("buildingCount.notEquals=" + UPDATED_BUILDING_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingCountIsInShouldWork() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where buildingCount in DEFAULT_BUILDING_COUNT or UPDATED_BUILDING_COUNT
        defaultHomelandStationShouldBeFound("buildingCount.in=" + DEFAULT_BUILDING_COUNT + "," + UPDATED_BUILDING_COUNT);

        // Get all the homelandStationList where buildingCount equals to UPDATED_BUILDING_COUNT
        defaultHomelandStationShouldNotBeFound("buildingCount.in=" + UPDATED_BUILDING_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where buildingCount is not null
        defaultHomelandStationShouldBeFound("buildingCount.specified=true");

        // Get all the homelandStationList where buildingCount is null
        defaultHomelandStationShouldNotBeFound("buildingCount.specified=false");
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where buildingCount is greater than or equal to DEFAULT_BUILDING_COUNT
        defaultHomelandStationShouldBeFound("buildingCount.greaterThanOrEqual=" + DEFAULT_BUILDING_COUNT);

        // Get all the homelandStationList where buildingCount is greater than or equal to UPDATED_BUILDING_COUNT
        defaultHomelandStationShouldNotBeFound("buildingCount.greaterThanOrEqual=" + UPDATED_BUILDING_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where buildingCount is less than or equal to DEFAULT_BUILDING_COUNT
        defaultHomelandStationShouldBeFound("buildingCount.lessThanOrEqual=" + DEFAULT_BUILDING_COUNT);

        // Get all the homelandStationList where buildingCount is less than or equal to SMALLER_BUILDING_COUNT
        defaultHomelandStationShouldNotBeFound("buildingCount.lessThanOrEqual=" + SMALLER_BUILDING_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingCountIsLessThanSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where buildingCount is less than DEFAULT_BUILDING_COUNT
        defaultHomelandStationShouldNotBeFound("buildingCount.lessThan=" + DEFAULT_BUILDING_COUNT);

        // Get all the homelandStationList where buildingCount is less than UPDATED_BUILDING_COUNT
        defaultHomelandStationShouldBeFound("buildingCount.lessThan=" + UPDATED_BUILDING_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where buildingCount is greater than DEFAULT_BUILDING_COUNT
        defaultHomelandStationShouldNotBeFound("buildingCount.greaterThan=" + DEFAULT_BUILDING_COUNT);

        // Get all the homelandStationList where buildingCount is greater than SMALLER_BUILDING_COUNT
        defaultHomelandStationShouldBeFound("buildingCount.greaterThan=" + SMALLER_BUILDING_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByEntranceCountIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where entranceCount equals to DEFAULT_ENTRANCE_COUNT
        defaultHomelandStationShouldBeFound("entranceCount.equals=" + DEFAULT_ENTRANCE_COUNT);

        // Get all the homelandStationList where entranceCount equals to UPDATED_ENTRANCE_COUNT
        defaultHomelandStationShouldNotBeFound("entranceCount.equals=" + UPDATED_ENTRANCE_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByEntranceCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where entranceCount not equals to DEFAULT_ENTRANCE_COUNT
        defaultHomelandStationShouldNotBeFound("entranceCount.notEquals=" + DEFAULT_ENTRANCE_COUNT);

        // Get all the homelandStationList where entranceCount not equals to UPDATED_ENTRANCE_COUNT
        defaultHomelandStationShouldBeFound("entranceCount.notEquals=" + UPDATED_ENTRANCE_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByEntranceCountIsInShouldWork() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where entranceCount in DEFAULT_ENTRANCE_COUNT or UPDATED_ENTRANCE_COUNT
        defaultHomelandStationShouldBeFound("entranceCount.in=" + DEFAULT_ENTRANCE_COUNT + "," + UPDATED_ENTRANCE_COUNT);

        // Get all the homelandStationList where entranceCount equals to UPDATED_ENTRANCE_COUNT
        defaultHomelandStationShouldNotBeFound("entranceCount.in=" + UPDATED_ENTRANCE_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByEntranceCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where entranceCount is not null
        defaultHomelandStationShouldBeFound("entranceCount.specified=true");

        // Get all the homelandStationList where entranceCount is null
        defaultHomelandStationShouldNotBeFound("entranceCount.specified=false");
    }

    @Test
    @Transactional
    void getAllHomelandStationsByEntranceCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where entranceCount is greater than or equal to DEFAULT_ENTRANCE_COUNT
        defaultHomelandStationShouldBeFound("entranceCount.greaterThanOrEqual=" + DEFAULT_ENTRANCE_COUNT);

        // Get all the homelandStationList where entranceCount is greater than or equal to UPDATED_ENTRANCE_COUNT
        defaultHomelandStationShouldNotBeFound("entranceCount.greaterThanOrEqual=" + UPDATED_ENTRANCE_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByEntranceCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where entranceCount is less than or equal to DEFAULT_ENTRANCE_COUNT
        defaultHomelandStationShouldBeFound("entranceCount.lessThanOrEqual=" + DEFAULT_ENTRANCE_COUNT);

        // Get all the homelandStationList where entranceCount is less than or equal to SMALLER_ENTRANCE_COUNT
        defaultHomelandStationShouldNotBeFound("entranceCount.lessThanOrEqual=" + SMALLER_ENTRANCE_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByEntranceCountIsLessThanSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where entranceCount is less than DEFAULT_ENTRANCE_COUNT
        defaultHomelandStationShouldNotBeFound("entranceCount.lessThan=" + DEFAULT_ENTRANCE_COUNT);

        // Get all the homelandStationList where entranceCount is less than UPDATED_ENTRANCE_COUNT
        defaultHomelandStationShouldBeFound("entranceCount.lessThan=" + UPDATED_ENTRANCE_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByEntranceCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where entranceCount is greater than DEFAULT_ENTRANCE_COUNT
        defaultHomelandStationShouldNotBeFound("entranceCount.greaterThan=" + DEFAULT_ENTRANCE_COUNT);

        // Get all the homelandStationList where entranceCount is greater than SMALLER_ENTRANCE_COUNT
        defaultHomelandStationShouldBeFound("entranceCount.greaterThan=" + SMALLER_ENTRANCE_COUNT);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longitude equals to DEFAULT_LONGITUDE
        defaultHomelandStationShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the homelandStationList where longitude equals to UPDATED_LONGITUDE
        defaultHomelandStationShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longitude not equals to DEFAULT_LONGITUDE
        defaultHomelandStationShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the homelandStationList where longitude not equals to UPDATED_LONGITUDE
        defaultHomelandStationShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultHomelandStationShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the homelandStationList where longitude equals to UPDATED_LONGITUDE
        defaultHomelandStationShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longitude is not null
        defaultHomelandStationShouldBeFound("longitude.specified=true");

        // Get all the homelandStationList where longitude is null
        defaultHomelandStationShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longitude contains DEFAULT_LONGITUDE
        defaultHomelandStationShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the homelandStationList where longitude contains UPDATED_LONGITUDE
        defaultHomelandStationShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where longitude does not contain DEFAULT_LONGITUDE
        defaultHomelandStationShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the homelandStationList where longitude does not contain UPDATED_LONGITUDE
        defaultHomelandStationShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where latitude equals to DEFAULT_LATITUDE
        defaultHomelandStationShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the homelandStationList where latitude equals to UPDATED_LATITUDE
        defaultHomelandStationShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where latitude not equals to DEFAULT_LATITUDE
        defaultHomelandStationShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the homelandStationList where latitude not equals to UPDATED_LATITUDE
        defaultHomelandStationShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultHomelandStationShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the homelandStationList where latitude equals to UPDATED_LATITUDE
        defaultHomelandStationShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where latitude is not null
        defaultHomelandStationShouldBeFound("latitude.specified=true");

        // Get all the homelandStationList where latitude is null
        defaultHomelandStationShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where latitude contains DEFAULT_LATITUDE
        defaultHomelandStationShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the homelandStationList where latitude contains UPDATED_LATITUDE
        defaultHomelandStationShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        // Get all the homelandStationList where latitude does not contain DEFAULT_LATITUDE
        defaultHomelandStationShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the homelandStationList where latitude does not contain UPDATED_LATITUDE
        defaultHomelandStationShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllHomelandStationsByBuildingsIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);
        Buildings buildings = BuildingsResourceIT.createEntity(em);
        em.persist(buildings);
        em.flush();
        homelandStation.addBuildings(buildings);
        homelandStationRepository.saveAndFlush(homelandStation);
        Long buildingsId = buildings.getId();

        // Get all the homelandStationList where buildings equals to buildingsId
        defaultHomelandStationShouldBeFound("buildingsId.equals=" + buildingsId);

        // Get all the homelandStationList where buildings equals to (buildingsId + 1)
        defaultHomelandStationShouldNotBeFound("buildingsId.equals=" + (buildingsId + 1));
    }

    @Test
    @Transactional
    void getAllHomelandStationsByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);
        Community community = CommunityResourceIT.createEntity(em);
        em.persist(community);
        em.flush();
        homelandStation.setCommunity(community);
        homelandStationRepository.saveAndFlush(homelandStation);
        Long communityId = community.getId();

        // Get all the homelandStationList where community equals to communityId
        defaultHomelandStationShouldBeFound("communityId.equals=" + communityId);

        // Get all the homelandStationList where community equals to (communityId + 1)
        defaultHomelandStationShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    @Test
    @Transactional
    void getAllHomelandStationsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        em.flush();
        homelandStation.setCompany(company);
        homelandStationRepository.saveAndFlush(homelandStation);
        Long companyId = company.getId();

        // Get all the homelandStationList where company equals to companyId
        defaultHomelandStationShouldBeFound("companyId.equals=" + companyId);

        // Get all the homelandStationList where company equals to (companyId + 1)
        defaultHomelandStationShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHomelandStationShouldBeFound(String filter) throws Exception {
        restHomelandStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homelandStation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].livingPopulation").value(hasItem(DEFAULT_LIVING_POPULATION)))
            .andExpect(jsonPath("$.[*].buildingCount").value(hasItem(DEFAULT_BUILDING_COUNT)))
            .andExpect(jsonPath("$.[*].entranceCount").value(hasItem(DEFAULT_ENTRANCE_COUNT)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)));

        // Check, that the count call also returns 1
        restHomelandStationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHomelandStationShouldNotBeFound(String filter) throws Exception {
        restHomelandStationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHomelandStationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHomelandStation() throws Exception {
        // Get the homelandStation
        restHomelandStationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHomelandStation() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();

        // Update the homelandStation
        HomelandStation updatedHomelandStation = homelandStationRepository.findById(homelandStation.getId()).get();
        // Disconnect from session so that the updates on updatedHomelandStation are not directly saved in db
        em.detach(updatedHomelandStation);
        updatedHomelandStation
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .address(UPDATED_ADDRESS)
            .livingPopulation(UPDATED_LIVING_POPULATION)
            .buildingCount(UPDATED_BUILDING_COUNT)
            .entranceCount(UPDATED_ENTRANCE_COUNT)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);

        restHomelandStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHomelandStation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHomelandStation))
            )
            .andExpect(status().isOk());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
        HomelandStation testHomelandStation = homelandStationList.get(homelandStationList.size() - 1);
        assertThat(testHomelandStation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHomelandStation.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testHomelandStation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHomelandStation.getLivingPopulation()).isEqualTo(UPDATED_LIVING_POPULATION);
        assertThat(testHomelandStation.getBuildingCount()).isEqualTo(UPDATED_BUILDING_COUNT);
        assertThat(testHomelandStation.getEntranceCount()).isEqualTo(UPDATED_ENTRANCE_COUNT);
        assertThat(testHomelandStation.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testHomelandStation.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testHomelandStation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testHomelandStation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void putNonExistingHomelandStation() throws Exception {
        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();
        homelandStation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomelandStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, homelandStation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHomelandStation() throws Exception {
        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();
        homelandStation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomelandStationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHomelandStation() throws Exception {
        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();
        homelandStation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomelandStationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHomelandStationWithPatch() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();

        // Update the homelandStation using partial update
        HomelandStation partialUpdatedHomelandStation = new HomelandStation();
        partialUpdatedHomelandStation.setId(homelandStation.getId());

        partialUpdatedHomelandStation.address(UPDATED_ADDRESS).entranceCount(UPDATED_ENTRANCE_COUNT);

        restHomelandStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHomelandStation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHomelandStation))
            )
            .andExpect(status().isOk());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
        HomelandStation testHomelandStation = homelandStationList.get(homelandStationList.size() - 1);
        assertThat(testHomelandStation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHomelandStation.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
        assertThat(testHomelandStation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHomelandStation.getLivingPopulation()).isEqualTo(DEFAULT_LIVING_POPULATION);
        assertThat(testHomelandStation.getBuildingCount()).isEqualTo(DEFAULT_BUILDING_COUNT);
        assertThat(testHomelandStation.getEntranceCount()).isEqualTo(UPDATED_ENTRANCE_COUNT);
        assertThat(testHomelandStation.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testHomelandStation.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testHomelandStation.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testHomelandStation.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void fullUpdateHomelandStationWithPatch() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();

        // Update the homelandStation using partial update
        HomelandStation partialUpdatedHomelandStation = new HomelandStation();
        partialUpdatedHomelandStation.setId(homelandStation.getId());

        partialUpdatedHomelandStation
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .address(UPDATED_ADDRESS)
            .livingPopulation(UPDATED_LIVING_POPULATION)
            .buildingCount(UPDATED_BUILDING_COUNT)
            .entranceCount(UPDATED_ENTRANCE_COUNT)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE);

        restHomelandStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHomelandStation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHomelandStation))
            )
            .andExpect(status().isOk());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
        HomelandStation testHomelandStation = homelandStationList.get(homelandStationList.size() - 1);
        assertThat(testHomelandStation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHomelandStation.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testHomelandStation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testHomelandStation.getLivingPopulation()).isEqualTo(UPDATED_LIVING_POPULATION);
        assertThat(testHomelandStation.getBuildingCount()).isEqualTo(UPDATED_BUILDING_COUNT);
        assertThat(testHomelandStation.getEntranceCount()).isEqualTo(UPDATED_ENTRANCE_COUNT);
        assertThat(testHomelandStation.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testHomelandStation.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testHomelandStation.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testHomelandStation.getLatitude()).isEqualTo(UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingHomelandStation() throws Exception {
        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();
        homelandStation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomelandStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, homelandStation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHomelandStation() throws Exception {
        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();
        homelandStation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomelandStationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isBadRequest());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHomelandStation() throws Exception {
        int databaseSizeBeforeUpdate = homelandStationRepository.findAll().size();
        homelandStation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHomelandStationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(homelandStation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HomelandStation in the database
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHomelandStation() throws Exception {
        // Initialize the database
        homelandStationRepository.saveAndFlush(homelandStation);

        int databaseSizeBeforeDelete = homelandStationRepository.findAll().size();

        // Delete the homelandStation
        restHomelandStationMockMvc
            .perform(delete(ENTITY_API_URL_ID, homelandStation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HomelandStation> homelandStationList = homelandStationRepository.findAll();
        assertThat(homelandStationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
