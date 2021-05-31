package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RoomAddr;
import com.mycompany.myapp.domain.Visitor;
import com.mycompany.myapp.repository.VisitorRepository;
import com.mycompany.myapp.service.criteria.VisitorCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link VisitorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VisitorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUM = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_CAR_PLATE_NUM = "AAAAAAAAAA";
    private static final String UPDATED_CAR_PLATE_NUM = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PASSWD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWD = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FACE_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FACE_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FACE_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FACE_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_WHICH_ENTRANCE = "AAAAAAAAAA";
    private static final String UPDATED_WHICH_ENTRANCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/visitors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisitorMockMvc;

    private Visitor visitor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visitor createEntity(EntityManager em) {
        Visitor visitor = new Visitor()
            .name(DEFAULT_NAME)
            .phoneNum(DEFAULT_PHONE_NUM)
            .carPlateNum(DEFAULT_CAR_PLATE_NUM)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .passwd(DEFAULT_PASSWD)
            .faceImage(DEFAULT_FACE_IMAGE)
            .faceImageContentType(DEFAULT_FACE_IMAGE_CONTENT_TYPE)
            .whichEntrance(DEFAULT_WHICH_ENTRANCE);
        return visitor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visitor createUpdatedEntity(EntityManager em) {
        Visitor visitor = new Visitor()
            .name(UPDATED_NAME)
            .phoneNum(UPDATED_PHONE_NUM)
            .carPlateNum(UPDATED_CAR_PLATE_NUM)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .passwd(UPDATED_PASSWD)
            .faceImage(UPDATED_FACE_IMAGE)
            .faceImageContentType(UPDATED_FACE_IMAGE_CONTENT_TYPE)
            .whichEntrance(UPDATED_WHICH_ENTRANCE);
        return visitor;
    }

    @BeforeEach
    public void initTest() {
        visitor = createEntity(em);
    }

    @Test
    @Transactional
    void createVisitor() throws Exception {
        int databaseSizeBeforeCreate = visitorRepository.findAll().size();
        // Create the Visitor
        restVisitorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isCreated());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeCreate + 1);
        Visitor testVisitor = visitorList.get(visitorList.size() - 1);
        assertThat(testVisitor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVisitor.getPhoneNum()).isEqualTo(DEFAULT_PHONE_NUM);
        assertThat(testVisitor.getCarPlateNum()).isEqualTo(DEFAULT_CAR_PLATE_NUM);
        assertThat(testVisitor.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testVisitor.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testVisitor.getPasswd()).isEqualTo(DEFAULT_PASSWD);
        assertThat(testVisitor.getFaceImage()).isEqualTo(DEFAULT_FACE_IMAGE);
        assertThat(testVisitor.getFaceImageContentType()).isEqualTo(DEFAULT_FACE_IMAGE_CONTENT_TYPE);
        assertThat(testVisitor.getWhichEntrance()).isEqualTo(DEFAULT_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void createVisitorWithExistingId() throws Exception {
        // Create the Visitor with an existing ID
        visitor.setId(1L);

        int databaseSizeBeforeCreate = visitorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setName(null);

        // Create the Visitor, which fails.

        restVisitorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setPhoneNum(null);

        // Create the Visitor, which fails.

        restVisitorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setStartTime(null);

        // Create the Visitor, which fails.

        restVisitorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitorRepository.findAll().size();
        // set the field null
        visitor.setEndTime(null);

        // Create the Visitor, which fails.

        restVisitorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isBadRequest());

        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVisitors() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList
        restVisitorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNum").value(hasItem(DEFAULT_PHONE_NUM)))
            .andExpect(jsonPath("$.[*].carPlateNum").value(hasItem(DEFAULT_CAR_PLATE_NUM)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].passwd").value(hasItem(DEFAULT_PASSWD)))
            .andExpect(jsonPath("$.[*].faceImageContentType").value(hasItem(DEFAULT_FACE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].faceImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_FACE_IMAGE))))
            .andExpect(jsonPath("$.[*].whichEntrance").value(hasItem(DEFAULT_WHICH_ENTRANCE)));
    }

    @Test
    @Transactional
    void getVisitor() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get the visitor
        restVisitorMockMvc
            .perform(get(ENTITY_API_URL_ID, visitor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visitor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phoneNum").value(DEFAULT_PHONE_NUM))
            .andExpect(jsonPath("$.carPlateNum").value(DEFAULT_CAR_PLATE_NUM))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.passwd").value(DEFAULT_PASSWD))
            .andExpect(jsonPath("$.faceImageContentType").value(DEFAULT_FACE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.faceImage").value(Base64Utils.encodeToString(DEFAULT_FACE_IMAGE)))
            .andExpect(jsonPath("$.whichEntrance").value(DEFAULT_WHICH_ENTRANCE));
    }

    @Test
    @Transactional
    void getVisitorsByIdFiltering() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        Long id = visitor.getId();

        defaultVisitorShouldBeFound("id.equals=" + id);
        defaultVisitorShouldNotBeFound("id.notEquals=" + id);

        defaultVisitorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVisitorShouldNotBeFound("id.greaterThan=" + id);

        defaultVisitorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVisitorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVisitorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where name equals to DEFAULT_NAME
        defaultVisitorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the visitorList where name equals to UPDATED_NAME
        defaultVisitorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVisitorsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where name not equals to DEFAULT_NAME
        defaultVisitorShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the visitorList where name not equals to UPDATED_NAME
        defaultVisitorShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVisitorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVisitorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the visitorList where name equals to UPDATED_NAME
        defaultVisitorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVisitorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where name is not null
        defaultVisitorShouldBeFound("name.specified=true");

        // Get all the visitorList where name is null
        defaultVisitorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByNameContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where name contains DEFAULT_NAME
        defaultVisitorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the visitorList where name contains UPDATED_NAME
        defaultVisitorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVisitorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where name does not contain DEFAULT_NAME
        defaultVisitorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the visitorList where name does not contain UPDATED_NAME
        defaultVisitorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVisitorsByPhoneNumIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where phoneNum equals to DEFAULT_PHONE_NUM
        defaultVisitorShouldBeFound("phoneNum.equals=" + DEFAULT_PHONE_NUM);

        // Get all the visitorList where phoneNum equals to UPDATED_PHONE_NUM
        defaultVisitorShouldNotBeFound("phoneNum.equals=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByPhoneNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where phoneNum not equals to DEFAULT_PHONE_NUM
        defaultVisitorShouldNotBeFound("phoneNum.notEquals=" + DEFAULT_PHONE_NUM);

        // Get all the visitorList where phoneNum not equals to UPDATED_PHONE_NUM
        defaultVisitorShouldBeFound("phoneNum.notEquals=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByPhoneNumIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where phoneNum in DEFAULT_PHONE_NUM or UPDATED_PHONE_NUM
        defaultVisitorShouldBeFound("phoneNum.in=" + DEFAULT_PHONE_NUM + "," + UPDATED_PHONE_NUM);

        // Get all the visitorList where phoneNum equals to UPDATED_PHONE_NUM
        defaultVisitorShouldNotBeFound("phoneNum.in=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByPhoneNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where phoneNum is not null
        defaultVisitorShouldBeFound("phoneNum.specified=true");

        // Get all the visitorList where phoneNum is null
        defaultVisitorShouldNotBeFound("phoneNum.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByPhoneNumContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where phoneNum contains DEFAULT_PHONE_NUM
        defaultVisitorShouldBeFound("phoneNum.contains=" + DEFAULT_PHONE_NUM);

        // Get all the visitorList where phoneNum contains UPDATED_PHONE_NUM
        defaultVisitorShouldNotBeFound("phoneNum.contains=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByPhoneNumNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where phoneNum does not contain DEFAULT_PHONE_NUM
        defaultVisitorShouldNotBeFound("phoneNum.doesNotContain=" + DEFAULT_PHONE_NUM);

        // Get all the visitorList where phoneNum does not contain UPDATED_PHONE_NUM
        defaultVisitorShouldBeFound("phoneNum.doesNotContain=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByCarPlateNumIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where carPlateNum equals to DEFAULT_CAR_PLATE_NUM
        defaultVisitorShouldBeFound("carPlateNum.equals=" + DEFAULT_CAR_PLATE_NUM);

        // Get all the visitorList where carPlateNum equals to UPDATED_CAR_PLATE_NUM
        defaultVisitorShouldNotBeFound("carPlateNum.equals=" + UPDATED_CAR_PLATE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByCarPlateNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where carPlateNum not equals to DEFAULT_CAR_PLATE_NUM
        defaultVisitorShouldNotBeFound("carPlateNum.notEquals=" + DEFAULT_CAR_PLATE_NUM);

        // Get all the visitorList where carPlateNum not equals to UPDATED_CAR_PLATE_NUM
        defaultVisitorShouldBeFound("carPlateNum.notEquals=" + UPDATED_CAR_PLATE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByCarPlateNumIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where carPlateNum in DEFAULT_CAR_PLATE_NUM or UPDATED_CAR_PLATE_NUM
        defaultVisitorShouldBeFound("carPlateNum.in=" + DEFAULT_CAR_PLATE_NUM + "," + UPDATED_CAR_PLATE_NUM);

        // Get all the visitorList where carPlateNum equals to UPDATED_CAR_PLATE_NUM
        defaultVisitorShouldNotBeFound("carPlateNum.in=" + UPDATED_CAR_PLATE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByCarPlateNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where carPlateNum is not null
        defaultVisitorShouldBeFound("carPlateNum.specified=true");

        // Get all the visitorList where carPlateNum is null
        defaultVisitorShouldNotBeFound("carPlateNum.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByCarPlateNumContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where carPlateNum contains DEFAULT_CAR_PLATE_NUM
        defaultVisitorShouldBeFound("carPlateNum.contains=" + DEFAULT_CAR_PLATE_NUM);

        // Get all the visitorList where carPlateNum contains UPDATED_CAR_PLATE_NUM
        defaultVisitorShouldNotBeFound("carPlateNum.contains=" + UPDATED_CAR_PLATE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByCarPlateNumNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where carPlateNum does not contain DEFAULT_CAR_PLATE_NUM
        defaultVisitorShouldNotBeFound("carPlateNum.doesNotContain=" + DEFAULT_CAR_PLATE_NUM);

        // Get all the visitorList where carPlateNum does not contain UPDATED_CAR_PLATE_NUM
        defaultVisitorShouldBeFound("carPlateNum.doesNotContain=" + UPDATED_CAR_PLATE_NUM);
    }

    @Test
    @Transactional
    void getAllVisitorsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where startTime equals to DEFAULT_START_TIME
        defaultVisitorShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the visitorList where startTime equals to UPDATED_START_TIME
        defaultVisitorShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllVisitorsByStartTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where startTime not equals to DEFAULT_START_TIME
        defaultVisitorShouldNotBeFound("startTime.notEquals=" + DEFAULT_START_TIME);

        // Get all the visitorList where startTime not equals to UPDATED_START_TIME
        defaultVisitorShouldBeFound("startTime.notEquals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllVisitorsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultVisitorShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the visitorList where startTime equals to UPDATED_START_TIME
        defaultVisitorShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllVisitorsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where startTime is not null
        defaultVisitorShouldBeFound("startTime.specified=true");

        // Get all the visitorList where startTime is null
        defaultVisitorShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where endTime equals to DEFAULT_END_TIME
        defaultVisitorShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the visitorList where endTime equals to UPDATED_END_TIME
        defaultVisitorShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllVisitorsByEndTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where endTime not equals to DEFAULT_END_TIME
        defaultVisitorShouldNotBeFound("endTime.notEquals=" + DEFAULT_END_TIME);

        // Get all the visitorList where endTime not equals to UPDATED_END_TIME
        defaultVisitorShouldBeFound("endTime.notEquals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllVisitorsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultVisitorShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the visitorList where endTime equals to UPDATED_END_TIME
        defaultVisitorShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllVisitorsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where endTime is not null
        defaultVisitorShouldBeFound("endTime.specified=true");

        // Get all the visitorList where endTime is null
        defaultVisitorShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByPasswdIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where passwd equals to DEFAULT_PASSWD
        defaultVisitorShouldBeFound("passwd.equals=" + DEFAULT_PASSWD);

        // Get all the visitorList where passwd equals to UPDATED_PASSWD
        defaultVisitorShouldNotBeFound("passwd.equals=" + UPDATED_PASSWD);
    }

    @Test
    @Transactional
    void getAllVisitorsByPasswdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where passwd not equals to DEFAULT_PASSWD
        defaultVisitorShouldNotBeFound("passwd.notEquals=" + DEFAULT_PASSWD);

        // Get all the visitorList where passwd not equals to UPDATED_PASSWD
        defaultVisitorShouldBeFound("passwd.notEquals=" + UPDATED_PASSWD);
    }

    @Test
    @Transactional
    void getAllVisitorsByPasswdIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where passwd in DEFAULT_PASSWD or UPDATED_PASSWD
        defaultVisitorShouldBeFound("passwd.in=" + DEFAULT_PASSWD + "," + UPDATED_PASSWD);

        // Get all the visitorList where passwd equals to UPDATED_PASSWD
        defaultVisitorShouldNotBeFound("passwd.in=" + UPDATED_PASSWD);
    }

    @Test
    @Transactional
    void getAllVisitorsByPasswdIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where passwd is not null
        defaultVisitorShouldBeFound("passwd.specified=true");

        // Get all the visitorList where passwd is null
        defaultVisitorShouldNotBeFound("passwd.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByPasswdContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where passwd contains DEFAULT_PASSWD
        defaultVisitorShouldBeFound("passwd.contains=" + DEFAULT_PASSWD);

        // Get all the visitorList where passwd contains UPDATED_PASSWD
        defaultVisitorShouldNotBeFound("passwd.contains=" + UPDATED_PASSWD);
    }

    @Test
    @Transactional
    void getAllVisitorsByPasswdNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where passwd does not contain DEFAULT_PASSWD
        defaultVisitorShouldNotBeFound("passwd.doesNotContain=" + DEFAULT_PASSWD);

        // Get all the visitorList where passwd does not contain UPDATED_PASSWD
        defaultVisitorShouldBeFound("passwd.doesNotContain=" + UPDATED_PASSWD);
    }

    @Test
    @Transactional
    void getAllVisitorsByWhichEntranceIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where whichEntrance equals to DEFAULT_WHICH_ENTRANCE
        defaultVisitorShouldBeFound("whichEntrance.equals=" + DEFAULT_WHICH_ENTRANCE);

        // Get all the visitorList where whichEntrance equals to UPDATED_WHICH_ENTRANCE
        defaultVisitorShouldNotBeFound("whichEntrance.equals=" + UPDATED_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void getAllVisitorsByWhichEntranceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where whichEntrance not equals to DEFAULT_WHICH_ENTRANCE
        defaultVisitorShouldNotBeFound("whichEntrance.notEquals=" + DEFAULT_WHICH_ENTRANCE);

        // Get all the visitorList where whichEntrance not equals to UPDATED_WHICH_ENTRANCE
        defaultVisitorShouldBeFound("whichEntrance.notEquals=" + UPDATED_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void getAllVisitorsByWhichEntranceIsInShouldWork() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where whichEntrance in DEFAULT_WHICH_ENTRANCE or UPDATED_WHICH_ENTRANCE
        defaultVisitorShouldBeFound("whichEntrance.in=" + DEFAULT_WHICH_ENTRANCE + "," + UPDATED_WHICH_ENTRANCE);

        // Get all the visitorList where whichEntrance equals to UPDATED_WHICH_ENTRANCE
        defaultVisitorShouldNotBeFound("whichEntrance.in=" + UPDATED_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void getAllVisitorsByWhichEntranceIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where whichEntrance is not null
        defaultVisitorShouldBeFound("whichEntrance.specified=true");

        // Get all the visitorList where whichEntrance is null
        defaultVisitorShouldNotBeFound("whichEntrance.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByWhichEntranceContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where whichEntrance contains DEFAULT_WHICH_ENTRANCE
        defaultVisitorShouldBeFound("whichEntrance.contains=" + DEFAULT_WHICH_ENTRANCE);

        // Get all the visitorList where whichEntrance contains UPDATED_WHICH_ENTRANCE
        defaultVisitorShouldNotBeFound("whichEntrance.contains=" + UPDATED_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void getAllVisitorsByWhichEntranceNotContainsSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        // Get all the visitorList where whichEntrance does not contain DEFAULT_WHICH_ENTRANCE
        defaultVisitorShouldNotBeFound("whichEntrance.doesNotContain=" + DEFAULT_WHICH_ENTRANCE);

        // Get all the visitorList where whichEntrance does not contain UPDATED_WHICH_ENTRANCE
        defaultVisitorShouldBeFound("whichEntrance.doesNotContain=" + UPDATED_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void getAllVisitorsByRoomAddrIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);
        RoomAddr roomAddr = RoomAddrResourceIT.createEntity(em);
        em.persist(roomAddr);
        em.flush();
        visitor.setRoomAddr(roomAddr);
        visitorRepository.saveAndFlush(visitor);
        Long roomAddrId = roomAddr.getId();

        // Get all the visitorList where roomAddr equals to roomAddrId
        defaultVisitorShouldBeFound("roomAddrId.equals=" + roomAddrId);

        // Get all the visitorList where roomAddr equals to (roomAddrId + 1)
        defaultVisitorShouldNotBeFound("roomAddrId.equals=" + (roomAddrId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVisitorShouldBeFound(String filter) throws Exception {
        restVisitorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phoneNum").value(hasItem(DEFAULT_PHONE_NUM)))
            .andExpect(jsonPath("$.[*].carPlateNum").value(hasItem(DEFAULT_CAR_PLATE_NUM)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].passwd").value(hasItem(DEFAULT_PASSWD)))
            .andExpect(jsonPath("$.[*].faceImageContentType").value(hasItem(DEFAULT_FACE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].faceImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_FACE_IMAGE))))
            .andExpect(jsonPath("$.[*].whichEntrance").value(hasItem(DEFAULT_WHICH_ENTRANCE)));

        // Check, that the count call also returns 1
        restVisitorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVisitorShouldNotBeFound(String filter) throws Exception {
        restVisitorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVisitorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVisitor() throws Exception {
        // Get the visitor
        restVisitorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVisitor() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();

        // Update the visitor
        Visitor updatedVisitor = visitorRepository.findById(visitor.getId()).get();
        // Disconnect from session so that the updates on updatedVisitor are not directly saved in db
        em.detach(updatedVisitor);
        updatedVisitor
            .name(UPDATED_NAME)
            .phoneNum(UPDATED_PHONE_NUM)
            .carPlateNum(UPDATED_CAR_PLATE_NUM)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .passwd(UPDATED_PASSWD)
            .faceImage(UPDATED_FACE_IMAGE)
            .faceImageContentType(UPDATED_FACE_IMAGE_CONTENT_TYPE)
            .whichEntrance(UPDATED_WHICH_ENTRANCE);

        restVisitorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVisitor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVisitor))
            )
            .andExpect(status().isOk());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
        Visitor testVisitor = visitorList.get(visitorList.size() - 1);
        assertThat(testVisitor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVisitor.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testVisitor.getCarPlateNum()).isEqualTo(UPDATED_CAR_PLATE_NUM);
        assertThat(testVisitor.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testVisitor.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testVisitor.getPasswd()).isEqualTo(UPDATED_PASSWD);
        assertThat(testVisitor.getFaceImage()).isEqualTo(UPDATED_FACE_IMAGE);
        assertThat(testVisitor.getFaceImageContentType()).isEqualTo(UPDATED_FACE_IMAGE_CONTENT_TYPE);
        assertThat(testVisitor.getWhichEntrance()).isEqualTo(UPDATED_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void putNonExistingVisitor() throws Exception {
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();
        visitor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visitor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visitor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisitor() throws Exception {
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();
        visitor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(visitor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisitor() throws Exception {
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();
        visitor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVisitorWithPatch() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();

        // Update the visitor using partial update
        Visitor partialUpdatedVisitor = new Visitor();
        partialUpdatedVisitor.setId(visitor.getId());

        partialUpdatedVisitor
            .phoneNum(UPDATED_PHONE_NUM)
            .carPlateNum(UPDATED_CAR_PLATE_NUM)
            .faceImage(UPDATED_FACE_IMAGE)
            .faceImageContentType(UPDATED_FACE_IMAGE_CONTENT_TYPE)
            .whichEntrance(UPDATED_WHICH_ENTRANCE);

        restVisitorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisitor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisitor))
            )
            .andExpect(status().isOk());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
        Visitor testVisitor = visitorList.get(visitorList.size() - 1);
        assertThat(testVisitor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVisitor.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testVisitor.getCarPlateNum()).isEqualTo(UPDATED_CAR_PLATE_NUM);
        assertThat(testVisitor.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testVisitor.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testVisitor.getPasswd()).isEqualTo(DEFAULT_PASSWD);
        assertThat(testVisitor.getFaceImage()).isEqualTo(UPDATED_FACE_IMAGE);
        assertThat(testVisitor.getFaceImageContentType()).isEqualTo(UPDATED_FACE_IMAGE_CONTENT_TYPE);
        assertThat(testVisitor.getWhichEntrance()).isEqualTo(UPDATED_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void fullUpdateVisitorWithPatch() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();

        // Update the visitor using partial update
        Visitor partialUpdatedVisitor = new Visitor();
        partialUpdatedVisitor.setId(visitor.getId());

        partialUpdatedVisitor
            .name(UPDATED_NAME)
            .phoneNum(UPDATED_PHONE_NUM)
            .carPlateNum(UPDATED_CAR_PLATE_NUM)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .passwd(UPDATED_PASSWD)
            .faceImage(UPDATED_FACE_IMAGE)
            .faceImageContentType(UPDATED_FACE_IMAGE_CONTENT_TYPE)
            .whichEntrance(UPDATED_WHICH_ENTRANCE);

        restVisitorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisitor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVisitor))
            )
            .andExpect(status().isOk());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
        Visitor testVisitor = visitorList.get(visitorList.size() - 1);
        assertThat(testVisitor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVisitor.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testVisitor.getCarPlateNum()).isEqualTo(UPDATED_CAR_PLATE_NUM);
        assertThat(testVisitor.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testVisitor.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testVisitor.getPasswd()).isEqualTo(UPDATED_PASSWD);
        assertThat(testVisitor.getFaceImage()).isEqualTo(UPDATED_FACE_IMAGE);
        assertThat(testVisitor.getFaceImageContentType()).isEqualTo(UPDATED_FACE_IMAGE_CONTENT_TYPE);
        assertThat(testVisitor.getWhichEntrance()).isEqualTo(UPDATED_WHICH_ENTRANCE);
    }

    @Test
    @Transactional
    void patchNonExistingVisitor() throws Exception {
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();
        visitor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visitor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visitor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisitor() throws Exception {
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();
        visitor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(visitor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisitor() throws Exception {
        int databaseSizeBeforeUpdate = visitorRepository.findAll().size();
        visitor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(visitor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visitor in the database
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVisitor() throws Exception {
        // Initialize the database
        visitorRepository.saveAndFlush(visitor);

        int databaseSizeBeforeDelete = visitorRepository.findAll().size();

        // Delete the visitor
        restVisitorMockMvc
            .perform(delete(ENTITY_API_URL_ID, visitor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Visitor> visitorList = visitorRepository.findAll();
        assertThat(visitorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
