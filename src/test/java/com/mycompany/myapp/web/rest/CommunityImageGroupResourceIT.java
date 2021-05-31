package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CommunityImageGroup;
import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.repository.CommunityImageGroupRepository;
import com.mycompany.myapp.service.criteria.CommunityImageGroupCriteria;
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
 * Integration tests for the {@link CommunityImageGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityImageGroupResourceIT {

    private static final String DEFAULT_IMG_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_IMG_GROUP_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_NUM = 1;
    private static final Integer UPDATED_ORDER_NUM = 2;
    private static final Integer SMALLER_ORDER_NUM = 1 - 1;

    private static final Instant DEFAULT_LAST_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFY_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFY_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/community-image-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommunityImageGroupRepository communityImageGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityImageGroupMockMvc;

    private CommunityImageGroup communityImageGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityImageGroup createEntity(EntityManager em) {
        CommunityImageGroup communityImageGroup = new CommunityImageGroup()
            .imgGroupName(DEFAULT_IMG_GROUP_NAME)
            .orderNum(DEFAULT_ORDER_NUM)
            .lastModifyDate(DEFAULT_LAST_MODIFY_DATE)
            .lastModifyBy(DEFAULT_LAST_MODIFY_BY);
        return communityImageGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityImageGroup createUpdatedEntity(EntityManager em) {
        CommunityImageGroup communityImageGroup = new CommunityImageGroup()
            .imgGroupName(UPDATED_IMG_GROUP_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);
        return communityImageGroup;
    }

    @BeforeEach
    public void initTest() {
        communityImageGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createCommunityImageGroup() throws Exception {
        int databaseSizeBeforeCreate = communityImageGroupRepository.findAll().size();
        // Create the CommunityImageGroup
        restCommunityImageGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityImageGroup))
            )
            .andExpect(status().isCreated());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeCreate + 1);
        CommunityImageGroup testCommunityImageGroup = communityImageGroupList.get(communityImageGroupList.size() - 1);
        assertThat(testCommunityImageGroup.getImgGroupName()).isEqualTo(DEFAULT_IMG_GROUP_NAME);
        assertThat(testCommunityImageGroup.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCommunityImageGroup.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testCommunityImageGroup.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void createCommunityImageGroupWithExistingId() throws Exception {
        // Create the CommunityImageGroup with an existing ID
        communityImageGroup.setId(1L);

        int databaseSizeBeforeCreate = communityImageGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityImageGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityImageGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroups() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList
        restCommunityImageGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityImageGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].imgGroupName").value(hasItem(DEFAULT_IMG_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));
    }

    @Test
    @Transactional
    void getCommunityImageGroup() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get the communityImageGroup
        restCommunityImageGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, communityImageGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communityImageGroup.getId().intValue()))
            .andExpect(jsonPath("$.imgGroupName").value(DEFAULT_IMG_GROUP_NAME))
            .andExpect(jsonPath("$.orderNum").value(DEFAULT_ORDER_NUM))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()))
            .andExpect(jsonPath("$.lastModifyBy").value(DEFAULT_LAST_MODIFY_BY));
    }

    @Test
    @Transactional
    void getCommunityImageGroupsByIdFiltering() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        Long id = communityImageGroup.getId();

        defaultCommunityImageGroupShouldBeFound("id.equals=" + id);
        defaultCommunityImageGroupShouldNotBeFound("id.notEquals=" + id);

        defaultCommunityImageGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommunityImageGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultCommunityImageGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommunityImageGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByImgGroupNameIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where imgGroupName equals to DEFAULT_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldBeFound("imgGroupName.equals=" + DEFAULT_IMG_GROUP_NAME);

        // Get all the communityImageGroupList where imgGroupName equals to UPDATED_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldNotBeFound("imgGroupName.equals=" + UPDATED_IMG_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByImgGroupNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where imgGroupName not equals to DEFAULT_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldNotBeFound("imgGroupName.notEquals=" + DEFAULT_IMG_GROUP_NAME);

        // Get all the communityImageGroupList where imgGroupName not equals to UPDATED_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldBeFound("imgGroupName.notEquals=" + UPDATED_IMG_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByImgGroupNameIsInShouldWork() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where imgGroupName in DEFAULT_IMG_GROUP_NAME or UPDATED_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldBeFound("imgGroupName.in=" + DEFAULT_IMG_GROUP_NAME + "," + UPDATED_IMG_GROUP_NAME);

        // Get all the communityImageGroupList where imgGroupName equals to UPDATED_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldNotBeFound("imgGroupName.in=" + UPDATED_IMG_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByImgGroupNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where imgGroupName is not null
        defaultCommunityImageGroupShouldBeFound("imgGroupName.specified=true");

        // Get all the communityImageGroupList where imgGroupName is null
        defaultCommunityImageGroupShouldNotBeFound("imgGroupName.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByImgGroupNameContainsSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where imgGroupName contains DEFAULT_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldBeFound("imgGroupName.contains=" + DEFAULT_IMG_GROUP_NAME);

        // Get all the communityImageGroupList where imgGroupName contains UPDATED_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldNotBeFound("imgGroupName.contains=" + UPDATED_IMG_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByImgGroupNameNotContainsSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where imgGroupName does not contain DEFAULT_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldNotBeFound("imgGroupName.doesNotContain=" + DEFAULT_IMG_GROUP_NAME);

        // Get all the communityImageGroupList where imgGroupName does not contain UPDATED_IMG_GROUP_NAME
        defaultCommunityImageGroupShouldBeFound("imgGroupName.doesNotContain=" + UPDATED_IMG_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByOrderNumIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where orderNum equals to DEFAULT_ORDER_NUM
        defaultCommunityImageGroupShouldBeFound("orderNum.equals=" + DEFAULT_ORDER_NUM);

        // Get all the communityImageGroupList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityImageGroupShouldNotBeFound("orderNum.equals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByOrderNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where orderNum not equals to DEFAULT_ORDER_NUM
        defaultCommunityImageGroupShouldNotBeFound("orderNum.notEquals=" + DEFAULT_ORDER_NUM);

        // Get all the communityImageGroupList where orderNum not equals to UPDATED_ORDER_NUM
        defaultCommunityImageGroupShouldBeFound("orderNum.notEquals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByOrderNumIsInShouldWork() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where orderNum in DEFAULT_ORDER_NUM or UPDATED_ORDER_NUM
        defaultCommunityImageGroupShouldBeFound("orderNum.in=" + DEFAULT_ORDER_NUM + "," + UPDATED_ORDER_NUM);

        // Get all the communityImageGroupList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityImageGroupShouldNotBeFound("orderNum.in=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByOrderNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where orderNum is not null
        defaultCommunityImageGroupShouldBeFound("orderNum.specified=true");

        // Get all the communityImageGroupList where orderNum is null
        defaultCommunityImageGroupShouldNotBeFound("orderNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByOrderNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where orderNum is greater than or equal to DEFAULT_ORDER_NUM
        defaultCommunityImageGroupShouldBeFound("orderNum.greaterThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityImageGroupList where orderNum is greater than or equal to UPDATED_ORDER_NUM
        defaultCommunityImageGroupShouldNotBeFound("orderNum.greaterThanOrEqual=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByOrderNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where orderNum is less than or equal to DEFAULT_ORDER_NUM
        defaultCommunityImageGroupShouldBeFound("orderNum.lessThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityImageGroupList where orderNum is less than or equal to SMALLER_ORDER_NUM
        defaultCommunityImageGroupShouldNotBeFound("orderNum.lessThanOrEqual=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByOrderNumIsLessThanSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where orderNum is less than DEFAULT_ORDER_NUM
        defaultCommunityImageGroupShouldNotBeFound("orderNum.lessThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityImageGroupList where orderNum is less than UPDATED_ORDER_NUM
        defaultCommunityImageGroupShouldBeFound("orderNum.lessThan=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByOrderNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where orderNum is greater than DEFAULT_ORDER_NUM
        defaultCommunityImageGroupShouldNotBeFound("orderNum.greaterThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityImageGroupList where orderNum is greater than SMALLER_ORDER_NUM
        defaultCommunityImageGroupShouldBeFound("orderNum.greaterThan=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyDate equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityImageGroupShouldBeFound("lastModifyDate.equals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityImageGroupList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityImageGroupShouldNotBeFound("lastModifyDate.equals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyDate not equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityImageGroupShouldNotBeFound("lastModifyDate.notEquals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityImageGroupList where lastModifyDate not equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityImageGroupShouldBeFound("lastModifyDate.notEquals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyDate in DEFAULT_LAST_MODIFY_DATE or UPDATED_LAST_MODIFY_DATE
        defaultCommunityImageGroupShouldBeFound("lastModifyDate.in=" + DEFAULT_LAST_MODIFY_DATE + "," + UPDATED_LAST_MODIFY_DATE);

        // Get all the communityImageGroupList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityImageGroupShouldNotBeFound("lastModifyDate.in=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyDate is not null
        defaultCommunityImageGroupShouldBeFound("lastModifyDate.specified=true");

        // Get all the communityImageGroupList where lastModifyDate is null
        defaultCommunityImageGroupShouldNotBeFound("lastModifyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyByIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyBy equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldBeFound("lastModifyBy.equals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityImageGroupList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldNotBeFound("lastModifyBy.equals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyBy not equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldNotBeFound("lastModifyBy.notEquals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityImageGroupList where lastModifyBy not equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldBeFound("lastModifyBy.notEquals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyByIsInShouldWork() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyBy in DEFAULT_LAST_MODIFY_BY or UPDATED_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldBeFound("lastModifyBy.in=" + DEFAULT_LAST_MODIFY_BY + "," + UPDATED_LAST_MODIFY_BY);

        // Get all the communityImageGroupList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldNotBeFound("lastModifyBy.in=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyByIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyBy is not null
        defaultCommunityImageGroupShouldBeFound("lastModifyBy.specified=true");

        // Get all the communityImageGroupList where lastModifyBy is null
        defaultCommunityImageGroupShouldNotBeFound("lastModifyBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyByContainsSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyBy contains DEFAULT_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldBeFound("lastModifyBy.contains=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityImageGroupList where lastModifyBy contains UPDATED_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldNotBeFound("lastModifyBy.contains=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByLastModifyByNotContainsSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        // Get all the communityImageGroupList where lastModifyBy does not contain DEFAULT_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldNotBeFound("lastModifyBy.doesNotContain=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityImageGroupList where lastModifyBy does not contain UPDATED_LAST_MODIFY_BY
        defaultCommunityImageGroupShouldBeFound("lastModifyBy.doesNotContain=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImageGroupsByCommunityImagesIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);
        CommunityImages communityImages = CommunityImagesResourceIT.createEntity(em);
        em.persist(communityImages);
        em.flush();
        communityImageGroup.addCommunityImages(communityImages);
        communityImageGroupRepository.saveAndFlush(communityImageGroup);
        Long communityImagesId = communityImages.getId();

        // Get all the communityImageGroupList where communityImages equals to communityImagesId
        defaultCommunityImageGroupShouldBeFound("communityImagesId.equals=" + communityImagesId);

        // Get all the communityImageGroupList where communityImages equals to (communityImagesId + 1)
        defaultCommunityImageGroupShouldNotBeFound("communityImagesId.equals=" + (communityImagesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommunityImageGroupShouldBeFound(String filter) throws Exception {
        restCommunityImageGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityImageGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].imgGroupName").value(hasItem(DEFAULT_IMG_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));

        // Check, that the count call also returns 1
        restCommunityImageGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommunityImageGroupShouldNotBeFound(String filter) throws Exception {
        restCommunityImageGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommunityImageGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommunityImageGroup() throws Exception {
        // Get the communityImageGroup
        restCommunityImageGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommunityImageGroup() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();

        // Update the communityImageGroup
        CommunityImageGroup updatedCommunityImageGroup = communityImageGroupRepository.findById(communityImageGroup.getId()).get();
        // Disconnect from session so that the updates on updatedCommunityImageGroup are not directly saved in db
        em.detach(updatedCommunityImageGroup);
        updatedCommunityImageGroup
            .imgGroupName(UPDATED_IMG_GROUP_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityImageGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunityImageGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommunityImageGroup))
            )
            .andExpect(status().isOk());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
        CommunityImageGroup testCommunityImageGroup = communityImageGroupList.get(communityImageGroupList.size() - 1);
        assertThat(testCommunityImageGroup.getImgGroupName()).isEqualTo(UPDATED_IMG_GROUP_NAME);
        assertThat(testCommunityImageGroup.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityImageGroup.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityImageGroup.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void putNonExistingCommunityImageGroup() throws Exception {
        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();
        communityImageGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityImageGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityImageGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityImageGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunityImageGroup() throws Exception {
        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();
        communityImageGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityImageGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityImageGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunityImageGroup() throws Exception {
        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();
        communityImageGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityImageGroupMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityImageGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommunityImageGroupWithPatch() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();

        // Update the communityImageGroup using partial update
        CommunityImageGroup partialUpdatedCommunityImageGroup = new CommunityImageGroup();
        partialUpdatedCommunityImageGroup.setId(communityImageGroup.getId());

        partialUpdatedCommunityImageGroup.orderNum(UPDATED_ORDER_NUM);

        restCommunityImageGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityImageGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunityImageGroup))
            )
            .andExpect(status().isOk());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
        CommunityImageGroup testCommunityImageGroup = communityImageGroupList.get(communityImageGroupList.size() - 1);
        assertThat(testCommunityImageGroup.getImgGroupName()).isEqualTo(DEFAULT_IMG_GROUP_NAME);
        assertThat(testCommunityImageGroup.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityImageGroup.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testCommunityImageGroup.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void fullUpdateCommunityImageGroupWithPatch() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();

        // Update the communityImageGroup using partial update
        CommunityImageGroup partialUpdatedCommunityImageGroup = new CommunityImageGroup();
        partialUpdatedCommunityImageGroup.setId(communityImageGroup.getId());

        partialUpdatedCommunityImageGroup
            .imgGroupName(UPDATED_IMG_GROUP_NAME)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityImageGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityImageGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunityImageGroup))
            )
            .andExpect(status().isOk());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
        CommunityImageGroup testCommunityImageGroup = communityImageGroupList.get(communityImageGroupList.size() - 1);
        assertThat(testCommunityImageGroup.getImgGroupName()).isEqualTo(UPDATED_IMG_GROUP_NAME);
        assertThat(testCommunityImageGroup.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityImageGroup.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityImageGroup.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCommunityImageGroup() throws Exception {
        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();
        communityImageGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityImageGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityImageGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityImageGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunityImageGroup() throws Exception {
        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();
        communityImageGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityImageGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityImageGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunityImageGroup() throws Exception {
        int databaseSizeBeforeUpdate = communityImageGroupRepository.findAll().size();
        communityImageGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityImageGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityImageGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityImageGroup in the database
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommunityImageGroup() throws Exception {
        // Initialize the database
        communityImageGroupRepository.saveAndFlush(communityImageGroup);

        int databaseSizeBeforeDelete = communityImageGroupRepository.findAll().size();

        // Delete the communityImageGroup
        restCommunityImageGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, communityImageGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommunityImageGroup> communityImageGroupList = communityImageGroupRepository.findAll();
        assertThat(communityImageGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
