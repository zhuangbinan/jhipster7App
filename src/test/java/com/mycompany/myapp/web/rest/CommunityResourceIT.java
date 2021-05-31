package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Community;
import com.mycompany.myapp.domain.CommunityLeader;
import com.mycompany.myapp.domain.CommunityNotice;
import com.mycompany.myapp.domain.HomelandStation;
import com.mycompany.myapp.repository.CommunityRepository;
import com.mycompany.myapp.service.criteria.CommunityCriteria;
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
 * Integration tests for the {@link CommunityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityResourceIT {

    private static final String DEFAULT_CNAME = "AAAAAAAAAA";
    private static final String UPDATED_CNAME = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_HOURS = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_HOURS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;
    private static final Long SMALLER_PARENT_ID = 1L - 1L;

    private static final String DEFAULT_ANCESTORS = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTORS = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LONG_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final Boolean DEFAULT_DEL_FLAG = false;
    private static final Boolean UPDATED_DEL_FLAG = true;

    private static final Integer DEFAULT_ORDER_NUM = 1;
    private static final Integer UPDATED_ORDER_NUM = 2;
    private static final Integer SMALLER_ORDER_NUM = 1 - 1;

    private static final Instant DEFAULT_LAST_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFY_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFY_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/communities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityMockMvc;

    private Community community;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Community createEntity(EntityManager em) {
        Community community = new Community()
            .cname(DEFAULT_CNAME)
            .managerName(DEFAULT_MANAGER_NAME)
            .address(DEFAULT_ADDRESS)
            .tel(DEFAULT_TEL)
            .email(DEFAULT_EMAIL)
            .officeHours(DEFAULT_OFFICE_HOURS)
            .description(DEFAULT_DESCRIPTION)
            .source(DEFAULT_SOURCE)
            .parentId(DEFAULT_PARENT_ID)
            .ancestors(DEFAULT_ANCESTORS)
            .longCode(DEFAULT_LONG_CODE)
            .enable(DEFAULT_ENABLE)
            .delFlag(DEFAULT_DEL_FLAG)
            .orderNum(DEFAULT_ORDER_NUM)
            .lastModifyDate(DEFAULT_LAST_MODIFY_DATE)
            .lastModifyBy(DEFAULT_LAST_MODIFY_BY);
        return community;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Community createUpdatedEntity(EntityManager em) {
        Community community = new Community()
            .cname(UPDATED_CNAME)
            .managerName(UPDATED_MANAGER_NAME)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .officeHours(UPDATED_OFFICE_HOURS)
            .description(UPDATED_DESCRIPTION)
            .source(UPDATED_SOURCE)
            .parentId(UPDATED_PARENT_ID)
            .ancestors(UPDATED_ANCESTORS)
            .longCode(UPDATED_LONG_CODE)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);
        return community;
    }

    @BeforeEach
    public void initTest() {
        community = createEntity(em);
    }

    @Test
    @Transactional
    void createCommunity() throws Exception {
        int databaseSizeBeforeCreate = communityRepository.findAll().size();
        // Create the Community
        restCommunityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(community)))
            .andExpect(status().isCreated());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeCreate + 1);
        Community testCommunity = communityList.get(communityList.size() - 1);
        assertThat(testCommunity.getCname()).isEqualTo(DEFAULT_CNAME);
        assertThat(testCommunity.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
        assertThat(testCommunity.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCommunity.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCommunity.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCommunity.getOfficeHours()).isEqualTo(DEFAULT_OFFICE_HOURS);
        assertThat(testCommunity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCommunity.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testCommunity.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testCommunity.getAncestors()).isEqualTo(DEFAULT_ANCESTORS);
        assertThat(testCommunity.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
        assertThat(testCommunity.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCommunity.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCommunity.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCommunity.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testCommunity.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void createCommunityWithExistingId() throws Exception {
        // Create the Community with an existing ID
        community.setId(1L);

        int databaseSizeBeforeCreate = communityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(community)))
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommunities() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(community.getId().intValue())))
            .andExpect(jsonPath("$.[*].cname").value(hasItem(DEFAULT_CNAME)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].officeHours").value(hasItem(DEFAULT_OFFICE_HOURS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].ancestors").value(hasItem(DEFAULT_ANCESTORS)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));
    }

    @Test
    @Transactional
    void getCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get the community
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL_ID, community.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(community.getId().intValue()))
            .andExpect(jsonPath("$.cname").value(DEFAULT_CNAME))
            .andExpect(jsonPath("$.managerName").value(DEFAULT_MANAGER_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.officeHours").value(DEFAULT_OFFICE_HOURS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.ancestors").value(DEFAULT_ANCESTORS))
            .andExpect(jsonPath("$.longCode").value(DEFAULT_LONG_CODE))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.booleanValue()))
            .andExpect(jsonPath("$.orderNum").value(DEFAULT_ORDER_NUM))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()))
            .andExpect(jsonPath("$.lastModifyBy").value(DEFAULT_LAST_MODIFY_BY));
    }

    @Test
    @Transactional
    void getCommunitiesByIdFiltering() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        Long id = community.getId();

        defaultCommunityShouldBeFound("id.equals=" + id);
        defaultCommunityShouldNotBeFound("id.notEquals=" + id);

        defaultCommunityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommunityShouldNotBeFound("id.greaterThan=" + id);

        defaultCommunityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommunityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCnameIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where cname equals to DEFAULT_CNAME
        defaultCommunityShouldBeFound("cname.equals=" + DEFAULT_CNAME);

        // Get all the communityList where cname equals to UPDATED_CNAME
        defaultCommunityShouldNotBeFound("cname.equals=" + UPDATED_CNAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where cname not equals to DEFAULT_CNAME
        defaultCommunityShouldNotBeFound("cname.notEquals=" + DEFAULT_CNAME);

        // Get all the communityList where cname not equals to UPDATED_CNAME
        defaultCommunityShouldBeFound("cname.notEquals=" + UPDATED_CNAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCnameIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where cname in DEFAULT_CNAME or UPDATED_CNAME
        defaultCommunityShouldBeFound("cname.in=" + DEFAULT_CNAME + "," + UPDATED_CNAME);

        // Get all the communityList where cname equals to UPDATED_CNAME
        defaultCommunityShouldNotBeFound("cname.in=" + UPDATED_CNAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where cname is not null
        defaultCommunityShouldBeFound("cname.specified=true");

        // Get all the communityList where cname is null
        defaultCommunityShouldNotBeFound("cname.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByCnameContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where cname contains DEFAULT_CNAME
        defaultCommunityShouldBeFound("cname.contains=" + DEFAULT_CNAME);

        // Get all the communityList where cname contains UPDATED_CNAME
        defaultCommunityShouldNotBeFound("cname.contains=" + UPDATED_CNAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCnameNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where cname does not contain DEFAULT_CNAME
        defaultCommunityShouldNotBeFound("cname.doesNotContain=" + DEFAULT_CNAME);

        // Get all the communityList where cname does not contain UPDATED_CNAME
        defaultCommunityShouldBeFound("cname.doesNotContain=" + UPDATED_CNAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByManagerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where managerName equals to DEFAULT_MANAGER_NAME
        defaultCommunityShouldBeFound("managerName.equals=" + DEFAULT_MANAGER_NAME);

        // Get all the communityList where managerName equals to UPDATED_MANAGER_NAME
        defaultCommunityShouldNotBeFound("managerName.equals=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByManagerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where managerName not equals to DEFAULT_MANAGER_NAME
        defaultCommunityShouldNotBeFound("managerName.notEquals=" + DEFAULT_MANAGER_NAME);

        // Get all the communityList where managerName not equals to UPDATED_MANAGER_NAME
        defaultCommunityShouldBeFound("managerName.notEquals=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByManagerNameIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where managerName in DEFAULT_MANAGER_NAME or UPDATED_MANAGER_NAME
        defaultCommunityShouldBeFound("managerName.in=" + DEFAULT_MANAGER_NAME + "," + UPDATED_MANAGER_NAME);

        // Get all the communityList where managerName equals to UPDATED_MANAGER_NAME
        defaultCommunityShouldNotBeFound("managerName.in=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByManagerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where managerName is not null
        defaultCommunityShouldBeFound("managerName.specified=true");

        // Get all the communityList where managerName is null
        defaultCommunityShouldNotBeFound("managerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByManagerNameContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where managerName contains DEFAULT_MANAGER_NAME
        defaultCommunityShouldBeFound("managerName.contains=" + DEFAULT_MANAGER_NAME);

        // Get all the communityList where managerName contains UPDATED_MANAGER_NAME
        defaultCommunityShouldNotBeFound("managerName.contains=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByManagerNameNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where managerName does not contain DEFAULT_MANAGER_NAME
        defaultCommunityShouldNotBeFound("managerName.doesNotContain=" + DEFAULT_MANAGER_NAME);

        // Get all the communityList where managerName does not contain UPDATED_MANAGER_NAME
        defaultCommunityShouldBeFound("managerName.doesNotContain=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where address equals to DEFAULT_ADDRESS
        defaultCommunityShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the communityList where address equals to UPDATED_ADDRESS
        defaultCommunityShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where address not equals to DEFAULT_ADDRESS
        defaultCommunityShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the communityList where address not equals to UPDATED_ADDRESS
        defaultCommunityShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCommunityShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the communityList where address equals to UPDATED_ADDRESS
        defaultCommunityShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where address is not null
        defaultCommunityShouldBeFound("address.specified=true");

        // Get all the communityList where address is null
        defaultCommunityShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByAddressContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where address contains DEFAULT_ADDRESS
        defaultCommunityShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the communityList where address contains UPDATED_ADDRESS
        defaultCommunityShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where address does not contain DEFAULT_ADDRESS
        defaultCommunityShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the communityList where address does not contain UPDATED_ADDRESS
        defaultCommunityShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where tel equals to DEFAULT_TEL
        defaultCommunityShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the communityList where tel equals to UPDATED_TEL
        defaultCommunityShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByTelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where tel not equals to DEFAULT_TEL
        defaultCommunityShouldNotBeFound("tel.notEquals=" + DEFAULT_TEL);

        // Get all the communityList where tel not equals to UPDATED_TEL
        defaultCommunityShouldBeFound("tel.notEquals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByTelIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultCommunityShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the communityList where tel equals to UPDATED_TEL
        defaultCommunityShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where tel is not null
        defaultCommunityShouldBeFound("tel.specified=true");

        // Get all the communityList where tel is null
        defaultCommunityShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByTelContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where tel contains DEFAULT_TEL
        defaultCommunityShouldBeFound("tel.contains=" + DEFAULT_TEL);

        // Get all the communityList where tel contains UPDATED_TEL
        defaultCommunityShouldNotBeFound("tel.contains=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByTelNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where tel does not contain DEFAULT_TEL
        defaultCommunityShouldNotBeFound("tel.doesNotContain=" + DEFAULT_TEL);

        // Get all the communityList where tel does not contain UPDATED_TEL
        defaultCommunityShouldBeFound("tel.doesNotContain=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where email equals to DEFAULT_EMAIL
        defaultCommunityShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the communityList where email equals to UPDATED_EMAIL
        defaultCommunityShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where email not equals to DEFAULT_EMAIL
        defaultCommunityShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the communityList where email not equals to UPDATED_EMAIL
        defaultCommunityShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCommunityShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the communityList where email equals to UPDATED_EMAIL
        defaultCommunityShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where email is not null
        defaultCommunityShouldBeFound("email.specified=true");

        // Get all the communityList where email is null
        defaultCommunityShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByEmailContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where email contains DEFAULT_EMAIL
        defaultCommunityShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the communityList where email contains UPDATED_EMAIL
        defaultCommunityShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where email does not contain DEFAULT_EMAIL
        defaultCommunityShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the communityList where email does not contain UPDATED_EMAIL
        defaultCommunityShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOfficeHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where officeHours equals to DEFAULT_OFFICE_HOURS
        defaultCommunityShouldBeFound("officeHours.equals=" + DEFAULT_OFFICE_HOURS);

        // Get all the communityList where officeHours equals to UPDATED_OFFICE_HOURS
        defaultCommunityShouldNotBeFound("officeHours.equals=" + UPDATED_OFFICE_HOURS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOfficeHoursIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where officeHours not equals to DEFAULT_OFFICE_HOURS
        defaultCommunityShouldNotBeFound("officeHours.notEquals=" + DEFAULT_OFFICE_HOURS);

        // Get all the communityList where officeHours not equals to UPDATED_OFFICE_HOURS
        defaultCommunityShouldBeFound("officeHours.notEquals=" + UPDATED_OFFICE_HOURS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOfficeHoursIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where officeHours in DEFAULT_OFFICE_HOURS or UPDATED_OFFICE_HOURS
        defaultCommunityShouldBeFound("officeHours.in=" + DEFAULT_OFFICE_HOURS + "," + UPDATED_OFFICE_HOURS);

        // Get all the communityList where officeHours equals to UPDATED_OFFICE_HOURS
        defaultCommunityShouldNotBeFound("officeHours.in=" + UPDATED_OFFICE_HOURS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOfficeHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where officeHours is not null
        defaultCommunityShouldBeFound("officeHours.specified=true");

        // Get all the communityList where officeHours is null
        defaultCommunityShouldNotBeFound("officeHours.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByOfficeHoursContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where officeHours contains DEFAULT_OFFICE_HOURS
        defaultCommunityShouldBeFound("officeHours.contains=" + DEFAULT_OFFICE_HOURS);

        // Get all the communityList where officeHours contains UPDATED_OFFICE_HOURS
        defaultCommunityShouldNotBeFound("officeHours.contains=" + UPDATED_OFFICE_HOURS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOfficeHoursNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where officeHours does not contain DEFAULT_OFFICE_HOURS
        defaultCommunityShouldNotBeFound("officeHours.doesNotContain=" + DEFAULT_OFFICE_HOURS);

        // Get all the communityList where officeHours does not contain UPDATED_OFFICE_HOURS
        defaultCommunityShouldBeFound("officeHours.doesNotContain=" + UPDATED_OFFICE_HOURS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where description equals to DEFAULT_DESCRIPTION
        defaultCommunityShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the communityList where description equals to UPDATED_DESCRIPTION
        defaultCommunityShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where description not equals to DEFAULT_DESCRIPTION
        defaultCommunityShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the communityList where description not equals to UPDATED_DESCRIPTION
        defaultCommunityShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCommunityShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the communityList where description equals to UPDATED_DESCRIPTION
        defaultCommunityShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where description is not null
        defaultCommunityShouldBeFound("description.specified=true");

        // Get all the communityList where description is null
        defaultCommunityShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where description contains DEFAULT_DESCRIPTION
        defaultCommunityShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the communityList where description contains UPDATED_DESCRIPTION
        defaultCommunityShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where description does not contain DEFAULT_DESCRIPTION
        defaultCommunityShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the communityList where description does not contain UPDATED_DESCRIPTION
        defaultCommunityShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommunitiesBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where source equals to DEFAULT_SOURCE
        defaultCommunityShouldBeFound("source.equals=" + DEFAULT_SOURCE);

        // Get all the communityList where source equals to UPDATED_SOURCE
        defaultCommunityShouldNotBeFound("source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCommunitiesBySourceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where source not equals to DEFAULT_SOURCE
        defaultCommunityShouldNotBeFound("source.notEquals=" + DEFAULT_SOURCE);

        // Get all the communityList where source not equals to UPDATED_SOURCE
        defaultCommunityShouldBeFound("source.notEquals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCommunitiesBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where source in DEFAULT_SOURCE or UPDATED_SOURCE
        defaultCommunityShouldBeFound("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE);

        // Get all the communityList where source equals to UPDATED_SOURCE
        defaultCommunityShouldNotBeFound("source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCommunitiesBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where source is not null
        defaultCommunityShouldBeFound("source.specified=true");

        // Get all the communityList where source is null
        defaultCommunityShouldNotBeFound("source.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesBySourceContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where source contains DEFAULT_SOURCE
        defaultCommunityShouldBeFound("source.contains=" + DEFAULT_SOURCE);

        // Get all the communityList where source contains UPDATED_SOURCE
        defaultCommunityShouldNotBeFound("source.contains=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCommunitiesBySourceNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where source does not contain DEFAULT_SOURCE
        defaultCommunityShouldNotBeFound("source.doesNotContain=" + DEFAULT_SOURCE);

        // Get all the communityList where source does not contain UPDATED_SOURCE
        defaultCommunityShouldBeFound("source.doesNotContain=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByParentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where parentId equals to DEFAULT_PARENT_ID
        defaultCommunityShouldBeFound("parentId.equals=" + DEFAULT_PARENT_ID);

        // Get all the communityList where parentId equals to UPDATED_PARENT_ID
        defaultCommunityShouldNotBeFound("parentId.equals=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCommunitiesByParentIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where parentId not equals to DEFAULT_PARENT_ID
        defaultCommunityShouldNotBeFound("parentId.notEquals=" + DEFAULT_PARENT_ID);

        // Get all the communityList where parentId not equals to UPDATED_PARENT_ID
        defaultCommunityShouldBeFound("parentId.notEquals=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCommunitiesByParentIdIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where parentId in DEFAULT_PARENT_ID or UPDATED_PARENT_ID
        defaultCommunityShouldBeFound("parentId.in=" + DEFAULT_PARENT_ID + "," + UPDATED_PARENT_ID);

        // Get all the communityList where parentId equals to UPDATED_PARENT_ID
        defaultCommunityShouldNotBeFound("parentId.in=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCommunitiesByParentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where parentId is not null
        defaultCommunityShouldBeFound("parentId.specified=true");

        // Get all the communityList where parentId is null
        defaultCommunityShouldNotBeFound("parentId.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByParentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where parentId is greater than or equal to DEFAULT_PARENT_ID
        defaultCommunityShouldBeFound("parentId.greaterThanOrEqual=" + DEFAULT_PARENT_ID);

        // Get all the communityList where parentId is greater than or equal to UPDATED_PARENT_ID
        defaultCommunityShouldNotBeFound("parentId.greaterThanOrEqual=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCommunitiesByParentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where parentId is less than or equal to DEFAULT_PARENT_ID
        defaultCommunityShouldBeFound("parentId.lessThanOrEqual=" + DEFAULT_PARENT_ID);

        // Get all the communityList where parentId is less than or equal to SMALLER_PARENT_ID
        defaultCommunityShouldNotBeFound("parentId.lessThanOrEqual=" + SMALLER_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCommunitiesByParentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where parentId is less than DEFAULT_PARENT_ID
        defaultCommunityShouldNotBeFound("parentId.lessThan=" + DEFAULT_PARENT_ID);

        // Get all the communityList where parentId is less than UPDATED_PARENT_ID
        defaultCommunityShouldBeFound("parentId.lessThan=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCommunitiesByParentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where parentId is greater than DEFAULT_PARENT_ID
        defaultCommunityShouldNotBeFound("parentId.greaterThan=" + DEFAULT_PARENT_ID);

        // Get all the communityList where parentId is greater than SMALLER_PARENT_ID
        defaultCommunityShouldBeFound("parentId.greaterThan=" + SMALLER_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAncestorsIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where ancestors equals to DEFAULT_ANCESTORS
        defaultCommunityShouldBeFound("ancestors.equals=" + DEFAULT_ANCESTORS);

        // Get all the communityList where ancestors equals to UPDATED_ANCESTORS
        defaultCommunityShouldNotBeFound("ancestors.equals=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAncestorsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where ancestors not equals to DEFAULT_ANCESTORS
        defaultCommunityShouldNotBeFound("ancestors.notEquals=" + DEFAULT_ANCESTORS);

        // Get all the communityList where ancestors not equals to UPDATED_ANCESTORS
        defaultCommunityShouldBeFound("ancestors.notEquals=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAncestorsIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where ancestors in DEFAULT_ANCESTORS or UPDATED_ANCESTORS
        defaultCommunityShouldBeFound("ancestors.in=" + DEFAULT_ANCESTORS + "," + UPDATED_ANCESTORS);

        // Get all the communityList where ancestors equals to UPDATED_ANCESTORS
        defaultCommunityShouldNotBeFound("ancestors.in=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAncestorsIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where ancestors is not null
        defaultCommunityShouldBeFound("ancestors.specified=true");

        // Get all the communityList where ancestors is null
        defaultCommunityShouldNotBeFound("ancestors.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByAncestorsContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where ancestors contains DEFAULT_ANCESTORS
        defaultCommunityShouldBeFound("ancestors.contains=" + DEFAULT_ANCESTORS);

        // Get all the communityList where ancestors contains UPDATED_ANCESTORS
        defaultCommunityShouldNotBeFound("ancestors.contains=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByAncestorsNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where ancestors does not contain DEFAULT_ANCESTORS
        defaultCommunityShouldNotBeFound("ancestors.doesNotContain=" + DEFAULT_ANCESTORS);

        // Get all the communityList where ancestors does not contain UPDATED_ANCESTORS
        defaultCommunityShouldBeFound("ancestors.doesNotContain=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLongCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where longCode equals to DEFAULT_LONG_CODE
        defaultCommunityShouldBeFound("longCode.equals=" + DEFAULT_LONG_CODE);

        // Get all the communityList where longCode equals to UPDATED_LONG_CODE
        defaultCommunityShouldNotBeFound("longCode.equals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLongCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where longCode not equals to DEFAULT_LONG_CODE
        defaultCommunityShouldNotBeFound("longCode.notEquals=" + DEFAULT_LONG_CODE);

        // Get all the communityList where longCode not equals to UPDATED_LONG_CODE
        defaultCommunityShouldBeFound("longCode.notEquals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLongCodeIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where longCode in DEFAULT_LONG_CODE or UPDATED_LONG_CODE
        defaultCommunityShouldBeFound("longCode.in=" + DEFAULT_LONG_CODE + "," + UPDATED_LONG_CODE);

        // Get all the communityList where longCode equals to UPDATED_LONG_CODE
        defaultCommunityShouldNotBeFound("longCode.in=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLongCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where longCode is not null
        defaultCommunityShouldBeFound("longCode.specified=true");

        // Get all the communityList where longCode is null
        defaultCommunityShouldNotBeFound("longCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByLongCodeContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where longCode contains DEFAULT_LONG_CODE
        defaultCommunityShouldBeFound("longCode.contains=" + DEFAULT_LONG_CODE);

        // Get all the communityList where longCode contains UPDATED_LONG_CODE
        defaultCommunityShouldNotBeFound("longCode.contains=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLongCodeNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where longCode does not contain DEFAULT_LONG_CODE
        defaultCommunityShouldNotBeFound("longCode.doesNotContain=" + DEFAULT_LONG_CODE);

        // Get all the communityList where longCode does not contain UPDATED_LONG_CODE
        defaultCommunityShouldBeFound("longCode.doesNotContain=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where enable equals to DEFAULT_ENABLE
        defaultCommunityShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the communityList where enable equals to UPDATED_ENABLE
        defaultCommunityShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where enable not equals to DEFAULT_ENABLE
        defaultCommunityShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the communityList where enable not equals to UPDATED_ENABLE
        defaultCommunityShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultCommunityShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the communityList where enable equals to UPDATED_ENABLE
        defaultCommunityShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where enable is not null
        defaultCommunityShouldBeFound("enable.specified=true");

        // Get all the communityList where enable is null
        defaultCommunityShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByDelFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where delFlag equals to DEFAULT_DEL_FLAG
        defaultCommunityShouldBeFound("delFlag.equals=" + DEFAULT_DEL_FLAG);

        // Get all the communityList where delFlag equals to UPDATED_DEL_FLAG
        defaultCommunityShouldNotBeFound("delFlag.equals=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunitiesByDelFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where delFlag not equals to DEFAULT_DEL_FLAG
        defaultCommunityShouldNotBeFound("delFlag.notEquals=" + DEFAULT_DEL_FLAG);

        // Get all the communityList where delFlag not equals to UPDATED_DEL_FLAG
        defaultCommunityShouldBeFound("delFlag.notEquals=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunitiesByDelFlagIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where delFlag in DEFAULT_DEL_FLAG or UPDATED_DEL_FLAG
        defaultCommunityShouldBeFound("delFlag.in=" + DEFAULT_DEL_FLAG + "," + UPDATED_DEL_FLAG);

        // Get all the communityList where delFlag equals to UPDATED_DEL_FLAG
        defaultCommunityShouldNotBeFound("delFlag.in=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunitiesByDelFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where delFlag is not null
        defaultCommunityShouldBeFound("delFlag.specified=true");

        // Get all the communityList where delFlag is null
        defaultCommunityShouldNotBeFound("delFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByOrderNumIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where orderNum equals to DEFAULT_ORDER_NUM
        defaultCommunityShouldBeFound("orderNum.equals=" + DEFAULT_ORDER_NUM);

        // Get all the communityList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityShouldNotBeFound("orderNum.equals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOrderNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where orderNum not equals to DEFAULT_ORDER_NUM
        defaultCommunityShouldNotBeFound("orderNum.notEquals=" + DEFAULT_ORDER_NUM);

        // Get all the communityList where orderNum not equals to UPDATED_ORDER_NUM
        defaultCommunityShouldBeFound("orderNum.notEquals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOrderNumIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where orderNum in DEFAULT_ORDER_NUM or UPDATED_ORDER_NUM
        defaultCommunityShouldBeFound("orderNum.in=" + DEFAULT_ORDER_NUM + "," + UPDATED_ORDER_NUM);

        // Get all the communityList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityShouldNotBeFound("orderNum.in=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOrderNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where orderNum is not null
        defaultCommunityShouldBeFound("orderNum.specified=true");

        // Get all the communityList where orderNum is null
        defaultCommunityShouldNotBeFound("orderNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByOrderNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where orderNum is greater than or equal to DEFAULT_ORDER_NUM
        defaultCommunityShouldBeFound("orderNum.greaterThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityList where orderNum is greater than or equal to UPDATED_ORDER_NUM
        defaultCommunityShouldNotBeFound("orderNum.greaterThanOrEqual=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOrderNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where orderNum is less than or equal to DEFAULT_ORDER_NUM
        defaultCommunityShouldBeFound("orderNum.lessThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityList where orderNum is less than or equal to SMALLER_ORDER_NUM
        defaultCommunityShouldNotBeFound("orderNum.lessThanOrEqual=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOrderNumIsLessThanSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where orderNum is less than DEFAULT_ORDER_NUM
        defaultCommunityShouldNotBeFound("orderNum.lessThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityList where orderNum is less than UPDATED_ORDER_NUM
        defaultCommunityShouldBeFound("orderNum.lessThan=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunitiesByOrderNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where orderNum is greater than DEFAULT_ORDER_NUM
        defaultCommunityShouldNotBeFound("orderNum.greaterThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityList where orderNum is greater than SMALLER_ORDER_NUM
        defaultCommunityShouldBeFound("orderNum.greaterThan=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyDate equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityShouldBeFound("lastModifyDate.equals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityShouldNotBeFound("lastModifyDate.equals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyDate not equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityShouldNotBeFound("lastModifyDate.notEquals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityList where lastModifyDate not equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityShouldBeFound("lastModifyDate.notEquals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyDate in DEFAULT_LAST_MODIFY_DATE or UPDATED_LAST_MODIFY_DATE
        defaultCommunityShouldBeFound("lastModifyDate.in=" + DEFAULT_LAST_MODIFY_DATE + "," + UPDATED_LAST_MODIFY_DATE);

        // Get all the communityList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityShouldNotBeFound("lastModifyDate.in=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyDate is not null
        defaultCommunityShouldBeFound("lastModifyDate.specified=true");

        // Get all the communityList where lastModifyDate is null
        defaultCommunityShouldNotBeFound("lastModifyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyByIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyBy equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityShouldBeFound("lastModifyBy.equals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityShouldNotBeFound("lastModifyBy.equals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyBy not equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityShouldNotBeFound("lastModifyBy.notEquals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityList where lastModifyBy not equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityShouldBeFound("lastModifyBy.notEquals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyByIsInShouldWork() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyBy in DEFAULT_LAST_MODIFY_BY or UPDATED_LAST_MODIFY_BY
        defaultCommunityShouldBeFound("lastModifyBy.in=" + DEFAULT_LAST_MODIFY_BY + "," + UPDATED_LAST_MODIFY_BY);

        // Get all the communityList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityShouldNotBeFound("lastModifyBy.in=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyByIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyBy is not null
        defaultCommunityShouldBeFound("lastModifyBy.specified=true");

        // Get all the communityList where lastModifyBy is null
        defaultCommunityShouldNotBeFound("lastModifyBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyByContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyBy contains DEFAULT_LAST_MODIFY_BY
        defaultCommunityShouldBeFound("lastModifyBy.contains=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityList where lastModifyBy contains UPDATED_LAST_MODIFY_BY
        defaultCommunityShouldNotBeFound("lastModifyBy.contains=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunitiesByLastModifyByNotContainsSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communityList where lastModifyBy does not contain DEFAULT_LAST_MODIFY_BY
        defaultCommunityShouldNotBeFound("lastModifyBy.doesNotContain=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityList where lastModifyBy does not contain UPDATED_LAST_MODIFY_BY
        defaultCommunityShouldBeFound("lastModifyBy.doesNotContain=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityNoticeIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        CommunityNotice communityNotice = CommunityNoticeResourceIT.createEntity(em);
        em.persist(communityNotice);
        em.flush();
        community.addCommunityNotice(communityNotice);
        communityRepository.saveAndFlush(community);
        Long communityNoticeId = communityNotice.getId();

        // Get all the communityList where communityNotice equals to communityNoticeId
        defaultCommunityShouldBeFound("communityNoticeId.equals=" + communityNoticeId);

        // Get all the communityList where communityNotice equals to (communityNoticeId + 1)
        defaultCommunityShouldNotBeFound("communityNoticeId.equals=" + (communityNoticeId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByCommunityLeaderIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        CommunityLeader communityLeader = CommunityLeaderResourceIT.createEntity(em);
        em.persist(communityLeader);
        em.flush();
        community.addCommunityLeader(communityLeader);
        communityRepository.saveAndFlush(community);
        Long communityLeaderId = communityLeader.getId();

        // Get all the communityList where communityLeader equals to communityLeaderId
        defaultCommunityShouldBeFound("communityLeaderId.equals=" + communityLeaderId);

        // Get all the communityList where communityLeader equals to (communityLeaderId + 1)
        defaultCommunityShouldNotBeFound("communityLeaderId.equals=" + (communityLeaderId + 1));
    }

    @Test
    @Transactional
    void getAllCommunitiesByHomelandStationIsEqualToSomething() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        HomelandStation homelandStation = HomelandStationResourceIT.createEntity(em);
        em.persist(homelandStation);
        em.flush();
        community.addHomelandStation(homelandStation);
        communityRepository.saveAndFlush(community);
        Long homelandStationId = homelandStation.getId();

        // Get all the communityList where homelandStation equals to homelandStationId
        defaultCommunityShouldBeFound("homelandStationId.equals=" + homelandStationId);

        // Get all the communityList where homelandStation equals to (homelandStationId + 1)
        defaultCommunityShouldNotBeFound("homelandStationId.equals=" + (homelandStationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommunityShouldBeFound(String filter) throws Exception {
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(community.getId().intValue())))
            .andExpect(jsonPath("$.[*].cname").value(hasItem(DEFAULT_CNAME)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].officeHours").value(hasItem(DEFAULT_OFFICE_HOURS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].ancestors").value(hasItem(DEFAULT_ANCESTORS)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));

        // Check, that the count call also returns 1
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommunityShouldNotBeFound(String filter) throws Exception {
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommunityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommunity() throws Exception {
        // Get the community
        restCommunityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        int databaseSizeBeforeUpdate = communityRepository.findAll().size();

        // Update the community
        Community updatedCommunity = communityRepository.findById(community.getId()).get();
        // Disconnect from session so that the updates on updatedCommunity are not directly saved in db
        em.detach(updatedCommunity);
        updatedCommunity
            .cname(UPDATED_CNAME)
            .managerName(UPDATED_MANAGER_NAME)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .officeHours(UPDATED_OFFICE_HOURS)
            .description(UPDATED_DESCRIPTION)
            .source(UPDATED_SOURCE)
            .parentId(UPDATED_PARENT_ID)
            .ancestors(UPDATED_ANCESTORS)
            .longCode(UPDATED_LONG_CODE)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommunity))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
        Community testCommunity = communityList.get(communityList.size() - 1);
        assertThat(testCommunity.getCname()).isEqualTo(UPDATED_CNAME);
        assertThat(testCommunity.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testCommunity.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCommunity.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCommunity.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCommunity.getOfficeHours()).isEqualTo(UPDATED_OFFICE_HOURS);
        assertThat(testCommunity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommunity.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testCommunity.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testCommunity.getAncestors()).isEqualTo(UPDATED_ANCESTORS);
        assertThat(testCommunity.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testCommunity.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCommunity.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCommunity.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunity.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunity.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void putNonExistingCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, community.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(community))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(community))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(community)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommunityWithPatch() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        int databaseSizeBeforeUpdate = communityRepository.findAll().size();

        // Update the community using partial update
        Community partialUpdatedCommunity = new Community();
        partialUpdatedCommunity.setId(community.getId());

        partialUpdatedCommunity
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .officeHours(UPDATED_OFFICE_HOURS)
            .description(UPDATED_DESCRIPTION)
            .source(UPDATED_SOURCE)
            .parentId(UPDATED_PARENT_ID)
            .longCode(UPDATED_LONG_CODE)
            .delFlag(UPDATED_DEL_FLAG)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);

        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunity))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
        Community testCommunity = communityList.get(communityList.size() - 1);
        assertThat(testCommunity.getCname()).isEqualTo(DEFAULT_CNAME);
        assertThat(testCommunity.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
        assertThat(testCommunity.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCommunity.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCommunity.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCommunity.getOfficeHours()).isEqualTo(UPDATED_OFFICE_HOURS);
        assertThat(testCommunity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommunity.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testCommunity.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testCommunity.getAncestors()).isEqualTo(DEFAULT_ANCESTORS);
        assertThat(testCommunity.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testCommunity.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCommunity.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCommunity.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCommunity.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunity.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void fullUpdateCommunityWithPatch() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        int databaseSizeBeforeUpdate = communityRepository.findAll().size();

        // Update the community using partial update
        Community partialUpdatedCommunity = new Community();
        partialUpdatedCommunity.setId(community.getId());

        partialUpdatedCommunity
            .cname(UPDATED_CNAME)
            .managerName(UPDATED_MANAGER_NAME)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .officeHours(UPDATED_OFFICE_HOURS)
            .description(UPDATED_DESCRIPTION)
            .source(UPDATED_SOURCE)
            .parentId(UPDATED_PARENT_ID)
            .ancestors(UPDATED_ANCESTORS)
            .longCode(UPDATED_LONG_CODE)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunity))
            )
            .andExpect(status().isOk());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
        Community testCommunity = communityList.get(communityList.size() - 1);
        assertThat(testCommunity.getCname()).isEqualTo(UPDATED_CNAME);
        assertThat(testCommunity.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testCommunity.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCommunity.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCommunity.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCommunity.getOfficeHours()).isEqualTo(UPDATED_OFFICE_HOURS);
        assertThat(testCommunity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommunity.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testCommunity.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testCommunity.getAncestors()).isEqualTo(UPDATED_ANCESTORS);
        assertThat(testCommunity.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testCommunity.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCommunity.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCommunity.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunity.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunity.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, community.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(community))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(community))
            )
            .andExpect(status().isBadRequest());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunity() throws Exception {
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();
        community.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(community))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Community in the database
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        int databaseSizeBeforeDelete = communityRepository.findAll().size();

        // Delete the community
        restCommunityMockMvc
            .perform(delete(ENTITY_API_URL_ID, community.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Community> communityList = communityRepository.findAll();
        assertThat(communityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
