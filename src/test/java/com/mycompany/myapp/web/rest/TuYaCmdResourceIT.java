package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TuYaCmd;
import com.mycompany.myapp.domain.TuYaDevice;
import com.mycompany.myapp.domain.enumeration.CmdType;
import com.mycompany.myapp.repository.TuYaCmdRepository;
import com.mycompany.myapp.service.criteria.TuYaCmdCriteria;
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
 * Integration tests for the {@link TuYaCmdResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TuYaCmdResourceIT {

    private static final String DEFAULT_CMD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CMD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CMD_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CMD_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VALUE = false;
    private static final Boolean UPDATED_VALUE = true;

    private static final CmdType DEFAULT_CMD_TYPE = CmdType.ON;
    private static final CmdType UPDATED_CMD_TYPE = CmdType.OFF;

    private static final Instant DEFAULT_CREATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final String ENTITY_API_URL = "/api/tu-ya-cmds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TuYaCmdRepository tuYaCmdRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTuYaCmdMockMvc;

    private TuYaCmd tuYaCmd;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TuYaCmd createEntity(EntityManager em) {
        TuYaCmd tuYaCmd = new TuYaCmd()
            .cmdName(DEFAULT_CMD_NAME)
            .cmdCode(DEFAULT_CMD_CODE)
            .value(DEFAULT_VALUE)
            .cmdType(DEFAULT_CMD_TYPE)
            .createTime(DEFAULT_CREATE_TIME)
            .enable(DEFAULT_ENABLE);
        return tuYaCmd;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TuYaCmd createUpdatedEntity(EntityManager em) {
        TuYaCmd tuYaCmd = new TuYaCmd()
            .cmdName(UPDATED_CMD_NAME)
            .cmdCode(UPDATED_CMD_CODE)
            .value(UPDATED_VALUE)
            .cmdType(UPDATED_CMD_TYPE)
            .createTime(UPDATED_CREATE_TIME)
            .enable(UPDATED_ENABLE);
        return tuYaCmd;
    }

    @BeforeEach
    public void initTest() {
        tuYaCmd = createEntity(em);
    }

    @Test
    @Transactional
    void createTuYaCmd() throws Exception {
        int databaseSizeBeforeCreate = tuYaCmdRepository.findAll().size();
        // Create the TuYaCmd
        restTuYaCmdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tuYaCmd)))
            .andExpect(status().isCreated());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeCreate + 1);
        TuYaCmd testTuYaCmd = tuYaCmdList.get(tuYaCmdList.size() - 1);
        assertThat(testTuYaCmd.getCmdName()).isEqualTo(DEFAULT_CMD_NAME);
        assertThat(testTuYaCmd.getCmdCode()).isEqualTo(DEFAULT_CMD_CODE);
        assertThat(testTuYaCmd.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testTuYaCmd.getCmdType()).isEqualTo(DEFAULT_CMD_TYPE);
        assertThat(testTuYaCmd.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testTuYaCmd.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    void createTuYaCmdWithExistingId() throws Exception {
        // Create the TuYaCmd with an existing ID
        tuYaCmd.setId(1L);

        int databaseSizeBeforeCreate = tuYaCmdRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTuYaCmdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tuYaCmd)))
            .andExpect(status().isBadRequest());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTuYaCmds() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList
        restTuYaCmdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tuYaCmd.getId().intValue())))
            .andExpect(jsonPath("$.[*].cmdName").value(hasItem(DEFAULT_CMD_NAME)))
            .andExpect(jsonPath("$.[*].cmdCode").value(hasItem(DEFAULT_CMD_CODE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.booleanValue())))
            .andExpect(jsonPath("$.[*].cmdType").value(hasItem(DEFAULT_CMD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getTuYaCmd() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get the tuYaCmd
        restTuYaCmdMockMvc
            .perform(get(ENTITY_API_URL_ID, tuYaCmd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tuYaCmd.getId().intValue()))
            .andExpect(jsonPath("$.cmdName").value(DEFAULT_CMD_NAME))
            .andExpect(jsonPath("$.cmdCode").value(DEFAULT_CMD_CODE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.booleanValue()))
            .andExpect(jsonPath("$.cmdType").value(DEFAULT_CMD_TYPE.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getTuYaCmdsByIdFiltering() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        Long id = tuYaCmd.getId();

        defaultTuYaCmdShouldBeFound("id.equals=" + id);
        defaultTuYaCmdShouldNotBeFound("id.notEquals=" + id);

        defaultTuYaCmdShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTuYaCmdShouldNotBeFound("id.greaterThan=" + id);

        defaultTuYaCmdShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTuYaCmdShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdName equals to DEFAULT_CMD_NAME
        defaultTuYaCmdShouldBeFound("cmdName.equals=" + DEFAULT_CMD_NAME);

        // Get all the tuYaCmdList where cmdName equals to UPDATED_CMD_NAME
        defaultTuYaCmdShouldNotBeFound("cmdName.equals=" + UPDATED_CMD_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdName not equals to DEFAULT_CMD_NAME
        defaultTuYaCmdShouldNotBeFound("cmdName.notEquals=" + DEFAULT_CMD_NAME);

        // Get all the tuYaCmdList where cmdName not equals to UPDATED_CMD_NAME
        defaultTuYaCmdShouldBeFound("cmdName.notEquals=" + UPDATED_CMD_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdNameIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdName in DEFAULT_CMD_NAME or UPDATED_CMD_NAME
        defaultTuYaCmdShouldBeFound("cmdName.in=" + DEFAULT_CMD_NAME + "," + UPDATED_CMD_NAME);

        // Get all the tuYaCmdList where cmdName equals to UPDATED_CMD_NAME
        defaultTuYaCmdShouldNotBeFound("cmdName.in=" + UPDATED_CMD_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdName is not null
        defaultTuYaCmdShouldBeFound("cmdName.specified=true");

        // Get all the tuYaCmdList where cmdName is null
        defaultTuYaCmdShouldNotBeFound("cmdName.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdNameContainsSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdName contains DEFAULT_CMD_NAME
        defaultTuYaCmdShouldBeFound("cmdName.contains=" + DEFAULT_CMD_NAME);

        // Get all the tuYaCmdList where cmdName contains UPDATED_CMD_NAME
        defaultTuYaCmdShouldNotBeFound("cmdName.contains=" + UPDATED_CMD_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdNameNotContainsSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdName does not contain DEFAULT_CMD_NAME
        defaultTuYaCmdShouldNotBeFound("cmdName.doesNotContain=" + DEFAULT_CMD_NAME);

        // Get all the tuYaCmdList where cmdName does not contain UPDATED_CMD_NAME
        defaultTuYaCmdShouldBeFound("cmdName.doesNotContain=" + UPDATED_CMD_NAME);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdCode equals to DEFAULT_CMD_CODE
        defaultTuYaCmdShouldBeFound("cmdCode.equals=" + DEFAULT_CMD_CODE);

        // Get all the tuYaCmdList where cmdCode equals to UPDATED_CMD_CODE
        defaultTuYaCmdShouldNotBeFound("cmdCode.equals=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdCode not equals to DEFAULT_CMD_CODE
        defaultTuYaCmdShouldNotBeFound("cmdCode.notEquals=" + DEFAULT_CMD_CODE);

        // Get all the tuYaCmdList where cmdCode not equals to UPDATED_CMD_CODE
        defaultTuYaCmdShouldBeFound("cmdCode.notEquals=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdCodeIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdCode in DEFAULT_CMD_CODE or UPDATED_CMD_CODE
        defaultTuYaCmdShouldBeFound("cmdCode.in=" + DEFAULT_CMD_CODE + "," + UPDATED_CMD_CODE);

        // Get all the tuYaCmdList where cmdCode equals to UPDATED_CMD_CODE
        defaultTuYaCmdShouldNotBeFound("cmdCode.in=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdCode is not null
        defaultTuYaCmdShouldBeFound("cmdCode.specified=true");

        // Get all the tuYaCmdList where cmdCode is null
        defaultTuYaCmdShouldNotBeFound("cmdCode.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdCodeContainsSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdCode contains DEFAULT_CMD_CODE
        defaultTuYaCmdShouldBeFound("cmdCode.contains=" + DEFAULT_CMD_CODE);

        // Get all the tuYaCmdList where cmdCode contains UPDATED_CMD_CODE
        defaultTuYaCmdShouldNotBeFound("cmdCode.contains=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdCodeNotContainsSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdCode does not contain DEFAULT_CMD_CODE
        defaultTuYaCmdShouldNotBeFound("cmdCode.doesNotContain=" + DEFAULT_CMD_CODE);

        // Get all the tuYaCmdList where cmdCode does not contain UPDATED_CMD_CODE
        defaultTuYaCmdShouldBeFound("cmdCode.doesNotContain=" + UPDATED_CMD_CODE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where value equals to DEFAULT_VALUE
        defaultTuYaCmdShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the tuYaCmdList where value equals to UPDATED_VALUE
        defaultTuYaCmdShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where value not equals to DEFAULT_VALUE
        defaultTuYaCmdShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the tuYaCmdList where value not equals to UPDATED_VALUE
        defaultTuYaCmdShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultTuYaCmdShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the tuYaCmdList where value equals to UPDATED_VALUE
        defaultTuYaCmdShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where value is not null
        defaultTuYaCmdShouldBeFound("value.specified=true");

        // Get all the tuYaCmdList where value is null
        defaultTuYaCmdShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdType equals to DEFAULT_CMD_TYPE
        defaultTuYaCmdShouldBeFound("cmdType.equals=" + DEFAULT_CMD_TYPE);

        // Get all the tuYaCmdList where cmdType equals to UPDATED_CMD_TYPE
        defaultTuYaCmdShouldNotBeFound("cmdType.equals=" + UPDATED_CMD_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdType not equals to DEFAULT_CMD_TYPE
        defaultTuYaCmdShouldNotBeFound("cmdType.notEquals=" + DEFAULT_CMD_TYPE);

        // Get all the tuYaCmdList where cmdType not equals to UPDATED_CMD_TYPE
        defaultTuYaCmdShouldBeFound("cmdType.notEquals=" + UPDATED_CMD_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdTypeIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdType in DEFAULT_CMD_TYPE or UPDATED_CMD_TYPE
        defaultTuYaCmdShouldBeFound("cmdType.in=" + DEFAULT_CMD_TYPE + "," + UPDATED_CMD_TYPE);

        // Get all the tuYaCmdList where cmdType equals to UPDATED_CMD_TYPE
        defaultTuYaCmdShouldNotBeFound("cmdType.in=" + UPDATED_CMD_TYPE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCmdTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where cmdType is not null
        defaultTuYaCmdShouldBeFound("cmdType.specified=true");

        // Get all the tuYaCmdList where cmdType is null
        defaultTuYaCmdShouldNotBeFound("cmdType.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCreateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where createTime equals to DEFAULT_CREATE_TIME
        defaultTuYaCmdShouldBeFound("createTime.equals=" + DEFAULT_CREATE_TIME);

        // Get all the tuYaCmdList where createTime equals to UPDATED_CREATE_TIME
        defaultTuYaCmdShouldNotBeFound("createTime.equals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCreateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where createTime not equals to DEFAULT_CREATE_TIME
        defaultTuYaCmdShouldNotBeFound("createTime.notEquals=" + DEFAULT_CREATE_TIME);

        // Get all the tuYaCmdList where createTime not equals to UPDATED_CREATE_TIME
        defaultTuYaCmdShouldBeFound("createTime.notEquals=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCreateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where createTime in DEFAULT_CREATE_TIME or UPDATED_CREATE_TIME
        defaultTuYaCmdShouldBeFound("createTime.in=" + DEFAULT_CREATE_TIME + "," + UPDATED_CREATE_TIME);

        // Get all the tuYaCmdList where createTime equals to UPDATED_CREATE_TIME
        defaultTuYaCmdShouldNotBeFound("createTime.in=" + UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByCreateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where createTime is not null
        defaultTuYaCmdShouldBeFound("createTime.specified=true");

        // Get all the tuYaCmdList where createTime is null
        defaultTuYaCmdShouldNotBeFound("createTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where enable equals to DEFAULT_ENABLE
        defaultTuYaCmdShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the tuYaCmdList where enable equals to UPDATED_ENABLE
        defaultTuYaCmdShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where enable not equals to DEFAULT_ENABLE
        defaultTuYaCmdShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the tuYaCmdList where enable not equals to UPDATED_ENABLE
        defaultTuYaCmdShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultTuYaCmdShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the tuYaCmdList where enable equals to UPDATED_ENABLE
        defaultTuYaCmdShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        // Get all the tuYaCmdList where enable is not null
        defaultTuYaCmdShouldBeFound("enable.specified=true");

        // Get all the tuYaCmdList where enable is null
        defaultTuYaCmdShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllTuYaCmdsByTuYaDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);
        TuYaDevice tuYaDevice = TuYaDeviceResourceIT.createEntity(em);
        em.persist(tuYaDevice);
        em.flush();
        tuYaCmd.setTuYaDevice(tuYaDevice);
        tuYaCmdRepository.saveAndFlush(tuYaCmd);
        Long tuYaDeviceId = tuYaDevice.getId();

        // Get all the tuYaCmdList where tuYaDevice equals to tuYaDeviceId
        defaultTuYaCmdShouldBeFound("tuYaDeviceId.equals=" + tuYaDeviceId);

        // Get all the tuYaCmdList where tuYaDevice equals to (tuYaDeviceId + 1)
        defaultTuYaCmdShouldNotBeFound("tuYaDeviceId.equals=" + (tuYaDeviceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTuYaCmdShouldBeFound(String filter) throws Exception {
        restTuYaCmdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tuYaCmd.getId().intValue())))
            .andExpect(jsonPath("$.[*].cmdName").value(hasItem(DEFAULT_CMD_NAME)))
            .andExpect(jsonPath("$.[*].cmdCode").value(hasItem(DEFAULT_CMD_CODE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.booleanValue())))
            .andExpect(jsonPath("$.[*].cmdType").value(hasItem(DEFAULT_CMD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));

        // Check, that the count call also returns 1
        restTuYaCmdMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTuYaCmdShouldNotBeFound(String filter) throws Exception {
        restTuYaCmdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTuYaCmdMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTuYaCmd() throws Exception {
        // Get the tuYaCmd
        restTuYaCmdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTuYaCmd() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();

        // Update the tuYaCmd
        TuYaCmd updatedTuYaCmd = tuYaCmdRepository.findById(tuYaCmd.getId()).get();
        // Disconnect from session so that the updates on updatedTuYaCmd are not directly saved in db
        em.detach(updatedTuYaCmd);
        updatedTuYaCmd
            .cmdName(UPDATED_CMD_NAME)
            .cmdCode(UPDATED_CMD_CODE)
            .value(UPDATED_VALUE)
            .cmdType(UPDATED_CMD_TYPE)
            .createTime(UPDATED_CREATE_TIME)
            .enable(UPDATED_ENABLE);

        restTuYaCmdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTuYaCmd.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTuYaCmd))
            )
            .andExpect(status().isOk());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
        TuYaCmd testTuYaCmd = tuYaCmdList.get(tuYaCmdList.size() - 1);
        assertThat(testTuYaCmd.getCmdName()).isEqualTo(UPDATED_CMD_NAME);
        assertThat(testTuYaCmd.getCmdCode()).isEqualTo(UPDATED_CMD_CODE);
        assertThat(testTuYaCmd.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTuYaCmd.getCmdType()).isEqualTo(UPDATED_CMD_TYPE);
        assertThat(testTuYaCmd.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testTuYaCmd.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void putNonExistingTuYaCmd() throws Exception {
        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();
        tuYaCmd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTuYaCmdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tuYaCmd.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tuYaCmd))
            )
            .andExpect(status().isBadRequest());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTuYaCmd() throws Exception {
        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();
        tuYaCmd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTuYaCmdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tuYaCmd))
            )
            .andExpect(status().isBadRequest());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTuYaCmd() throws Exception {
        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();
        tuYaCmd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTuYaCmdMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tuYaCmd)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTuYaCmdWithPatch() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();

        // Update the tuYaCmd using partial update
        TuYaCmd partialUpdatedTuYaCmd = new TuYaCmd();
        partialUpdatedTuYaCmd.setId(tuYaCmd.getId());

        partialUpdatedTuYaCmd.enable(UPDATED_ENABLE);

        restTuYaCmdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTuYaCmd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTuYaCmd))
            )
            .andExpect(status().isOk());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
        TuYaCmd testTuYaCmd = tuYaCmdList.get(tuYaCmdList.size() - 1);
        assertThat(testTuYaCmd.getCmdName()).isEqualTo(DEFAULT_CMD_NAME);
        assertThat(testTuYaCmd.getCmdCode()).isEqualTo(DEFAULT_CMD_CODE);
        assertThat(testTuYaCmd.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testTuYaCmd.getCmdType()).isEqualTo(DEFAULT_CMD_TYPE);
        assertThat(testTuYaCmd.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testTuYaCmd.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void fullUpdateTuYaCmdWithPatch() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();

        // Update the tuYaCmd using partial update
        TuYaCmd partialUpdatedTuYaCmd = new TuYaCmd();
        partialUpdatedTuYaCmd.setId(tuYaCmd.getId());

        partialUpdatedTuYaCmd
            .cmdName(UPDATED_CMD_NAME)
            .cmdCode(UPDATED_CMD_CODE)
            .value(UPDATED_VALUE)
            .cmdType(UPDATED_CMD_TYPE)
            .createTime(UPDATED_CREATE_TIME)
            .enable(UPDATED_ENABLE);

        restTuYaCmdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTuYaCmd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTuYaCmd))
            )
            .andExpect(status().isOk());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
        TuYaCmd testTuYaCmd = tuYaCmdList.get(tuYaCmdList.size() - 1);
        assertThat(testTuYaCmd.getCmdName()).isEqualTo(UPDATED_CMD_NAME);
        assertThat(testTuYaCmd.getCmdCode()).isEqualTo(UPDATED_CMD_CODE);
        assertThat(testTuYaCmd.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTuYaCmd.getCmdType()).isEqualTo(UPDATED_CMD_TYPE);
        assertThat(testTuYaCmd.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testTuYaCmd.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void patchNonExistingTuYaCmd() throws Exception {
        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();
        tuYaCmd.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTuYaCmdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tuYaCmd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tuYaCmd))
            )
            .andExpect(status().isBadRequest());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTuYaCmd() throws Exception {
        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();
        tuYaCmd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTuYaCmdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tuYaCmd))
            )
            .andExpect(status().isBadRequest());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTuYaCmd() throws Exception {
        int databaseSizeBeforeUpdate = tuYaCmdRepository.findAll().size();
        tuYaCmd.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTuYaCmdMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tuYaCmd)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TuYaCmd in the database
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTuYaCmd() throws Exception {
        // Initialize the database
        tuYaCmdRepository.saveAndFlush(tuYaCmd);

        int databaseSizeBeforeDelete = tuYaCmdRepository.findAll().size();

        // Delete the tuYaCmd
        restTuYaCmdMockMvc
            .perform(delete(ENTITY_API_URL_ID, tuYaCmd.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TuYaCmd> tuYaCmdList = tuYaCmdRepository.findAll();
        assertThat(tuYaCmdList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
