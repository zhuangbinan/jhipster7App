package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CompanyPost;
import com.mycompany.myapp.domain.WamoliUser;
import com.mycompany.myapp.repository.CompanyPostRepository;
import com.mycompany.myapp.service.CompanyPostService;
import com.mycompany.myapp.service.criteria.CompanyPostCriteria;
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
 * Integration tests for the {@link CompanyPostResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompanyPostResourceIT {

    private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_POST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_NUM = 1;
    private static final Integer UPDATED_ORDER_NUM = 2;
    private static final Integer SMALLER_ORDER_NUM = 1 - 1;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFY_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFY_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/company-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyPostRepository companyPostRepository;

    @Mock
    private CompanyPostRepository companyPostRepositoryMock;

    @Mock
    private CompanyPostService companyPostServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyPostMockMvc;

    private CompanyPost companyPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyPost createEntity(EntityManager em) {
        CompanyPost companyPost = new CompanyPost()
            .postCode(DEFAULT_POST_CODE)
            .postName(DEFAULT_POST_NAME)
            .orderNum(DEFAULT_ORDER_NUM)
            .remark(DEFAULT_REMARK)
            .enable(DEFAULT_ENABLE)
            .createBy(DEFAULT_CREATE_BY)
            .createDate(DEFAULT_CREATE_DATE)
            .lastModifyBy(DEFAULT_LAST_MODIFY_BY)
            .lastModifyDate(DEFAULT_LAST_MODIFY_DATE);
        return companyPost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyPost createUpdatedEntity(EntityManager em) {
        CompanyPost companyPost = new CompanyPost()
            .postCode(UPDATED_POST_CODE)
            .postName(UPDATED_POST_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .remark(UPDATED_REMARK)
            .enable(UPDATED_ENABLE)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);
        return companyPost;
    }

    @BeforeEach
    public void initTest() {
        companyPost = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyPost() throws Exception {
        int databaseSizeBeforeCreate = companyPostRepository.findAll().size();
        // Create the CompanyPost
        restCompanyPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyPost)))
            .andExpect(status().isCreated());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyPost testCompanyPost = companyPostList.get(companyPostList.size() - 1);
        assertThat(testCompanyPost.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testCompanyPost.getPostName()).isEqualTo(DEFAULT_POST_NAME);
        assertThat(testCompanyPost.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCompanyPost.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testCompanyPost.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCompanyPost.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testCompanyPost.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCompanyPost.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
        assertThat(testCompanyPost.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void createCompanyPostWithExistingId() throws Exception {
        // Create the CompanyPost with an existing ID
        companyPost.setId(1L);

        int databaseSizeBeforeCreate = companyPostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyPostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyPost)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanyPosts() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList
        restCompanyPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
            .andExpect(jsonPath("$.[*].postName").value(hasItem(DEFAULT_POST_NAME)))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompanyPostsWithEagerRelationshipsIsEnabled() throws Exception {
        when(companyPostServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompanyPostMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(companyPostServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompanyPostsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(companyPostServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompanyPostMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(companyPostServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCompanyPost() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get the companyPost
        restCompanyPostMockMvc
            .perform(get(ENTITY_API_URL_ID, companyPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyPost.getId().intValue()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE))
            .andExpect(jsonPath("$.postName").value(DEFAULT_POST_NAME))
            .andExpect(jsonPath("$.orderNum").value(DEFAULT_ORDER_NUM))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.lastModifyBy").value(DEFAULT_LAST_MODIFY_BY))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    void getCompanyPostsByIdFiltering() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        Long id = companyPost.getId();

        defaultCompanyPostShouldBeFound("id.equals=" + id);
        defaultCompanyPostShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyPostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyPostShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyPostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyPostShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postCode equals to DEFAULT_POST_CODE
        defaultCompanyPostShouldBeFound("postCode.equals=" + DEFAULT_POST_CODE);

        // Get all the companyPostList where postCode equals to UPDATED_POST_CODE
        defaultCompanyPostShouldNotBeFound("postCode.equals=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postCode not equals to DEFAULT_POST_CODE
        defaultCompanyPostShouldNotBeFound("postCode.notEquals=" + DEFAULT_POST_CODE);

        // Get all the companyPostList where postCode not equals to UPDATED_POST_CODE
        defaultCompanyPostShouldBeFound("postCode.notEquals=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostCodeIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postCode in DEFAULT_POST_CODE or UPDATED_POST_CODE
        defaultCompanyPostShouldBeFound("postCode.in=" + DEFAULT_POST_CODE + "," + UPDATED_POST_CODE);

        // Get all the companyPostList where postCode equals to UPDATED_POST_CODE
        defaultCompanyPostShouldNotBeFound("postCode.in=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postCode is not null
        defaultCompanyPostShouldBeFound("postCode.specified=true");

        // Get all the companyPostList where postCode is null
        defaultCompanyPostShouldNotBeFound("postCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostCodeContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postCode contains DEFAULT_POST_CODE
        defaultCompanyPostShouldBeFound("postCode.contains=" + DEFAULT_POST_CODE);

        // Get all the companyPostList where postCode contains UPDATED_POST_CODE
        defaultCompanyPostShouldNotBeFound("postCode.contains=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostCodeNotContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postCode does not contain DEFAULT_POST_CODE
        defaultCompanyPostShouldNotBeFound("postCode.doesNotContain=" + DEFAULT_POST_CODE);

        // Get all the companyPostList where postCode does not contain UPDATED_POST_CODE
        defaultCompanyPostShouldBeFound("postCode.doesNotContain=" + UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postName equals to DEFAULT_POST_NAME
        defaultCompanyPostShouldBeFound("postName.equals=" + DEFAULT_POST_NAME);

        // Get all the companyPostList where postName equals to UPDATED_POST_NAME
        defaultCompanyPostShouldNotBeFound("postName.equals=" + UPDATED_POST_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postName not equals to DEFAULT_POST_NAME
        defaultCompanyPostShouldNotBeFound("postName.notEquals=" + DEFAULT_POST_NAME);

        // Get all the companyPostList where postName not equals to UPDATED_POST_NAME
        defaultCompanyPostShouldBeFound("postName.notEquals=" + UPDATED_POST_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postName in DEFAULT_POST_NAME or UPDATED_POST_NAME
        defaultCompanyPostShouldBeFound("postName.in=" + DEFAULT_POST_NAME + "," + UPDATED_POST_NAME);

        // Get all the companyPostList where postName equals to UPDATED_POST_NAME
        defaultCompanyPostShouldNotBeFound("postName.in=" + UPDATED_POST_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postName is not null
        defaultCompanyPostShouldBeFound("postName.specified=true");

        // Get all the companyPostList where postName is null
        defaultCompanyPostShouldNotBeFound("postName.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostNameContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postName contains DEFAULT_POST_NAME
        defaultCompanyPostShouldBeFound("postName.contains=" + DEFAULT_POST_NAME);

        // Get all the companyPostList where postName contains UPDATED_POST_NAME
        defaultCompanyPostShouldNotBeFound("postName.contains=" + UPDATED_POST_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByPostNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where postName does not contain DEFAULT_POST_NAME
        defaultCompanyPostShouldNotBeFound("postName.doesNotContain=" + DEFAULT_POST_NAME);

        // Get all the companyPostList where postName does not contain UPDATED_POST_NAME
        defaultCompanyPostShouldBeFound("postName.doesNotContain=" + UPDATED_POST_NAME);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByOrderNumIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where orderNum equals to DEFAULT_ORDER_NUM
        defaultCompanyPostShouldBeFound("orderNum.equals=" + DEFAULT_ORDER_NUM);

        // Get all the companyPostList where orderNum equals to UPDATED_ORDER_NUM
        defaultCompanyPostShouldNotBeFound("orderNum.equals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByOrderNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where orderNum not equals to DEFAULT_ORDER_NUM
        defaultCompanyPostShouldNotBeFound("orderNum.notEquals=" + DEFAULT_ORDER_NUM);

        // Get all the companyPostList where orderNum not equals to UPDATED_ORDER_NUM
        defaultCompanyPostShouldBeFound("orderNum.notEquals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByOrderNumIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where orderNum in DEFAULT_ORDER_NUM or UPDATED_ORDER_NUM
        defaultCompanyPostShouldBeFound("orderNum.in=" + DEFAULT_ORDER_NUM + "," + UPDATED_ORDER_NUM);

        // Get all the companyPostList where orderNum equals to UPDATED_ORDER_NUM
        defaultCompanyPostShouldNotBeFound("orderNum.in=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByOrderNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where orderNum is not null
        defaultCompanyPostShouldBeFound("orderNum.specified=true");

        // Get all the companyPostList where orderNum is null
        defaultCompanyPostShouldNotBeFound("orderNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByOrderNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where orderNum is greater than or equal to DEFAULT_ORDER_NUM
        defaultCompanyPostShouldBeFound("orderNum.greaterThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the companyPostList where orderNum is greater than or equal to UPDATED_ORDER_NUM
        defaultCompanyPostShouldNotBeFound("orderNum.greaterThanOrEqual=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByOrderNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where orderNum is less than or equal to DEFAULT_ORDER_NUM
        defaultCompanyPostShouldBeFound("orderNum.lessThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the companyPostList where orderNum is less than or equal to SMALLER_ORDER_NUM
        defaultCompanyPostShouldNotBeFound("orderNum.lessThanOrEqual=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByOrderNumIsLessThanSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where orderNum is less than DEFAULT_ORDER_NUM
        defaultCompanyPostShouldNotBeFound("orderNum.lessThan=" + DEFAULT_ORDER_NUM);

        // Get all the companyPostList where orderNum is less than UPDATED_ORDER_NUM
        defaultCompanyPostShouldBeFound("orderNum.lessThan=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByOrderNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where orderNum is greater than DEFAULT_ORDER_NUM
        defaultCompanyPostShouldNotBeFound("orderNum.greaterThan=" + DEFAULT_ORDER_NUM);

        // Get all the companyPostList where orderNum is greater than SMALLER_ORDER_NUM
        defaultCompanyPostShouldBeFound("orderNum.greaterThan=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByRemarkIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where remark equals to DEFAULT_REMARK
        defaultCompanyPostShouldBeFound("remark.equals=" + DEFAULT_REMARK);

        // Get all the companyPostList where remark equals to UPDATED_REMARK
        defaultCompanyPostShouldNotBeFound("remark.equals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByRemarkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where remark not equals to DEFAULT_REMARK
        defaultCompanyPostShouldNotBeFound("remark.notEquals=" + DEFAULT_REMARK);

        // Get all the companyPostList where remark not equals to UPDATED_REMARK
        defaultCompanyPostShouldBeFound("remark.notEquals=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByRemarkIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where remark in DEFAULT_REMARK or UPDATED_REMARK
        defaultCompanyPostShouldBeFound("remark.in=" + DEFAULT_REMARK + "," + UPDATED_REMARK);

        // Get all the companyPostList where remark equals to UPDATED_REMARK
        defaultCompanyPostShouldNotBeFound("remark.in=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByRemarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where remark is not null
        defaultCompanyPostShouldBeFound("remark.specified=true");

        // Get all the companyPostList where remark is null
        defaultCompanyPostShouldNotBeFound("remark.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByRemarkContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where remark contains DEFAULT_REMARK
        defaultCompanyPostShouldBeFound("remark.contains=" + DEFAULT_REMARK);

        // Get all the companyPostList where remark contains UPDATED_REMARK
        defaultCompanyPostShouldNotBeFound("remark.contains=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByRemarkNotContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where remark does not contain DEFAULT_REMARK
        defaultCompanyPostShouldNotBeFound("remark.doesNotContain=" + DEFAULT_REMARK);

        // Get all the companyPostList where remark does not contain UPDATED_REMARK
        defaultCompanyPostShouldBeFound("remark.doesNotContain=" + UPDATED_REMARK);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where enable equals to DEFAULT_ENABLE
        defaultCompanyPostShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the companyPostList where enable equals to UPDATED_ENABLE
        defaultCompanyPostShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where enable not equals to DEFAULT_ENABLE
        defaultCompanyPostShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the companyPostList where enable not equals to UPDATED_ENABLE
        defaultCompanyPostShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultCompanyPostShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the companyPostList where enable equals to UPDATED_ENABLE
        defaultCompanyPostShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where enable is not null
        defaultCompanyPostShouldBeFound("enable.specified=true");

        // Get all the companyPostList where enable is null
        defaultCompanyPostShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateByIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createBy equals to DEFAULT_CREATE_BY
        defaultCompanyPostShouldBeFound("createBy.equals=" + DEFAULT_CREATE_BY);

        // Get all the companyPostList where createBy equals to UPDATED_CREATE_BY
        defaultCompanyPostShouldNotBeFound("createBy.equals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createBy not equals to DEFAULT_CREATE_BY
        defaultCompanyPostShouldNotBeFound("createBy.notEquals=" + DEFAULT_CREATE_BY);

        // Get all the companyPostList where createBy not equals to UPDATED_CREATE_BY
        defaultCompanyPostShouldBeFound("createBy.notEquals=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateByIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createBy in DEFAULT_CREATE_BY or UPDATED_CREATE_BY
        defaultCompanyPostShouldBeFound("createBy.in=" + DEFAULT_CREATE_BY + "," + UPDATED_CREATE_BY);

        // Get all the companyPostList where createBy equals to UPDATED_CREATE_BY
        defaultCompanyPostShouldNotBeFound("createBy.in=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateByIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createBy is not null
        defaultCompanyPostShouldBeFound("createBy.specified=true");

        // Get all the companyPostList where createBy is null
        defaultCompanyPostShouldNotBeFound("createBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateByContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createBy contains DEFAULT_CREATE_BY
        defaultCompanyPostShouldBeFound("createBy.contains=" + DEFAULT_CREATE_BY);

        // Get all the companyPostList where createBy contains UPDATED_CREATE_BY
        defaultCompanyPostShouldNotBeFound("createBy.contains=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateByNotContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createBy does not contain DEFAULT_CREATE_BY
        defaultCompanyPostShouldNotBeFound("createBy.doesNotContain=" + DEFAULT_CREATE_BY);

        // Get all the companyPostList where createBy does not contain UPDATED_CREATE_BY
        defaultCompanyPostShouldBeFound("createBy.doesNotContain=" + UPDATED_CREATE_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createDate equals to DEFAULT_CREATE_DATE
        defaultCompanyPostShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the companyPostList where createDate equals to UPDATED_CREATE_DATE
        defaultCompanyPostShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createDate not equals to DEFAULT_CREATE_DATE
        defaultCompanyPostShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the companyPostList where createDate not equals to UPDATED_CREATE_DATE
        defaultCompanyPostShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultCompanyPostShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the companyPostList where createDate equals to UPDATED_CREATE_DATE
        defaultCompanyPostShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where createDate is not null
        defaultCompanyPostShouldBeFound("createDate.specified=true");

        // Get all the companyPostList where createDate is null
        defaultCompanyPostShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyByIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyBy equals to DEFAULT_LAST_MODIFY_BY
        defaultCompanyPostShouldBeFound("lastModifyBy.equals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the companyPostList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCompanyPostShouldNotBeFound("lastModifyBy.equals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyBy not equals to DEFAULT_LAST_MODIFY_BY
        defaultCompanyPostShouldNotBeFound("lastModifyBy.notEquals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the companyPostList where lastModifyBy not equals to UPDATED_LAST_MODIFY_BY
        defaultCompanyPostShouldBeFound("lastModifyBy.notEquals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyByIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyBy in DEFAULT_LAST_MODIFY_BY or UPDATED_LAST_MODIFY_BY
        defaultCompanyPostShouldBeFound("lastModifyBy.in=" + DEFAULT_LAST_MODIFY_BY + "," + UPDATED_LAST_MODIFY_BY);

        // Get all the companyPostList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCompanyPostShouldNotBeFound("lastModifyBy.in=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyByIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyBy is not null
        defaultCompanyPostShouldBeFound("lastModifyBy.specified=true");

        // Get all the companyPostList where lastModifyBy is null
        defaultCompanyPostShouldNotBeFound("lastModifyBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyByContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyBy contains DEFAULT_LAST_MODIFY_BY
        defaultCompanyPostShouldBeFound("lastModifyBy.contains=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the companyPostList where lastModifyBy contains UPDATED_LAST_MODIFY_BY
        defaultCompanyPostShouldNotBeFound("lastModifyBy.contains=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyByNotContainsSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyBy does not contain DEFAULT_LAST_MODIFY_BY
        defaultCompanyPostShouldNotBeFound("lastModifyBy.doesNotContain=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the companyPostList where lastModifyBy does not contain UPDATED_LAST_MODIFY_BY
        defaultCompanyPostShouldBeFound("lastModifyBy.doesNotContain=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyDate equals to DEFAULT_LAST_MODIFY_DATE
        defaultCompanyPostShouldBeFound("lastModifyDate.equals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the companyPostList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCompanyPostShouldNotBeFound("lastModifyDate.equals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyDate not equals to DEFAULT_LAST_MODIFY_DATE
        defaultCompanyPostShouldNotBeFound("lastModifyDate.notEquals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the companyPostList where lastModifyDate not equals to UPDATED_LAST_MODIFY_DATE
        defaultCompanyPostShouldBeFound("lastModifyDate.notEquals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyDate in DEFAULT_LAST_MODIFY_DATE or UPDATED_LAST_MODIFY_DATE
        defaultCompanyPostShouldBeFound("lastModifyDate.in=" + DEFAULT_LAST_MODIFY_DATE + "," + UPDATED_LAST_MODIFY_DATE);

        // Get all the companyPostList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCompanyPostShouldNotBeFound("lastModifyDate.in=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCompanyPostsByLastModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        // Get all the companyPostList where lastModifyDate is not null
        defaultCompanyPostShouldBeFound("lastModifyDate.specified=true");

        // Get all the companyPostList where lastModifyDate is null
        defaultCompanyPostShouldNotBeFound("lastModifyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCompanyPostsByWamoliUserIsEqualToSomething() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);
        WamoliUser wamoliUser = WamoliUserResourceIT.createEntity(em);
        em.persist(wamoliUser);
        em.flush();
        companyPost.addWamoliUser(wamoliUser);
        companyPostRepository.saveAndFlush(companyPost);
        Long wamoliUserId = wamoliUser.getId();

        // Get all the companyPostList where wamoliUser equals to wamoliUserId
        defaultCompanyPostShouldBeFound("wamoliUserId.equals=" + wamoliUserId);

        // Get all the companyPostList where wamoliUser equals to (wamoliUserId + 1)
        defaultCompanyPostShouldNotBeFound("wamoliUserId.equals=" + (wamoliUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyPostShouldBeFound(String filter) throws Exception {
        restCompanyPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
            .andExpect(jsonPath("$.[*].postName").value(hasItem(DEFAULT_POST_NAME)))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restCompanyPostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyPostShouldNotBeFound(String filter) throws Exception {
        restCompanyPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyPostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompanyPost() throws Exception {
        // Get the companyPost
        restCompanyPostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompanyPost() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();

        // Update the companyPost
        CompanyPost updatedCompanyPost = companyPostRepository.findById(companyPost.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyPost are not directly saved in db
        em.detach(updatedCompanyPost);
        updatedCompanyPost
            .postCode(UPDATED_POST_CODE)
            .postName(UPDATED_POST_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .remark(UPDATED_REMARK)
            .enable(UPDATED_ENABLE)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);

        restCompanyPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanyPost))
            )
            .andExpect(status().isOk());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
        CompanyPost testCompanyPost = companyPostList.get(companyPostList.size() - 1);
        assertThat(testCompanyPost.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testCompanyPost.getPostName()).isEqualTo(UPDATED_POST_NAME);
        assertThat(testCompanyPost.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCompanyPost.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testCompanyPost.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCompanyPost.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testCompanyPost.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCompanyPost.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
        assertThat(testCompanyPost.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCompanyPost() throws Exception {
        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();
        companyPost.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyPost() throws Exception {
        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();
        companyPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyPost() throws Exception {
        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();
        companyPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyPostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyPost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyPostWithPatch() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();

        // Update the companyPost using partial update
        CompanyPost partialUpdatedCompanyPost = new CompanyPost();
        partialUpdatedCompanyPost.setId(companyPost.getId());

        partialUpdatedCompanyPost
            .postCode(UPDATED_POST_CODE)
            .postName(UPDATED_POST_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .enable(UPDATED_ENABLE)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);

        restCompanyPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyPost))
            )
            .andExpect(status().isOk());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
        CompanyPost testCompanyPost = companyPostList.get(companyPostList.size() - 1);
        assertThat(testCompanyPost.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testCompanyPost.getPostName()).isEqualTo(UPDATED_POST_NAME);
        assertThat(testCompanyPost.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCompanyPost.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testCompanyPost.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCompanyPost.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testCompanyPost.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCompanyPost.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
        assertThat(testCompanyPost.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCompanyPostWithPatch() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();

        // Update the companyPost using partial update
        CompanyPost partialUpdatedCompanyPost = new CompanyPost();
        partialUpdatedCompanyPost.setId(companyPost.getId());

        partialUpdatedCompanyPost
            .postCode(UPDATED_POST_CODE)
            .postName(UPDATED_POST_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .remark(UPDATED_REMARK)
            .enable(UPDATED_ENABLE)
            .createBy(UPDATED_CREATE_BY)
            .createDate(UPDATED_CREATE_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE);

        restCompanyPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyPost))
            )
            .andExpect(status().isOk());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
        CompanyPost testCompanyPost = companyPostList.get(companyPostList.size() - 1);
        assertThat(testCompanyPost.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testCompanyPost.getPostName()).isEqualTo(UPDATED_POST_NAME);
        assertThat(testCompanyPost.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCompanyPost.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testCompanyPost.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCompanyPost.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testCompanyPost.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCompanyPost.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
        assertThat(testCompanyPost.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyPost() throws Exception {
        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();
        companyPost.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyPost() throws Exception {
        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();
        companyPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyPost() throws Exception {
        int databaseSizeBeforeUpdate = companyPostRepository.findAll().size();
        companyPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyPostMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companyPost))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyPost in the database
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyPost() throws Exception {
        // Initialize the database
        companyPostRepository.saveAndFlush(companyPost);

        int databaseSizeBeforeDelete = companyPostRepository.findAll().size();

        // Delete the companyPost
        restCompanyPostMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyPost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyPost> companyPostList = companyPostRepository.findAll();
        assertThat(companyPostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
