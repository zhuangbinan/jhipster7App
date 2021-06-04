package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.domain.CompanyPost;
import com.mycompany.myapp.domain.RoomAddr;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.WamoliUser;
import com.mycompany.myapp.domain.enumeration.CertificateType;
import com.mycompany.myapp.domain.enumeration.UserType;
import com.mycompany.myapp.repository.WamoliUserRepository;
import com.mycompany.myapp.service.WamoliUserService;
import com.mycompany.myapp.service.criteria.WamoliUserCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WamoliUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WamoliUserResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUM = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_ADDR = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_ADDR = "BBBBBBBBBB";

    private static final Long DEFAULT_ROOM_ADDR = 1L;
    private static final Long UPDATED_ROOM_ADDR = 2L;
    private static final Long SMALLER_ROOM_ADDR = 1L - 1L;

    private static final String DEFAULT_ID_CARD_NUM = "AAAAAAAAAA";
    private static final String UPDATED_ID_CARD_NUM = "BBBBBBBBBB";

    private static final CertificateType DEFAULT_ID_CARD_TYPE = CertificateType.IDCARD;
    private static final CertificateType UPDATED_ID_CARD_TYPE = CertificateType.DRIVINGLICENSE;

    private static final UserType DEFAULT_USER_TYPE = UserType.OWNER;
    private static final UserType UPDATED_USER_TYPE = UserType.MANAGER;

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final Boolean DEFAULT_IS_MANAGER = false;
    private static final Boolean UPDATED_IS_MANAGER = true;

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/wamoli-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WamoliUserRepository wamoliUserRepository;

    @Mock
    private WamoliUserRepository wamoliUserRepositoryMock;

    @Mock
    private WamoliUserService wamoliUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWamoliUserMockMvc;

    private WamoliUser wamoliUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WamoliUser createEntity(EntityManager em) {
        WamoliUser wamoliUser = new WamoliUser()
            .userName(DEFAULT_USER_NAME)
            .gender(DEFAULT_GENDER)
            .phoneNum(DEFAULT_PHONE_NUM)
            .email(DEFAULT_EMAIL)
            .unitAddr(DEFAULT_UNIT_ADDR)
            .roomAddr(DEFAULT_ROOM_ADDR)
            .idCardNum(DEFAULT_ID_CARD_NUM)
            .idCardType(DEFAULT_ID_CARD_TYPE)
            .userType(DEFAULT_USER_TYPE)
            .enable(DEFAULT_ENABLE)
            .isManager(DEFAULT_IS_MANAGER)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return wamoliUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WamoliUser createUpdatedEntity(EntityManager em) {
        WamoliUser wamoliUser = new WamoliUser()
            .userName(UPDATED_USER_NAME)
            .gender(UPDATED_GENDER)
            .phoneNum(UPDATED_PHONE_NUM)
            .email(UPDATED_EMAIL)
            .unitAddr(UPDATED_UNIT_ADDR)
            .roomAddr(UPDATED_ROOM_ADDR)
            .idCardNum(UPDATED_ID_CARD_NUM)
            .idCardType(UPDATED_ID_CARD_TYPE)
            .userType(UPDATED_USER_TYPE)
            .enable(UPDATED_ENABLE)
            .isManager(UPDATED_IS_MANAGER)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return wamoliUser;
    }

    @BeforeEach
    public void initTest() {
        wamoliUser = createEntity(em);
    }

    @Test
    @Transactional
    void createWamoliUser() throws Exception {
        int databaseSizeBeforeCreate = wamoliUserRepository.findAll().size();
        // Create the WamoliUser
        restWamoliUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUser)))
            .andExpect(status().isCreated());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeCreate + 1);
        WamoliUser testWamoliUser = wamoliUserList.get(wamoliUserList.size() - 1);
        assertThat(testWamoliUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testWamoliUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testWamoliUser.getPhoneNum()).isEqualTo(DEFAULT_PHONE_NUM);
        assertThat(testWamoliUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testWamoliUser.getUnitAddr()).isEqualTo(DEFAULT_UNIT_ADDR);
        assertThat(testWamoliUser.getRoomAddr()).isEqualTo(DEFAULT_ROOM_ADDR);
        assertThat(testWamoliUser.getIdCardNum()).isEqualTo(DEFAULT_ID_CARD_NUM);
        assertThat(testWamoliUser.getIdCardType()).isEqualTo(DEFAULT_ID_CARD_TYPE);
        assertThat(testWamoliUser.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
        assertThat(testWamoliUser.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testWamoliUser.getIsManager()).isEqualTo(DEFAULT_IS_MANAGER);
        assertThat(testWamoliUser.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testWamoliUser.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createWamoliUserWithExistingId() throws Exception {
        // Create the WamoliUser with an existing ID
        wamoliUser.setId(1L);

        int databaseSizeBeforeCreate = wamoliUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWamoliUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUser)))
            .andExpect(status().isBadRequest());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wamoliUserRepository.findAll().size();
        // set the field null
        wamoliUser.setUserName(null);

        // Create the WamoliUser, which fails.

        restWamoliUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUser)))
            .andExpect(status().isBadRequest());

        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = wamoliUserRepository.findAll().size();
        // set the field null
        wamoliUser.setPhoneNum(null);

        // Create the WamoliUser, which fails.

        restWamoliUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUser)))
            .andExpect(status().isBadRequest());

        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWamoliUsers() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList
        restWamoliUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wamoliUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].phoneNum").value(hasItem(DEFAULT_PHONE_NUM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].unitAddr").value(hasItem(DEFAULT_UNIT_ADDR)))
            .andExpect(jsonPath("$.[*].roomAddr").value(hasItem(DEFAULT_ROOM_ADDR.intValue())))
            .andExpect(jsonPath("$.[*].idCardNum").value(hasItem(DEFAULT_ID_CARD_NUM)))
            .andExpect(jsonPath("$.[*].idCardType").value(hasItem(DEFAULT_ID_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isManager").value(hasItem(DEFAULT_IS_MANAGER.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWamoliUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(wamoliUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWamoliUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(wamoliUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWamoliUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(wamoliUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWamoliUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(wamoliUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWamoliUser() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get the wamoliUser
        restWamoliUserMockMvc
            .perform(get(ENTITY_API_URL_ID, wamoliUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wamoliUser.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.phoneNum").value(DEFAULT_PHONE_NUM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.unitAddr").value(DEFAULT_UNIT_ADDR))
            .andExpect(jsonPath("$.roomAddr").value(DEFAULT_ROOM_ADDR.intValue()))
            .andExpect(jsonPath("$.idCardNum").value(DEFAULT_ID_CARD_NUM))
            .andExpect(jsonPath("$.idCardType").value(DEFAULT_ID_CARD_TYPE.toString()))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.isManager").value(DEFAULT_IS_MANAGER.booleanValue()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getWamoliUsersByIdFiltering() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        Long id = wamoliUser.getId();

        defaultWamoliUserShouldBeFound("id.equals=" + id);
        defaultWamoliUserShouldNotBeFound("id.notEquals=" + id);

        defaultWamoliUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWamoliUserShouldNotBeFound("id.greaterThan=" + id);

        defaultWamoliUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWamoliUserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userName equals to DEFAULT_USER_NAME
        defaultWamoliUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the wamoliUserList where userName equals to UPDATED_USER_NAME
        defaultWamoliUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userName not equals to DEFAULT_USER_NAME
        defaultWamoliUserShouldNotBeFound("userName.notEquals=" + DEFAULT_USER_NAME);

        // Get all the wamoliUserList where userName not equals to UPDATED_USER_NAME
        defaultWamoliUserShouldBeFound("userName.notEquals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultWamoliUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the wamoliUserList where userName equals to UPDATED_USER_NAME
        defaultWamoliUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userName is not null
        defaultWamoliUserShouldBeFound("userName.specified=true");

        // Get all the wamoliUserList where userName is null
        defaultWamoliUserShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserNameContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userName contains DEFAULT_USER_NAME
        defaultWamoliUserShouldBeFound("userName.contains=" + DEFAULT_USER_NAME);

        // Get all the wamoliUserList where userName contains UPDATED_USER_NAME
        defaultWamoliUserShouldNotBeFound("userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userName does not contain DEFAULT_USER_NAME
        defaultWamoliUserShouldNotBeFound("userName.doesNotContain=" + DEFAULT_USER_NAME);

        // Get all the wamoliUserList where userName does not contain UPDATED_USER_NAME
        defaultWamoliUserShouldBeFound("userName.doesNotContain=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where gender equals to DEFAULT_GENDER
        defaultWamoliUserShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the wamoliUserList where gender equals to UPDATED_GENDER
        defaultWamoliUserShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where gender not equals to DEFAULT_GENDER
        defaultWamoliUserShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the wamoliUserList where gender not equals to UPDATED_GENDER
        defaultWamoliUserShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultWamoliUserShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the wamoliUserList where gender equals to UPDATED_GENDER
        defaultWamoliUserShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where gender is not null
        defaultWamoliUserShouldBeFound("gender.specified=true");

        // Get all the wamoliUserList where gender is null
        defaultWamoliUserShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByGenderContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where gender contains DEFAULT_GENDER
        defaultWamoliUserShouldBeFound("gender.contains=" + DEFAULT_GENDER);

        // Get all the wamoliUserList where gender contains UPDATED_GENDER
        defaultWamoliUserShouldNotBeFound("gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where gender does not contain DEFAULT_GENDER
        defaultWamoliUserShouldNotBeFound("gender.doesNotContain=" + DEFAULT_GENDER);

        // Get all the wamoliUserList where gender does not contain UPDATED_GENDER
        defaultWamoliUserShouldBeFound("gender.doesNotContain=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByPhoneNumIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where phoneNum equals to DEFAULT_PHONE_NUM
        defaultWamoliUserShouldBeFound("phoneNum.equals=" + DEFAULT_PHONE_NUM);

        // Get all the wamoliUserList where phoneNum equals to UPDATED_PHONE_NUM
        defaultWamoliUserShouldNotBeFound("phoneNum.equals=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByPhoneNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where phoneNum not equals to DEFAULT_PHONE_NUM
        defaultWamoliUserShouldNotBeFound("phoneNum.notEquals=" + DEFAULT_PHONE_NUM);

        // Get all the wamoliUserList where phoneNum not equals to UPDATED_PHONE_NUM
        defaultWamoliUserShouldBeFound("phoneNum.notEquals=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByPhoneNumIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where phoneNum in DEFAULT_PHONE_NUM or UPDATED_PHONE_NUM
        defaultWamoliUserShouldBeFound("phoneNum.in=" + DEFAULT_PHONE_NUM + "," + UPDATED_PHONE_NUM);

        // Get all the wamoliUserList where phoneNum equals to UPDATED_PHONE_NUM
        defaultWamoliUserShouldNotBeFound("phoneNum.in=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByPhoneNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where phoneNum is not null
        defaultWamoliUserShouldBeFound("phoneNum.specified=true");

        // Get all the wamoliUserList where phoneNum is null
        defaultWamoliUserShouldNotBeFound("phoneNum.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByPhoneNumContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where phoneNum contains DEFAULT_PHONE_NUM
        defaultWamoliUserShouldBeFound("phoneNum.contains=" + DEFAULT_PHONE_NUM);

        // Get all the wamoliUserList where phoneNum contains UPDATED_PHONE_NUM
        defaultWamoliUserShouldNotBeFound("phoneNum.contains=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByPhoneNumNotContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where phoneNum does not contain DEFAULT_PHONE_NUM
        defaultWamoliUserShouldNotBeFound("phoneNum.doesNotContain=" + DEFAULT_PHONE_NUM);

        // Get all the wamoliUserList where phoneNum does not contain UPDATED_PHONE_NUM
        defaultWamoliUserShouldBeFound("phoneNum.doesNotContain=" + UPDATED_PHONE_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where email equals to DEFAULT_EMAIL
        defaultWamoliUserShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the wamoliUserList where email equals to UPDATED_EMAIL
        defaultWamoliUserShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where email not equals to DEFAULT_EMAIL
        defaultWamoliUserShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the wamoliUserList where email not equals to UPDATED_EMAIL
        defaultWamoliUserShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultWamoliUserShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the wamoliUserList where email equals to UPDATED_EMAIL
        defaultWamoliUserShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where email is not null
        defaultWamoliUserShouldBeFound("email.specified=true");

        // Get all the wamoliUserList where email is null
        defaultWamoliUserShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEmailContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where email contains DEFAULT_EMAIL
        defaultWamoliUserShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the wamoliUserList where email contains UPDATED_EMAIL
        defaultWamoliUserShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where email does not contain DEFAULT_EMAIL
        defaultWamoliUserShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the wamoliUserList where email does not contain UPDATED_EMAIL
        defaultWamoliUserShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUnitAddrIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where unitAddr equals to DEFAULT_UNIT_ADDR
        defaultWamoliUserShouldBeFound("unitAddr.equals=" + DEFAULT_UNIT_ADDR);

        // Get all the wamoliUserList where unitAddr equals to UPDATED_UNIT_ADDR
        defaultWamoliUserShouldNotBeFound("unitAddr.equals=" + UPDATED_UNIT_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUnitAddrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where unitAddr not equals to DEFAULT_UNIT_ADDR
        defaultWamoliUserShouldNotBeFound("unitAddr.notEquals=" + DEFAULT_UNIT_ADDR);

        // Get all the wamoliUserList where unitAddr not equals to UPDATED_UNIT_ADDR
        defaultWamoliUserShouldBeFound("unitAddr.notEquals=" + UPDATED_UNIT_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUnitAddrIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where unitAddr in DEFAULT_UNIT_ADDR or UPDATED_UNIT_ADDR
        defaultWamoliUserShouldBeFound("unitAddr.in=" + DEFAULT_UNIT_ADDR + "," + UPDATED_UNIT_ADDR);

        // Get all the wamoliUserList where unitAddr equals to UPDATED_UNIT_ADDR
        defaultWamoliUserShouldNotBeFound("unitAddr.in=" + UPDATED_UNIT_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUnitAddrIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where unitAddr is not null
        defaultWamoliUserShouldBeFound("unitAddr.specified=true");

        // Get all the wamoliUserList where unitAddr is null
        defaultWamoliUserShouldNotBeFound("unitAddr.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUnitAddrContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where unitAddr contains DEFAULT_UNIT_ADDR
        defaultWamoliUserShouldBeFound("unitAddr.contains=" + DEFAULT_UNIT_ADDR);

        // Get all the wamoliUserList where unitAddr contains UPDATED_UNIT_ADDR
        defaultWamoliUserShouldNotBeFound("unitAddr.contains=" + UPDATED_UNIT_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUnitAddrNotContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where unitAddr does not contain DEFAULT_UNIT_ADDR
        defaultWamoliUserShouldNotBeFound("unitAddr.doesNotContain=" + DEFAULT_UNIT_ADDR);

        // Get all the wamoliUserList where unitAddr does not contain UPDATED_UNIT_ADDR
        defaultWamoliUserShouldBeFound("unitAddr.doesNotContain=" + UPDATED_UNIT_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByRoomAddrIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where roomAddr equals to DEFAULT_ROOM_ADDR
        defaultWamoliUserShouldBeFound("roomAddr.equals=" + DEFAULT_ROOM_ADDR);

        // Get all the wamoliUserList where roomAddr equals to UPDATED_ROOM_ADDR
        defaultWamoliUserShouldNotBeFound("roomAddr.equals=" + UPDATED_ROOM_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByRoomAddrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where roomAddr not equals to DEFAULT_ROOM_ADDR
        defaultWamoliUserShouldNotBeFound("roomAddr.notEquals=" + DEFAULT_ROOM_ADDR);

        // Get all the wamoliUserList where roomAddr not equals to UPDATED_ROOM_ADDR
        defaultWamoliUserShouldBeFound("roomAddr.notEquals=" + UPDATED_ROOM_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByRoomAddrIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where roomAddr in DEFAULT_ROOM_ADDR or UPDATED_ROOM_ADDR
        defaultWamoliUserShouldBeFound("roomAddr.in=" + DEFAULT_ROOM_ADDR + "," + UPDATED_ROOM_ADDR);

        // Get all the wamoliUserList where roomAddr equals to UPDATED_ROOM_ADDR
        defaultWamoliUserShouldNotBeFound("roomAddr.in=" + UPDATED_ROOM_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByRoomAddrIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where roomAddr is not null
        defaultWamoliUserShouldBeFound("roomAddr.specified=true");

        // Get all the wamoliUserList where roomAddr is null
        defaultWamoliUserShouldNotBeFound("roomAddr.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByRoomAddrIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where roomAddr is greater than or equal to DEFAULT_ROOM_ADDR
        defaultWamoliUserShouldBeFound("roomAddr.greaterThanOrEqual=" + DEFAULT_ROOM_ADDR);

        // Get all the wamoliUserList where roomAddr is greater than or equal to UPDATED_ROOM_ADDR
        defaultWamoliUserShouldNotBeFound("roomAddr.greaterThanOrEqual=" + UPDATED_ROOM_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByRoomAddrIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where roomAddr is less than or equal to DEFAULT_ROOM_ADDR
        defaultWamoliUserShouldBeFound("roomAddr.lessThanOrEqual=" + DEFAULT_ROOM_ADDR);

        // Get all the wamoliUserList where roomAddr is less than or equal to SMALLER_ROOM_ADDR
        defaultWamoliUserShouldNotBeFound("roomAddr.lessThanOrEqual=" + SMALLER_ROOM_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByRoomAddrIsLessThanSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where roomAddr is less than DEFAULT_ROOM_ADDR
        defaultWamoliUserShouldNotBeFound("roomAddr.lessThan=" + DEFAULT_ROOM_ADDR);

        // Get all the wamoliUserList where roomAddr is less than UPDATED_ROOM_ADDR
        defaultWamoliUserShouldBeFound("roomAddr.lessThan=" + UPDATED_ROOM_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByRoomAddrIsGreaterThanSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where roomAddr is greater than DEFAULT_ROOM_ADDR
        defaultWamoliUserShouldNotBeFound("roomAddr.greaterThan=" + DEFAULT_ROOM_ADDR);

        // Get all the wamoliUserList where roomAddr is greater than SMALLER_ROOM_ADDR
        defaultWamoliUserShouldBeFound("roomAddr.greaterThan=" + SMALLER_ROOM_ADDR);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardNumIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardNum equals to DEFAULT_ID_CARD_NUM
        defaultWamoliUserShouldBeFound("idCardNum.equals=" + DEFAULT_ID_CARD_NUM);

        // Get all the wamoliUserList where idCardNum equals to UPDATED_ID_CARD_NUM
        defaultWamoliUserShouldNotBeFound("idCardNum.equals=" + UPDATED_ID_CARD_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardNum not equals to DEFAULT_ID_CARD_NUM
        defaultWamoliUserShouldNotBeFound("idCardNum.notEquals=" + DEFAULT_ID_CARD_NUM);

        // Get all the wamoliUserList where idCardNum not equals to UPDATED_ID_CARD_NUM
        defaultWamoliUserShouldBeFound("idCardNum.notEquals=" + UPDATED_ID_CARD_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardNumIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardNum in DEFAULT_ID_CARD_NUM or UPDATED_ID_CARD_NUM
        defaultWamoliUserShouldBeFound("idCardNum.in=" + DEFAULT_ID_CARD_NUM + "," + UPDATED_ID_CARD_NUM);

        // Get all the wamoliUserList where idCardNum equals to UPDATED_ID_CARD_NUM
        defaultWamoliUserShouldNotBeFound("idCardNum.in=" + UPDATED_ID_CARD_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardNum is not null
        defaultWamoliUserShouldBeFound("idCardNum.specified=true");

        // Get all the wamoliUserList where idCardNum is null
        defaultWamoliUserShouldNotBeFound("idCardNum.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardNumContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardNum contains DEFAULT_ID_CARD_NUM
        defaultWamoliUserShouldBeFound("idCardNum.contains=" + DEFAULT_ID_CARD_NUM);

        // Get all the wamoliUserList where idCardNum contains UPDATED_ID_CARD_NUM
        defaultWamoliUserShouldNotBeFound("idCardNum.contains=" + UPDATED_ID_CARD_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardNumNotContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardNum does not contain DEFAULT_ID_CARD_NUM
        defaultWamoliUserShouldNotBeFound("idCardNum.doesNotContain=" + DEFAULT_ID_CARD_NUM);

        // Get all the wamoliUserList where idCardNum does not contain UPDATED_ID_CARD_NUM
        defaultWamoliUserShouldBeFound("idCardNum.doesNotContain=" + UPDATED_ID_CARD_NUM);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardType equals to DEFAULT_ID_CARD_TYPE
        defaultWamoliUserShouldBeFound("idCardType.equals=" + DEFAULT_ID_CARD_TYPE);

        // Get all the wamoliUserList where idCardType equals to UPDATED_ID_CARD_TYPE
        defaultWamoliUserShouldNotBeFound("idCardType.equals=" + UPDATED_ID_CARD_TYPE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardType not equals to DEFAULT_ID_CARD_TYPE
        defaultWamoliUserShouldNotBeFound("idCardType.notEquals=" + DEFAULT_ID_CARD_TYPE);

        // Get all the wamoliUserList where idCardType not equals to UPDATED_ID_CARD_TYPE
        defaultWamoliUserShouldBeFound("idCardType.notEquals=" + UPDATED_ID_CARD_TYPE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardTypeIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardType in DEFAULT_ID_CARD_TYPE or UPDATED_ID_CARD_TYPE
        defaultWamoliUserShouldBeFound("idCardType.in=" + DEFAULT_ID_CARD_TYPE + "," + UPDATED_ID_CARD_TYPE);

        // Get all the wamoliUserList where idCardType equals to UPDATED_ID_CARD_TYPE
        defaultWamoliUserShouldNotBeFound("idCardType.in=" + UPDATED_ID_CARD_TYPE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIdCardTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where idCardType is not null
        defaultWamoliUserShouldBeFound("idCardType.specified=true");

        // Get all the wamoliUserList where idCardType is null
        defaultWamoliUserShouldNotBeFound("idCardType.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userType equals to DEFAULT_USER_TYPE
        defaultWamoliUserShouldBeFound("userType.equals=" + DEFAULT_USER_TYPE);

        // Get all the wamoliUserList where userType equals to UPDATED_USER_TYPE
        defaultWamoliUserShouldNotBeFound("userType.equals=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userType not equals to DEFAULT_USER_TYPE
        defaultWamoliUserShouldNotBeFound("userType.notEquals=" + DEFAULT_USER_TYPE);

        // Get all the wamoliUserList where userType not equals to UPDATED_USER_TYPE
        defaultWamoliUserShouldBeFound("userType.notEquals=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserTypeIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userType in DEFAULT_USER_TYPE or UPDATED_USER_TYPE
        defaultWamoliUserShouldBeFound("userType.in=" + DEFAULT_USER_TYPE + "," + UPDATED_USER_TYPE);

        // Get all the wamoliUserList where userType equals to UPDATED_USER_TYPE
        defaultWamoliUserShouldNotBeFound("userType.in=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where userType is not null
        defaultWamoliUserShouldBeFound("userType.specified=true");

        // Get all the wamoliUserList where userType is null
        defaultWamoliUserShouldNotBeFound("userType.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where enable equals to DEFAULT_ENABLE
        defaultWamoliUserShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the wamoliUserList where enable equals to UPDATED_ENABLE
        defaultWamoliUserShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where enable not equals to DEFAULT_ENABLE
        defaultWamoliUserShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the wamoliUserList where enable not equals to UPDATED_ENABLE
        defaultWamoliUserShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultWamoliUserShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the wamoliUserList where enable equals to UPDATED_ENABLE
        defaultWamoliUserShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where enable is not null
        defaultWamoliUserShouldBeFound("enable.specified=true");

        // Get all the wamoliUserList where enable is null
        defaultWamoliUserShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIsManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where isManager equals to DEFAULT_IS_MANAGER
        defaultWamoliUserShouldBeFound("isManager.equals=" + DEFAULT_IS_MANAGER);

        // Get all the wamoliUserList where isManager equals to UPDATED_IS_MANAGER
        defaultWamoliUserShouldNotBeFound("isManager.equals=" + UPDATED_IS_MANAGER);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIsManagerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where isManager not equals to DEFAULT_IS_MANAGER
        defaultWamoliUserShouldNotBeFound("isManager.notEquals=" + DEFAULT_IS_MANAGER);

        // Get all the wamoliUserList where isManager not equals to UPDATED_IS_MANAGER
        defaultWamoliUserShouldBeFound("isManager.notEquals=" + UPDATED_IS_MANAGER);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIsManagerIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where isManager in DEFAULT_IS_MANAGER or UPDATED_IS_MANAGER
        defaultWamoliUserShouldBeFound("isManager.in=" + DEFAULT_IS_MANAGER + "," + UPDATED_IS_MANAGER);

        // Get all the wamoliUserList where isManager equals to UPDATED_IS_MANAGER
        defaultWamoliUserShouldNotBeFound("isManager.in=" + UPDATED_IS_MANAGER);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByIsManagerIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where isManager is not null
        defaultWamoliUserShouldBeFound("isManager.specified=true");

        // Get all the wamoliUserList where isManager is null
        defaultWamoliUserShouldNotBeFound("isManager.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultWamoliUserShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the wamoliUserList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultWamoliUserShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultWamoliUserShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the wamoliUserList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultWamoliUserShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultWamoliUserShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the wamoliUserList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultWamoliUserShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedBy is not null
        defaultWamoliUserShouldBeFound("lastModifiedBy.specified=true");

        // Get all the wamoliUserList where lastModifiedBy is null
        defaultWamoliUserShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultWamoliUserShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the wamoliUserList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultWamoliUserShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultWamoliUserShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the wamoliUserList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultWamoliUserShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultWamoliUserShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the wamoliUserList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultWamoliUserShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultWamoliUserShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the wamoliUserList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultWamoliUserShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultWamoliUserShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the wamoliUserList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultWamoliUserShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllWamoliUsersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        // Get all the wamoliUserList where lastModifiedDate is not null
        defaultWamoliUserShouldBeFound("lastModifiedDate.specified=true");

        // Get all the wamoliUserList where lastModifiedDate is null
        defaultWamoliUserShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWamoliUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        wamoliUser.setUser(user);
        wamoliUserRepository.saveAndFlush(wamoliUser);
        Long userId = user.getId();

        // Get all the wamoliUserList where user equals to userId
        defaultWamoliUserShouldBeFound("userId.equals=" + userId);

        // Get all the wamoliUserList where user equals to (userId + 1)
        defaultWamoliUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    void getAllWamoliUsersByCompanyDeptIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);
        CompanyDept companyDept = CompanyDeptResourceIT.createEntity(em);
        em.persist(companyDept);
        em.flush();
        wamoliUser.addCompanyDept(companyDept);
        wamoliUserRepository.saveAndFlush(wamoliUser);
        Long companyDeptId = companyDept.getId();

        // Get all the wamoliUserList where companyDept equals to companyDeptId
        defaultWamoliUserShouldBeFound("companyDeptId.equals=" + companyDeptId);

        // Get all the wamoliUserList where companyDept equals to (companyDeptId + 1)
        defaultWamoliUserShouldNotBeFound("companyDeptId.equals=" + (companyDeptId + 1));
    }

    @Test
    @Transactional
    void getAllWamoliUsersByCompanyPostIsEqualToSomething() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);
        CompanyPost companyPost = CompanyPostResourceIT.createEntity(em);
        em.persist(companyPost);
        em.flush();
        wamoliUser.addCompanyPost(companyPost);
        wamoliUserRepository.saveAndFlush(wamoliUser);
        Long companyPostId = companyPost.getId();

        // Get all the wamoliUserList where companyPost equals to companyPostId
        defaultWamoliUserShouldBeFound("companyPostId.equals=" + companyPostId);

        // Get all the wamoliUserList where companyPost equals to (companyPostId + 1)
        defaultWamoliUserShouldNotBeFound("companyPostId.equals=" + (companyPostId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWamoliUserShouldBeFound(String filter) throws Exception {
        restWamoliUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wamoliUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].phoneNum").value(hasItem(DEFAULT_PHONE_NUM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].unitAddr").value(hasItem(DEFAULT_UNIT_ADDR)))
            .andExpect(jsonPath("$.[*].roomAddr").value(hasItem(DEFAULT_ROOM_ADDR.intValue())))
            .andExpect(jsonPath("$.[*].idCardNum").value(hasItem(DEFAULT_ID_CARD_NUM)))
            .andExpect(jsonPath("$.[*].idCardType").value(hasItem(DEFAULT_ID_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isManager").value(hasItem(DEFAULT_IS_MANAGER.booleanValue())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restWamoliUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWamoliUserShouldNotBeFound(String filter) throws Exception {
        restWamoliUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWamoliUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWamoliUser() throws Exception {
        // Get the wamoliUser
        restWamoliUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWamoliUser() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();

        // Update the wamoliUser
        WamoliUser updatedWamoliUser = wamoliUserRepository.findById(wamoliUser.getId()).get();
        // Disconnect from session so that the updates on updatedWamoliUser are not directly saved in db
        em.detach(updatedWamoliUser);
        updatedWamoliUser
            .userName(UPDATED_USER_NAME)
            .gender(UPDATED_GENDER)
            .phoneNum(UPDATED_PHONE_NUM)
            .email(UPDATED_EMAIL)
            .unitAddr(UPDATED_UNIT_ADDR)
            .roomAddr(UPDATED_ROOM_ADDR)
            .idCardNum(UPDATED_ID_CARD_NUM)
            .idCardType(UPDATED_ID_CARD_TYPE)
            .userType(UPDATED_USER_TYPE)
            .enable(UPDATED_ENABLE)
            .isManager(UPDATED_IS_MANAGER)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWamoliUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWamoliUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWamoliUser))
            )
            .andExpect(status().isOk());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
        WamoliUser testWamoliUser = wamoliUserList.get(wamoliUserList.size() - 1);
        assertThat(testWamoliUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testWamoliUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testWamoliUser.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testWamoliUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWamoliUser.getUnitAddr()).isEqualTo(UPDATED_UNIT_ADDR);
        assertThat(testWamoliUser.getRoomAddr()).isEqualTo(UPDATED_ROOM_ADDR);
        assertThat(testWamoliUser.getIdCardNum()).isEqualTo(UPDATED_ID_CARD_NUM);
        assertThat(testWamoliUser.getIdCardType()).isEqualTo(UPDATED_ID_CARD_TYPE);
        assertThat(testWamoliUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testWamoliUser.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testWamoliUser.getIsManager()).isEqualTo(UPDATED_IS_MANAGER);
        assertThat(testWamoliUser.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWamoliUser.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingWamoliUser() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();
        wamoliUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWamoliUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wamoliUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWamoliUser() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();
        wamoliUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWamoliUser() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();
        wamoliUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wamoliUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWamoliUserWithPatch() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();

        // Update the wamoliUser using partial update
        WamoliUser partialUpdatedWamoliUser = new WamoliUser();
        partialUpdatedWamoliUser.setId(wamoliUser.getId());

        partialUpdatedWamoliUser
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .roomAddr(UPDATED_ROOM_ADDR)
            .idCardType(UPDATED_ID_CARD_TYPE)
            .userType(UPDATED_USER_TYPE)
            .enable(UPDATED_ENABLE)
            .isManager(UPDATED_IS_MANAGER)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWamoliUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWamoliUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWamoliUser))
            )
            .andExpect(status().isOk());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
        WamoliUser testWamoliUser = wamoliUserList.get(wamoliUserList.size() - 1);
        assertThat(testWamoliUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testWamoliUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testWamoliUser.getPhoneNum()).isEqualTo(DEFAULT_PHONE_NUM);
        assertThat(testWamoliUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWamoliUser.getUnitAddr()).isEqualTo(DEFAULT_UNIT_ADDR);
        assertThat(testWamoliUser.getRoomAddr()).isEqualTo(UPDATED_ROOM_ADDR);
        assertThat(testWamoliUser.getIdCardNum()).isEqualTo(DEFAULT_ID_CARD_NUM);
        assertThat(testWamoliUser.getIdCardType()).isEqualTo(UPDATED_ID_CARD_TYPE);
        assertThat(testWamoliUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testWamoliUser.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testWamoliUser.getIsManager()).isEqualTo(UPDATED_IS_MANAGER);
        assertThat(testWamoliUser.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testWamoliUser.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateWamoliUserWithPatch() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();

        // Update the wamoliUser using partial update
        WamoliUser partialUpdatedWamoliUser = new WamoliUser();
        partialUpdatedWamoliUser.setId(wamoliUser.getId());

        partialUpdatedWamoliUser
            .userName(UPDATED_USER_NAME)
            .gender(UPDATED_GENDER)
            .phoneNum(UPDATED_PHONE_NUM)
            .email(UPDATED_EMAIL)
            .unitAddr(UPDATED_UNIT_ADDR)
            .roomAddr(UPDATED_ROOM_ADDR)
            .idCardNum(UPDATED_ID_CARD_NUM)
            .idCardType(UPDATED_ID_CARD_TYPE)
            .userType(UPDATED_USER_TYPE)
            .enable(UPDATED_ENABLE)
            .isManager(UPDATED_IS_MANAGER)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWamoliUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWamoliUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWamoliUser))
            )
            .andExpect(status().isOk());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
        WamoliUser testWamoliUser = wamoliUserList.get(wamoliUserList.size() - 1);
        assertThat(testWamoliUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testWamoliUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testWamoliUser.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testWamoliUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWamoliUser.getUnitAddr()).isEqualTo(UPDATED_UNIT_ADDR);
        assertThat(testWamoliUser.getRoomAddr()).isEqualTo(UPDATED_ROOM_ADDR);
        assertThat(testWamoliUser.getIdCardNum()).isEqualTo(UPDATED_ID_CARD_NUM);
        assertThat(testWamoliUser.getIdCardType()).isEqualTo(UPDATED_ID_CARD_TYPE);
        assertThat(testWamoliUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
        assertThat(testWamoliUser.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testWamoliUser.getIsManager()).isEqualTo(UPDATED_IS_MANAGER);
        assertThat(testWamoliUser.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWamoliUser.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingWamoliUser() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();
        wamoliUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWamoliUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wamoliUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWamoliUser() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();
        wamoliUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wamoliUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWamoliUser() throws Exception {
        int databaseSizeBeforeUpdate = wamoliUserRepository.findAll().size();
        wamoliUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWamoliUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wamoliUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WamoliUser in the database
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWamoliUser() throws Exception {
        // Initialize the database
        wamoliUserRepository.saveAndFlush(wamoliUser);

        int databaseSizeBeforeDelete = wamoliUserRepository.findAll().size();

        // Delete the wamoliUser
        restWamoliUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, wamoliUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WamoliUser> wamoliUserList = wamoliUserRepository.findAll();
        assertThat(wamoliUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
