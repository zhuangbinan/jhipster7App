package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.domain.CompanyPost;
import com.mycompany.myapp.domain.WamoliUser;
import com.mycompany.myapp.repository.CompanyDeptRepository;
import com.mycompany.myapp.service.criteria.CompanyDeptCriteria;
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
 * Integration tests for the {@link CompanyDeptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyDeptResourceIT {

    private static final Long DEFAULT_PARENT_ID = 1L;
    private static final Long UPDATED_PARENT_ID = 2L;
    private static final Long SMALLER_PARENT_ID = 1L - 1L;

    private static final String DEFAULT_ANCESTORS = "AAAAAAAAAA";
    private static final String UPDATED_ANCESTORS = "BBBBBBBBBB";

    private static final String DEFAULT_DEPT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_NUM = 1;
    private static final Integer UPDATED_ORDER_NUM = 2;
    private static final Integer SMALLER_ORDER_NUM = 1 - 1;

    private static final String DEFAULT_LEADER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEADER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final Boolean DEFAULT_DEL_FLAG = false;
    private static final Boolean UPDATED_DEL_FLAG = true;

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFY_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFY_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/company-depts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyDeptRepository companyDeptRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyDeptMockMvc;

    private CompanyDept companyDept;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyDept createEntity(EntityManager em) {
        CompanyDept companyDept = new CompanyDept()
            .parentId(DEFAULT_PARENT_ID)
            .ancestors(DEFAULT_ANCESTORS)
            .deptName(DEFAULT_DEPT_NAME)
            .orderNum(DEFAULT_ORDER_NUM)
            .leaderName(DEFAULT_LEADER_NAME)
            .tel(DEFAULT_TEL)
            .email(DEFAULT_EMAIL)
            .enable(DEFAULT_ENABLE)
            .delFlag(DEFAULT_DEL_FLAG)
            .createBy(DEFAULT_CREATE_BY)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModifyBy(DEFAULT_LAST_MODIFY_BY)
            .lastModifyDate(DEFAULT_LAST_MODIFY_DATE);
        return companyDept;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyDept createUpdatedEntity(EntityManager em) {
        CompanyDept companyDept = new CompanyDept()
            .parentId(UPDATED_PARENT_ID)
            .ancestors(UPDATED_ANCESTORS)
            .deptName(UPDATED_DEPT_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .leaderName(UPDATED_LEADER_NAME)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);
        return companyDept;
    }

    @BeforeEach
    public void initTest() {
        companyDept = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyDept() throws Exception {
        int databaseSizeBeforeCreate = companyDeptRepository.findAll().size();
        // Create the CompanyDept
        restCompanyDeptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDept)))
            .andExpect(status().isCreated());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyDept testCompanyDept = companyDeptList.get(companyDeptList.size() - 1);
        assertThat(testCompanyDept.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testCompanyDept.getAncestors()).isEqualTo(DEFAULT_ANCESTORS);
        assertThat(testCompanyDept.getDeptName()).isEqualTo(DEFAULT_DEPT_NAME);
        assertThat(testCompanyDept.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCompanyDept.getLeaderName()).isEqualTo(DEFAULT_LEADER_NAME);
        assertThat(testCompanyDept.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCompanyDept.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompanyDept.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCompanyDept.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCompanyDept.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testCompanyDept.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCompanyDept.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
        assertThat(testCompanyDept.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void createCompanyDeptWithExistingId() throws Exception {
        // Create the CompanyDept with an existing ID
        companyDept.setId(1L);

        int databaseSizeBeforeCreate = companyDeptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyDeptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDept)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanyDepts() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList
        restCompanyDeptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyDept.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].ancestors").value(hasItem(DEFAULT_ANCESTORS)))
            .andExpect(jsonPath("$.[*].deptName").value(hasItem(DEFAULT_DEPT_NAME)))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].leaderName").value(hasItem(DEFAULT_LEADER_NAME)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())));
    }

    @Test
    @Transactional
    void getCompanyDept() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get the companyDept
        restCompanyDeptMockMvc
            .perform(get(ENTITY_API_URL_ID, companyDept.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyDept.getId().intValue()))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID.intValue()))
            .andExpect(jsonPath("$.ancestors").value(DEFAULT_ANCESTORS))
            .andExpect(jsonPath("$.deptName").value(DEFAULT_DEPT_NAME))
            .andExpect(jsonPath("$.orderNum").value(DEFAULT_ORDER_NUM))
            .andExpect(jsonPath("$.leaderName").value(DEFAULT_LEADER_NAME))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModifyBy").value(DEFAULT_LAST_MODIFY_BY))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    void getCompanyDeptsByIdFiltering() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        Long id = companyDept.getId();

        defaultCompanyDeptShouldBeFound("id.equals=" + id);
        defaultCompanyDeptShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyDeptShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyDeptShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyDeptShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyDeptShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByParentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where parentId equals to DEFAULT_PARENT_ID
        defaultCompanyDeptShouldBeFound("parentId.equals=" + DEFAULT_PARENT_ID);

        // Get all the companyDeptList where parentId equals to UPDATED_PARENT_ID
        defaultCompanyDeptShouldNotBeFound("parentId.equals=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByParentIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where parentId not equals to DEFAULT_PARENT_ID
        defaultCompanyDeptShouldNotBeFound("parentId.notEquals=" + DEFAULT_PARENT_ID);

        // Get all the companyDeptList where parentId not equals to UPDATED_PARENT_ID
        defaultCompanyDeptShouldBeFound("parentId.notEquals=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByParentIdIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where parentId in DEFAULT_PARENT_ID or UPDATED_PARENT_ID
        defaultCompanyDeptShouldBeFound("parentId.in=" + DEFAULT_PARENT_ID + "," + UPDATED_PARENT_ID);

        // Get all the companyDeptList where parentId equals to UPDATED_PARENT_ID
        defaultCompanyDeptShouldNotBeFound("parentId.in=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByParentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where parentId is not null
        defaultCompanyDeptShouldBeFound("parentId.specified=true");

        // Get all the companyDeptList where parentId is null
        defaultCompanyDeptShouldNotBeFound("parentId.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByParentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where parentId is greater than or equal to DEFAULT_PARENT_ID
        defaultCompanyDeptShouldBeFound("parentId.greaterThanOrEqual=" + DEFAULT_PARENT_ID);

        // Get all the companyDeptList where parentId is greater than or equal to UPDATED_PARENT_ID
        defaultCompanyDeptShouldNotBeFound("parentId.greaterThanOrEqual=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByParentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where parentId is less than or equal to DEFAULT_PARENT_ID
        defaultCompanyDeptShouldBeFound("parentId.lessThanOrEqual=" + DEFAULT_PARENT_ID);

        // Get all the companyDeptList where parentId is less than or equal to SMALLER_PARENT_ID
        defaultCompanyDeptShouldNotBeFound("parentId.lessThanOrEqual=" + SMALLER_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByParentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where parentId is less than DEFAULT_PARENT_ID
        defaultCompanyDeptShouldNotBeFound("parentId.lessThan=" + DEFAULT_PARENT_ID);

        // Get all the companyDeptList where parentId is less than UPDATED_PARENT_ID
        defaultCompanyDeptShouldBeFound("parentId.lessThan=" + UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByParentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where parentId is greater than DEFAULT_PARENT_ID
        defaultCompanyDeptShouldNotBeFound("parentId.greaterThan=" + DEFAULT_PARENT_ID);

        // Get all the companyDeptList where parentId is greater than SMALLER_PARENT_ID
        defaultCompanyDeptShouldBeFound("parentId.greaterThan=" + SMALLER_PARENT_ID);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByAncestorsIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where ancestors equals to DEFAULT_ANCESTORS
        defaultCompanyDeptShouldBeFound("ancestors.equals=" + DEFAULT_ANCESTORS);

        // Get all the companyDeptList where ancestors equals to UPDATED_ANCESTORS
        defaultCompanyDeptShouldNotBeFound("ancestors.equals=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByAncestorsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where ancestors not equals to DEFAULT_ANCESTORS
        defaultCompanyDeptShouldNotBeFound("ancestors.notEquals=" + DEFAULT_ANCESTORS);

        // Get all the companyDeptList where ancestors not equals to UPDATED_ANCESTORS
        defaultCompanyDeptShouldBeFound("ancestors.notEquals=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByAncestorsIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where ancestors in DEFAULT_ANCESTORS or UPDATED_ANCESTORS
        defaultCompanyDeptShouldBeFound("ancestors.in=" + DEFAULT_ANCESTORS + "," + UPDATED_ANCESTORS);

        // Get all the companyDeptList where ancestors equals to UPDATED_ANCESTORS
        defaultCompanyDeptShouldNotBeFound("ancestors.in=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByAncestorsIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where ancestors is not null
        defaultCompanyDeptShouldBeFound("ancestors.specified=true");

        // Get all the companyDeptList where ancestors is null
        defaultCompanyDeptShouldNotBeFound("ancestors.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByAncestorsContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where ancestors contains DEFAULT_ANCESTORS
        defaultCompanyDeptShouldBeFound("ancestors.contains=" + DEFAULT_ANCESTORS);

        // Get all the companyDeptList where ancestors contains UPDATED_ANCESTORS
        defaultCompanyDeptShouldNotBeFound("ancestors.contains=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByAncestorsNotContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where ancestors does not contain DEFAULT_ANCESTORS
        defaultCompanyDeptShouldNotBeFound("ancestors.doesNotContain=" + DEFAULT_ANCESTORS);

        // Get all the companyDeptList where ancestors does not contain UPDATED_ANCESTORS
        defaultCompanyDeptShouldBeFound("ancestors.doesNotContain=" + UPDATED_ANCESTORS);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDeptNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where deptName equals to DEFAULT_DEPT_NAME
        defaultCompanyDeptShouldBeFound("deptName.equals=" + DEFAULT_DEPT_NAME);

        // Get all the companyDeptList where deptName equals to UPDATED_DEPT_NAME
        defaultCompanyDeptShouldNotBeFound("deptName.equals=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDeptNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where deptName not equals to DEFAULT_DEPT_NAME
        defaultCompanyDeptShouldNotBeFound("deptName.notEquals=" + DEFAULT_DEPT_NAME);

        // Get all the companyDeptList where deptName not equals to UPDATED_DEPT_NAME
        defaultCompanyDeptShouldBeFound("deptName.notEquals=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDeptNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where deptName in DEFAULT_DEPT_NAME or UPDATED_DEPT_NAME
        defaultCompanyDeptShouldBeFound("deptName.in=" + DEFAULT_DEPT_NAME + "," + UPDATED_DEPT_NAME);

        // Get all the companyDeptList where deptName equals to UPDATED_DEPT_NAME
        defaultCompanyDeptShouldNotBeFound("deptName.in=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDeptNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where deptName is not null
        defaultCompanyDeptShouldBeFound("deptName.specified=true");

        // Get all the companyDeptList where deptName is null
        defaultCompanyDeptShouldNotBeFound("deptName.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDeptNameContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where deptName contains DEFAULT_DEPT_NAME
        defaultCompanyDeptShouldBeFound("deptName.contains=" + DEFAULT_DEPT_NAME);

        // Get all the companyDeptList where deptName contains UPDATED_DEPT_NAME
        defaultCompanyDeptShouldNotBeFound("deptName.contains=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDeptNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where deptName does not contain DEFAULT_DEPT_NAME
        defaultCompanyDeptShouldNotBeFound("deptName.doesNotContain=" + DEFAULT_DEPT_NAME);

        // Get all the companyDeptList where deptName does not contain UPDATED_DEPT_NAME
        defaultCompanyDeptShouldBeFound("deptName.doesNotContain=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByOrderNumIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where orderNum equals to DEFAULT_ORDER_NUM
        defaultCompanyDeptShouldBeFound("orderNum.equals=" + DEFAULT_ORDER_NUM);

        // Get all the companyDeptList where orderNum equals to UPDATED_ORDER_NUM
        defaultCompanyDeptShouldNotBeFound("orderNum.equals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByOrderNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where orderNum not equals to DEFAULT_ORDER_NUM
        defaultCompanyDeptShouldNotBeFound("orderNum.notEquals=" + DEFAULT_ORDER_NUM);

        // Get all the companyDeptList where orderNum not equals to UPDATED_ORDER_NUM
        defaultCompanyDeptShouldBeFound("orderNum.notEquals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByOrderNumIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where orderNum in DEFAULT_ORDER_NUM or UPDATED_ORDER_NUM
        defaultCompanyDeptShouldBeFound("orderNum.in=" + DEFAULT_ORDER_NUM + "," + UPDATED_ORDER_NUM);

        // Get all the companyDeptList where orderNum equals to UPDATED_ORDER_NUM
        defaultCompanyDeptShouldNotBeFound("orderNum.in=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByOrderNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where orderNum is not null
        defaultCompanyDeptShouldBeFound("orderNum.specified=true");

        // Get all the companyDeptList where orderNum is null
        defaultCompanyDeptShouldNotBeFound("orderNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByOrderNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where orderNum is greater than or equal to DEFAULT_ORDER_NUM
        defaultCompanyDeptShouldBeFound("orderNum.greaterThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the companyDeptList where orderNum is greater than or equal to UPDATED_ORDER_NUM
        defaultCompanyDeptShouldNotBeFound("orderNum.greaterThanOrEqual=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByOrderNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where orderNum is less than or equal to DEFAULT_ORDER_NUM
        defaultCompanyDeptShouldBeFound("orderNum.lessThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the companyDeptList where orderNum is less than or equal to SMALLER_ORDER_NUM
        defaultCompanyDeptShouldNotBeFound("orderNum.lessThanOrEqual=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByOrderNumIsLessThanSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where orderNum is less than DEFAULT_ORDER_NUM
        defaultCompanyDeptShouldNotBeFound("orderNum.lessThan=" + DEFAULT_ORDER_NUM);

        // Get all the companyDeptList where orderNum is less than UPDATED_ORDER_NUM
        defaultCompanyDeptShouldBeFound("orderNum.lessThan=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByOrderNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where orderNum is greater than DEFAULT_ORDER_NUM
        defaultCompanyDeptShouldNotBeFound("orderNum.greaterThan=" + DEFAULT_ORDER_NUM);

        // Get all the companyDeptList where orderNum is greater than SMALLER_ORDER_NUM
        defaultCompanyDeptShouldBeFound("orderNum.greaterThan=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLeaderNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where leaderName equals to DEFAULT_LEADER_NAME
        defaultCompanyDeptShouldBeFound("leaderName.equals=" + DEFAULT_LEADER_NAME);

        // Get all the companyDeptList where leaderName equals to UPDATED_LEADER_NAME
        defaultCompanyDeptShouldNotBeFound("leaderName.equals=" + UPDATED_LEADER_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLeaderNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where leaderName not equals to DEFAULT_LEADER_NAME
        defaultCompanyDeptShouldNotBeFound("leaderName.notEquals=" + DEFAULT_LEADER_NAME);

        // Get all the companyDeptList where leaderName not equals to UPDATED_LEADER_NAME
        defaultCompanyDeptShouldBeFound("leaderName.notEquals=" + UPDATED_LEADER_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLeaderNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where leaderName in DEFAULT_LEADER_NAME or UPDATED_LEADER_NAME
        defaultCompanyDeptShouldBeFound("leaderName.in=" + DEFAULT_LEADER_NAME + "," + UPDATED_LEADER_NAME);

        // Get all the companyDeptList where leaderName equals to UPDATED_LEADER_NAME
        defaultCompanyDeptShouldNotBeFound("leaderName.in=" + UPDATED_LEADER_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLeaderNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where leaderName is not null
        defaultCompanyDeptShouldBeFound("leaderName.specified=true");

        // Get all the companyDeptList where leaderName is null
        defaultCompanyDeptShouldNotBeFound("leaderName.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLeaderNameContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where leaderName contains DEFAULT_LEADER_NAME
        defaultCompanyDeptShouldBeFound("leaderName.contains=" + DEFAULT_LEADER_NAME);

        // Get all the companyDeptList where leaderName contains UPDATED_LEADER_NAME
        defaultCompanyDeptShouldNotBeFound("leaderName.contains=" + UPDATED_LEADER_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLeaderNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where leaderName does not contain DEFAULT_LEADER_NAME
        defaultCompanyDeptShouldNotBeFound("leaderName.doesNotContain=" + DEFAULT_LEADER_NAME);

        // Get all the companyDeptList where leaderName does not contain UPDATED_LEADER_NAME
        defaultCompanyDeptShouldBeFound("leaderName.doesNotContain=" + UPDATED_LEADER_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where tel equals to DEFAULT_TEL
        defaultCompanyDeptShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the companyDeptList where tel equals to UPDATED_TEL
        defaultCompanyDeptShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByTelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where tel not equals to DEFAULT_TEL
        defaultCompanyDeptShouldNotBeFound("tel.notEquals=" + DEFAULT_TEL);

        // Get all the companyDeptList where tel not equals to UPDATED_TEL
        defaultCompanyDeptShouldBeFound("tel.notEquals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByTelIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultCompanyDeptShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the companyDeptList where tel equals to UPDATED_TEL
        defaultCompanyDeptShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where tel is not null
        defaultCompanyDeptShouldBeFound("tel.specified=true");

        // Get all the companyDeptList where tel is null
        defaultCompanyDeptShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByTelContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where tel contains DEFAULT_TEL
        defaultCompanyDeptShouldBeFound("tel.contains=" + DEFAULT_TEL);

        // Get all the companyDeptList where tel contains UPDATED_TEL
        defaultCompanyDeptShouldNotBeFound("tel.contains=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByTelNotContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where tel does not contain DEFAULT_TEL
        defaultCompanyDeptShouldNotBeFound("tel.doesNotContain=" + DEFAULT_TEL);

        // Get all the companyDeptList where tel does not contain UPDATED_TEL
        defaultCompanyDeptShouldBeFound("tel.doesNotContain=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where email equals to DEFAULT_EMAIL
        defaultCompanyDeptShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the companyDeptList where email equals to UPDATED_EMAIL
        defaultCompanyDeptShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where email not equals to DEFAULT_EMAIL
        defaultCompanyDeptShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the companyDeptList where email not equals to UPDATED_EMAIL
        defaultCompanyDeptShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCompanyDeptShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the companyDeptList where email equals to UPDATED_EMAIL
        defaultCompanyDeptShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where email is not null
        defaultCompanyDeptShouldBeFound("email.specified=true");

        // Get all the companyDeptList where email is null
        defaultCompanyDeptShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEmailContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where email contains DEFAULT_EMAIL
        defaultCompanyDeptShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the companyDeptList where email contains UPDATED_EMAIL
        defaultCompanyDeptShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where email does not contain DEFAULT_EMAIL
        defaultCompanyDeptShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the companyDeptList where email does not contain UPDATED_EMAIL
        defaultCompanyDeptShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where enable equals to DEFAULT_ENABLE
        defaultCompanyDeptShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the companyDeptList where enable equals to UPDATED_ENABLE
        defaultCompanyDeptShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where enable not equals to DEFAULT_ENABLE
        defaultCompanyDeptShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the companyDeptList where enable not equals to UPDATED_ENABLE
        defaultCompanyDeptShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultCompanyDeptShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the companyDeptList where enable equals to UPDATED_ENABLE
        defaultCompanyDeptShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where enable is not null
        defaultCompanyDeptShouldBeFound("enable.specified=true");

        // Get all the companyDeptList where enable is null
        defaultCompanyDeptShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDelFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where delFlag equals to DEFAULT_DEL_FLAG
        defaultCompanyDeptShouldBeFound("delFlag.equals=" + DEFAULT_DEL_FLAG);

        // Get all the companyDeptList where delFlag equals to UPDATED_DEL_FLAG
        defaultCompanyDeptShouldNotBeFound("delFlag.equals=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDelFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where delFlag not equals to DEFAULT_DEL_FLAG
        defaultCompanyDeptShouldNotBeFound("delFlag.notEquals=" + DEFAULT_DEL_FLAG);

        // Get all the companyDeptList where delFlag not equals to UPDATED_DEL_FLAG
        defaultCompanyDeptShouldBeFound("delFlag.notEquals=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDelFlagIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where delFlag in DEFAULT_DEL_FLAG or UPDATED_DEL_FLAG
        defaultCompanyDeptShouldBeFound("delFlag.in=" + DEFAULT_DEL_FLAG + "," + UPDATED_DEL_FLAG);

        // Get all the companyDeptList where delFlag equals to UPDATED_DEL_FLAG
        defaultCompanyDeptShouldNotBeFound("delFlag.in=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByDelFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where delFlag is not null
        defaultCompanyDeptShouldBeFound("delFlag.specified=true");

        // Get all the companyDeptList where delFlag is null
        defaultCompanyDeptShouldNotBeFound("delFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createBy equals to DEFAULT_CREATE_BY
        defaultCompanyDeptShouldBeFound("createBy.equals=" + DEFAULT_CREATE_BY);

        // Get all the companyDeptList where createBy equals to UPDATED_CREATE_BY
        defaultCompanyDeptShouldNotBeFound("createBy.equals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createBy not equals to DEFAULT_CREATE_BY
        defaultCompanyDeptShouldNotBeFound("createBy.notEquals=" + DEFAULT_CREATE_BY);

        // Get all the companyDeptList where createBy not equals to UPDATED_CREATE_BY
        defaultCompanyDeptShouldBeFound("createBy.notEquals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateByIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createBy in DEFAULT_CREATE_BY or UPDATED_CREATE_BY
        defaultCompanyDeptShouldBeFound("createBy.in=" + DEFAULT_CREATE_BY + "," + UPDATED_CREATE_BY);

        // Get all the companyDeptList where createBy equals to UPDATED_CREATE_BY
        defaultCompanyDeptShouldNotBeFound("createBy.in=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateByIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createBy is not null
        defaultCompanyDeptShouldBeFound("createBy.specified=true");

        // Get all the companyDeptList where createBy is null
        defaultCompanyDeptShouldNotBeFound("createBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateByContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createBy contains DEFAULT_CREATE_BY
        defaultCompanyDeptShouldBeFound("createBy.contains=" + DEFAULT_CREATE_BY);

        // Get all the companyDeptList where createBy contains UPDATED_CREATE_BY
        defaultCompanyDeptShouldNotBeFound("createBy.contains=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateByNotContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createBy does not contain DEFAULT_CREATE_BY
        defaultCompanyDeptShouldNotBeFound("createBy.doesNotContain=" + DEFAULT_CREATE_BY);

        // Get all the companyDeptList where createBy does not contain UPDATED_CREATE_BY
        defaultCompanyDeptShouldBeFound("createBy.doesNotContain=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createDate equals to DEFAULT_CREATE_DATE
        defaultCompanyDeptShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the companyDeptList where createDate equals to UPDATED_CREATE_DATE
        defaultCompanyDeptShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createDate not equals to DEFAULT_CREATE_DATE
        defaultCompanyDeptShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the companyDeptList where createDate not equals to UPDATED_CREATE_DATE
        defaultCompanyDeptShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultCompanyDeptShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the companyDeptList where createDate equals to UPDATED_CREATE_DATE
        defaultCompanyDeptShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where createDate is not null
        defaultCompanyDeptShouldBeFound("createDate.specified=true");

        // Get all the companyDeptList where createDate is null
        defaultCompanyDeptShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyByIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyBy equals to DEFAULT_LAST_MODIFY_BY
        defaultCompanyDeptShouldBeFound("lastModifyBy.equals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the companyDeptList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCompanyDeptShouldNotBeFound("lastModifyBy.equals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyBy not equals to DEFAULT_LAST_MODIFY_BY
        defaultCompanyDeptShouldNotBeFound("lastModifyBy.notEquals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the companyDeptList where lastModifyBy not equals to UPDATED_LAST_MODIFY_BY
        defaultCompanyDeptShouldBeFound("lastModifyBy.notEquals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyByIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyBy in DEFAULT_LAST_MODIFY_BY or UPDATED_LAST_MODIFY_BY
        defaultCompanyDeptShouldBeFound("lastModifyBy.in=" + DEFAULT_LAST_MODIFY_BY + "," + UPDATED_LAST_MODIFY_BY);

        // Get all the companyDeptList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCompanyDeptShouldNotBeFound("lastModifyBy.in=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyByIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyBy is not null
        defaultCompanyDeptShouldBeFound("lastModifyBy.specified=true");

        // Get all the companyDeptList where lastModifyBy is null
        defaultCompanyDeptShouldNotBeFound("lastModifyBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyByContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyBy contains DEFAULT_LAST_MODIFY_BY
        defaultCompanyDeptShouldBeFound("lastModifyBy.contains=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the companyDeptList where lastModifyBy contains UPDATED_LAST_MODIFY_BY
        defaultCompanyDeptShouldNotBeFound("lastModifyBy.contains=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyByNotContainsSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyBy does not contain DEFAULT_LAST_MODIFY_BY
        defaultCompanyDeptShouldNotBeFound("lastModifyBy.doesNotContain=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the companyDeptList where lastModifyBy does not contain UPDATED_LAST_MODIFY_BY
        defaultCompanyDeptShouldBeFound("lastModifyBy.doesNotContain=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyDate equals to DEFAULT_LAST_MODIFY_DATE
        defaultCompanyDeptShouldBeFound("lastModifyDate.equals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the companyDeptList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCompanyDeptShouldNotBeFound("lastModifyDate.equals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyDate not equals to DEFAULT_LAST_MODIFY_DATE
        defaultCompanyDeptShouldNotBeFound("lastModifyDate.notEquals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the companyDeptList where lastModifyDate not equals to UPDATED_LAST_MODIFY_DATE
        defaultCompanyDeptShouldBeFound("lastModifyDate.notEquals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyDate in DEFAULT_LAST_MODIFY_DATE or UPDATED_LAST_MODIFY_DATE
        defaultCompanyDeptShouldBeFound("lastModifyDate.in=" + DEFAULT_LAST_MODIFY_DATE + "," + UPDATED_LAST_MODIFY_DATE);

        // Get all the companyDeptList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCompanyDeptShouldNotBeFound("lastModifyDate.in=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByLastModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        // Get all the companyDeptList where lastModifyDate is not null
        defaultCompanyDeptShouldBeFound("lastModifyDate.specified=true");

        // Get all the companyDeptList where lastModifyDate is null
        defaultCompanyDeptShouldNotBeFound("lastModifyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByCompanyPostIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);
        CompanyPost companyPost = CompanyPostResourceIT.createEntity(em);
        em.persist(companyPost);
        em.flush();
        companyDept.addCompanyPost(companyPost);
        companyDeptRepository.saveAndFlush(companyDept);
        Long companyPostId = companyPost.getId();

        // Get all the companyDeptList where companyPost equals to companyPostId
        defaultCompanyDeptShouldBeFound("companyPostId.equals=" + companyPostId);

        // Get all the companyDeptList where companyPost equals to (companyPostId + 1)
        defaultCompanyDeptShouldNotBeFound("companyPostId.equals=" + (companyPostId + 1));
    }

    @Test
    @Transactional
    void getAllCompanyDeptsByWamoliUserIsEqualToSomething() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);
        WamoliUser wamoliUser = WamoliUserResourceIT.createEntity(em);
        em.persist(wamoliUser);
        em.flush();
        companyDept.addWamoliUser(wamoliUser);
        companyDeptRepository.saveAndFlush(companyDept);
        Long wamoliUserId = wamoliUser.getId();

        // Get all the companyDeptList where wamoliUser equals to wamoliUserId
        defaultCompanyDeptShouldBeFound("wamoliUserId.equals=" + wamoliUserId);

        // Get all the companyDeptList where wamoliUser equals to (wamoliUserId + 1)
        defaultCompanyDeptShouldNotBeFound("wamoliUserId.equals=" + (wamoliUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyDeptShouldBeFound(String filter) throws Exception {
        restCompanyDeptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyDept.getId().intValue())))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].ancestors").value(hasItem(DEFAULT_ANCESTORS)))
            .andExpect(jsonPath("$.[*].deptName").value(hasItem(DEFAULT_DEPT_NAME)))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].leaderName").value(hasItem(DEFAULT_LEADER_NAME)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restCompanyDeptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyDeptShouldNotBeFound(String filter) throws Exception {
        restCompanyDeptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyDeptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompanyDept() throws Exception {
        // Get the companyDept
        restCompanyDeptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompanyDept() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();

        // Update the companyDept
        CompanyDept updatedCompanyDept = companyDeptRepository.findById(companyDept.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyDept are not directly saved in db
        em.detach(updatedCompanyDept);
        updatedCompanyDept
            .parentId(UPDATED_PARENT_ID)
            .ancestors(UPDATED_ANCESTORS)
            .deptName(UPDATED_DEPT_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .leaderName(UPDATED_LEADER_NAME)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);

        restCompanyDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyDept.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanyDept))
            )
            .andExpect(status().isOk());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
        CompanyDept testCompanyDept = companyDeptList.get(companyDeptList.size() - 1);
        assertThat(testCompanyDept.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testCompanyDept.getAncestors()).isEqualTo(UPDATED_ANCESTORS);
        assertThat(testCompanyDept.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testCompanyDept.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCompanyDept.getLeaderName()).isEqualTo(UPDATED_LEADER_NAME);
        assertThat(testCompanyDept.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCompanyDept.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompanyDept.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCompanyDept.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCompanyDept.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testCompanyDept.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCompanyDept.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
        assertThat(testCompanyDept.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCompanyDept() throws Exception {
        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();
        companyDept.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyDept.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyDept() throws Exception {
        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();
        companyDept.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyDept() throws Exception {
        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();
        companyDept.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyDeptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyDept)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyDeptWithPatch() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();

        // Update the companyDept using partial update
        CompanyDept partialUpdatedCompanyDept = new CompanyDept();
        partialUpdatedCompanyDept.setId(companyDept.getId());

        partialUpdatedCompanyDept
            .ancestors(UPDATED_ANCESTORS)
            .deptName(UPDATED_DEPT_NAME)
            .tel(UPDATED_TEL)
            .enable(UPDATED_ENABLE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);

        restCompanyDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyDept))
            )
            .andExpect(status().isOk());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
        CompanyDept testCompanyDept = companyDeptList.get(companyDeptList.size() - 1);
        assertThat(testCompanyDept.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
        assertThat(testCompanyDept.getAncestors()).isEqualTo(UPDATED_ANCESTORS);
        assertThat(testCompanyDept.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testCompanyDept.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCompanyDept.getLeaderName()).isEqualTo(DEFAULT_LEADER_NAME);
        assertThat(testCompanyDept.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCompanyDept.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompanyDept.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCompanyDept.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCompanyDept.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testCompanyDept.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCompanyDept.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
        assertThat(testCompanyDept.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCompanyDeptWithPatch() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();

        // Update the companyDept using partial update
        CompanyDept partialUpdatedCompanyDept = new CompanyDept();
        partialUpdatedCompanyDept.setId(companyDept.getId());

        partialUpdatedCompanyDept
            .parentId(UPDATED_PARENT_ID)
            .ancestors(UPDATED_ANCESTORS)
            .deptName(UPDATED_DEPT_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .leaderName(UPDATED_LEADER_NAME)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);

        restCompanyDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyDept))
            )
            .andExpect(status().isOk());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
        CompanyDept testCompanyDept = companyDeptList.get(companyDeptList.size() - 1);
        assertThat(testCompanyDept.getParentId()).isEqualTo(UPDATED_PARENT_ID);
        assertThat(testCompanyDept.getAncestors()).isEqualTo(UPDATED_ANCESTORS);
        assertThat(testCompanyDept.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testCompanyDept.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCompanyDept.getLeaderName()).isEqualTo(UPDATED_LEADER_NAME);
        assertThat(testCompanyDept.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCompanyDept.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompanyDept.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCompanyDept.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCompanyDept.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testCompanyDept.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCompanyDept.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
        assertThat(testCompanyDept.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyDept() throws Exception {
        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();
        companyDept.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyDept() throws Exception {
        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();
        companyDept.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyDept() throws Exception {
        int databaseSizeBeforeUpdate = companyDeptRepository.findAll().size();
        companyDept.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyDeptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companyDept))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyDept in the database
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyDept() throws Exception {
        // Initialize the database
        companyDeptRepository.saveAndFlush(companyDept);

        int databaseSizeBeforeDelete = companyDeptRepository.findAll().size();

        // Delete the companyDept
        restCompanyDeptMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyDept.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyDept> companyDeptList = companyDeptRepository.findAll();
        assertThat(companyDeptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
