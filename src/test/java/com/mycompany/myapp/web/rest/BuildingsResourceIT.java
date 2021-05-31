package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Buildings;
import com.mycompany.myapp.domain.HomelandStation;
import com.mycompany.myapp.domain.RoomAddr;
import com.mycompany.myapp.repository.BuildingsRepository;
import com.mycompany.myapp.service.criteria.BuildingsCriteria;
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
 * Integration tests for the {@link BuildingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BuildingsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LONG_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FLOOR_COUNT = 1;
    private static final Integer UPDATED_FLOOR_COUNT = 2;
    private static final Integer SMALLER_FLOOR_COUNT = 1 - 1;

    private static final Integer DEFAULT_UNITES = 1;
    private static final Integer UPDATED_UNITES = 2;
    private static final Integer SMALLER_UNITES = 1 - 1;

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final String ENTITY_API_URL = "/api/buildings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BuildingsRepository buildingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBuildingsMockMvc;

    private Buildings buildings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Buildings createEntity(EntityManager em) {
        Buildings buildings = new Buildings()
            .name(DEFAULT_NAME)
            .longCode(DEFAULT_LONG_CODE)
            .floorCount(DEFAULT_FLOOR_COUNT)
            .unites(DEFAULT_UNITES)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .enable(DEFAULT_ENABLE);
        return buildings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Buildings createUpdatedEntity(EntityManager em) {
        Buildings buildings = new Buildings()
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .floorCount(UPDATED_FLOOR_COUNT)
            .unites(UPDATED_UNITES)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .enable(UPDATED_ENABLE);
        return buildings;
    }

    @BeforeEach
    public void initTest() {
        buildings = createEntity(em);
    }

    @Test
    @Transactional
    void createBuildings() throws Exception {
        int databaseSizeBeforeCreate = buildingsRepository.findAll().size();
        // Create the Buildings
        restBuildingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildings)))
            .andExpect(status().isCreated());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeCreate + 1);
        Buildings testBuildings = buildingsList.get(buildingsList.size() - 1);
        assertThat(testBuildings.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBuildings.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
        assertThat(testBuildings.getFloorCount()).isEqualTo(DEFAULT_FLOOR_COUNT);
        assertThat(testBuildings.getUnites()).isEqualTo(DEFAULT_UNITES);
        assertThat(testBuildings.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testBuildings.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testBuildings.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    void createBuildingsWithExistingId() throws Exception {
        // Create the Buildings with an existing ID
        buildings.setId(1L);

        int databaseSizeBeforeCreate = buildingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBuildingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildings)))
            .andExpect(status().isBadRequest());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLongCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingsRepository.findAll().size();
        // set the field null
        buildings.setLongCode(null);

        // Create the Buildings, which fails.

        restBuildingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildings)))
            .andExpect(status().isBadRequest());

        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingsRepository.findAll().size();
        // set the field null
        buildings.setLongitude(null);

        // Create the Buildings, which fails.

        restBuildingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildings)))
            .andExpect(status().isBadRequest());

        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = buildingsRepository.findAll().size();
        // set the field null
        buildings.setLatitude(null);

        // Create the Buildings, which fails.

        restBuildingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildings)))
            .andExpect(status().isBadRequest());

        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBuildings() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList
        restBuildingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(buildings.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)))
            .andExpect(jsonPath("$.[*].floorCount").value(hasItem(DEFAULT_FLOOR_COUNT)))
            .andExpect(jsonPath("$.[*].unites").value(hasItem(DEFAULT_UNITES)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getBuildings() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get the buildings
        restBuildingsMockMvc
            .perform(get(ENTITY_API_URL_ID, buildings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(buildings.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.longCode").value(DEFAULT_LONG_CODE))
            .andExpect(jsonPath("$.floorCount").value(DEFAULT_FLOOR_COUNT))
            .andExpect(jsonPath("$.unites").value(DEFAULT_UNITES))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getBuildingsByIdFiltering() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        Long id = buildings.getId();

        defaultBuildingsShouldBeFound("id.equals=" + id);
        defaultBuildingsShouldNotBeFound("id.notEquals=" + id);

        defaultBuildingsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBuildingsShouldNotBeFound("id.greaterThan=" + id);

        defaultBuildingsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBuildingsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBuildingsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where name equals to DEFAULT_NAME
        defaultBuildingsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the buildingsList where name equals to UPDATED_NAME
        defaultBuildingsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBuildingsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where name not equals to DEFAULT_NAME
        defaultBuildingsShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the buildingsList where name not equals to UPDATED_NAME
        defaultBuildingsShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBuildingsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBuildingsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the buildingsList where name equals to UPDATED_NAME
        defaultBuildingsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBuildingsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where name is not null
        defaultBuildingsShouldBeFound("name.specified=true");

        // Get all the buildingsList where name is null
        defaultBuildingsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllBuildingsByNameContainsSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where name contains DEFAULT_NAME
        defaultBuildingsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the buildingsList where name contains UPDATED_NAME
        defaultBuildingsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBuildingsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where name does not contain DEFAULT_NAME
        defaultBuildingsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the buildingsList where name does not contain UPDATED_NAME
        defaultBuildingsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longCode equals to DEFAULT_LONG_CODE
        defaultBuildingsShouldBeFound("longCode.equals=" + DEFAULT_LONG_CODE);

        // Get all the buildingsList where longCode equals to UPDATED_LONG_CODE
        defaultBuildingsShouldNotBeFound("longCode.equals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longCode not equals to DEFAULT_LONG_CODE
        defaultBuildingsShouldNotBeFound("longCode.notEquals=" + DEFAULT_LONG_CODE);

        // Get all the buildingsList where longCode not equals to UPDATED_LONG_CODE
        defaultBuildingsShouldBeFound("longCode.notEquals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongCodeIsInShouldWork() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longCode in DEFAULT_LONG_CODE or UPDATED_LONG_CODE
        defaultBuildingsShouldBeFound("longCode.in=" + DEFAULT_LONG_CODE + "," + UPDATED_LONG_CODE);

        // Get all the buildingsList where longCode equals to UPDATED_LONG_CODE
        defaultBuildingsShouldNotBeFound("longCode.in=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longCode is not null
        defaultBuildingsShouldBeFound("longCode.specified=true");

        // Get all the buildingsList where longCode is null
        defaultBuildingsShouldNotBeFound("longCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBuildingsByLongCodeContainsSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longCode contains DEFAULT_LONG_CODE
        defaultBuildingsShouldBeFound("longCode.contains=" + DEFAULT_LONG_CODE);

        // Get all the buildingsList where longCode contains UPDATED_LONG_CODE
        defaultBuildingsShouldNotBeFound("longCode.contains=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongCodeNotContainsSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longCode does not contain DEFAULT_LONG_CODE
        defaultBuildingsShouldNotBeFound("longCode.doesNotContain=" + DEFAULT_LONG_CODE);

        // Get all the buildingsList where longCode does not contain UPDATED_LONG_CODE
        defaultBuildingsShouldBeFound("longCode.doesNotContain=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllBuildingsByFloorCountIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where floorCount equals to DEFAULT_FLOOR_COUNT
        defaultBuildingsShouldBeFound("floorCount.equals=" + DEFAULT_FLOOR_COUNT);

        // Get all the buildingsList where floorCount equals to UPDATED_FLOOR_COUNT
        defaultBuildingsShouldNotBeFound("floorCount.equals=" + UPDATED_FLOOR_COUNT);
    }

    @Test
    @Transactional
    void getAllBuildingsByFloorCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where floorCount not equals to DEFAULT_FLOOR_COUNT
        defaultBuildingsShouldNotBeFound("floorCount.notEquals=" + DEFAULT_FLOOR_COUNT);

        // Get all the buildingsList where floorCount not equals to UPDATED_FLOOR_COUNT
        defaultBuildingsShouldBeFound("floorCount.notEquals=" + UPDATED_FLOOR_COUNT);
    }

    @Test
    @Transactional
    void getAllBuildingsByFloorCountIsInShouldWork() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where floorCount in DEFAULT_FLOOR_COUNT or UPDATED_FLOOR_COUNT
        defaultBuildingsShouldBeFound("floorCount.in=" + DEFAULT_FLOOR_COUNT + "," + UPDATED_FLOOR_COUNT);

        // Get all the buildingsList where floorCount equals to UPDATED_FLOOR_COUNT
        defaultBuildingsShouldNotBeFound("floorCount.in=" + UPDATED_FLOOR_COUNT);
    }

    @Test
    @Transactional
    void getAllBuildingsByFloorCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where floorCount is not null
        defaultBuildingsShouldBeFound("floorCount.specified=true");

        // Get all the buildingsList where floorCount is null
        defaultBuildingsShouldNotBeFound("floorCount.specified=false");
    }

    @Test
    @Transactional
    void getAllBuildingsByFloorCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where floorCount is greater than or equal to DEFAULT_FLOOR_COUNT
        defaultBuildingsShouldBeFound("floorCount.greaterThanOrEqual=" + DEFAULT_FLOOR_COUNT);

        // Get all the buildingsList where floorCount is greater than or equal to UPDATED_FLOOR_COUNT
        defaultBuildingsShouldNotBeFound("floorCount.greaterThanOrEqual=" + UPDATED_FLOOR_COUNT);
    }

    @Test
    @Transactional
    void getAllBuildingsByFloorCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where floorCount is less than or equal to DEFAULT_FLOOR_COUNT
        defaultBuildingsShouldBeFound("floorCount.lessThanOrEqual=" + DEFAULT_FLOOR_COUNT);

        // Get all the buildingsList where floorCount is less than or equal to SMALLER_FLOOR_COUNT
        defaultBuildingsShouldNotBeFound("floorCount.lessThanOrEqual=" + SMALLER_FLOOR_COUNT);
    }

    @Test
    @Transactional
    void getAllBuildingsByFloorCountIsLessThanSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where floorCount is less than DEFAULT_FLOOR_COUNT
        defaultBuildingsShouldNotBeFound("floorCount.lessThan=" + DEFAULT_FLOOR_COUNT);

        // Get all the buildingsList where floorCount is less than UPDATED_FLOOR_COUNT
        defaultBuildingsShouldBeFound("floorCount.lessThan=" + UPDATED_FLOOR_COUNT);
    }

    @Test
    @Transactional
    void getAllBuildingsByFloorCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where floorCount is greater than DEFAULT_FLOOR_COUNT
        defaultBuildingsShouldNotBeFound("floorCount.greaterThan=" + DEFAULT_FLOOR_COUNT);

        // Get all the buildingsList where floorCount is greater than SMALLER_FLOOR_COUNT
        defaultBuildingsShouldBeFound("floorCount.greaterThan=" + SMALLER_FLOOR_COUNT);
    }

    @Test
    @Transactional
    void getAllBuildingsByUnitesIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where unites equals to DEFAULT_UNITES
        defaultBuildingsShouldBeFound("unites.equals=" + DEFAULT_UNITES);

        // Get all the buildingsList where unites equals to UPDATED_UNITES
        defaultBuildingsShouldNotBeFound("unites.equals=" + UPDATED_UNITES);
    }

    @Test
    @Transactional
    void getAllBuildingsByUnitesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where unites not equals to DEFAULT_UNITES
        defaultBuildingsShouldNotBeFound("unites.notEquals=" + DEFAULT_UNITES);

        // Get all the buildingsList where unites not equals to UPDATED_UNITES
        defaultBuildingsShouldBeFound("unites.notEquals=" + UPDATED_UNITES);
    }

    @Test
    @Transactional
    void getAllBuildingsByUnitesIsInShouldWork() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where unites in DEFAULT_UNITES or UPDATED_UNITES
        defaultBuildingsShouldBeFound("unites.in=" + DEFAULT_UNITES + "," + UPDATED_UNITES);

        // Get all the buildingsList where unites equals to UPDATED_UNITES
        defaultBuildingsShouldNotBeFound("unites.in=" + UPDATED_UNITES);
    }

    @Test
    @Transactional
    void getAllBuildingsByUnitesIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where unites is not null
        defaultBuildingsShouldBeFound("unites.specified=true");

        // Get all the buildingsList where unites is null
        defaultBuildingsShouldNotBeFound("unites.specified=false");
    }

    @Test
    @Transactional
    void getAllBuildingsByUnitesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where unites is greater than or equal to DEFAULT_UNITES
        defaultBuildingsShouldBeFound("unites.greaterThanOrEqual=" + DEFAULT_UNITES);

        // Get all the buildingsList where unites is greater than or equal to UPDATED_UNITES
        defaultBuildingsShouldNotBeFound("unites.greaterThanOrEqual=" + UPDATED_UNITES);
    }

    @Test
    @Transactional
    void getAllBuildingsByUnitesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where unites is less than or equal to DEFAULT_UNITES
        defaultBuildingsShouldBeFound("unites.lessThanOrEqual=" + DEFAULT_UNITES);

        // Get all the buildingsList where unites is less than or equal to SMALLER_UNITES
        defaultBuildingsShouldNotBeFound("unites.lessThanOrEqual=" + SMALLER_UNITES);
    }

    @Test
    @Transactional
    void getAllBuildingsByUnitesIsLessThanSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where unites is less than DEFAULT_UNITES
        defaultBuildingsShouldNotBeFound("unites.lessThan=" + DEFAULT_UNITES);

        // Get all the buildingsList where unites is less than UPDATED_UNITES
        defaultBuildingsShouldBeFound("unites.lessThan=" + UPDATED_UNITES);
    }

    @Test
    @Transactional
    void getAllBuildingsByUnitesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where unites is greater than DEFAULT_UNITES
        defaultBuildingsShouldNotBeFound("unites.greaterThan=" + DEFAULT_UNITES);

        // Get all the buildingsList where unites is greater than SMALLER_UNITES
        defaultBuildingsShouldBeFound("unites.greaterThan=" + SMALLER_UNITES);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longitude equals to DEFAULT_LONGITUDE
        defaultBuildingsShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the buildingsList where longitude equals to UPDATED_LONGITUDE
        defaultBuildingsShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longitude not equals to DEFAULT_LONGITUDE
        defaultBuildingsShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the buildingsList where longitude not equals to UPDATED_LONGITUDE
        defaultBuildingsShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultBuildingsShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the buildingsList where longitude equals to UPDATED_LONGITUDE
        defaultBuildingsShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longitude is not null
        defaultBuildingsShouldBeFound("longitude.specified=true");

        // Get all the buildingsList where longitude is null
        defaultBuildingsShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllBuildingsByLongitudeContainsSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longitude contains DEFAULT_LONGITUDE
        defaultBuildingsShouldBeFound("longitude.contains=" + DEFAULT_LONGITUDE);

        // Get all the buildingsList where longitude contains UPDATED_LONGITUDE
        defaultBuildingsShouldNotBeFound("longitude.contains=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLongitudeNotContainsSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where longitude does not contain DEFAULT_LONGITUDE
        defaultBuildingsShouldNotBeFound("longitude.doesNotContain=" + DEFAULT_LONGITUDE);

        // Get all the buildingsList where longitude does not contain UPDATED_LONGITUDE
        defaultBuildingsShouldBeFound("longitude.doesNotContain=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where latitude equals to DEFAULT_LATITUDE
        defaultBuildingsShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the buildingsList where latitude equals to UPDATED_LATITUDE
        defaultBuildingsShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where latitude not equals to DEFAULT_LATITUDE
        defaultBuildingsShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the buildingsList where latitude not equals to UPDATED_LATITUDE
        defaultBuildingsShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultBuildingsShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the buildingsList where latitude equals to UPDATED_LATITUDE
        defaultBuildingsShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where latitude is not null
        defaultBuildingsShouldBeFound("latitude.specified=true");

        // Get all the buildingsList where latitude is null
        defaultBuildingsShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllBuildingsByLatitudeContainsSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where latitude contains DEFAULT_LATITUDE
        defaultBuildingsShouldBeFound("latitude.contains=" + DEFAULT_LATITUDE);

        // Get all the buildingsList where latitude contains UPDATED_LATITUDE
        defaultBuildingsShouldNotBeFound("latitude.contains=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByLatitudeNotContainsSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where latitude does not contain DEFAULT_LATITUDE
        defaultBuildingsShouldNotBeFound("latitude.doesNotContain=" + DEFAULT_LATITUDE);

        // Get all the buildingsList where latitude does not contain UPDATED_LATITUDE
        defaultBuildingsShouldBeFound("latitude.doesNotContain=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllBuildingsByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where enable equals to DEFAULT_ENABLE
        defaultBuildingsShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the buildingsList where enable equals to UPDATED_ENABLE
        defaultBuildingsShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllBuildingsByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where enable not equals to DEFAULT_ENABLE
        defaultBuildingsShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the buildingsList where enable not equals to UPDATED_ENABLE
        defaultBuildingsShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllBuildingsByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultBuildingsShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the buildingsList where enable equals to UPDATED_ENABLE
        defaultBuildingsShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllBuildingsByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        // Get all the buildingsList where enable is not null
        defaultBuildingsShouldBeFound("enable.specified=true");

        // Get all the buildingsList where enable is null
        defaultBuildingsShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllBuildingsByRoomAddrIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);
        RoomAddr roomAddr = RoomAddrResourceIT.createEntity(em);
        em.persist(roomAddr);
        em.flush();
        buildings.addRoomAddr(roomAddr);
        buildingsRepository.saveAndFlush(buildings);
        Long roomAddrId = roomAddr.getId();

        // Get all the buildingsList where roomAddr equals to roomAddrId
        defaultBuildingsShouldBeFound("roomAddrId.equals=" + roomAddrId);

        // Get all the buildingsList where roomAddr equals to (roomAddrId + 1)
        defaultBuildingsShouldNotBeFound("roomAddrId.equals=" + (roomAddrId + 1));
    }

    @Test
    @Transactional
    void getAllBuildingsByHomelandStationIsEqualToSomething() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);
        HomelandStation homelandStation = HomelandStationResourceIT.createEntity(em);
        em.persist(homelandStation);
        em.flush();
        buildings.setHomelandStation(homelandStation);
        buildingsRepository.saveAndFlush(buildings);
        Long homelandStationId = homelandStation.getId();

        // Get all the buildingsList where homelandStation equals to homelandStationId
        defaultBuildingsShouldBeFound("homelandStationId.equals=" + homelandStationId);

        // Get all the buildingsList where homelandStation equals to (homelandStationId + 1)
        defaultBuildingsShouldNotBeFound("homelandStationId.equals=" + (homelandStationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBuildingsShouldBeFound(String filter) throws Exception {
        restBuildingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(buildings.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)))
            .andExpect(jsonPath("$.[*].floorCount").value(hasItem(DEFAULT_FLOOR_COUNT)))
            .andExpect(jsonPath("$.[*].unites").value(hasItem(DEFAULT_UNITES)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));

        // Check, that the count call also returns 1
        restBuildingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBuildingsShouldNotBeFound(String filter) throws Exception {
        restBuildingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBuildingsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBuildings() throws Exception {
        // Get the buildings
        restBuildingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBuildings() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();

        // Update the buildings
        Buildings updatedBuildings = buildingsRepository.findById(buildings.getId()).get();
        // Disconnect from session so that the updates on updatedBuildings are not directly saved in db
        em.detach(updatedBuildings);
        updatedBuildings
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .floorCount(UPDATED_FLOOR_COUNT)
            .unites(UPDATED_UNITES)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .enable(UPDATED_ENABLE);

        restBuildingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBuildings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBuildings))
            )
            .andExpect(status().isOk());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
        Buildings testBuildings = buildingsList.get(buildingsList.size() - 1);
        assertThat(testBuildings.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBuildings.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testBuildings.getFloorCount()).isEqualTo(UPDATED_FLOOR_COUNT);
        assertThat(testBuildings.getUnites()).isEqualTo(UPDATED_UNITES);
        assertThat(testBuildings.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testBuildings.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testBuildings.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void putNonExistingBuildings() throws Exception {
        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();
        buildings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, buildings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buildings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBuildings() throws Exception {
        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();
        buildings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(buildings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBuildings() throws Exception {
        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();
        buildings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(buildings)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBuildingsWithPatch() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();

        // Update the buildings using partial update
        Buildings partialUpdatedBuildings = new Buildings();
        partialUpdatedBuildings.setId(buildings.getId());

        partialUpdatedBuildings.floorCount(UPDATED_FLOOR_COUNT).longitude(UPDATED_LONGITUDE).enable(UPDATED_ENABLE);

        restBuildingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBuildings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuildings))
            )
            .andExpect(status().isOk());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
        Buildings testBuildings = buildingsList.get(buildingsList.size() - 1);
        assertThat(testBuildings.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBuildings.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
        assertThat(testBuildings.getFloorCount()).isEqualTo(UPDATED_FLOOR_COUNT);
        assertThat(testBuildings.getUnites()).isEqualTo(DEFAULT_UNITES);
        assertThat(testBuildings.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testBuildings.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testBuildings.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void fullUpdateBuildingsWithPatch() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();

        // Update the buildings using partial update
        Buildings partialUpdatedBuildings = new Buildings();
        partialUpdatedBuildings.setId(buildings.getId());

        partialUpdatedBuildings
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .floorCount(UPDATED_FLOOR_COUNT)
            .unites(UPDATED_UNITES)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .enable(UPDATED_ENABLE);

        restBuildingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBuildings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBuildings))
            )
            .andExpect(status().isOk());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
        Buildings testBuildings = buildingsList.get(buildingsList.size() - 1);
        assertThat(testBuildings.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBuildings.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testBuildings.getFloorCount()).isEqualTo(UPDATED_FLOOR_COUNT);
        assertThat(testBuildings.getUnites()).isEqualTo(UPDATED_UNITES);
        assertThat(testBuildings.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testBuildings.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testBuildings.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void patchNonExistingBuildings() throws Exception {
        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();
        buildings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBuildingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, buildings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buildings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBuildings() throws Exception {
        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();
        buildings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(buildings))
            )
            .andExpect(status().isBadRequest());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBuildings() throws Exception {
        int databaseSizeBeforeUpdate = buildingsRepository.findAll().size();
        buildings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBuildingsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(buildings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Buildings in the database
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBuildings() throws Exception {
        // Initialize the database
        buildingsRepository.saveAndFlush(buildings);

        int databaseSizeBeforeDelete = buildingsRepository.findAll().size();

        // Delete the buildings
        restBuildingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, buildings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Buildings> buildingsList = buildingsRepository.findAll();
        assertThat(buildingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
