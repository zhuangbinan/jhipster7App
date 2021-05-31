package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CommunityImageGroup;
import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.repository.CommunityImagesRepository;
import com.mycompany.myapp.service.criteria.CommunityImagesCriteria;
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
 * Integration tests for the {@link CommunityImagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityImagesResourceIT {

    private static final byte[] DEFAULT_IMG_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMG_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMG_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_IMG_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_IMG_DESC = "AAAAAAAAAA";
    private static final String UPDATED_IMG_DESC = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_NUM = 1;
    private static final Integer UPDATED_ORDER_NUM = 2;
    private static final Integer SMALLER_ORDER_NUM = 1 - 1;

    private static final Instant DEFAULT_LAST_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFY_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFY_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/community-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommunityImagesRepository communityImagesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityImagesMockMvc;

    private CommunityImages communityImages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityImages createEntity(EntityManager em) {
        CommunityImages communityImages = new CommunityImages()
            .imgContent(DEFAULT_IMG_CONTENT)
            .imgContentContentType(DEFAULT_IMG_CONTENT_CONTENT_TYPE)
            .imgTitle(DEFAULT_IMG_TITLE)
            .imgDesc(DEFAULT_IMG_DESC)
            .orderNum(DEFAULT_ORDER_NUM)
            .lastModifyDate(DEFAULT_LAST_MODIFY_DATE)
            .lastModifyBy(DEFAULT_LAST_MODIFY_BY);
        return communityImages;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityImages createUpdatedEntity(EntityManager em) {
        CommunityImages communityImages = new CommunityImages()
            .imgContent(UPDATED_IMG_CONTENT)
            .imgContentContentType(UPDATED_IMG_CONTENT_CONTENT_TYPE)
            .imgTitle(UPDATED_IMG_TITLE)
            .imgDesc(UPDATED_IMG_DESC)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);
        return communityImages;
    }

    @BeforeEach
    public void initTest() {
        communityImages = createEntity(em);
    }

    @Test
    @Transactional
    void createCommunityImages() throws Exception {
        int databaseSizeBeforeCreate = communityImagesRepository.findAll().size();
        // Create the CommunityImages
        restCommunityImagesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityImages))
            )
            .andExpect(status().isCreated());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeCreate + 1);
        CommunityImages testCommunityImages = communityImagesList.get(communityImagesList.size() - 1);
        assertThat(testCommunityImages.getImgContent()).isEqualTo(DEFAULT_IMG_CONTENT);
        assertThat(testCommunityImages.getImgContentContentType()).isEqualTo(DEFAULT_IMG_CONTENT_CONTENT_TYPE);
        assertThat(testCommunityImages.getImgTitle()).isEqualTo(DEFAULT_IMG_TITLE);
        assertThat(testCommunityImages.getImgDesc()).isEqualTo(DEFAULT_IMG_DESC);
        assertThat(testCommunityImages.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCommunityImages.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testCommunityImages.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void createCommunityImagesWithExistingId() throws Exception {
        // Create the CommunityImages with an existing ID
        communityImages.setId(1L);

        int databaseSizeBeforeCreate = communityImagesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityImagesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityImages))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommunityImages() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList
        restCommunityImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityImages.getId().intValue())))
            .andExpect(jsonPath("$.[*].imgContentContentType").value(hasItem(DEFAULT_IMG_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imgContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_CONTENT))))
            .andExpect(jsonPath("$.[*].imgTitle").value(hasItem(DEFAULT_IMG_TITLE)))
            .andExpect(jsonPath("$.[*].imgDesc").value(hasItem(DEFAULT_IMG_DESC)))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));
    }

    @Test
    @Transactional
    void getCommunityImages() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get the communityImages
        restCommunityImagesMockMvc
            .perform(get(ENTITY_API_URL_ID, communityImages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communityImages.getId().intValue()))
            .andExpect(jsonPath("$.imgContentContentType").value(DEFAULT_IMG_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.imgContent").value(Base64Utils.encodeToString(DEFAULT_IMG_CONTENT)))
            .andExpect(jsonPath("$.imgTitle").value(DEFAULT_IMG_TITLE))
            .andExpect(jsonPath("$.imgDesc").value(DEFAULT_IMG_DESC))
            .andExpect(jsonPath("$.orderNum").value(DEFAULT_ORDER_NUM))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()))
            .andExpect(jsonPath("$.lastModifyBy").value(DEFAULT_LAST_MODIFY_BY));
    }

    @Test
    @Transactional
    void getCommunityImagesByIdFiltering() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        Long id = communityImages.getId();

        defaultCommunityImagesShouldBeFound("id.equals=" + id);
        defaultCommunityImagesShouldNotBeFound("id.notEquals=" + id);

        defaultCommunityImagesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommunityImagesShouldNotBeFound("id.greaterThan=" + id);

        defaultCommunityImagesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommunityImagesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgTitle equals to DEFAULT_IMG_TITLE
        defaultCommunityImagesShouldBeFound("imgTitle.equals=" + DEFAULT_IMG_TITLE);

        // Get all the communityImagesList where imgTitle equals to UPDATED_IMG_TITLE
        defaultCommunityImagesShouldNotBeFound("imgTitle.equals=" + UPDATED_IMG_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgTitle not equals to DEFAULT_IMG_TITLE
        defaultCommunityImagesShouldNotBeFound("imgTitle.notEquals=" + DEFAULT_IMG_TITLE);

        // Get all the communityImagesList where imgTitle not equals to UPDATED_IMG_TITLE
        defaultCommunityImagesShouldBeFound("imgTitle.notEquals=" + UPDATED_IMG_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgTitleIsInShouldWork() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgTitle in DEFAULT_IMG_TITLE or UPDATED_IMG_TITLE
        defaultCommunityImagesShouldBeFound("imgTitle.in=" + DEFAULT_IMG_TITLE + "," + UPDATED_IMG_TITLE);

        // Get all the communityImagesList where imgTitle equals to UPDATED_IMG_TITLE
        defaultCommunityImagesShouldNotBeFound("imgTitle.in=" + UPDATED_IMG_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgTitle is not null
        defaultCommunityImagesShouldBeFound("imgTitle.specified=true");

        // Get all the communityImagesList where imgTitle is null
        defaultCommunityImagesShouldNotBeFound("imgTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgTitleContainsSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgTitle contains DEFAULT_IMG_TITLE
        defaultCommunityImagesShouldBeFound("imgTitle.contains=" + DEFAULT_IMG_TITLE);

        // Get all the communityImagesList where imgTitle contains UPDATED_IMG_TITLE
        defaultCommunityImagesShouldNotBeFound("imgTitle.contains=" + UPDATED_IMG_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgTitleNotContainsSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgTitle does not contain DEFAULT_IMG_TITLE
        defaultCommunityImagesShouldNotBeFound("imgTitle.doesNotContain=" + DEFAULT_IMG_TITLE);

        // Get all the communityImagesList where imgTitle does not contain UPDATED_IMG_TITLE
        defaultCommunityImagesShouldBeFound("imgTitle.doesNotContain=" + UPDATED_IMG_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgDescIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgDesc equals to DEFAULT_IMG_DESC
        defaultCommunityImagesShouldBeFound("imgDesc.equals=" + DEFAULT_IMG_DESC);

        // Get all the communityImagesList where imgDesc equals to UPDATED_IMG_DESC
        defaultCommunityImagesShouldNotBeFound("imgDesc.equals=" + UPDATED_IMG_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgDescIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgDesc not equals to DEFAULT_IMG_DESC
        defaultCommunityImagesShouldNotBeFound("imgDesc.notEquals=" + DEFAULT_IMG_DESC);

        // Get all the communityImagesList where imgDesc not equals to UPDATED_IMG_DESC
        defaultCommunityImagesShouldBeFound("imgDesc.notEquals=" + UPDATED_IMG_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgDescIsInShouldWork() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgDesc in DEFAULT_IMG_DESC or UPDATED_IMG_DESC
        defaultCommunityImagesShouldBeFound("imgDesc.in=" + DEFAULT_IMG_DESC + "," + UPDATED_IMG_DESC);

        // Get all the communityImagesList where imgDesc equals to UPDATED_IMG_DESC
        defaultCommunityImagesShouldNotBeFound("imgDesc.in=" + UPDATED_IMG_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgDesc is not null
        defaultCommunityImagesShouldBeFound("imgDesc.specified=true");

        // Get all the communityImagesList where imgDesc is null
        defaultCommunityImagesShouldNotBeFound("imgDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgDescContainsSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgDesc contains DEFAULT_IMG_DESC
        defaultCommunityImagesShouldBeFound("imgDesc.contains=" + DEFAULT_IMG_DESC);

        // Get all the communityImagesList where imgDesc contains UPDATED_IMG_DESC
        defaultCommunityImagesShouldNotBeFound("imgDesc.contains=" + UPDATED_IMG_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByImgDescNotContainsSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where imgDesc does not contain DEFAULT_IMG_DESC
        defaultCommunityImagesShouldNotBeFound("imgDesc.doesNotContain=" + DEFAULT_IMG_DESC);

        // Get all the communityImagesList where imgDesc does not contain UPDATED_IMG_DESC
        defaultCommunityImagesShouldBeFound("imgDesc.doesNotContain=" + UPDATED_IMG_DESC);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByOrderNumIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where orderNum equals to DEFAULT_ORDER_NUM
        defaultCommunityImagesShouldBeFound("orderNum.equals=" + DEFAULT_ORDER_NUM);

        // Get all the communityImagesList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityImagesShouldNotBeFound("orderNum.equals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByOrderNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where orderNum not equals to DEFAULT_ORDER_NUM
        defaultCommunityImagesShouldNotBeFound("orderNum.notEquals=" + DEFAULT_ORDER_NUM);

        // Get all the communityImagesList where orderNum not equals to UPDATED_ORDER_NUM
        defaultCommunityImagesShouldBeFound("orderNum.notEquals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByOrderNumIsInShouldWork() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where orderNum in DEFAULT_ORDER_NUM or UPDATED_ORDER_NUM
        defaultCommunityImagesShouldBeFound("orderNum.in=" + DEFAULT_ORDER_NUM + "," + UPDATED_ORDER_NUM);

        // Get all the communityImagesList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityImagesShouldNotBeFound("orderNum.in=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByOrderNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where orderNum is not null
        defaultCommunityImagesShouldBeFound("orderNum.specified=true");

        // Get all the communityImagesList where orderNum is null
        defaultCommunityImagesShouldNotBeFound("orderNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImagesByOrderNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where orderNum is greater than or equal to DEFAULT_ORDER_NUM
        defaultCommunityImagesShouldBeFound("orderNum.greaterThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityImagesList where orderNum is greater than or equal to UPDATED_ORDER_NUM
        defaultCommunityImagesShouldNotBeFound("orderNum.greaterThanOrEqual=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByOrderNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where orderNum is less than or equal to DEFAULT_ORDER_NUM
        defaultCommunityImagesShouldBeFound("orderNum.lessThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityImagesList where orderNum is less than or equal to SMALLER_ORDER_NUM
        defaultCommunityImagesShouldNotBeFound("orderNum.lessThanOrEqual=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByOrderNumIsLessThanSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where orderNum is less than DEFAULT_ORDER_NUM
        defaultCommunityImagesShouldNotBeFound("orderNum.lessThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityImagesList where orderNum is less than UPDATED_ORDER_NUM
        defaultCommunityImagesShouldBeFound("orderNum.lessThan=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByOrderNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where orderNum is greater than DEFAULT_ORDER_NUM
        defaultCommunityImagesShouldNotBeFound("orderNum.greaterThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityImagesList where orderNum is greater than SMALLER_ORDER_NUM
        defaultCommunityImagesShouldBeFound("orderNum.greaterThan=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyDate equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityImagesShouldBeFound("lastModifyDate.equals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityImagesList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityImagesShouldNotBeFound("lastModifyDate.equals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyDate not equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityImagesShouldNotBeFound("lastModifyDate.notEquals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityImagesList where lastModifyDate not equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityImagesShouldBeFound("lastModifyDate.notEquals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyDate in DEFAULT_LAST_MODIFY_DATE or UPDATED_LAST_MODIFY_DATE
        defaultCommunityImagesShouldBeFound("lastModifyDate.in=" + DEFAULT_LAST_MODIFY_DATE + "," + UPDATED_LAST_MODIFY_DATE);

        // Get all the communityImagesList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityImagesShouldNotBeFound("lastModifyDate.in=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyDate is not null
        defaultCommunityImagesShouldBeFound("lastModifyDate.specified=true");

        // Get all the communityImagesList where lastModifyDate is null
        defaultCommunityImagesShouldNotBeFound("lastModifyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyByIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyBy equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityImagesShouldBeFound("lastModifyBy.equals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityImagesList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityImagesShouldNotBeFound("lastModifyBy.equals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyBy not equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityImagesShouldNotBeFound("lastModifyBy.notEquals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityImagesList where lastModifyBy not equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityImagesShouldBeFound("lastModifyBy.notEquals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyByIsInShouldWork() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyBy in DEFAULT_LAST_MODIFY_BY or UPDATED_LAST_MODIFY_BY
        defaultCommunityImagesShouldBeFound("lastModifyBy.in=" + DEFAULT_LAST_MODIFY_BY + "," + UPDATED_LAST_MODIFY_BY);

        // Get all the communityImagesList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityImagesShouldNotBeFound("lastModifyBy.in=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyByIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyBy is not null
        defaultCommunityImagesShouldBeFound("lastModifyBy.specified=true");

        // Get all the communityImagesList where lastModifyBy is null
        defaultCommunityImagesShouldNotBeFound("lastModifyBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyByContainsSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyBy contains DEFAULT_LAST_MODIFY_BY
        defaultCommunityImagesShouldBeFound("lastModifyBy.contains=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityImagesList where lastModifyBy contains UPDATED_LAST_MODIFY_BY
        defaultCommunityImagesShouldNotBeFound("lastModifyBy.contains=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByLastModifyByNotContainsSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        // Get all the communityImagesList where lastModifyBy does not contain DEFAULT_LAST_MODIFY_BY
        defaultCommunityImagesShouldNotBeFound("lastModifyBy.doesNotContain=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityImagesList where lastModifyBy does not contain UPDATED_LAST_MODIFY_BY
        defaultCommunityImagesShouldBeFound("lastModifyBy.doesNotContain=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityImagesByCommunityImageGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);
        CommunityImageGroup communityImageGroup = CommunityImageGroupResourceIT.createEntity(em);
        em.persist(communityImageGroup);
        em.flush();
        communityImages.setCommunityImageGroup(communityImageGroup);
        communityImagesRepository.saveAndFlush(communityImages);
        Long communityImageGroupId = communityImageGroup.getId();

        // Get all the communityImagesList where communityImageGroup equals to communityImageGroupId
        defaultCommunityImagesShouldBeFound("communityImageGroupId.equals=" + communityImageGroupId);

        // Get all the communityImagesList where communityImageGroup equals to (communityImageGroupId + 1)
        defaultCommunityImagesShouldNotBeFound("communityImageGroupId.equals=" + (communityImageGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommunityImagesShouldBeFound(String filter) throws Exception {
        restCommunityImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityImages.getId().intValue())))
            .andExpect(jsonPath("$.[*].imgContentContentType").value(hasItem(DEFAULT_IMG_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imgContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_CONTENT))))
            .andExpect(jsonPath("$.[*].imgTitle").value(hasItem(DEFAULT_IMG_TITLE)))
            .andExpect(jsonPath("$.[*].imgDesc").value(hasItem(DEFAULT_IMG_DESC)))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));

        // Check, that the count call also returns 1
        restCommunityImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommunityImagesShouldNotBeFound(String filter) throws Exception {
        restCommunityImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommunityImagesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommunityImages() throws Exception {
        // Get the communityImages
        restCommunityImagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommunityImages() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();

        // Update the communityImages
        CommunityImages updatedCommunityImages = communityImagesRepository.findById(communityImages.getId()).get();
        // Disconnect from session so that the updates on updatedCommunityImages are not directly saved in db
        em.detach(updatedCommunityImages);
        updatedCommunityImages
            .imgContent(UPDATED_IMG_CONTENT)
            .imgContentContentType(UPDATED_IMG_CONTENT_CONTENT_TYPE)
            .imgTitle(UPDATED_IMG_TITLE)
            .imgDesc(UPDATED_IMG_DESC)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunityImages.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommunityImages))
            )
            .andExpect(status().isOk());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
        CommunityImages testCommunityImages = communityImagesList.get(communityImagesList.size() - 1);
        assertThat(testCommunityImages.getImgContent()).isEqualTo(UPDATED_IMG_CONTENT);
        assertThat(testCommunityImages.getImgContentContentType()).isEqualTo(UPDATED_IMG_CONTENT_CONTENT_TYPE);
        assertThat(testCommunityImages.getImgTitle()).isEqualTo(UPDATED_IMG_TITLE);
        assertThat(testCommunityImages.getImgDesc()).isEqualTo(UPDATED_IMG_DESC);
        assertThat(testCommunityImages.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityImages.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityImages.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void putNonExistingCommunityImages() throws Exception {
        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();
        communityImages.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityImages.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityImages))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunityImages() throws Exception {
        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();
        communityImages.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityImages))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunityImages() throws Exception {
        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();
        communityImages.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityImagesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityImages))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommunityImagesWithPatch() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();

        // Update the communityImages using partial update
        CommunityImages partialUpdatedCommunityImages = new CommunityImages();
        partialUpdatedCommunityImages.setId(communityImages.getId());

        partialUpdatedCommunityImages
            .imgContent(UPDATED_IMG_CONTENT)
            .imgContentContentType(UPDATED_IMG_CONTENT_CONTENT_TYPE)
            .imgTitle(UPDATED_IMG_TITLE)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunityImages))
            )
            .andExpect(status().isOk());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
        CommunityImages testCommunityImages = communityImagesList.get(communityImagesList.size() - 1);
        assertThat(testCommunityImages.getImgContent()).isEqualTo(UPDATED_IMG_CONTENT);
        assertThat(testCommunityImages.getImgContentContentType()).isEqualTo(UPDATED_IMG_CONTENT_CONTENT_TYPE);
        assertThat(testCommunityImages.getImgTitle()).isEqualTo(UPDATED_IMG_TITLE);
        assertThat(testCommunityImages.getImgDesc()).isEqualTo(DEFAULT_IMG_DESC);
        assertThat(testCommunityImages.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCommunityImages.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityImages.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void fullUpdateCommunityImagesWithPatch() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();

        // Update the communityImages using partial update
        CommunityImages partialUpdatedCommunityImages = new CommunityImages();
        partialUpdatedCommunityImages.setId(communityImages.getId());

        partialUpdatedCommunityImages
            .imgContent(UPDATED_IMG_CONTENT)
            .imgContentContentType(UPDATED_IMG_CONTENT_CONTENT_TYPE)
            .imgTitle(UPDATED_IMG_TITLE)
            .imgDesc(UPDATED_IMG_DESC)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunityImages))
            )
            .andExpect(status().isOk());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
        CommunityImages testCommunityImages = communityImagesList.get(communityImagesList.size() - 1);
        assertThat(testCommunityImages.getImgContent()).isEqualTo(UPDATED_IMG_CONTENT);
        assertThat(testCommunityImages.getImgContentContentType()).isEqualTo(UPDATED_IMG_CONTENT_CONTENT_TYPE);
        assertThat(testCommunityImages.getImgTitle()).isEqualTo(UPDATED_IMG_TITLE);
        assertThat(testCommunityImages.getImgDesc()).isEqualTo(UPDATED_IMG_DESC);
        assertThat(testCommunityImages.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityImages.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityImages.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCommunityImages() throws Exception {
        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();
        communityImages.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityImages))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunityImages() throws Exception {
        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();
        communityImages.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityImages))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunityImages() throws Exception {
        int databaseSizeBeforeUpdate = communityImagesRepository.findAll().size();
        communityImages.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityImagesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityImages))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityImages in the database
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommunityImages() throws Exception {
        // Initialize the database
        communityImagesRepository.saveAndFlush(communityImages);

        int databaseSizeBeforeDelete = communityImagesRepository.findAll().size();

        // Delete the communityImages
        restCommunityImagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, communityImages.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommunityImages> communityImagesList = communityImagesRepository.findAll();
        assertThat(communityImagesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
