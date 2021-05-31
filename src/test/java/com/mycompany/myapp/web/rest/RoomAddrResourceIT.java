package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Buildings;
import com.mycompany.myapp.domain.RoomAddr;
import com.mycompany.myapp.domain.Visitor;
import com.mycompany.myapp.domain.WamoliUser;
import com.mycompany.myapp.repository.RoomAddrRepository;
import com.mycompany.myapp.service.criteria.RoomAddrCriteria;
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
 * Integration tests for the {@link RoomAddrResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomAddrResourceIT {

    private static final String DEFAULT_ROOM_NUM = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_ROOM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_TYPE = "BBBBBBBBBB";

    private static final Float DEFAULT_ROOM_AREA = 1F;
    private static final Float UPDATED_ROOM_AREA = 2F;
    private static final Float SMALLER_ROOM_AREA = 1F - 1F;

    private static final Boolean DEFAULT_USED = false;
    private static final Boolean UPDATED_USED = true;

    private static final Boolean DEFAULT_AUTO_CONTROL = false;
    private static final Boolean UPDATED_AUTO_CONTROL = true;

    private static final String DEFAULT_LONG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LONG_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/room-addrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomAddrRepository roomAddrRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomAddrMockMvc;

    private RoomAddr roomAddr;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomAddr createEntity(EntityManager em) {
        RoomAddr roomAddr = new RoomAddr()
            .roomNum(DEFAULT_ROOM_NUM)
            .unit(DEFAULT_UNIT)
            .roomType(DEFAULT_ROOM_TYPE)
            .roomArea(DEFAULT_ROOM_AREA)
            .used(DEFAULT_USED)
            .autoControl(DEFAULT_AUTO_CONTROL)
            .longCode(DEFAULT_LONG_CODE);
        return roomAddr;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomAddr createUpdatedEntity(EntityManager em) {
        RoomAddr roomAddr = new RoomAddr()
            .roomNum(UPDATED_ROOM_NUM)
            .unit(UPDATED_UNIT)
            .roomType(UPDATED_ROOM_TYPE)
            .roomArea(UPDATED_ROOM_AREA)
            .used(UPDATED_USED)
            .autoControl(UPDATED_AUTO_CONTROL)
            .longCode(UPDATED_LONG_CODE);
        return roomAddr;
    }

    @BeforeEach
    public void initTest() {
        roomAddr = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomAddr() throws Exception {
        int databaseSizeBeforeCreate = roomAddrRepository.findAll().size();
        // Create the RoomAddr
        restRoomAddrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomAddr)))
            .andExpect(status().isCreated());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeCreate + 1);
        RoomAddr testRoomAddr = roomAddrList.get(roomAddrList.size() - 1);
        assertThat(testRoomAddr.getRoomNum()).isEqualTo(DEFAULT_ROOM_NUM);
        assertThat(testRoomAddr.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testRoomAddr.getRoomType()).isEqualTo(DEFAULT_ROOM_TYPE);
        assertThat(testRoomAddr.getRoomArea()).isEqualTo(DEFAULT_ROOM_AREA);
        assertThat(testRoomAddr.getUsed()).isEqualTo(DEFAULT_USED);
        assertThat(testRoomAddr.getAutoControl()).isEqualTo(DEFAULT_AUTO_CONTROL);
        assertThat(testRoomAddr.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
    }

    @Test
    @Transactional
    void createRoomAddrWithExistingId() throws Exception {
        // Create the RoomAddr with an existing ID
        roomAddr.setId(1L);

        int databaseSizeBeforeCreate = roomAddrRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomAddrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomAddr)))
            .andExpect(status().isBadRequest());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoomNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomAddrRepository.findAll().size();
        // set the field null
        roomAddr.setRoomNum(null);

        // Create the RoomAddr, which fails.

        restRoomAddrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomAddr)))
            .andExpect(status().isBadRequest());

        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomAddrRepository.findAll().size();
        // set the field null
        roomAddr.setLongCode(null);

        // Create the RoomAddr, which fails.

        restRoomAddrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomAddr)))
            .andExpect(status().isBadRequest());

        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoomAddrs() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList
        restRoomAddrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomAddr.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNum").value(hasItem(DEFAULT_ROOM_NUM)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].roomType").value(hasItem(DEFAULT_ROOM_TYPE)))
            .andExpect(jsonPath("$.[*].roomArea").value(hasItem(DEFAULT_ROOM_AREA.doubleValue())))
            .andExpect(jsonPath("$.[*].used").value(hasItem(DEFAULT_USED.booleanValue())))
            .andExpect(jsonPath("$.[*].autoControl").value(hasItem(DEFAULT_AUTO_CONTROL.booleanValue())))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)));
    }

    @Test
    @Transactional
    void getRoomAddr() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get the roomAddr
        restRoomAddrMockMvc
            .perform(get(ENTITY_API_URL_ID, roomAddr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomAddr.getId().intValue()))
            .andExpect(jsonPath("$.roomNum").value(DEFAULT_ROOM_NUM))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.roomType").value(DEFAULT_ROOM_TYPE))
            .andExpect(jsonPath("$.roomArea").value(DEFAULT_ROOM_AREA.doubleValue()))
            .andExpect(jsonPath("$.used").value(DEFAULT_USED.booleanValue()))
            .andExpect(jsonPath("$.autoControl").value(DEFAULT_AUTO_CONTROL.booleanValue()))
            .andExpect(jsonPath("$.longCode").value(DEFAULT_LONG_CODE));
    }

    @Test
    @Transactional
    void getRoomAddrsByIdFiltering() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        Long id = roomAddr.getId();

        defaultRoomAddrShouldBeFound("id.equals=" + id);
        defaultRoomAddrShouldNotBeFound("id.notEquals=" + id);

        defaultRoomAddrShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRoomAddrShouldNotBeFound("id.greaterThan=" + id);

        defaultRoomAddrShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRoomAddrShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomNumIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomNum equals to DEFAULT_ROOM_NUM
        defaultRoomAddrShouldBeFound("roomNum.equals=" + DEFAULT_ROOM_NUM);

        // Get all the roomAddrList where roomNum equals to UPDATED_ROOM_NUM
        defaultRoomAddrShouldNotBeFound("roomNum.equals=" + UPDATED_ROOM_NUM);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomNum not equals to DEFAULT_ROOM_NUM
        defaultRoomAddrShouldNotBeFound("roomNum.notEquals=" + DEFAULT_ROOM_NUM);

        // Get all the roomAddrList where roomNum not equals to UPDATED_ROOM_NUM
        defaultRoomAddrShouldBeFound("roomNum.notEquals=" + UPDATED_ROOM_NUM);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomNumIsInShouldWork() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomNum in DEFAULT_ROOM_NUM or UPDATED_ROOM_NUM
        defaultRoomAddrShouldBeFound("roomNum.in=" + DEFAULT_ROOM_NUM + "," + UPDATED_ROOM_NUM);

        // Get all the roomAddrList where roomNum equals to UPDATED_ROOM_NUM
        defaultRoomAddrShouldNotBeFound("roomNum.in=" + UPDATED_ROOM_NUM);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomNum is not null
        defaultRoomAddrShouldBeFound("roomNum.specified=true");

        // Get all the roomAddrList where roomNum is null
        defaultRoomAddrShouldNotBeFound("roomNum.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomNumContainsSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomNum contains DEFAULT_ROOM_NUM
        defaultRoomAddrShouldBeFound("roomNum.contains=" + DEFAULT_ROOM_NUM);

        // Get all the roomAddrList where roomNum contains UPDATED_ROOM_NUM
        defaultRoomAddrShouldNotBeFound("roomNum.contains=" + UPDATED_ROOM_NUM);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomNumNotContainsSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomNum does not contain DEFAULT_ROOM_NUM
        defaultRoomAddrShouldNotBeFound("roomNum.doesNotContain=" + DEFAULT_ROOM_NUM);

        // Get all the roomAddrList where roomNum does not contain UPDATED_ROOM_NUM
        defaultRoomAddrShouldBeFound("roomNum.doesNotContain=" + UPDATED_ROOM_NUM);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where unit equals to DEFAULT_UNIT
        defaultRoomAddrShouldBeFound("unit.equals=" + DEFAULT_UNIT);

        // Get all the roomAddrList where unit equals to UPDATED_UNIT
        defaultRoomAddrShouldNotBeFound("unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where unit not equals to DEFAULT_UNIT
        defaultRoomAddrShouldNotBeFound("unit.notEquals=" + DEFAULT_UNIT);

        // Get all the roomAddrList where unit not equals to UPDATED_UNIT
        defaultRoomAddrShouldBeFound("unit.notEquals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where unit in DEFAULT_UNIT or UPDATED_UNIT
        defaultRoomAddrShouldBeFound("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT);

        // Get all the roomAddrList where unit equals to UPDATED_UNIT
        defaultRoomAddrShouldNotBeFound("unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where unit is not null
        defaultRoomAddrShouldBeFound("unit.specified=true");

        // Get all the roomAddrList where unit is null
        defaultRoomAddrShouldNotBeFound("unit.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUnitContainsSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where unit contains DEFAULT_UNIT
        defaultRoomAddrShouldBeFound("unit.contains=" + DEFAULT_UNIT);

        // Get all the roomAddrList where unit contains UPDATED_UNIT
        defaultRoomAddrShouldNotBeFound("unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where unit does not contain DEFAULT_UNIT
        defaultRoomAddrShouldNotBeFound("unit.doesNotContain=" + DEFAULT_UNIT);

        // Get all the roomAddrList where unit does not contain UPDATED_UNIT
        defaultRoomAddrShouldBeFound("unit.doesNotContain=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomType equals to DEFAULT_ROOM_TYPE
        defaultRoomAddrShouldBeFound("roomType.equals=" + DEFAULT_ROOM_TYPE);

        // Get all the roomAddrList where roomType equals to UPDATED_ROOM_TYPE
        defaultRoomAddrShouldNotBeFound("roomType.equals=" + UPDATED_ROOM_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomType not equals to DEFAULT_ROOM_TYPE
        defaultRoomAddrShouldNotBeFound("roomType.notEquals=" + DEFAULT_ROOM_TYPE);

        // Get all the roomAddrList where roomType not equals to UPDATED_ROOM_TYPE
        defaultRoomAddrShouldBeFound("roomType.notEquals=" + UPDATED_ROOM_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomTypeIsInShouldWork() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomType in DEFAULT_ROOM_TYPE or UPDATED_ROOM_TYPE
        defaultRoomAddrShouldBeFound("roomType.in=" + DEFAULT_ROOM_TYPE + "," + UPDATED_ROOM_TYPE);

        // Get all the roomAddrList where roomType equals to UPDATED_ROOM_TYPE
        defaultRoomAddrShouldNotBeFound("roomType.in=" + UPDATED_ROOM_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomType is not null
        defaultRoomAddrShouldBeFound("roomType.specified=true");

        // Get all the roomAddrList where roomType is null
        defaultRoomAddrShouldNotBeFound("roomType.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomTypeContainsSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomType contains DEFAULT_ROOM_TYPE
        defaultRoomAddrShouldBeFound("roomType.contains=" + DEFAULT_ROOM_TYPE);

        // Get all the roomAddrList where roomType contains UPDATED_ROOM_TYPE
        defaultRoomAddrShouldNotBeFound("roomType.contains=" + UPDATED_ROOM_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomTypeNotContainsSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomType does not contain DEFAULT_ROOM_TYPE
        defaultRoomAddrShouldNotBeFound("roomType.doesNotContain=" + DEFAULT_ROOM_TYPE);

        // Get all the roomAddrList where roomType does not contain UPDATED_ROOM_TYPE
        defaultRoomAddrShouldBeFound("roomType.doesNotContain=" + UPDATED_ROOM_TYPE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomArea equals to DEFAULT_ROOM_AREA
        defaultRoomAddrShouldBeFound("roomArea.equals=" + DEFAULT_ROOM_AREA);

        // Get all the roomAddrList where roomArea equals to UPDATED_ROOM_AREA
        defaultRoomAddrShouldNotBeFound("roomArea.equals=" + UPDATED_ROOM_AREA);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomAreaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomArea not equals to DEFAULT_ROOM_AREA
        defaultRoomAddrShouldNotBeFound("roomArea.notEquals=" + DEFAULT_ROOM_AREA);

        // Get all the roomAddrList where roomArea not equals to UPDATED_ROOM_AREA
        defaultRoomAddrShouldBeFound("roomArea.notEquals=" + UPDATED_ROOM_AREA);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomAreaIsInShouldWork() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomArea in DEFAULT_ROOM_AREA or UPDATED_ROOM_AREA
        defaultRoomAddrShouldBeFound("roomArea.in=" + DEFAULT_ROOM_AREA + "," + UPDATED_ROOM_AREA);

        // Get all the roomAddrList where roomArea equals to UPDATED_ROOM_AREA
        defaultRoomAddrShouldNotBeFound("roomArea.in=" + UPDATED_ROOM_AREA);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomArea is not null
        defaultRoomAddrShouldBeFound("roomArea.specified=true");

        // Get all the roomAddrList where roomArea is null
        defaultRoomAddrShouldNotBeFound("roomArea.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomArea is greater than or equal to DEFAULT_ROOM_AREA
        defaultRoomAddrShouldBeFound("roomArea.greaterThanOrEqual=" + DEFAULT_ROOM_AREA);

        // Get all the roomAddrList where roomArea is greater than or equal to UPDATED_ROOM_AREA
        defaultRoomAddrShouldNotBeFound("roomArea.greaterThanOrEqual=" + UPDATED_ROOM_AREA);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomArea is less than or equal to DEFAULT_ROOM_AREA
        defaultRoomAddrShouldBeFound("roomArea.lessThanOrEqual=" + DEFAULT_ROOM_AREA);

        // Get all the roomAddrList where roomArea is less than or equal to SMALLER_ROOM_AREA
        defaultRoomAddrShouldNotBeFound("roomArea.lessThanOrEqual=" + SMALLER_ROOM_AREA);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomArea is less than DEFAULT_ROOM_AREA
        defaultRoomAddrShouldNotBeFound("roomArea.lessThan=" + DEFAULT_ROOM_AREA);

        // Get all the roomAddrList where roomArea is less than UPDATED_ROOM_AREA
        defaultRoomAddrShouldBeFound("roomArea.lessThan=" + UPDATED_ROOM_AREA);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByRoomAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where roomArea is greater than DEFAULT_ROOM_AREA
        defaultRoomAddrShouldNotBeFound("roomArea.greaterThan=" + DEFAULT_ROOM_AREA);

        // Get all the roomAddrList where roomArea is greater than SMALLER_ROOM_AREA
        defaultRoomAddrShouldBeFound("roomArea.greaterThan=" + SMALLER_ROOM_AREA);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where used equals to DEFAULT_USED
        defaultRoomAddrShouldBeFound("used.equals=" + DEFAULT_USED);

        // Get all the roomAddrList where used equals to UPDATED_USED
        defaultRoomAddrShouldNotBeFound("used.equals=" + UPDATED_USED);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where used not equals to DEFAULT_USED
        defaultRoomAddrShouldNotBeFound("used.notEquals=" + DEFAULT_USED);

        // Get all the roomAddrList where used not equals to UPDATED_USED
        defaultRoomAddrShouldBeFound("used.notEquals=" + UPDATED_USED);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUsedIsInShouldWork() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where used in DEFAULT_USED or UPDATED_USED
        defaultRoomAddrShouldBeFound("used.in=" + DEFAULT_USED + "," + UPDATED_USED);

        // Get all the roomAddrList where used equals to UPDATED_USED
        defaultRoomAddrShouldNotBeFound("used.in=" + UPDATED_USED);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where used is not null
        defaultRoomAddrShouldBeFound("used.specified=true");

        // Get all the roomAddrList where used is null
        defaultRoomAddrShouldNotBeFound("used.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomAddrsByAutoControlIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where autoControl equals to DEFAULT_AUTO_CONTROL
        defaultRoomAddrShouldBeFound("autoControl.equals=" + DEFAULT_AUTO_CONTROL);

        // Get all the roomAddrList where autoControl equals to UPDATED_AUTO_CONTROL
        defaultRoomAddrShouldNotBeFound("autoControl.equals=" + UPDATED_AUTO_CONTROL);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByAutoControlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where autoControl not equals to DEFAULT_AUTO_CONTROL
        defaultRoomAddrShouldNotBeFound("autoControl.notEquals=" + DEFAULT_AUTO_CONTROL);

        // Get all the roomAddrList where autoControl not equals to UPDATED_AUTO_CONTROL
        defaultRoomAddrShouldBeFound("autoControl.notEquals=" + UPDATED_AUTO_CONTROL);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByAutoControlIsInShouldWork() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where autoControl in DEFAULT_AUTO_CONTROL or UPDATED_AUTO_CONTROL
        defaultRoomAddrShouldBeFound("autoControl.in=" + DEFAULT_AUTO_CONTROL + "," + UPDATED_AUTO_CONTROL);

        // Get all the roomAddrList where autoControl equals to UPDATED_AUTO_CONTROL
        defaultRoomAddrShouldNotBeFound("autoControl.in=" + UPDATED_AUTO_CONTROL);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByAutoControlIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where autoControl is not null
        defaultRoomAddrShouldBeFound("autoControl.specified=true");

        // Get all the roomAddrList where autoControl is null
        defaultRoomAddrShouldNotBeFound("autoControl.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomAddrsByLongCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where longCode equals to DEFAULT_LONG_CODE
        defaultRoomAddrShouldBeFound("longCode.equals=" + DEFAULT_LONG_CODE);

        // Get all the roomAddrList where longCode equals to UPDATED_LONG_CODE
        defaultRoomAddrShouldNotBeFound("longCode.equals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByLongCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where longCode not equals to DEFAULT_LONG_CODE
        defaultRoomAddrShouldNotBeFound("longCode.notEquals=" + DEFAULT_LONG_CODE);

        // Get all the roomAddrList where longCode not equals to UPDATED_LONG_CODE
        defaultRoomAddrShouldBeFound("longCode.notEquals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByLongCodeIsInShouldWork() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where longCode in DEFAULT_LONG_CODE or UPDATED_LONG_CODE
        defaultRoomAddrShouldBeFound("longCode.in=" + DEFAULT_LONG_CODE + "," + UPDATED_LONG_CODE);

        // Get all the roomAddrList where longCode equals to UPDATED_LONG_CODE
        defaultRoomAddrShouldNotBeFound("longCode.in=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByLongCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where longCode is not null
        defaultRoomAddrShouldBeFound("longCode.specified=true");

        // Get all the roomAddrList where longCode is null
        defaultRoomAddrShouldNotBeFound("longCode.specified=false");
    }

    @Test
    @Transactional
    void getAllRoomAddrsByLongCodeContainsSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where longCode contains DEFAULT_LONG_CODE
        defaultRoomAddrShouldBeFound("longCode.contains=" + DEFAULT_LONG_CODE);

        // Get all the roomAddrList where longCode contains UPDATED_LONG_CODE
        defaultRoomAddrShouldNotBeFound("longCode.contains=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByLongCodeNotContainsSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        // Get all the roomAddrList where longCode does not contain DEFAULT_LONG_CODE
        defaultRoomAddrShouldNotBeFound("longCode.doesNotContain=" + DEFAULT_LONG_CODE);

        // Get all the roomAddrList where longCode does not contain UPDATED_LONG_CODE
        defaultRoomAddrShouldBeFound("longCode.doesNotContain=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllRoomAddrsByVisitorIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);
        Visitor visitor = VisitorResourceIT.createEntity(em);
        em.persist(visitor);
        em.flush();
        roomAddr.addVisitor(visitor);
        roomAddrRepository.saveAndFlush(roomAddr);
        Long visitorId = visitor.getId();

        // Get all the roomAddrList where visitor equals to visitorId
        defaultRoomAddrShouldBeFound("visitorId.equals=" + visitorId);

        // Get all the roomAddrList where visitor equals to (visitorId + 1)
        defaultRoomAddrShouldNotBeFound("visitorId.equals=" + (visitorId + 1));
    }

    @Test
    @Transactional
    void getAllRoomAddrsByBuildingsIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);
        Buildings buildings = BuildingsResourceIT.createEntity(em);
        em.persist(buildings);
        em.flush();
        roomAddr.setBuildings(buildings);
        roomAddrRepository.saveAndFlush(roomAddr);
        Long buildingsId = buildings.getId();

        // Get all the roomAddrList where buildings equals to buildingsId
        defaultRoomAddrShouldBeFound("buildingsId.equals=" + buildingsId);

        // Get all the roomAddrList where buildings equals to (buildingsId + 1)
        defaultRoomAddrShouldNotBeFound("buildingsId.equals=" + (buildingsId + 1));
    }

    @Test
    @Transactional
    void getAllRoomAddrsByWamoliUserIsEqualToSomething() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);
        WamoliUser wamoliUser = WamoliUserResourceIT.createEntity(em);
        em.persist(wamoliUser);
        em.flush();
        roomAddr.addWamoliUser(wamoliUser);
        roomAddrRepository.saveAndFlush(roomAddr);
        Long wamoliUserId = wamoliUser.getId();

        // Get all the roomAddrList where wamoliUser equals to wamoliUserId
        defaultRoomAddrShouldBeFound("wamoliUserId.equals=" + wamoliUserId);

        // Get all the roomAddrList where wamoliUser equals to (wamoliUserId + 1)
        defaultRoomAddrShouldNotBeFound("wamoliUserId.equals=" + (wamoliUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoomAddrShouldBeFound(String filter) throws Exception {
        restRoomAddrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomAddr.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomNum").value(hasItem(DEFAULT_ROOM_NUM)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].roomType").value(hasItem(DEFAULT_ROOM_TYPE)))
            .andExpect(jsonPath("$.[*].roomArea").value(hasItem(DEFAULT_ROOM_AREA.doubleValue())))
            .andExpect(jsonPath("$.[*].used").value(hasItem(DEFAULT_USED.booleanValue())))
            .andExpect(jsonPath("$.[*].autoControl").value(hasItem(DEFAULT_AUTO_CONTROL.booleanValue())))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)));

        // Check, that the count call also returns 1
        restRoomAddrMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoomAddrShouldNotBeFound(String filter) throws Exception {
        restRoomAddrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoomAddrMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRoomAddr() throws Exception {
        // Get the roomAddr
        restRoomAddrMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoomAddr() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();

        // Update the roomAddr
        RoomAddr updatedRoomAddr = roomAddrRepository.findById(roomAddr.getId()).get();
        // Disconnect from session so that the updates on updatedRoomAddr are not directly saved in db
        em.detach(updatedRoomAddr);
        updatedRoomAddr
            .roomNum(UPDATED_ROOM_NUM)
            .unit(UPDATED_UNIT)
            .roomType(UPDATED_ROOM_TYPE)
            .roomArea(UPDATED_ROOM_AREA)
            .used(UPDATED_USED)
            .autoControl(UPDATED_AUTO_CONTROL)
            .longCode(UPDATED_LONG_CODE);

        restRoomAddrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoomAddr.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoomAddr))
            )
            .andExpect(status().isOk());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
        RoomAddr testRoomAddr = roomAddrList.get(roomAddrList.size() - 1);
        assertThat(testRoomAddr.getRoomNum()).isEqualTo(UPDATED_ROOM_NUM);
        assertThat(testRoomAddr.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testRoomAddr.getRoomType()).isEqualTo(UPDATED_ROOM_TYPE);
        assertThat(testRoomAddr.getRoomArea()).isEqualTo(UPDATED_ROOM_AREA);
        assertThat(testRoomAddr.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testRoomAddr.getAutoControl()).isEqualTo(UPDATED_AUTO_CONTROL);
        assertThat(testRoomAddr.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void putNonExistingRoomAddr() throws Exception {
        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();
        roomAddr.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomAddrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomAddr.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomAddr))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomAddr() throws Exception {
        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();
        roomAddr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomAddrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomAddr))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomAddr() throws Exception {
        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();
        roomAddr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomAddrMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomAddr)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomAddrWithPatch() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();

        // Update the roomAddr using partial update
        RoomAddr partialUpdatedRoomAddr = new RoomAddr();
        partialUpdatedRoomAddr.setId(roomAddr.getId());

        partialUpdatedRoomAddr.unit(UPDATED_UNIT).used(UPDATED_USED).autoControl(UPDATED_AUTO_CONTROL);

        restRoomAddrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomAddr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomAddr))
            )
            .andExpect(status().isOk());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
        RoomAddr testRoomAddr = roomAddrList.get(roomAddrList.size() - 1);
        assertThat(testRoomAddr.getRoomNum()).isEqualTo(DEFAULT_ROOM_NUM);
        assertThat(testRoomAddr.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testRoomAddr.getRoomType()).isEqualTo(DEFAULT_ROOM_TYPE);
        assertThat(testRoomAddr.getRoomArea()).isEqualTo(DEFAULT_ROOM_AREA);
        assertThat(testRoomAddr.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testRoomAddr.getAutoControl()).isEqualTo(UPDATED_AUTO_CONTROL);
        assertThat(testRoomAddr.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
    }

    @Test
    @Transactional
    void fullUpdateRoomAddrWithPatch() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();

        // Update the roomAddr using partial update
        RoomAddr partialUpdatedRoomAddr = new RoomAddr();
        partialUpdatedRoomAddr.setId(roomAddr.getId());

        partialUpdatedRoomAddr
            .roomNum(UPDATED_ROOM_NUM)
            .unit(UPDATED_UNIT)
            .roomType(UPDATED_ROOM_TYPE)
            .roomArea(UPDATED_ROOM_AREA)
            .used(UPDATED_USED)
            .autoControl(UPDATED_AUTO_CONTROL)
            .longCode(UPDATED_LONG_CODE);

        restRoomAddrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomAddr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomAddr))
            )
            .andExpect(status().isOk());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
        RoomAddr testRoomAddr = roomAddrList.get(roomAddrList.size() - 1);
        assertThat(testRoomAddr.getRoomNum()).isEqualTo(UPDATED_ROOM_NUM);
        assertThat(testRoomAddr.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testRoomAddr.getRoomType()).isEqualTo(UPDATED_ROOM_TYPE);
        assertThat(testRoomAddr.getRoomArea()).isEqualTo(UPDATED_ROOM_AREA);
        assertThat(testRoomAddr.getUsed()).isEqualTo(UPDATED_USED);
        assertThat(testRoomAddr.getAutoControl()).isEqualTo(UPDATED_AUTO_CONTROL);
        assertThat(testRoomAddr.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingRoomAddr() throws Exception {
        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();
        roomAddr.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomAddrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomAddr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomAddr))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomAddr() throws Exception {
        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();
        roomAddr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomAddrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomAddr))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomAddr() throws Exception {
        int databaseSizeBeforeUpdate = roomAddrRepository.findAll().size();
        roomAddr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomAddrMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomAddr)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomAddr in the database
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomAddr() throws Exception {
        // Initialize the database
        roomAddrRepository.saveAndFlush(roomAddr);

        int databaseSizeBeforeDelete = roomAddrRepository.findAll().size();

        // Delete the roomAddr
        restRoomAddrMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomAddr.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomAddr> roomAddrList = roomAddrRepository.findAll();
        assertThat(roomAddrList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
