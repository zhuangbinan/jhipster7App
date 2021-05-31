package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RoomAddr;
import com.mycompany.myapp.domain.TuYaCmd;
import com.mycompany.myapp.domain.TuYaDevice;
import com.mycompany.myapp.repository.TuYaDeviceRepository;
import com.mycompany.myapp.service.criteria.TuYaDeviceCriteria;
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
 * Integration tests for the {@link TuYaDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TuYaDeviceResourceIT {

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_LONG_CODE = 1L;
    private static final Long UPDATED_LONG_CODE = 2L;
    private static final Long SMALLER_LONG_CODE = 1L - 1L;

    private static final String DEFAULT_TY_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_TY_DEVICE_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEVICE_TYPE = 1;
    private static final Integer UPDATED_DEVICE_TYPE = 2;
    private static final Integer SMALLER_DEVICE_TYPE = 1 - 1;

    private static final String DEFAULT_CMD_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CMD_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final String ENTITY_API_URL = "/api/tu-ya-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TuYaDeviceRepository tuYaDeviceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTuYaDeviceMockMvc;

    private TuYaDevice tuYaDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TuYaDevice createEntity(EntityManager em) {
        TuYaDevice tuYaDevice = new TuYaDevice()
            .deviceName(DEFAULT_DEVICE_NAME)
            .longCode(DEFAULT_LONG_CODE)
            .tyDeviceId(DEFAULT_TY_DEVICE_ID)
            .deviceType(DEFAULT_DEVICE_TYPE)
            .cmdCode(DEFAULT_CMD_CODE)
            .createTime(DEFAULT_CREATE_TIME)
            .enable(DEFAULT_ENABLE);
        return tuYaDevice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TuYaDevice createUpdatedEntity(EntityManager em) {
        TuYaDevice tuYaDevice = new TuYaDevice()
            .deviceName(UPDATED_DEVICE_NAME)
            .longCode(UPDATED_LONG_CODE)
            .tyDeviceId(UPDATED_TY_DEVICE_ID)
            .deviceType(UPDATED_DEVICE_TYPE)
            .cmdCode(UPDATED_CMD_CODE)
            .createTime(UPDATED_CREATE_TIME)
            .enable(UPDATED_ENABLE);
        return tuYaDevice;
    }

    @BeforeEach
    public void initTest() {
        tuYaDevice = createEntity(em);
    }

    @Test
    @Transactional
    void createTuYaDevice() throws Exception {
        int databaseSizeBeforeCreate = tuYaDeviceRepository.findAll().size();
        // Create the TuYaDevice
        restTuYaDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tuYaDevice)))
            .andExpect(status().isCreated());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        TuYaDevice testTuYaDevice = tuYaDeviceList.get(tuYaDeviceList.size() - 1);
        assertThat(testTuYaDevice.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
        assertThat(testTuYaDevice.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
        assertThat(testTuYaDevice.getTyDeviceId()).isEqualTo(DEFAULT_TY_DEVICE_ID);
        assertThat(testTuYaDevice.getDeviceType()).isEqualTo(DEFAULT_DEVICE_TYPE);
        assertThat(testTuYaDevice.getCmdCode()).isEqualTo(DEFAULT_CMD_CODE);
        assertThat(testTuYaDevice.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testTuYaDevice.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    void createTuYaDeviceWithExistingId() throws Exception {
        // Create the TuYaDevice with an existing ID
        tuYaDevice.setId(1L);

        int databaseSizeBeforeCreate = tuYaDeviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTuYaDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tuYaDevice)))
            .andExpect(status().isBadRequest());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTuYaDevices() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList
        restTuYaDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tuYaDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE.intValue())))
            .andExpect(jsonPath("$.[*].tyDeviceId").value(hasItem(DEFAULT_TY_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE)))
            .andExpect(jsonPath("$.[*].cmdCode").value(hasItem(DEFAULT_CMD_CODE)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getTuYaDevice() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get the tuYaDevice
        restTuYaDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, tuYaDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tuYaDevice.getId().intValue()))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME))
            .andExpect(jsonPath("$.longCode").value(DEFAULT_LONG_CODE.intValue()))
            .andExpect(jsonPath("$.tyDeviceId").value(DEFAULT_TY_DEVICE_ID))
            .andExpect(jsonPath("$.deviceType").value(DEFAULT_DEVICE_TYPE))
            .andExpect(jsonPath("$.cmdCode").value(DEFAULT_CMD_CODE))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getTuYaDevicesByIdFiltering() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        Long id = tuYaDevice.getId();

        defaultTuYaDeviceShouldBeFound("id.equals=" + id);
        defaultTuYaDeviceShouldNotBeFound("id.notEquals=" + id);

        defaultTuYaDeviceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTuYaDeviceShouldNotBeFound("id.greaterThan=" + id);

        defaultTuYaDeviceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTuYaDeviceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceName equals to DEFAULT_DEVICE_NAME
        defaultTuYaDeviceShouldBeFound("deviceName.equals=" + DEFAULT_DEVICE_NAME);

        // Get all the tuYaDeviceList where deviceName equals to UPDATED_DEVICE_NAME
        defaultTuYaDeviceShouldNotBeFound("deviceName.equals=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceName not equals to DEFAULT_DEVICE_NAME
        defaultTuYaDeviceShouldNotBeFound("deviceName.notEquals=" + DEFAULT_DEVICE_NAME);

        // Get all the tuYaDeviceList where deviceName not equals to UPDATED_DEVICE_NAME
        defaultTuYaDeviceShouldBeFound("deviceName.notEquals=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceNameIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceName in DEFAULT_DEVICE_NAME or UPDATED_DEVICE_NAME
        defaultTuYaDeviceShouldBeFound("deviceName.in=" + DEFAULT_DEVICE_NAME + "," + UPDATED_DEVICE_NAME);

        // Get all the tuYaDeviceList where deviceName equals to UPDATED_DEVICE_NAME
        defaultTuYaDeviceShouldNotBeFound("deviceName.in=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceName is not null
        defaultTuYaDeviceShouldBeFound("deviceName.specified=true");

        // Get all the tuYaDeviceList where deviceName is null
        defaultTuYaDeviceShouldNotBeFound("deviceName.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceNameContainsSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceName contains DEFAULT_DEVICE_NAME
        defaultTuYaDeviceShouldBeFound("deviceName.contains=" + DEFAULT_DEVICE_NAME);

        // Get all the tuYaDeviceList where deviceName contains UPDATED_DEVICE_NAME
        defaultTuYaDeviceShouldNotBeFound("deviceName.contains=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceNameNotContainsSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceName does not contain DEFAULT_DEVICE_NAME
        defaultTuYaDeviceShouldNotBeFound("deviceName.doesNotContain=" + DEFAULT_DEVICE_NAME);

        // Get all the tuYaDeviceList where deviceName does not contain UPDATED_DEVICE_NAME
        defaultTuYaDeviceShouldBeFound("deviceName.doesNotContain=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByLongCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where longCode equals to DEFAULT_LONG_CODE
        defaultTuYaDeviceShouldBeFound("longCode.equals=" + DEFAULT_LONG_CODE);

        // Get all the tuYaDeviceList where longCode equals to UPDATED_LONG_CODE
        defaultTuYaDeviceShouldNotBeFound("longCode.equals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByLongCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where longCode not equals to DEFAULT_LONG_CODE
        defaultTuYaDeviceShouldNotBeFound("longCode.notEquals=" + DEFAULT_LONG_CODE);

        // Get all the tuYaDeviceList where longCode not equals to UPDATED_LONG_CODE
        defaultTuYaDeviceShouldBeFound("longCode.notEquals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByLongCodeIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where longCode in DEFAULT_LONG_CODE or UPDATED_LONG_CODE
        defaultTuYaDeviceShouldBeFound("longCode.in=" + DEFAULT_LONG_CODE + "," + UPDATED_LONG_CODE);

        // Get all the tuYaDeviceList where longCode equals to UPDATED_LONG_CODE
        defaultTuYaDeviceShouldNotBeFound("longCode.in=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByLongCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where longCode is not null
        defaultTuYaDeviceShouldBeFound("longCode.specified=true");

        // Get all the tuYaDeviceList where longCode is null
        defaultTuYaDeviceShouldNotBeFound("longCode.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByLongCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where longCode is greater than or equal to DEFAULT_LONG_CODE
        defaultTuYaDeviceShouldBeFound("longCode.greaterThanOrEqual=" + DEFAULT_LONG_CODE);

        // Get all the tuYaDeviceList where longCode is greater than or equal to UPDATED_LONG_CODE
        defaultTuYaDeviceShouldNotBeFound("longCode.greaterThanOrEqual=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByLongCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where longCode is less than or equal to DEFAULT_LONG_CODE
        defaultTuYaDeviceShouldBeFound("longCode.lessThanOrEqual=" + DEFAULT_LONG_CODE);

        // Get all the tuYaDeviceList where longCode is less than or equal to SMALLER_LONG_CODE
        defaultTuYaDeviceShouldNotBeFound("longCode.lessThanOrEqual=" + SMALLER_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByLongCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where longCode is less than DEFAULT_LONG_CODE
        defaultTuYaDeviceShouldNotBeFound("longCode.lessThan=" + DEFAULT_LONG_CODE);

        // Get all the tuYaDeviceList where longCode is less than UPDATED_LONG_CODE
        defaultTuYaDeviceShouldBeFound("longCode.lessThan=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByLongCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where longCode is greater than DEFAULT_LONG_CODE
        defaultTuYaDeviceShouldNotBeFound("longCode.greaterThan=" + DEFAULT_LONG_CODE);

        // Get all the tuYaDeviceList where longCode is greater than SMALLER_LONG_CODE
        defaultTuYaDeviceShouldBeFound("longCode.greaterThan=" + SMALLER_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByTyDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where tyDeviceId equals to DEFAULT_TY_DEVICE_ID
        defaultTuYaDeviceShouldBeFound("tyDeviceId.equals=" + DEFAULT_TY_DEVICE_ID);

        // Get all the tuYaDeviceList where tyDeviceId equals to UPDATED_TY_DEVICE_ID
        defaultTuYaDeviceShouldNotBeFound("tyDeviceId.equals=" + UPDATED_TY_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByTyDeviceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where tyDeviceId not equals to DEFAULT_TY_DEVICE_ID
        defaultTuYaDeviceShouldNotBeFound("tyDeviceId.notEquals=" + DEFAULT_TY_DEVICE_ID);

        // Get all the tuYaDeviceList where tyDeviceId not equals to UPDATED_TY_DEVICE_ID
        defaultTuYaDeviceShouldBeFound("tyDeviceId.notEquals=" + UPDATED_TY_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByTyDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where tyDeviceId in DEFAULT_TY_DEVICE_ID or UPDATED_TY_DEVICE_ID
        defaultTuYaDeviceShouldBeFound("tyDeviceId.in=" + DEFAULT_TY_DEVICE_ID + "," + UPDATED_TY_DEVICE_ID);

        // Get all the tuYaDeviceList where tyDeviceId equals to UPDATED_TY_DEVICE_ID
        defaultTuYaDeviceShouldNotBeFound("tyDeviceId.in=" + UPDATED_TY_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByTyDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where tyDeviceId is not null
        defaultTuYaDeviceShouldBeFound("tyDeviceId.specified=true");

        // Get all the tuYaDeviceList where tyDeviceId is null
        defaultTuYaDeviceShouldNotBeFound("tyDeviceId.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByTyDeviceIdContainsSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where tyDeviceId contains DEFAULT_TY_DEVICE_ID
        defaultTuYaDeviceShouldBeFound("tyDeviceId.contains=" + DEFAULT_TY_DEVICE_ID);

        // Get all the tuYaDeviceList where tyDeviceId contains UPDATED_TY_DEVICE_ID
        defaultTuYaDeviceShouldNotBeFound("tyDeviceId.contains=" + UPDATED_TY_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByTyDeviceIdNotContainsSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where tyDeviceId does not contain DEFAULT_TY_DEVICE_ID
        defaultTuYaDeviceShouldNotBeFound("tyDeviceId.doesNotContain=" + DEFAULT_TY_DEVICE_ID);

        // Get all the tuYaDeviceList where tyDeviceId does not contain UPDATED_TY_DEVICE_ID
        defaultTuYaDeviceShouldBeFound("tyDeviceId.doesNotContain=" + UPDATED_TY_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceType equals to DEFAULT_DEVICE_TYPE
        defaultTuYaDeviceShouldBeFound("deviceType.equals=" + DEFAULT_DEVICE_TYPE);

        // Get all the tuYaDeviceList where deviceType equals to UPDATED_DEVICE_TYPE
        defaultTuYaDeviceShouldNotBeFound("deviceType.equals=" + UPDATED_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceType not equals to DEFAULT_DEVICE_TYPE
        defaultTuYaDeviceShouldNotBeFound("deviceType.notEquals=" + DEFAULT_DEVICE_TYPE);

        // Get all the tuYaDeviceList where deviceType not equals to UPDATED_DEVICE_TYPE
        defaultTuYaDeviceShouldBeFound("deviceType.notEquals=" + UPDATED_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceType in DEFAULT_DEVICE_TYPE or UPDATED_DEVICE_TYPE
        defaultTuYaDeviceShouldBeFound("deviceType.in=" + DEFAULT_DEVICE_TYPE + "," + UPDATED_DEVICE_TYPE);

        // Get all the tuYaDeviceList where deviceType equals to UPDATED_DEVICE_TYPE
        defaultTuYaDeviceShouldNotBeFound("deviceType.in=" + UPDATED_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceType is not null
        defaultTuYaDeviceShouldBeFound("deviceType.specified=true");

        // Get all the tuYaDeviceList where deviceType is null
        defaultTuYaDeviceShouldNotBeFound("deviceType.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceType is greater than or equal to DEFAULT_DEVICE_TYPE
        defaultTuYaDeviceShouldBeFound("deviceType.greaterThanOrEqual=" + DEFAULT_DEVICE_TYPE);

        // Get all the tuYaDeviceList where deviceType is greater than or equal to UPDATED_DEVICE_TYPE
        defaultTuYaDeviceShouldNotBeFound("deviceType.greaterThanOrEqual=" + UPDATED_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceType is less than or equal to DEFAULT_DEVICE_TYPE
        defaultTuYaDeviceShouldBeFound("deviceType.lessThanOrEqual=" + DEFAULT_DEVICE_TYPE);

        // Get all the tuYaDeviceList where deviceType is less than or equal to SMALLER_DEVICE_TYPE
        defaultTuYaDeviceShouldNotBeFound("deviceType.lessThanOrEqual=" + SMALLER_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceType is less than DEFAULT_DEVICE_TYPE
        defaultTuYaDeviceShouldNotBeFound("deviceType.lessThan=" + DEFAULT_DEVICE_TYPE);

        // Get all the tuYaDeviceList where deviceType is less than UPDATED_DEVICE_TYPE
        defaultTuYaDeviceShouldBeFound("deviceType.lessThan=" + UPDATED_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByDeviceTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where deviceType is greater than DEFAULT_DEVICE_TYPE
        defaultTuYaDeviceShouldNotBeFound("deviceType.greaterThan=" + DEFAULT_DEVICE_TYPE);

        // Get all the tuYaDeviceList where deviceType is greater than SMALLER_DEVICE_TYPE
        defaultTuYaDeviceShouldBeFound("deviceType.greaterThan=" + SMALLER_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCmdCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where cmdCode equals to DEFAULT_CMD_CODE
        defaultTuYaDeviceShouldBeFound("cmdCode.equals=" + DEFAULT_CMD_CODE);

        // Get all the tuYaDeviceList where cmdCode equals to UPDATED_CMD_CODE
        defaultTuYaDeviceShouldNotBeFound("cmdCode.equals=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCmdCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where cmdCode not equals to DEFAULT_CMD_CODE
        defaultTuYaDeviceShouldNotBeFound("cmdCode.notEquals=" + DEFAULT_CMD_CODE);

        // Get all the tuYaDeviceList where cmdCode not equals to UPDATED_CMD_CODE
        defaultTuYaDeviceShouldBeFound("cmdCode.notEquals=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCmdCodeIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where cmdCode in DEFAULT_CMD_CODE or UPDATED_CMD_CODE
        defaultTuYaDeviceShouldBeFound("cmdCode.in=" + DEFAULT_CMD_CODE + "," + UPDATED_CMD_CODE);

        // Get all the tuYaDeviceList where cmdCode equals to UPDATED_CMD_CODE
        defaultTuYaDeviceShouldNotBeFound("cmdCode.in=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCmdCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where cmdCode is not null
        defaultTuYaDeviceShouldBeFound("cmdCode.specified=true");

        // Get all the tuYaDeviceList where cmdCode is null
        defaultTuYaDeviceShouldNotBeFound("cmdCode.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCmdCodeContainsSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where cmdCode contains DEFAULT_CMD_CODE
        defaultTuYaDeviceShouldBeFound("cmdCode.contains=" + DEFAULT_CMD_CODE);

        // Get all the tuYaDeviceList where cmdCode contains UPDATED_CMD_CODE
        defaultTuYaDeviceShouldNotBeFound("cmdCode.contains=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCmdCodeNotContainsSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where cmdCode does not contain DEFAULT_CMD_CODE
        defaultTuYaDeviceShouldNotBeFound("cmdCode.doesNotContain=" + DEFAULT_CMD_CODE);

        // Get all the tuYaDeviceList where cmdCode does not contain UPDATED_CMD_CODE
        defaultTuYaDeviceShouldBeFound("cmdCode.doesNotContain=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCreateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where createTime equals to DEFAULT_CREATE_TIME
        defaultTuYaDeviceShouldBeFound("createTime.equals=" + DEFAULT_CREATE_TIME);

        // Get all the tuYaDeviceList where createTime equals to UPDATED_CREATE_TIME
        defaultTuYaDeviceShouldNotBeFound("createTime.equals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCreateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where createTime not equals to DEFAULT_CREATE_TIME
        defaultTuYaDeviceShouldNotBeFound("createTime.notEquals=" + DEFAULT_CREATE_TIME);

        // Get all the tuYaDeviceList where createTime not equals to UPDATED_CREATE_TIME
        defaultTuYaDeviceShouldBeFound("createTime.notEquals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCreateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where createTime in DEFAULT_CREATE_TIME or UPDATED_CREATE_TIME
        defaultTuYaDeviceShouldBeFound("createTime.in=" + DEFAULT_CREATE_TIME + "," + UPDATED_CREATE_TIME);

        // Get all the tuYaDeviceList where createTime equals to UPDATED_CREATE_TIME
        defaultTuYaDeviceShouldNotBeFound("createTime.in=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByCreateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where createTime is not null
        defaultTuYaDeviceShouldBeFound("createTime.specified=true");

        // Get all the tuYaDeviceList where createTime is null
        defaultTuYaDeviceShouldNotBeFound("createTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where enable equals to DEFAULT_ENABLE
        defaultTuYaDeviceShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the tuYaDeviceList where enable equals to UPDATED_ENABLE
        defaultTuYaDeviceShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where enable not equals to DEFAULT_ENABLE
        defaultTuYaDeviceShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the tuYaDeviceList where enable not equals to UPDATED_ENABLE
        defaultTuYaDeviceShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultTuYaDeviceShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the tuYaDeviceList where enable equals to UPDATED_ENABLE
        defaultTuYaDeviceShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        // Get all the tuYaDeviceList where enable is not null
        defaultTuYaDeviceShouldBeFound("enable.specified=true");

        // Get all the tuYaDeviceList where enable is null
        defaultTuYaDeviceShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByTuYaCmdIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);
        TuYaCmd tuYaCmd = TuYaCmdResourceIT.createEntity(em);
        em.persist(tuYaCmd);
        em.flush();
        tuYaDevice.addTuYaCmd(tuYaCmd);
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);
        Long tuYaCmdId = tuYaCmd.getId();

        // Get all the tuYaDeviceList where tuYaCmd equals to tuYaCmdId
        defaultTuYaDeviceShouldBeFound("tuYaCmdId.equals=" + tuYaCmdId);

        // Get all the tuYaDeviceList where tuYaCmd equals to (tuYaCmdId + 1)
        defaultTuYaDeviceShouldNotBeFound("tuYaCmdId.equals=" + (tuYaCmdId + 1));
    }

    @Test
    @Transactional
    void getAllTuYaDevicesByRoomAddrIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);
        RoomAddr roomAddr = RoomAddrResourceIT.createEntity(em);
        em.persist(roomAddr);
        em.flush();
        tuYaDevice.setRoomAddr(roomAddr);
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);
        Long roomAddrId = roomAddr.getId();

        // Get all the tuYaDeviceList where roomAddr equals to roomAddrId
        defaultTuYaDeviceShouldBeFound("roomAddrId.equals=" + roomAddrId);

        // Get all the tuYaDeviceList where roomAddr equals to (roomAddrId + 1)
        defaultTuYaDeviceShouldNotBeFound("roomAddrId.equals=" + (roomAddrId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTuYaDeviceShouldBeFound(String filter) throws Exception {
        restTuYaDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tuYaDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE.intValue())))
            .andExpect(jsonPath("$.[*].tyDeviceId").value(hasItem(DEFAULT_TY_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE)))
            .andExpect(jsonPath("$.[*].cmdCode").value(hasItem(DEFAULT_CMD_CODE)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));

        // Check, that the count call also returns 1
        restTuYaDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTuYaDeviceShouldNotBeFound(String filter) throws Exception {
        restTuYaDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTuYaDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTuYaDevice() throws Exception {
        // Get the tuYaDevice
        restTuYaDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTuYaDevice() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();

        // Update the tuYaDevice
        TuYaDevice updatedTuYaDevice = tuYaDeviceRepository.findById(tuYaDevice.getId()).get();
        // Disconnect from session so that the updates on updatedTuYaDevice are not directly saved in db
        em.detach(updatedTuYaDevice);
        updatedTuYaDevice
            .deviceName(UPDATED_DEVICE_NAME)
            .longCode(UPDATED_LONG_CODE)
            .tyDeviceId(UPDATED_TY_DEVICE_ID)
            .deviceType(UPDATED_DEVICE_TYPE)
            .cmdCode(UPDATED_CMD_CODE)
            .createTime(UPDATED_CREATE_TIME)
            .enable(UPDATED_ENABLE);

        restTuYaDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTuYaDevice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTuYaDevice))
            )
            .andExpect(status().isOk());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
        TuYaDevice testTuYaDevice = tuYaDeviceList.get(tuYaDeviceList.size() - 1);
        assertThat(testTuYaDevice.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
        assertThat(testTuYaDevice.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testTuYaDevice.getTyDeviceId()).isEqualTo(UPDATED_TY_DEVICE_ID);
        assertThat(testTuYaDevice.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
        assertThat(testTuYaDevice.getCmdCode()).isEqualTo(UPDATED_CMD_CODE);
        assertThat(testTuYaDevice.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testTuYaDevice.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void putNonExistingTuYaDevice() throws Exception {
        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();
        tuYaDevice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTuYaDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tuYaDevice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tuYaDevice))
            )
            .andExpect(status().isBadRequest());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTuYaDevice() throws Exception {
        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();
        tuYaDevice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTuYaDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tuYaDevice))
            )
            .andExpect(status().isBadRequest());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTuYaDevice() throws Exception {
        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();
        tuYaDevice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTuYaDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tuYaDevice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTuYaDeviceWithPatch() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();

        // Update the tuYaDevice using partial update
        TuYaDevice partialUpdatedTuYaDevice = new TuYaDevice();
        partialUpdatedTuYaDevice.setId(tuYaDevice.getId());

        partialUpdatedTuYaDevice.deviceName(UPDATED_DEVICE_NAME).longCode(UPDATED_LONG_CODE);

        restTuYaDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTuYaDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTuYaDevice))
            )
            .andExpect(status().isOk());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
        TuYaDevice testTuYaDevice = tuYaDeviceList.get(tuYaDeviceList.size() - 1);
        assertThat(testTuYaDevice.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
        assertThat(testTuYaDevice.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testTuYaDevice.getTyDeviceId()).isEqualTo(DEFAULT_TY_DEVICE_ID);
        assertThat(testTuYaDevice.getDeviceType()).isEqualTo(DEFAULT_DEVICE_TYPE);
        assertThat(testTuYaDevice.getCmdCode()).isEqualTo(DEFAULT_CMD_CODE);
        assertThat(testTuYaDevice.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testTuYaDevice.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    void fullUpdateTuYaDeviceWithPatch() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();

        // Update the tuYaDevice using partial update
        TuYaDevice partialUpdatedTuYaDevice = new TuYaDevice();
        partialUpdatedTuYaDevice.setId(tuYaDevice.getId());

        partialUpdatedTuYaDevice
            .deviceName(UPDATED_DEVICE_NAME)
            .longCode(UPDATED_LONG_CODE)
            .tyDeviceId(UPDATED_TY_DEVICE_ID)
            .deviceType(UPDATED_DEVICE_TYPE)
            .cmdCode(UPDATED_CMD_CODE)
            .createTime(UPDATED_CREATE_TIME)
            .enable(UPDATED_ENABLE);

        restTuYaDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTuYaDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTuYaDevice))
            )
            .andExpect(status().isOk());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
        TuYaDevice testTuYaDevice = tuYaDeviceList.get(tuYaDeviceList.size() - 1);
        assertThat(testTuYaDevice.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
        assertThat(testTuYaDevice.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testTuYaDevice.getTyDeviceId()).isEqualTo(UPDATED_TY_DEVICE_ID);
        assertThat(testTuYaDevice.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
        assertThat(testTuYaDevice.getCmdCode()).isEqualTo(UPDATED_CMD_CODE);
        assertThat(testTuYaDevice.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testTuYaDevice.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void patchNonExistingTuYaDevice() throws Exception {
        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();
        tuYaDevice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTuYaDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tuYaDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tuYaDevice))
            )
            .andExpect(status().isBadRequest());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTuYaDevice() throws Exception {
        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();
        tuYaDevice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTuYaDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tuYaDevice))
            )
            .andExpect(status().isBadRequest());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTuYaDevice() throws Exception {
        int databaseSizeBeforeUpdate = tuYaDeviceRepository.findAll().size();
        tuYaDevice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTuYaDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tuYaDevice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TuYaDevice in the database
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTuYaDevice() throws Exception {
        // Initialize the database
        tuYaDeviceRepository.saveAndFlush(tuYaDevice);

        int databaseSizeBeforeDelete = tuYaDeviceRepository.findAll().size();

        // Delete the tuYaDevice
        restTuYaDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, tuYaDevice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TuYaDevice> tuYaDeviceList = tuYaDeviceRepository.findAll();
        assertThat(tuYaDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
