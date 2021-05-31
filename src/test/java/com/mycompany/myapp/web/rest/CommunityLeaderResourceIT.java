package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Community;
import com.mycompany.myapp.domain.CommunityLeader;
import com.mycompany.myapp.repository.CommunityLeaderRepository;
import com.mycompany.myapp.service.criteria.CommunityLeaderCriteria;
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
 * Integration tests for the {@link CommunityLeaderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityLeaderResourceIT {

    private static final byte[] DEFAULT_AVATAR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AVATAR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AVATAR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AVATAR_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_REAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_DESC = "AAAAAAAAAA";
    private static final String UPDATED_JOB_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_CAREER_DESC = "AAAAAAAAAA";
    private static final String UPDATED_JOB_CAREER_DESC = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/community-leaders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommunityLeaderRepository communityLeaderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityLeaderMockMvc;

    private CommunityLeader communityLeader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityLeader createEntity(EntityManager em) {
        CommunityLeader communityLeader = new CommunityLeader()
            .avatar(DEFAULT_AVATAR)
            .avatarContentType(DEFAULT_AVATAR_CONTENT_TYPE)
            .realName(DEFAULT_REAL_NAME)
            .tel(DEFAULT_TEL)
            .jobTitle(DEFAULT_JOB_TITLE)
            .jobDesc(DEFAULT_JOB_DESC)
            .jobCareerDesc(DEFAULT_JOB_CAREER_DESC)
            .enable(DEFAULT_ENABLE)
            .delFlag(DEFAULT_DEL_FLAG)
            .orderNum(DEFAULT_ORDER_NUM)
            .lastModifyDate(DEFAULT_LAST_MODIFY_DATE)
            .lastModifyBy(DEFAULT_LAST_MODIFY_BY);
        return communityLeader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityLeader createUpdatedEntity(EntityManager em) {
        CommunityLeader communityLeader = new CommunityLeader()
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .realName(UPDATED_REAL_NAME)
            .tel(UPDATED_TEL)
            .jobTitle(UPDATED_JOB_TITLE)
            .jobDesc(UPDATED_JOB_DESC)
            .jobCareerDesc(UPDATED_JOB_CAREER_DESC)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);
        return communityLeader;
    }

    @BeforeEach
    public void initTest() {
        communityLeader = createEntity(em);
    }

    @Test
    @Transactional
    void createCommunityLeader() throws Exception {
        int databaseSizeBeforeCreate = communityLeaderRepository.findAll().size();
        // Create the CommunityLeader
        restCommunityLeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityLeader))
            )
            .andExpect(status().isCreated());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeCreate + 1);
        CommunityLeader testCommunityLeader = communityLeaderList.get(communityLeaderList.size() - 1);
        assertThat(testCommunityLeader.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testCommunityLeader.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
        assertThat(testCommunityLeader.getRealName()).isEqualTo(DEFAULT_REAL_NAME);
        assertThat(testCommunityLeader.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCommunityLeader.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testCommunityLeader.getJobDesc()).isEqualTo(DEFAULT_JOB_DESC);
        assertThat(testCommunityLeader.getJobCareerDesc()).isEqualTo(DEFAULT_JOB_CAREER_DESC);
        assertThat(testCommunityLeader.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCommunityLeader.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCommunityLeader.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCommunityLeader.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testCommunityLeader.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void createCommunityLeaderWithExistingId() throws Exception {
        // Create the CommunityLeader with an existing ID
        communityLeader.setId(1L);

        int databaseSizeBeforeCreate = communityLeaderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityLeaderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityLeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommunityLeaders() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList
        restCommunityLeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityLeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))))
            .andExpect(jsonPath("$.[*].realName").value(hasItem(DEFAULT_REAL_NAME)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].jobDesc").value(hasItem(DEFAULT_JOB_DESC)))
            .andExpect(jsonPath("$.[*].jobCareerDesc").value(hasItem(DEFAULT_JOB_CAREER_DESC.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));
    }

    @Test
    @Transactional
    void getCommunityLeader() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get the communityLeader
        restCommunityLeaderMockMvc
            .perform(get(ENTITY_API_URL_ID, communityLeader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communityLeader.getId().intValue()))
            .andExpect(jsonPath("$.avatarContentType").value(DEFAULT_AVATAR_CONTENT_TYPE))
            .andExpect(jsonPath("$.avatar").value(Base64Utils.encodeToString(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.realName").value(DEFAULT_REAL_NAME))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.jobDesc").value(DEFAULT_JOB_DESC))
            .andExpect(jsonPath("$.jobCareerDesc").value(DEFAULT_JOB_CAREER_DESC.toString()))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.booleanValue()))
            .andExpect(jsonPath("$.orderNum").value(DEFAULT_ORDER_NUM))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()))
            .andExpect(jsonPath("$.lastModifyBy").value(DEFAULT_LAST_MODIFY_BY));
    }

    @Test
    @Transactional
    void getCommunityLeadersByIdFiltering() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        Long id = communityLeader.getId();

        defaultCommunityLeaderShouldBeFound("id.equals=" + id);
        defaultCommunityLeaderShouldNotBeFound("id.notEquals=" + id);

        defaultCommunityLeaderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommunityLeaderShouldNotBeFound("id.greaterThan=" + id);

        defaultCommunityLeaderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommunityLeaderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByRealNameIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where realName equals to DEFAULT_REAL_NAME
        defaultCommunityLeaderShouldBeFound("realName.equals=" + DEFAULT_REAL_NAME);

        // Get all the communityLeaderList where realName equals to UPDATED_REAL_NAME
        defaultCommunityLeaderShouldNotBeFound("realName.equals=" + UPDATED_REAL_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByRealNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where realName not equals to DEFAULT_REAL_NAME
        defaultCommunityLeaderShouldNotBeFound("realName.notEquals=" + DEFAULT_REAL_NAME);

        // Get all the communityLeaderList where realName not equals to UPDATED_REAL_NAME
        defaultCommunityLeaderShouldBeFound("realName.notEquals=" + UPDATED_REAL_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByRealNameIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where realName in DEFAULT_REAL_NAME or UPDATED_REAL_NAME
        defaultCommunityLeaderShouldBeFound("realName.in=" + DEFAULT_REAL_NAME + "," + UPDATED_REAL_NAME);

        // Get all the communityLeaderList where realName equals to UPDATED_REAL_NAME
        defaultCommunityLeaderShouldNotBeFound("realName.in=" + UPDATED_REAL_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByRealNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where realName is not null
        defaultCommunityLeaderShouldBeFound("realName.specified=true");

        // Get all the communityLeaderList where realName is null
        defaultCommunityLeaderShouldNotBeFound("realName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByRealNameContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where realName contains DEFAULT_REAL_NAME
        defaultCommunityLeaderShouldBeFound("realName.contains=" + DEFAULT_REAL_NAME);

        // Get all the communityLeaderList where realName contains UPDATED_REAL_NAME
        defaultCommunityLeaderShouldNotBeFound("realName.contains=" + UPDATED_REAL_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByRealNameNotContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where realName does not contain DEFAULT_REAL_NAME
        defaultCommunityLeaderShouldNotBeFound("realName.doesNotContain=" + DEFAULT_REAL_NAME);

        // Get all the communityLeaderList where realName does not contain UPDATED_REAL_NAME
        defaultCommunityLeaderShouldBeFound("realName.doesNotContain=" + UPDATED_REAL_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where tel equals to DEFAULT_TEL
        defaultCommunityLeaderShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the communityLeaderList where tel equals to UPDATED_TEL
        defaultCommunityLeaderShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByTelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where tel not equals to DEFAULT_TEL
        defaultCommunityLeaderShouldNotBeFound("tel.notEquals=" + DEFAULT_TEL);

        // Get all the communityLeaderList where tel not equals to UPDATED_TEL
        defaultCommunityLeaderShouldBeFound("tel.notEquals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByTelIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultCommunityLeaderShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the communityLeaderList where tel equals to UPDATED_TEL
        defaultCommunityLeaderShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where tel is not null
        defaultCommunityLeaderShouldBeFound("tel.specified=true");

        // Get all the communityLeaderList where tel is null
        defaultCommunityLeaderShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByTelContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where tel contains DEFAULT_TEL
        defaultCommunityLeaderShouldBeFound("tel.contains=" + DEFAULT_TEL);

        // Get all the communityLeaderList where tel contains UPDATED_TEL
        defaultCommunityLeaderShouldNotBeFound("tel.contains=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByTelNotContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where tel does not contain DEFAULT_TEL
        defaultCommunityLeaderShouldNotBeFound("tel.doesNotContain=" + DEFAULT_TEL);

        // Get all the communityLeaderList where tel does not contain UPDATED_TEL
        defaultCommunityLeaderShouldBeFound("tel.doesNotContain=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobTitle equals to DEFAULT_JOB_TITLE
        defaultCommunityLeaderShouldBeFound("jobTitle.equals=" + DEFAULT_JOB_TITLE);

        // Get all the communityLeaderList where jobTitle equals to UPDATED_JOB_TITLE
        defaultCommunityLeaderShouldNotBeFound("jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobTitle not equals to DEFAULT_JOB_TITLE
        defaultCommunityLeaderShouldNotBeFound("jobTitle.notEquals=" + DEFAULT_JOB_TITLE);

        // Get all the communityLeaderList where jobTitle not equals to UPDATED_JOB_TITLE
        defaultCommunityLeaderShouldBeFound("jobTitle.notEquals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobTitle in DEFAULT_JOB_TITLE or UPDATED_JOB_TITLE
        defaultCommunityLeaderShouldBeFound("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE);

        // Get all the communityLeaderList where jobTitle equals to UPDATED_JOB_TITLE
        defaultCommunityLeaderShouldNotBeFound("jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobTitle is not null
        defaultCommunityLeaderShouldBeFound("jobTitle.specified=true");

        // Get all the communityLeaderList where jobTitle is null
        defaultCommunityLeaderShouldNotBeFound("jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobTitle contains DEFAULT_JOB_TITLE
        defaultCommunityLeaderShouldBeFound("jobTitle.contains=" + DEFAULT_JOB_TITLE);

        // Get all the communityLeaderList where jobTitle contains UPDATED_JOB_TITLE
        defaultCommunityLeaderShouldNotBeFound("jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobTitle does not contain DEFAULT_JOB_TITLE
        defaultCommunityLeaderShouldNotBeFound("jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);

        // Get all the communityLeaderList where jobTitle does not contain UPDATED_JOB_TITLE
        defaultCommunityLeaderShouldBeFound("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobDescIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobDesc equals to DEFAULT_JOB_DESC
        defaultCommunityLeaderShouldBeFound("jobDesc.equals=" + DEFAULT_JOB_DESC);

        // Get all the communityLeaderList where jobDesc equals to UPDATED_JOB_DESC
        defaultCommunityLeaderShouldNotBeFound("jobDesc.equals=" + UPDATED_JOB_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobDescIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobDesc not equals to DEFAULT_JOB_DESC
        defaultCommunityLeaderShouldNotBeFound("jobDesc.notEquals=" + DEFAULT_JOB_DESC);

        // Get all the communityLeaderList where jobDesc not equals to UPDATED_JOB_DESC
        defaultCommunityLeaderShouldBeFound("jobDesc.notEquals=" + UPDATED_JOB_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobDescIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobDesc in DEFAULT_JOB_DESC or UPDATED_JOB_DESC
        defaultCommunityLeaderShouldBeFound("jobDesc.in=" + DEFAULT_JOB_DESC + "," + UPDATED_JOB_DESC);

        // Get all the communityLeaderList where jobDesc equals to UPDATED_JOB_DESC
        defaultCommunityLeaderShouldNotBeFound("jobDesc.in=" + UPDATED_JOB_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobDesc is not null
        defaultCommunityLeaderShouldBeFound("jobDesc.specified=true");

        // Get all the communityLeaderList where jobDesc is null
        defaultCommunityLeaderShouldNotBeFound("jobDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobDescContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobDesc contains DEFAULT_JOB_DESC
        defaultCommunityLeaderShouldBeFound("jobDesc.contains=" + DEFAULT_JOB_DESC);

        // Get all the communityLeaderList where jobDesc contains UPDATED_JOB_DESC
        defaultCommunityLeaderShouldNotBeFound("jobDesc.contains=" + UPDATED_JOB_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByJobDescNotContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where jobDesc does not contain DEFAULT_JOB_DESC
        defaultCommunityLeaderShouldNotBeFound("jobDesc.doesNotContain=" + DEFAULT_JOB_DESC);

        // Get all the communityLeaderList where jobDesc does not contain UPDATED_JOB_DESC
        defaultCommunityLeaderShouldBeFound("jobDesc.doesNotContain=" + UPDATED_JOB_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where enable equals to DEFAULT_ENABLE
        defaultCommunityLeaderShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the communityLeaderList where enable equals to UPDATED_ENABLE
        defaultCommunityLeaderShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where enable not equals to DEFAULT_ENABLE
        defaultCommunityLeaderShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the communityLeaderList where enable not equals to UPDATED_ENABLE
        defaultCommunityLeaderShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultCommunityLeaderShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the communityLeaderList where enable equals to UPDATED_ENABLE
        defaultCommunityLeaderShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where enable is not null
        defaultCommunityLeaderShouldBeFound("enable.specified=true");

        // Get all the communityLeaderList where enable is null
        defaultCommunityLeaderShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByDelFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where delFlag equals to DEFAULT_DEL_FLAG
        defaultCommunityLeaderShouldBeFound("delFlag.equals=" + DEFAULT_DEL_FLAG);

        // Get all the communityLeaderList where delFlag equals to UPDATED_DEL_FLAG
        defaultCommunityLeaderShouldNotBeFound("delFlag.equals=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByDelFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where delFlag not equals to DEFAULT_DEL_FLAG
        defaultCommunityLeaderShouldNotBeFound("delFlag.notEquals=" + DEFAULT_DEL_FLAG);

        // Get all the communityLeaderList where delFlag not equals to UPDATED_DEL_FLAG
        defaultCommunityLeaderShouldBeFound("delFlag.notEquals=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByDelFlagIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where delFlag in DEFAULT_DEL_FLAG or UPDATED_DEL_FLAG
        defaultCommunityLeaderShouldBeFound("delFlag.in=" + DEFAULT_DEL_FLAG + "," + UPDATED_DEL_FLAG);

        // Get all the communityLeaderList where delFlag equals to UPDATED_DEL_FLAG
        defaultCommunityLeaderShouldNotBeFound("delFlag.in=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByDelFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where delFlag is not null
        defaultCommunityLeaderShouldBeFound("delFlag.specified=true");

        // Get all the communityLeaderList where delFlag is null
        defaultCommunityLeaderShouldNotBeFound("delFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByOrderNumIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where orderNum equals to DEFAULT_ORDER_NUM
        defaultCommunityLeaderShouldBeFound("orderNum.equals=" + DEFAULT_ORDER_NUM);

        // Get all the communityLeaderList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityLeaderShouldNotBeFound("orderNum.equals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByOrderNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where orderNum not equals to DEFAULT_ORDER_NUM
        defaultCommunityLeaderShouldNotBeFound("orderNum.notEquals=" + DEFAULT_ORDER_NUM);

        // Get all the communityLeaderList where orderNum not equals to UPDATED_ORDER_NUM
        defaultCommunityLeaderShouldBeFound("orderNum.notEquals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByOrderNumIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where orderNum in DEFAULT_ORDER_NUM or UPDATED_ORDER_NUM
        defaultCommunityLeaderShouldBeFound("orderNum.in=" + DEFAULT_ORDER_NUM + "," + UPDATED_ORDER_NUM);

        // Get all the communityLeaderList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityLeaderShouldNotBeFound("orderNum.in=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByOrderNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where orderNum is not null
        defaultCommunityLeaderShouldBeFound("orderNum.specified=true");

        // Get all the communityLeaderList where orderNum is null
        defaultCommunityLeaderShouldNotBeFound("orderNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByOrderNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where orderNum is greater than or equal to DEFAULT_ORDER_NUM
        defaultCommunityLeaderShouldBeFound("orderNum.greaterThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityLeaderList where orderNum is greater than or equal to UPDATED_ORDER_NUM
        defaultCommunityLeaderShouldNotBeFound("orderNum.greaterThanOrEqual=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByOrderNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where orderNum is less than or equal to DEFAULT_ORDER_NUM
        defaultCommunityLeaderShouldBeFound("orderNum.lessThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityLeaderList where orderNum is less than or equal to SMALLER_ORDER_NUM
        defaultCommunityLeaderShouldNotBeFound("orderNum.lessThanOrEqual=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByOrderNumIsLessThanSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where orderNum is less than DEFAULT_ORDER_NUM
        defaultCommunityLeaderShouldNotBeFound("orderNum.lessThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityLeaderList where orderNum is less than UPDATED_ORDER_NUM
        defaultCommunityLeaderShouldBeFound("orderNum.lessThan=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByOrderNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where orderNum is greater than DEFAULT_ORDER_NUM
        defaultCommunityLeaderShouldNotBeFound("orderNum.greaterThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityLeaderList where orderNum is greater than SMALLER_ORDER_NUM
        defaultCommunityLeaderShouldBeFound("orderNum.greaterThan=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyDate equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityLeaderShouldBeFound("lastModifyDate.equals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityLeaderList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityLeaderShouldNotBeFound("lastModifyDate.equals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyDate not equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityLeaderShouldNotBeFound("lastModifyDate.notEquals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityLeaderList where lastModifyDate not equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityLeaderShouldBeFound("lastModifyDate.notEquals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyDate in DEFAULT_LAST_MODIFY_DATE or UPDATED_LAST_MODIFY_DATE
        defaultCommunityLeaderShouldBeFound("lastModifyDate.in=" + DEFAULT_LAST_MODIFY_DATE + "," + UPDATED_LAST_MODIFY_DATE);

        // Get all the communityLeaderList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityLeaderShouldNotBeFound("lastModifyDate.in=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyDate is not null
        defaultCommunityLeaderShouldBeFound("lastModifyDate.specified=true");

        // Get all the communityLeaderList where lastModifyDate is null
        defaultCommunityLeaderShouldNotBeFound("lastModifyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyByIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyBy equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityLeaderShouldBeFound("lastModifyBy.equals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityLeaderList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityLeaderShouldNotBeFound("lastModifyBy.equals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyBy not equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityLeaderShouldNotBeFound("lastModifyBy.notEquals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityLeaderList where lastModifyBy not equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityLeaderShouldBeFound("lastModifyBy.notEquals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyByIsInShouldWork() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyBy in DEFAULT_LAST_MODIFY_BY or UPDATED_LAST_MODIFY_BY
        defaultCommunityLeaderShouldBeFound("lastModifyBy.in=" + DEFAULT_LAST_MODIFY_BY + "," + UPDATED_LAST_MODIFY_BY);

        // Get all the communityLeaderList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityLeaderShouldNotBeFound("lastModifyBy.in=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyByIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyBy is not null
        defaultCommunityLeaderShouldBeFound("lastModifyBy.specified=true");

        // Get all the communityLeaderList where lastModifyBy is null
        defaultCommunityLeaderShouldNotBeFound("lastModifyBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyByContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyBy contains DEFAULT_LAST_MODIFY_BY
        defaultCommunityLeaderShouldBeFound("lastModifyBy.contains=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityLeaderList where lastModifyBy contains UPDATED_LAST_MODIFY_BY
        defaultCommunityLeaderShouldNotBeFound("lastModifyBy.contains=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByLastModifyByNotContainsSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        // Get all the communityLeaderList where lastModifyBy does not contain DEFAULT_LAST_MODIFY_BY
        defaultCommunityLeaderShouldNotBeFound("lastModifyBy.doesNotContain=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityLeaderList where lastModifyBy does not contain UPDATED_LAST_MODIFY_BY
        defaultCommunityLeaderShouldBeFound("lastModifyBy.doesNotContain=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityLeadersByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);
        Community community = CommunityResourceIT.createEntity(em);
        em.persist(community);
        em.flush();
        communityLeader.setCommunity(community);
        communityLeaderRepository.saveAndFlush(communityLeader);
        Long communityId = community.getId();

        // Get all the communityLeaderList where community equals to communityId
        defaultCommunityLeaderShouldBeFound("communityId.equals=" + communityId);

        // Get all the communityLeaderList where community equals to (communityId + 1)
        defaultCommunityLeaderShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommunityLeaderShouldBeFound(String filter) throws Exception {
        restCommunityLeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityLeader.getId().intValue())))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))))
            .andExpect(jsonPath("$.[*].realName").value(hasItem(DEFAULT_REAL_NAME)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].jobDesc").value(hasItem(DEFAULT_JOB_DESC)))
            .andExpect(jsonPath("$.[*].jobCareerDesc").value(hasItem(DEFAULT_JOB_CAREER_DESC.toString())))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));

        // Check, that the count call also returns 1
        restCommunityLeaderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommunityLeaderShouldNotBeFound(String filter) throws Exception {
        restCommunityLeaderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommunityLeaderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommunityLeader() throws Exception {
        // Get the communityLeader
        restCommunityLeaderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommunityLeader() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();

        // Update the communityLeader
        CommunityLeader updatedCommunityLeader = communityLeaderRepository.findById(communityLeader.getId()).get();
        // Disconnect from session so that the updates on updatedCommunityLeader are not directly saved in db
        em.detach(updatedCommunityLeader);
        updatedCommunityLeader
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .realName(UPDATED_REAL_NAME)
            .tel(UPDATED_TEL)
            .jobTitle(UPDATED_JOB_TITLE)
            .jobDesc(UPDATED_JOB_DESC)
            .jobCareerDesc(UPDATED_JOB_CAREER_DESC)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityLeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunityLeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommunityLeader))
            )
            .andExpect(status().isOk());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
        CommunityLeader testCommunityLeader = communityLeaderList.get(communityLeaderList.size() - 1);
        assertThat(testCommunityLeader.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testCommunityLeader.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testCommunityLeader.getRealName()).isEqualTo(UPDATED_REAL_NAME);
        assertThat(testCommunityLeader.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCommunityLeader.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCommunityLeader.getJobDesc()).isEqualTo(UPDATED_JOB_DESC);
        assertThat(testCommunityLeader.getJobCareerDesc()).isEqualTo(UPDATED_JOB_CAREER_DESC);
        assertThat(testCommunityLeader.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCommunityLeader.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCommunityLeader.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityLeader.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityLeader.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void putNonExistingCommunityLeader() throws Exception {
        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();
        communityLeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityLeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityLeader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityLeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunityLeader() throws Exception {
        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();
        communityLeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityLeaderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityLeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunityLeader() throws Exception {
        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();
        communityLeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityLeaderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityLeader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommunityLeaderWithPatch() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();

        // Update the communityLeader using partial update
        CommunityLeader partialUpdatedCommunityLeader = new CommunityLeader();
        partialUpdatedCommunityLeader.setId(communityLeader.getId());

        partialUpdatedCommunityLeader
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .realName(UPDATED_REAL_NAME)
            .jobDesc(UPDATED_JOB_DESC)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityLeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityLeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunityLeader))
            )
            .andExpect(status().isOk());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
        CommunityLeader testCommunityLeader = communityLeaderList.get(communityLeaderList.size() - 1);
        assertThat(testCommunityLeader.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testCommunityLeader.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testCommunityLeader.getRealName()).isEqualTo(UPDATED_REAL_NAME);
        assertThat(testCommunityLeader.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCommunityLeader.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testCommunityLeader.getJobDesc()).isEqualTo(UPDATED_JOB_DESC);
        assertThat(testCommunityLeader.getJobCareerDesc()).isEqualTo(DEFAULT_JOB_CAREER_DESC);
        assertThat(testCommunityLeader.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCommunityLeader.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCommunityLeader.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityLeader.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityLeader.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void fullUpdateCommunityLeaderWithPatch() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();

        // Update the communityLeader using partial update
        CommunityLeader partialUpdatedCommunityLeader = new CommunityLeader();
        partialUpdatedCommunityLeader.setId(communityLeader.getId());

        partialUpdatedCommunityLeader
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .realName(UPDATED_REAL_NAME)
            .tel(UPDATED_TEL)
            .jobTitle(UPDATED_JOB_TITLE)
            .jobDesc(UPDATED_JOB_DESC)
            .jobCareerDesc(UPDATED_JOB_CAREER_DESC)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityLeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityLeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunityLeader))
            )
            .andExpect(status().isOk());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
        CommunityLeader testCommunityLeader = communityLeaderList.get(communityLeaderList.size() - 1);
        assertThat(testCommunityLeader.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testCommunityLeader.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testCommunityLeader.getRealName()).isEqualTo(UPDATED_REAL_NAME);
        assertThat(testCommunityLeader.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCommunityLeader.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCommunityLeader.getJobDesc()).isEqualTo(UPDATED_JOB_DESC);
        assertThat(testCommunityLeader.getJobCareerDesc()).isEqualTo(UPDATED_JOB_CAREER_DESC);
        assertThat(testCommunityLeader.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCommunityLeader.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCommunityLeader.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityLeader.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityLeader.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCommunityLeader() throws Exception {
        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();
        communityLeader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityLeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityLeader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityLeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunityLeader() throws Exception {
        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();
        communityLeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityLeaderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityLeader))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunityLeader() throws Exception {
        int databaseSizeBeforeUpdate = communityLeaderRepository.findAll().size();
        communityLeader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityLeaderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityLeader))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityLeader in the database
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommunityLeader() throws Exception {
        // Initialize the database
        communityLeaderRepository.saveAndFlush(communityLeader);

        int databaseSizeBeforeDelete = communityLeaderRepository.findAll().size();

        // Delete the communityLeader
        restCommunityLeaderMockMvc
            .perform(delete(ENTITY_API_URL_ID, communityLeader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommunityLeader> communityLeaderList = communityLeaderRepository.findAll();
        assertThat(communityLeaderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
