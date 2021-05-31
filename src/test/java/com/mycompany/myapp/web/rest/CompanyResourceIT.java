package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.domain.HomelandStation;
import com.mycompany.myapp.repository.CompanyRepository;
import com.mycompany.myapp.service.criteria.CompanyCriteria;
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
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LONG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LONG_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .name(DEFAULT_NAME)
            .longCode(DEFAULT_LONG_CODE)
            .address(DEFAULT_ADDRESS)
            .tel(DEFAULT_TEL)
            .email(DEFAULT_EMAIL)
            .managerName(DEFAULT_MANAGER_NAME)
            .enable(DEFAULT_ENABLE);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .managerName(UPDATED_MANAGER_NAME)
            .enable(UPDATED_ENABLE);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();
        // Create the Company
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompany.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
        assertThat(testCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCompany.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompany.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
        assertThat(testCompany.getEnable()).isEqualTo(DEFAULT_ENABLE);
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);

        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setName(null);

        // Create the Company, which fails.

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setLongCode(null);

        // Create the Company, which fails.

        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.longCode").value(DEFAULT_LONG_CODE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.managerName").value(DEFAULT_MANAGER_NAME))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        Long id = company.getId();

        defaultCompanyShouldBeFound("id.equals=" + id);
        defaultCompanyShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name equals to DEFAULT_NAME
        defaultCompanyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the companyList where name equals to UPDATED_NAME
        defaultCompanyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name not equals to DEFAULT_NAME
        defaultCompanyShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the companyList where name not equals to UPDATED_NAME
        defaultCompanyShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCompanyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the companyList where name equals to UPDATED_NAME
        defaultCompanyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name is not null
        defaultCompanyShouldBeFound("name.specified=true");

        // Get all the companyList where name is null
        defaultCompanyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByNameContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name contains DEFAULT_NAME
        defaultCompanyShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the companyList where name contains UPDATED_NAME
        defaultCompanyShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where name does not contain DEFAULT_NAME
        defaultCompanyShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the companyList where name does not contain UPDATED_NAME
        defaultCompanyShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByLongCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where longCode equals to DEFAULT_LONG_CODE
        defaultCompanyShouldBeFound("longCode.equals=" + DEFAULT_LONG_CODE);

        // Get all the companyList where longCode equals to UPDATED_LONG_CODE
        defaultCompanyShouldNotBeFound("longCode.equals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByLongCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where longCode not equals to DEFAULT_LONG_CODE
        defaultCompanyShouldNotBeFound("longCode.notEquals=" + DEFAULT_LONG_CODE);

        // Get all the companyList where longCode not equals to UPDATED_LONG_CODE
        defaultCompanyShouldBeFound("longCode.notEquals=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByLongCodeIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where longCode in DEFAULT_LONG_CODE or UPDATED_LONG_CODE
        defaultCompanyShouldBeFound("longCode.in=" + DEFAULT_LONG_CODE + "," + UPDATED_LONG_CODE);

        // Get all the companyList where longCode equals to UPDATED_LONG_CODE
        defaultCompanyShouldNotBeFound("longCode.in=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByLongCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where longCode is not null
        defaultCompanyShouldBeFound("longCode.specified=true");

        // Get all the companyList where longCode is null
        defaultCompanyShouldNotBeFound("longCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByLongCodeContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where longCode contains DEFAULT_LONG_CODE
        defaultCompanyShouldBeFound("longCode.contains=" + DEFAULT_LONG_CODE);

        // Get all the companyList where longCode contains UPDATED_LONG_CODE
        defaultCompanyShouldNotBeFound("longCode.contains=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByLongCodeNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where longCode does not contain DEFAULT_LONG_CODE
        defaultCompanyShouldNotBeFound("longCode.doesNotContain=" + DEFAULT_LONG_CODE);

        // Get all the companyList where longCode does not contain UPDATED_LONG_CODE
        defaultCompanyShouldBeFound("longCode.doesNotContain=" + UPDATED_LONG_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address equals to DEFAULT_ADDRESS
        defaultCompanyShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the companyList where address equals to UPDATED_ADDRESS
        defaultCompanyShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address not equals to DEFAULT_ADDRESS
        defaultCompanyShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the companyList where address not equals to UPDATED_ADDRESS
        defaultCompanyShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCompanyShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the companyList where address equals to UPDATED_ADDRESS
        defaultCompanyShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address is not null
        defaultCompanyShouldBeFound("address.specified=true");

        // Get all the companyList where address is null
        defaultCompanyShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address contains DEFAULT_ADDRESS
        defaultCompanyShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the companyList where address contains UPDATED_ADDRESS
        defaultCompanyShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where address does not contain DEFAULT_ADDRESS
        defaultCompanyShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the companyList where address does not contain UPDATED_ADDRESS
        defaultCompanyShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel equals to DEFAULT_TEL
        defaultCompanyShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the companyList where tel equals to UPDATED_TEL
        defaultCompanyShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompaniesByTelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel not equals to DEFAULT_TEL
        defaultCompanyShouldNotBeFound("tel.notEquals=" + DEFAULT_TEL);

        // Get all the companyList where tel not equals to UPDATED_TEL
        defaultCompanyShouldBeFound("tel.notEquals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompaniesByTelIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultCompanyShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the companyList where tel equals to UPDATED_TEL
        defaultCompanyShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompaniesByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel is not null
        defaultCompanyShouldBeFound("tel.specified=true");

        // Get all the companyList where tel is null
        defaultCompanyShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByTelContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel contains DEFAULT_TEL
        defaultCompanyShouldBeFound("tel.contains=" + DEFAULT_TEL);

        // Get all the companyList where tel contains UPDATED_TEL
        defaultCompanyShouldNotBeFound("tel.contains=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompaniesByTelNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where tel does not contain DEFAULT_TEL
        defaultCompanyShouldNotBeFound("tel.doesNotContain=" + DEFAULT_TEL);

        // Get all the companyList where tel does not contain UPDATED_TEL
        defaultCompanyShouldBeFound("tel.doesNotContain=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email equals to DEFAULT_EMAIL
        defaultCompanyShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email not equals to DEFAULT_EMAIL
        defaultCompanyShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the companyList where email not equals to UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the companyList where email equals to UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email is not null
        defaultCompanyShouldBeFound("email.specified=true");

        // Get all the companyList where email is null
        defaultCompanyShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email contains DEFAULT_EMAIL
        defaultCompanyShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the companyList where email contains UPDATED_EMAIL
        defaultCompanyShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where email does not contain DEFAULT_EMAIL
        defaultCompanyShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the companyList where email does not contain UPDATED_EMAIL
        defaultCompanyShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCompaniesByManagerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where managerName equals to DEFAULT_MANAGER_NAME
        defaultCompanyShouldBeFound("managerName.equals=" + DEFAULT_MANAGER_NAME);

        // Get all the companyList where managerName equals to UPDATED_MANAGER_NAME
        defaultCompanyShouldNotBeFound("managerName.equals=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByManagerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where managerName not equals to DEFAULT_MANAGER_NAME
        defaultCompanyShouldNotBeFound("managerName.notEquals=" + DEFAULT_MANAGER_NAME);

        // Get all the companyList where managerName not equals to UPDATED_MANAGER_NAME
        defaultCompanyShouldBeFound("managerName.notEquals=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByManagerNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where managerName in DEFAULT_MANAGER_NAME or UPDATED_MANAGER_NAME
        defaultCompanyShouldBeFound("managerName.in=" + DEFAULT_MANAGER_NAME + "," + UPDATED_MANAGER_NAME);

        // Get all the companyList where managerName equals to UPDATED_MANAGER_NAME
        defaultCompanyShouldNotBeFound("managerName.in=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByManagerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where managerName is not null
        defaultCompanyShouldBeFound("managerName.specified=true");

        // Get all the companyList where managerName is null
        defaultCompanyShouldNotBeFound("managerName.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByManagerNameContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where managerName contains DEFAULT_MANAGER_NAME
        defaultCompanyShouldBeFound("managerName.contains=" + DEFAULT_MANAGER_NAME);

        // Get all the companyList where managerName contains UPDATED_MANAGER_NAME
        defaultCompanyShouldNotBeFound("managerName.contains=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByManagerNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where managerName does not contain DEFAULT_MANAGER_NAME
        defaultCompanyShouldNotBeFound("managerName.doesNotContain=" + DEFAULT_MANAGER_NAME);

        // Get all the companyList where managerName does not contain UPDATED_MANAGER_NAME
        defaultCompanyShouldBeFound("managerName.doesNotContain=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where enable equals to DEFAULT_ENABLE
        defaultCompanyShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the companyList where enable equals to UPDATED_ENABLE
        defaultCompanyShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where enable not equals to DEFAULT_ENABLE
        defaultCompanyShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the companyList where enable not equals to UPDATED_ENABLE
        defaultCompanyShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultCompanyShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the companyList where enable equals to UPDATED_ENABLE
        defaultCompanyShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where enable is not null
        defaultCompanyShouldBeFound("enable.specified=true");

        // Get all the companyList where enable is null
        defaultCompanyShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByHomelandStationIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        HomelandStation homelandStation = HomelandStationResourceIT.createEntity(em);
        em.persist(homelandStation);
        em.flush();
        company.addHomelandStation(homelandStation);
        companyRepository.saveAndFlush(company);
        Long homelandStationId = homelandStation.getId();

        // Get all the companyList where homelandStation equals to homelandStationId
        defaultCompanyShouldBeFound("homelandStationId.equals=" + homelandStationId);

        // Get all the companyList where homelandStation equals to (homelandStationId + 1)
        defaultCompanyShouldNotBeFound("homelandStationId.equals=" + (homelandStationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].longCode").value(hasItem(DEFAULT_LONG_CODE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())));

        // Check, that the count call also returns 1
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .managerName(UPDATED_MANAGER_NAME)
            .enable(UPDATED_ENABLE);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompany.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompany.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testCompany.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, company.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany.enable(UPDATED_ENABLE);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompany.getLongCode()).isEqualTo(DEFAULT_LONG_CODE);
        assertThat(testCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCompany.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompany.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
        assertThat(testCompany.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .name(UPDATED_NAME)
            .longCode(UPDATED_LONG_CODE)
            .address(UPDATED_ADDRESS)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .managerName(UPDATED_MANAGER_NAME)
            .enable(UPDATED_ENABLE);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompany.getLongCode()).isEqualTo(UPDATED_LONG_CODE);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompany.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testCompany.getEnable()).isEqualTo(UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, company.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
