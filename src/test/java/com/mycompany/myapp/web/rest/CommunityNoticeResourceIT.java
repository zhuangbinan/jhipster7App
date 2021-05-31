package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Community;
import com.mycompany.myapp.domain.CommunityNotice;
import com.mycompany.myapp.repository.CommunityNoticeRepository;
import com.mycompany.myapp.service.criteria.CommunityNoticeCriteria;
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
 * Integration tests for the {@link CommunityNoticeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommunityNoticeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMG_1 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG_1 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMG_1_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_1_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMG_1_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_IMG_1_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_1 = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_1 = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMG_2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG_2 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMG_2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_2_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMG_2_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_IMG_2_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_2 = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_2 = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMG_3 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG_3 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMG_3_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_3_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMG_3_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_IMG_3_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_3 = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_3 = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMG_4 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG_4 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMG_4_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_4_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMG_4_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_IMG_4_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_4 = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_4 = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMG_5 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG_5 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMG_5_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_5_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMG_5_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_IMG_5_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_5 = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_5 = "BBBBBBBBBB";

    private static final String DEFAULT_TAIL = "AAAAAAAAAA";
    private static final String UPDATED_TAIL = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/community-notices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommunityNoticeRepository communityNoticeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommunityNoticeMockMvc;

    private CommunityNotice communityNotice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityNotice createEntity(EntityManager em) {
        CommunityNotice communityNotice = new CommunityNotice()
            .title(DEFAULT_TITLE)
            .img1(DEFAULT_IMG_1)
            .img1ContentType(DEFAULT_IMG_1_CONTENT_TYPE)
            .img1Title(DEFAULT_IMG_1_TITLE)
            .content1(DEFAULT_CONTENT_1)
            .img2(DEFAULT_IMG_2)
            .img2ContentType(DEFAULT_IMG_2_CONTENT_TYPE)
            .img2Title(DEFAULT_IMG_2_TITLE)
            .content2(DEFAULT_CONTENT_2)
            .img3(DEFAULT_IMG_3)
            .img3ContentType(DEFAULT_IMG_3_CONTENT_TYPE)
            .img3Title(DEFAULT_IMG_3_TITLE)
            .content3(DEFAULT_CONTENT_3)
            .img4(DEFAULT_IMG_4)
            .img4ContentType(DEFAULT_IMG_4_CONTENT_TYPE)
            .img4Title(DEFAULT_IMG_4_TITLE)
            .content4(DEFAULT_CONTENT_4)
            .img5(DEFAULT_IMG_5)
            .img5ContentType(DEFAULT_IMG_5_CONTENT_TYPE)
            .img5Title(DEFAULT_IMG_5_TITLE)
            .content5(DEFAULT_CONTENT_5)
            .tail(DEFAULT_TAIL)
            .enable(DEFAULT_ENABLE)
            .delFlag(DEFAULT_DEL_FLAG)
            .orderNum(DEFAULT_ORDER_NUM)
            .lastModifyDate(DEFAULT_LAST_MODIFY_DATE)
            .lastModifyBy(DEFAULT_LAST_MODIFY_BY);
        return communityNotice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommunityNotice createUpdatedEntity(EntityManager em) {
        CommunityNotice communityNotice = new CommunityNotice()
            .title(UPDATED_TITLE)
            .img1(UPDATED_IMG_1)
            .img1ContentType(UPDATED_IMG_1_CONTENT_TYPE)
            .img1Title(UPDATED_IMG_1_TITLE)
            .content1(UPDATED_CONTENT_1)
            .img2(UPDATED_IMG_2)
            .img2ContentType(UPDATED_IMG_2_CONTENT_TYPE)
            .img2Title(UPDATED_IMG_2_TITLE)
            .content2(UPDATED_CONTENT_2)
            .img3(UPDATED_IMG_3)
            .img3ContentType(UPDATED_IMG_3_CONTENT_TYPE)
            .img3Title(UPDATED_IMG_3_TITLE)
            .content3(UPDATED_CONTENT_3)
            .img4(UPDATED_IMG_4)
            .img4ContentType(UPDATED_IMG_4_CONTENT_TYPE)
            .img4Title(UPDATED_IMG_4_TITLE)
            .content4(UPDATED_CONTENT_4)
            .img5(UPDATED_IMG_5)
            .img5ContentType(UPDATED_IMG_5_CONTENT_TYPE)
            .img5Title(UPDATED_IMG_5_TITLE)
            .content5(UPDATED_CONTENT_5)
            .tail(UPDATED_TAIL)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);
        return communityNotice;
    }

    @BeforeEach
    public void initTest() {
        communityNotice = createEntity(em);
    }

    @Test
    @Transactional
    void createCommunityNotice() throws Exception {
        int databaseSizeBeforeCreate = communityNoticeRepository.findAll().size();
        // Create the CommunityNotice
        restCommunityNoticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityNotice))
            )
            .andExpect(status().isCreated());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeCreate + 1);
        CommunityNotice testCommunityNotice = communityNoticeList.get(communityNoticeList.size() - 1);
        assertThat(testCommunityNotice.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCommunityNotice.getImg1()).isEqualTo(DEFAULT_IMG_1);
        assertThat(testCommunityNotice.getImg1ContentType()).isEqualTo(DEFAULT_IMG_1_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg1Title()).isEqualTo(DEFAULT_IMG_1_TITLE);
        assertThat(testCommunityNotice.getContent1()).isEqualTo(DEFAULT_CONTENT_1);
        assertThat(testCommunityNotice.getImg2()).isEqualTo(DEFAULT_IMG_2);
        assertThat(testCommunityNotice.getImg2ContentType()).isEqualTo(DEFAULT_IMG_2_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg2Title()).isEqualTo(DEFAULT_IMG_2_TITLE);
        assertThat(testCommunityNotice.getContent2()).isEqualTo(DEFAULT_CONTENT_2);
        assertThat(testCommunityNotice.getImg3()).isEqualTo(DEFAULT_IMG_3);
        assertThat(testCommunityNotice.getImg3ContentType()).isEqualTo(DEFAULT_IMG_3_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg3Title()).isEqualTo(DEFAULT_IMG_3_TITLE);
        assertThat(testCommunityNotice.getContent3()).isEqualTo(DEFAULT_CONTENT_3);
        assertThat(testCommunityNotice.getImg4()).isEqualTo(DEFAULT_IMG_4);
        assertThat(testCommunityNotice.getImg4ContentType()).isEqualTo(DEFAULT_IMG_4_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg4Title()).isEqualTo(DEFAULT_IMG_4_TITLE);
        assertThat(testCommunityNotice.getContent4()).isEqualTo(DEFAULT_CONTENT_4);
        assertThat(testCommunityNotice.getImg5()).isEqualTo(DEFAULT_IMG_5);
        assertThat(testCommunityNotice.getImg5ContentType()).isEqualTo(DEFAULT_IMG_5_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg5Title()).isEqualTo(DEFAULT_IMG_5_TITLE);
        assertThat(testCommunityNotice.getContent5()).isEqualTo(DEFAULT_CONTENT_5);
        assertThat(testCommunityNotice.getTail()).isEqualTo(DEFAULT_TAIL);
        assertThat(testCommunityNotice.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCommunityNotice.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCommunityNotice.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(testCommunityNotice.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testCommunityNotice.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void createCommunityNoticeWithExistingId() throws Exception {
        // Create the CommunityNotice with an existing ID
        communityNotice.setId(1L);

        int databaseSizeBeforeCreate = communityNoticeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommunityNoticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityNotice))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommunityNotices() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList
        restCommunityNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].img1ContentType").value(hasItem(DEFAULT_IMG_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img1").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_1))))
            .andExpect(jsonPath("$.[*].img1Title").value(hasItem(DEFAULT_IMG_1_TITLE)))
            .andExpect(jsonPath("$.[*].content1").value(hasItem(DEFAULT_CONTENT_1.toString())))
            .andExpect(jsonPath("$.[*].img2ContentType").value(hasItem(DEFAULT_IMG_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img2").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_2))))
            .andExpect(jsonPath("$.[*].img2Title").value(hasItem(DEFAULT_IMG_2_TITLE)))
            .andExpect(jsonPath("$.[*].content2").value(hasItem(DEFAULT_CONTENT_2.toString())))
            .andExpect(jsonPath("$.[*].img3ContentType").value(hasItem(DEFAULT_IMG_3_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img3").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_3))))
            .andExpect(jsonPath("$.[*].img3Title").value(hasItem(DEFAULT_IMG_3_TITLE)))
            .andExpect(jsonPath("$.[*].content3").value(hasItem(DEFAULT_CONTENT_3.toString())))
            .andExpect(jsonPath("$.[*].img4ContentType").value(hasItem(DEFAULT_IMG_4_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img4").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_4))))
            .andExpect(jsonPath("$.[*].img4Title").value(hasItem(DEFAULT_IMG_4_TITLE)))
            .andExpect(jsonPath("$.[*].content4").value(hasItem(DEFAULT_CONTENT_4.toString())))
            .andExpect(jsonPath("$.[*].img5ContentType").value(hasItem(DEFAULT_IMG_5_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img5").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_5))))
            .andExpect(jsonPath("$.[*].img5Title").value(hasItem(DEFAULT_IMG_5_TITLE)))
            .andExpect(jsonPath("$.[*].content5").value(hasItem(DEFAULT_CONTENT_5.toString())))
            .andExpect(jsonPath("$.[*].tail").value(hasItem(DEFAULT_TAIL)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));
    }

    @Test
    @Transactional
    void getCommunityNotice() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get the communityNotice
        restCommunityNoticeMockMvc
            .perform(get(ENTITY_API_URL_ID, communityNotice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(communityNotice.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.img1ContentType").value(DEFAULT_IMG_1_CONTENT_TYPE))
            .andExpect(jsonPath("$.img1").value(Base64Utils.encodeToString(DEFAULT_IMG_1)))
            .andExpect(jsonPath("$.img1Title").value(DEFAULT_IMG_1_TITLE))
            .andExpect(jsonPath("$.content1").value(DEFAULT_CONTENT_1.toString()))
            .andExpect(jsonPath("$.img2ContentType").value(DEFAULT_IMG_2_CONTENT_TYPE))
            .andExpect(jsonPath("$.img2").value(Base64Utils.encodeToString(DEFAULT_IMG_2)))
            .andExpect(jsonPath("$.img2Title").value(DEFAULT_IMG_2_TITLE))
            .andExpect(jsonPath("$.content2").value(DEFAULT_CONTENT_2.toString()))
            .andExpect(jsonPath("$.img3ContentType").value(DEFAULT_IMG_3_CONTENT_TYPE))
            .andExpect(jsonPath("$.img3").value(Base64Utils.encodeToString(DEFAULT_IMG_3)))
            .andExpect(jsonPath("$.img3Title").value(DEFAULT_IMG_3_TITLE))
            .andExpect(jsonPath("$.content3").value(DEFAULT_CONTENT_3.toString()))
            .andExpect(jsonPath("$.img4ContentType").value(DEFAULT_IMG_4_CONTENT_TYPE))
            .andExpect(jsonPath("$.img4").value(Base64Utils.encodeToString(DEFAULT_IMG_4)))
            .andExpect(jsonPath("$.img4Title").value(DEFAULT_IMG_4_TITLE))
            .andExpect(jsonPath("$.content4").value(DEFAULT_CONTENT_4.toString()))
            .andExpect(jsonPath("$.img5ContentType").value(DEFAULT_IMG_5_CONTENT_TYPE))
            .andExpect(jsonPath("$.img5").value(Base64Utils.encodeToString(DEFAULT_IMG_5)))
            .andExpect(jsonPath("$.img5Title").value(DEFAULT_IMG_5_TITLE))
            .andExpect(jsonPath("$.content5").value(DEFAULT_CONTENT_5.toString()))
            .andExpect(jsonPath("$.tail").value(DEFAULT_TAIL))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.booleanValue()))
            .andExpect(jsonPath("$.orderNum").value(DEFAULT_ORDER_NUM))
            .andExpect(jsonPath("$.lastModifyDate").value(DEFAULT_LAST_MODIFY_DATE.toString()))
            .andExpect(jsonPath("$.lastModifyBy").value(DEFAULT_LAST_MODIFY_BY));
    }

    @Test
    @Transactional
    void getCommunityNoticesByIdFiltering() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        Long id = communityNotice.getId();

        defaultCommunityNoticeShouldBeFound("id.equals=" + id);
        defaultCommunityNoticeShouldNotBeFound("id.notEquals=" + id);

        defaultCommunityNoticeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommunityNoticeShouldNotBeFound("id.greaterThan=" + id);

        defaultCommunityNoticeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommunityNoticeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where title equals to DEFAULT_TITLE
        defaultCommunityNoticeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the communityNoticeList where title equals to UPDATED_TITLE
        defaultCommunityNoticeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where title not equals to DEFAULT_TITLE
        defaultCommunityNoticeShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the communityNoticeList where title not equals to UPDATED_TITLE
        defaultCommunityNoticeShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCommunityNoticeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the communityNoticeList where title equals to UPDATED_TITLE
        defaultCommunityNoticeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where title is not null
        defaultCommunityNoticeShouldBeFound("title.specified=true");

        // Get all the communityNoticeList where title is null
        defaultCommunityNoticeShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTitleContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where title contains DEFAULT_TITLE
        defaultCommunityNoticeShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the communityNoticeList where title contains UPDATED_TITLE
        defaultCommunityNoticeShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where title does not contain DEFAULT_TITLE
        defaultCommunityNoticeShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the communityNoticeList where title does not contain UPDATED_TITLE
        defaultCommunityNoticeShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg1TitleIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img1Title equals to DEFAULT_IMG_1_TITLE
        defaultCommunityNoticeShouldBeFound("img1Title.equals=" + DEFAULT_IMG_1_TITLE);

        // Get all the communityNoticeList where img1Title equals to UPDATED_IMG_1_TITLE
        defaultCommunityNoticeShouldNotBeFound("img1Title.equals=" + UPDATED_IMG_1_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg1TitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img1Title not equals to DEFAULT_IMG_1_TITLE
        defaultCommunityNoticeShouldNotBeFound("img1Title.notEquals=" + DEFAULT_IMG_1_TITLE);

        // Get all the communityNoticeList where img1Title not equals to UPDATED_IMG_1_TITLE
        defaultCommunityNoticeShouldBeFound("img1Title.notEquals=" + UPDATED_IMG_1_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg1TitleIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img1Title in DEFAULT_IMG_1_TITLE or UPDATED_IMG_1_TITLE
        defaultCommunityNoticeShouldBeFound("img1Title.in=" + DEFAULT_IMG_1_TITLE + "," + UPDATED_IMG_1_TITLE);

        // Get all the communityNoticeList where img1Title equals to UPDATED_IMG_1_TITLE
        defaultCommunityNoticeShouldNotBeFound("img1Title.in=" + UPDATED_IMG_1_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg1TitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img1Title is not null
        defaultCommunityNoticeShouldBeFound("img1Title.specified=true");

        // Get all the communityNoticeList where img1Title is null
        defaultCommunityNoticeShouldNotBeFound("img1Title.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg1TitleContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img1Title contains DEFAULT_IMG_1_TITLE
        defaultCommunityNoticeShouldBeFound("img1Title.contains=" + DEFAULT_IMG_1_TITLE);

        // Get all the communityNoticeList where img1Title contains UPDATED_IMG_1_TITLE
        defaultCommunityNoticeShouldNotBeFound("img1Title.contains=" + UPDATED_IMG_1_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg1TitleNotContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img1Title does not contain DEFAULT_IMG_1_TITLE
        defaultCommunityNoticeShouldNotBeFound("img1Title.doesNotContain=" + DEFAULT_IMG_1_TITLE);

        // Get all the communityNoticeList where img1Title does not contain UPDATED_IMG_1_TITLE
        defaultCommunityNoticeShouldBeFound("img1Title.doesNotContain=" + UPDATED_IMG_1_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg2TitleIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img2Title equals to DEFAULT_IMG_2_TITLE
        defaultCommunityNoticeShouldBeFound("img2Title.equals=" + DEFAULT_IMG_2_TITLE);

        // Get all the communityNoticeList where img2Title equals to UPDATED_IMG_2_TITLE
        defaultCommunityNoticeShouldNotBeFound("img2Title.equals=" + UPDATED_IMG_2_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg2TitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img2Title not equals to DEFAULT_IMG_2_TITLE
        defaultCommunityNoticeShouldNotBeFound("img2Title.notEquals=" + DEFAULT_IMG_2_TITLE);

        // Get all the communityNoticeList where img2Title not equals to UPDATED_IMG_2_TITLE
        defaultCommunityNoticeShouldBeFound("img2Title.notEquals=" + UPDATED_IMG_2_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg2TitleIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img2Title in DEFAULT_IMG_2_TITLE or UPDATED_IMG_2_TITLE
        defaultCommunityNoticeShouldBeFound("img2Title.in=" + DEFAULT_IMG_2_TITLE + "," + UPDATED_IMG_2_TITLE);

        // Get all the communityNoticeList where img2Title equals to UPDATED_IMG_2_TITLE
        defaultCommunityNoticeShouldNotBeFound("img2Title.in=" + UPDATED_IMG_2_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg2TitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img2Title is not null
        defaultCommunityNoticeShouldBeFound("img2Title.specified=true");

        // Get all the communityNoticeList where img2Title is null
        defaultCommunityNoticeShouldNotBeFound("img2Title.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg2TitleContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img2Title contains DEFAULT_IMG_2_TITLE
        defaultCommunityNoticeShouldBeFound("img2Title.contains=" + DEFAULT_IMG_2_TITLE);

        // Get all the communityNoticeList where img2Title contains UPDATED_IMG_2_TITLE
        defaultCommunityNoticeShouldNotBeFound("img2Title.contains=" + UPDATED_IMG_2_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg2TitleNotContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img2Title does not contain DEFAULT_IMG_2_TITLE
        defaultCommunityNoticeShouldNotBeFound("img2Title.doesNotContain=" + DEFAULT_IMG_2_TITLE);

        // Get all the communityNoticeList where img2Title does not contain UPDATED_IMG_2_TITLE
        defaultCommunityNoticeShouldBeFound("img2Title.doesNotContain=" + UPDATED_IMG_2_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg3TitleIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img3Title equals to DEFAULT_IMG_3_TITLE
        defaultCommunityNoticeShouldBeFound("img3Title.equals=" + DEFAULT_IMG_3_TITLE);

        // Get all the communityNoticeList where img3Title equals to UPDATED_IMG_3_TITLE
        defaultCommunityNoticeShouldNotBeFound("img3Title.equals=" + UPDATED_IMG_3_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg3TitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img3Title not equals to DEFAULT_IMG_3_TITLE
        defaultCommunityNoticeShouldNotBeFound("img3Title.notEquals=" + DEFAULT_IMG_3_TITLE);

        // Get all the communityNoticeList where img3Title not equals to UPDATED_IMG_3_TITLE
        defaultCommunityNoticeShouldBeFound("img3Title.notEquals=" + UPDATED_IMG_3_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg3TitleIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img3Title in DEFAULT_IMG_3_TITLE or UPDATED_IMG_3_TITLE
        defaultCommunityNoticeShouldBeFound("img3Title.in=" + DEFAULT_IMG_3_TITLE + "," + UPDATED_IMG_3_TITLE);

        // Get all the communityNoticeList where img3Title equals to UPDATED_IMG_3_TITLE
        defaultCommunityNoticeShouldNotBeFound("img3Title.in=" + UPDATED_IMG_3_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg3TitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img3Title is not null
        defaultCommunityNoticeShouldBeFound("img3Title.specified=true");

        // Get all the communityNoticeList where img3Title is null
        defaultCommunityNoticeShouldNotBeFound("img3Title.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg3TitleContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img3Title contains DEFAULT_IMG_3_TITLE
        defaultCommunityNoticeShouldBeFound("img3Title.contains=" + DEFAULT_IMG_3_TITLE);

        // Get all the communityNoticeList where img3Title contains UPDATED_IMG_3_TITLE
        defaultCommunityNoticeShouldNotBeFound("img3Title.contains=" + UPDATED_IMG_3_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg3TitleNotContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img3Title does not contain DEFAULT_IMG_3_TITLE
        defaultCommunityNoticeShouldNotBeFound("img3Title.doesNotContain=" + DEFAULT_IMG_3_TITLE);

        // Get all the communityNoticeList where img3Title does not contain UPDATED_IMG_3_TITLE
        defaultCommunityNoticeShouldBeFound("img3Title.doesNotContain=" + UPDATED_IMG_3_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg4TitleIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img4Title equals to DEFAULT_IMG_4_TITLE
        defaultCommunityNoticeShouldBeFound("img4Title.equals=" + DEFAULT_IMG_4_TITLE);

        // Get all the communityNoticeList where img4Title equals to UPDATED_IMG_4_TITLE
        defaultCommunityNoticeShouldNotBeFound("img4Title.equals=" + UPDATED_IMG_4_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg4TitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img4Title not equals to DEFAULT_IMG_4_TITLE
        defaultCommunityNoticeShouldNotBeFound("img4Title.notEquals=" + DEFAULT_IMG_4_TITLE);

        // Get all the communityNoticeList where img4Title not equals to UPDATED_IMG_4_TITLE
        defaultCommunityNoticeShouldBeFound("img4Title.notEquals=" + UPDATED_IMG_4_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg4TitleIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img4Title in DEFAULT_IMG_4_TITLE or UPDATED_IMG_4_TITLE
        defaultCommunityNoticeShouldBeFound("img4Title.in=" + DEFAULT_IMG_4_TITLE + "," + UPDATED_IMG_4_TITLE);

        // Get all the communityNoticeList where img4Title equals to UPDATED_IMG_4_TITLE
        defaultCommunityNoticeShouldNotBeFound("img4Title.in=" + UPDATED_IMG_4_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg4TitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img4Title is not null
        defaultCommunityNoticeShouldBeFound("img4Title.specified=true");

        // Get all the communityNoticeList where img4Title is null
        defaultCommunityNoticeShouldNotBeFound("img4Title.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg4TitleContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img4Title contains DEFAULT_IMG_4_TITLE
        defaultCommunityNoticeShouldBeFound("img4Title.contains=" + DEFAULT_IMG_4_TITLE);

        // Get all the communityNoticeList where img4Title contains UPDATED_IMG_4_TITLE
        defaultCommunityNoticeShouldNotBeFound("img4Title.contains=" + UPDATED_IMG_4_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg4TitleNotContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img4Title does not contain DEFAULT_IMG_4_TITLE
        defaultCommunityNoticeShouldNotBeFound("img4Title.doesNotContain=" + DEFAULT_IMG_4_TITLE);

        // Get all the communityNoticeList where img4Title does not contain UPDATED_IMG_4_TITLE
        defaultCommunityNoticeShouldBeFound("img4Title.doesNotContain=" + UPDATED_IMG_4_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg5TitleIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img5Title equals to DEFAULT_IMG_5_TITLE
        defaultCommunityNoticeShouldBeFound("img5Title.equals=" + DEFAULT_IMG_5_TITLE);

        // Get all the communityNoticeList where img5Title equals to UPDATED_IMG_5_TITLE
        defaultCommunityNoticeShouldNotBeFound("img5Title.equals=" + UPDATED_IMG_5_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg5TitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img5Title not equals to DEFAULT_IMG_5_TITLE
        defaultCommunityNoticeShouldNotBeFound("img5Title.notEquals=" + DEFAULT_IMG_5_TITLE);

        // Get all the communityNoticeList where img5Title not equals to UPDATED_IMG_5_TITLE
        defaultCommunityNoticeShouldBeFound("img5Title.notEquals=" + UPDATED_IMG_5_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg5TitleIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img5Title in DEFAULT_IMG_5_TITLE or UPDATED_IMG_5_TITLE
        defaultCommunityNoticeShouldBeFound("img5Title.in=" + DEFAULT_IMG_5_TITLE + "," + UPDATED_IMG_5_TITLE);

        // Get all the communityNoticeList where img5Title equals to UPDATED_IMG_5_TITLE
        defaultCommunityNoticeShouldNotBeFound("img5Title.in=" + UPDATED_IMG_5_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg5TitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img5Title is not null
        defaultCommunityNoticeShouldBeFound("img5Title.specified=true");

        // Get all the communityNoticeList where img5Title is null
        defaultCommunityNoticeShouldNotBeFound("img5Title.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg5TitleContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img5Title contains DEFAULT_IMG_5_TITLE
        defaultCommunityNoticeShouldBeFound("img5Title.contains=" + DEFAULT_IMG_5_TITLE);

        // Get all the communityNoticeList where img5Title contains UPDATED_IMG_5_TITLE
        defaultCommunityNoticeShouldNotBeFound("img5Title.contains=" + UPDATED_IMG_5_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByImg5TitleNotContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where img5Title does not contain DEFAULT_IMG_5_TITLE
        defaultCommunityNoticeShouldNotBeFound("img5Title.doesNotContain=" + DEFAULT_IMG_5_TITLE);

        // Get all the communityNoticeList where img5Title does not contain UPDATED_IMG_5_TITLE
        defaultCommunityNoticeShouldBeFound("img5Title.doesNotContain=" + UPDATED_IMG_5_TITLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTailIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where tail equals to DEFAULT_TAIL
        defaultCommunityNoticeShouldBeFound("tail.equals=" + DEFAULT_TAIL);

        // Get all the communityNoticeList where tail equals to UPDATED_TAIL
        defaultCommunityNoticeShouldNotBeFound("tail.equals=" + UPDATED_TAIL);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where tail not equals to DEFAULT_TAIL
        defaultCommunityNoticeShouldNotBeFound("tail.notEquals=" + DEFAULT_TAIL);

        // Get all the communityNoticeList where tail not equals to UPDATED_TAIL
        defaultCommunityNoticeShouldBeFound("tail.notEquals=" + UPDATED_TAIL);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTailIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where tail in DEFAULT_TAIL or UPDATED_TAIL
        defaultCommunityNoticeShouldBeFound("tail.in=" + DEFAULT_TAIL + "," + UPDATED_TAIL);

        // Get all the communityNoticeList where tail equals to UPDATED_TAIL
        defaultCommunityNoticeShouldNotBeFound("tail.in=" + UPDATED_TAIL);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTailIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where tail is not null
        defaultCommunityNoticeShouldBeFound("tail.specified=true");

        // Get all the communityNoticeList where tail is null
        defaultCommunityNoticeShouldNotBeFound("tail.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTailContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where tail contains DEFAULT_TAIL
        defaultCommunityNoticeShouldBeFound("tail.contains=" + DEFAULT_TAIL);

        // Get all the communityNoticeList where tail contains UPDATED_TAIL
        defaultCommunityNoticeShouldNotBeFound("tail.contains=" + UPDATED_TAIL);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByTailNotContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where tail does not contain DEFAULT_TAIL
        defaultCommunityNoticeShouldNotBeFound("tail.doesNotContain=" + DEFAULT_TAIL);

        // Get all the communityNoticeList where tail does not contain UPDATED_TAIL
        defaultCommunityNoticeShouldBeFound("tail.doesNotContain=" + UPDATED_TAIL);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByEnableIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where enable equals to DEFAULT_ENABLE
        defaultCommunityNoticeShouldBeFound("enable.equals=" + DEFAULT_ENABLE);

        // Get all the communityNoticeList where enable equals to UPDATED_ENABLE
        defaultCommunityNoticeShouldNotBeFound("enable.equals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByEnableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where enable not equals to DEFAULT_ENABLE
        defaultCommunityNoticeShouldNotBeFound("enable.notEquals=" + DEFAULT_ENABLE);

        // Get all the communityNoticeList where enable not equals to UPDATED_ENABLE
        defaultCommunityNoticeShouldBeFound("enable.notEquals=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByEnableIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where enable in DEFAULT_ENABLE or UPDATED_ENABLE
        defaultCommunityNoticeShouldBeFound("enable.in=" + DEFAULT_ENABLE + "," + UPDATED_ENABLE);

        // Get all the communityNoticeList where enable equals to UPDATED_ENABLE
        defaultCommunityNoticeShouldNotBeFound("enable.in=" + UPDATED_ENABLE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByEnableIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where enable is not null
        defaultCommunityNoticeShouldBeFound("enable.specified=true");

        // Get all the communityNoticeList where enable is null
        defaultCommunityNoticeShouldNotBeFound("enable.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByDelFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where delFlag equals to DEFAULT_DEL_FLAG
        defaultCommunityNoticeShouldBeFound("delFlag.equals=" + DEFAULT_DEL_FLAG);

        // Get all the communityNoticeList where delFlag equals to UPDATED_DEL_FLAG
        defaultCommunityNoticeShouldNotBeFound("delFlag.equals=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByDelFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where delFlag not equals to DEFAULT_DEL_FLAG
        defaultCommunityNoticeShouldNotBeFound("delFlag.notEquals=" + DEFAULT_DEL_FLAG);

        // Get all the communityNoticeList where delFlag not equals to UPDATED_DEL_FLAG
        defaultCommunityNoticeShouldBeFound("delFlag.notEquals=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByDelFlagIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where delFlag in DEFAULT_DEL_FLAG or UPDATED_DEL_FLAG
        defaultCommunityNoticeShouldBeFound("delFlag.in=" + DEFAULT_DEL_FLAG + "," + UPDATED_DEL_FLAG);

        // Get all the communityNoticeList where delFlag equals to UPDATED_DEL_FLAG
        defaultCommunityNoticeShouldNotBeFound("delFlag.in=" + UPDATED_DEL_FLAG);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByDelFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where delFlag is not null
        defaultCommunityNoticeShouldBeFound("delFlag.specified=true");

        // Get all the communityNoticeList where delFlag is null
        defaultCommunityNoticeShouldNotBeFound("delFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByOrderNumIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where orderNum equals to DEFAULT_ORDER_NUM
        defaultCommunityNoticeShouldBeFound("orderNum.equals=" + DEFAULT_ORDER_NUM);

        // Get all the communityNoticeList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityNoticeShouldNotBeFound("orderNum.equals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByOrderNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where orderNum not equals to DEFAULT_ORDER_NUM
        defaultCommunityNoticeShouldNotBeFound("orderNum.notEquals=" + DEFAULT_ORDER_NUM);

        // Get all the communityNoticeList where orderNum not equals to UPDATED_ORDER_NUM
        defaultCommunityNoticeShouldBeFound("orderNum.notEquals=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByOrderNumIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where orderNum in DEFAULT_ORDER_NUM or UPDATED_ORDER_NUM
        defaultCommunityNoticeShouldBeFound("orderNum.in=" + DEFAULT_ORDER_NUM + "," + UPDATED_ORDER_NUM);

        // Get all the communityNoticeList where orderNum equals to UPDATED_ORDER_NUM
        defaultCommunityNoticeShouldNotBeFound("orderNum.in=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByOrderNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where orderNum is not null
        defaultCommunityNoticeShouldBeFound("orderNum.specified=true");

        // Get all the communityNoticeList where orderNum is null
        defaultCommunityNoticeShouldNotBeFound("orderNum.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByOrderNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where orderNum is greater than or equal to DEFAULT_ORDER_NUM
        defaultCommunityNoticeShouldBeFound("orderNum.greaterThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityNoticeList where orderNum is greater than or equal to UPDATED_ORDER_NUM
        defaultCommunityNoticeShouldNotBeFound("orderNum.greaterThanOrEqual=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByOrderNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where orderNum is less than or equal to DEFAULT_ORDER_NUM
        defaultCommunityNoticeShouldBeFound("orderNum.lessThanOrEqual=" + DEFAULT_ORDER_NUM);

        // Get all the communityNoticeList where orderNum is less than or equal to SMALLER_ORDER_NUM
        defaultCommunityNoticeShouldNotBeFound("orderNum.lessThanOrEqual=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByOrderNumIsLessThanSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where orderNum is less than DEFAULT_ORDER_NUM
        defaultCommunityNoticeShouldNotBeFound("orderNum.lessThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityNoticeList where orderNum is less than UPDATED_ORDER_NUM
        defaultCommunityNoticeShouldBeFound("orderNum.lessThan=" + UPDATED_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByOrderNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where orderNum is greater than DEFAULT_ORDER_NUM
        defaultCommunityNoticeShouldNotBeFound("orderNum.greaterThan=" + DEFAULT_ORDER_NUM);

        // Get all the communityNoticeList where orderNum is greater than SMALLER_ORDER_NUM
        defaultCommunityNoticeShouldBeFound("orderNum.greaterThan=" + SMALLER_ORDER_NUM);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyDate equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityNoticeShouldBeFound("lastModifyDate.equals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityNoticeList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityNoticeShouldNotBeFound("lastModifyDate.equals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyDate not equals to DEFAULT_LAST_MODIFY_DATE
        defaultCommunityNoticeShouldNotBeFound("lastModifyDate.notEquals=" + DEFAULT_LAST_MODIFY_DATE);

        // Get all the communityNoticeList where lastModifyDate not equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityNoticeShouldBeFound("lastModifyDate.notEquals=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyDate in DEFAULT_LAST_MODIFY_DATE or UPDATED_LAST_MODIFY_DATE
        defaultCommunityNoticeShouldBeFound("lastModifyDate.in=" + DEFAULT_LAST_MODIFY_DATE + "," + UPDATED_LAST_MODIFY_DATE);

        // Get all the communityNoticeList where lastModifyDate equals to UPDATED_LAST_MODIFY_DATE
        defaultCommunityNoticeShouldNotBeFound("lastModifyDate.in=" + UPDATED_LAST_MODIFY_DATE);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyDate is not null
        defaultCommunityNoticeShouldBeFound("lastModifyDate.specified=true");

        // Get all the communityNoticeList where lastModifyDate is null
        defaultCommunityNoticeShouldNotBeFound("lastModifyDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyByIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyBy equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityNoticeShouldBeFound("lastModifyBy.equals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityNoticeList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityNoticeShouldNotBeFound("lastModifyBy.equals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyBy not equals to DEFAULT_LAST_MODIFY_BY
        defaultCommunityNoticeShouldNotBeFound("lastModifyBy.notEquals=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityNoticeList where lastModifyBy not equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityNoticeShouldBeFound("lastModifyBy.notEquals=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyByIsInShouldWork() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyBy in DEFAULT_LAST_MODIFY_BY or UPDATED_LAST_MODIFY_BY
        defaultCommunityNoticeShouldBeFound("lastModifyBy.in=" + DEFAULT_LAST_MODIFY_BY + "," + UPDATED_LAST_MODIFY_BY);

        // Get all the communityNoticeList where lastModifyBy equals to UPDATED_LAST_MODIFY_BY
        defaultCommunityNoticeShouldNotBeFound("lastModifyBy.in=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyByIsNullOrNotNull() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyBy is not null
        defaultCommunityNoticeShouldBeFound("lastModifyBy.specified=true");

        // Get all the communityNoticeList where lastModifyBy is null
        defaultCommunityNoticeShouldNotBeFound("lastModifyBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyByContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyBy contains DEFAULT_LAST_MODIFY_BY
        defaultCommunityNoticeShouldBeFound("lastModifyBy.contains=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityNoticeList where lastModifyBy contains UPDATED_LAST_MODIFY_BY
        defaultCommunityNoticeShouldNotBeFound("lastModifyBy.contains=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByLastModifyByNotContainsSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        // Get all the communityNoticeList where lastModifyBy does not contain DEFAULT_LAST_MODIFY_BY
        defaultCommunityNoticeShouldNotBeFound("lastModifyBy.doesNotContain=" + DEFAULT_LAST_MODIFY_BY);

        // Get all the communityNoticeList where lastModifyBy does not contain UPDATED_LAST_MODIFY_BY
        defaultCommunityNoticeShouldBeFound("lastModifyBy.doesNotContain=" + UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void getAllCommunityNoticesByCommunityIsEqualToSomething() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);
        Community community = CommunityResourceIT.createEntity(em);
        em.persist(community);
        em.flush();
        communityNotice.setCommunity(community);
        communityNoticeRepository.saveAndFlush(communityNotice);
        Long communityId = community.getId();

        // Get all the communityNoticeList where community equals to communityId
        defaultCommunityNoticeShouldBeFound("communityId.equals=" + communityId);

        // Get all the communityNoticeList where community equals to (communityId + 1)
        defaultCommunityNoticeShouldNotBeFound("communityId.equals=" + (communityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommunityNoticeShouldBeFound(String filter) throws Exception {
        restCommunityNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(communityNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].img1ContentType").value(hasItem(DEFAULT_IMG_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img1").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_1))))
            .andExpect(jsonPath("$.[*].img1Title").value(hasItem(DEFAULT_IMG_1_TITLE)))
            .andExpect(jsonPath("$.[*].content1").value(hasItem(DEFAULT_CONTENT_1.toString())))
            .andExpect(jsonPath("$.[*].img2ContentType").value(hasItem(DEFAULT_IMG_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img2").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_2))))
            .andExpect(jsonPath("$.[*].img2Title").value(hasItem(DEFAULT_IMG_2_TITLE)))
            .andExpect(jsonPath("$.[*].content2").value(hasItem(DEFAULT_CONTENT_2.toString())))
            .andExpect(jsonPath("$.[*].img3ContentType").value(hasItem(DEFAULT_IMG_3_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img3").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_3))))
            .andExpect(jsonPath("$.[*].img3Title").value(hasItem(DEFAULT_IMG_3_TITLE)))
            .andExpect(jsonPath("$.[*].content3").value(hasItem(DEFAULT_CONTENT_3.toString())))
            .andExpect(jsonPath("$.[*].img4ContentType").value(hasItem(DEFAULT_IMG_4_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img4").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_4))))
            .andExpect(jsonPath("$.[*].img4Title").value(hasItem(DEFAULT_IMG_4_TITLE)))
            .andExpect(jsonPath("$.[*].content4").value(hasItem(DEFAULT_CONTENT_4.toString())))
            .andExpect(jsonPath("$.[*].img5ContentType").value(hasItem(DEFAULT_IMG_5_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img5").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG_5))))
            .andExpect(jsonPath("$.[*].img5Title").value(hasItem(DEFAULT_IMG_5_TITLE)))
            .andExpect(jsonPath("$.[*].content5").value(hasItem(DEFAULT_CONTENT_5.toString())))
            .andExpect(jsonPath("$.[*].tail").value(hasItem(DEFAULT_TAIL)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].orderNum").value(hasItem(DEFAULT_ORDER_NUM)))
            .andExpect(jsonPath("$.[*].lastModifyDate").value(hasItem(DEFAULT_LAST_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifyBy").value(hasItem(DEFAULT_LAST_MODIFY_BY)));

        // Check, that the count call also returns 1
        restCommunityNoticeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommunityNoticeShouldNotBeFound(String filter) throws Exception {
        restCommunityNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommunityNoticeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommunityNotice() throws Exception {
        // Get the communityNotice
        restCommunityNoticeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommunityNotice() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();

        // Update the communityNotice
        CommunityNotice updatedCommunityNotice = communityNoticeRepository.findById(communityNotice.getId()).get();
        // Disconnect from session so that the updates on updatedCommunityNotice are not directly saved in db
        em.detach(updatedCommunityNotice);
        updatedCommunityNotice
            .title(UPDATED_TITLE)
            .img1(UPDATED_IMG_1)
            .img1ContentType(UPDATED_IMG_1_CONTENT_TYPE)
            .img1Title(UPDATED_IMG_1_TITLE)
            .content1(UPDATED_CONTENT_1)
            .img2(UPDATED_IMG_2)
            .img2ContentType(UPDATED_IMG_2_CONTENT_TYPE)
            .img2Title(UPDATED_IMG_2_TITLE)
            .content2(UPDATED_CONTENT_2)
            .img3(UPDATED_IMG_3)
            .img3ContentType(UPDATED_IMG_3_CONTENT_TYPE)
            .img3Title(UPDATED_IMG_3_TITLE)
            .content3(UPDATED_CONTENT_3)
            .img4(UPDATED_IMG_4)
            .img4ContentType(UPDATED_IMG_4_CONTENT_TYPE)
            .img4Title(UPDATED_IMG_4_TITLE)
            .content4(UPDATED_CONTENT_4)
            .img5(UPDATED_IMG_5)
            .img5ContentType(UPDATED_IMG_5_CONTENT_TYPE)
            .img5Title(UPDATED_IMG_5_TITLE)
            .content5(UPDATED_CONTENT_5)
            .tail(UPDATED_TAIL)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommunityNotice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommunityNotice))
            )
            .andExpect(status().isOk());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
        CommunityNotice testCommunityNotice = communityNoticeList.get(communityNoticeList.size() - 1);
        assertThat(testCommunityNotice.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCommunityNotice.getImg1()).isEqualTo(UPDATED_IMG_1);
        assertThat(testCommunityNotice.getImg1ContentType()).isEqualTo(UPDATED_IMG_1_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg1Title()).isEqualTo(UPDATED_IMG_1_TITLE);
        assertThat(testCommunityNotice.getContent1()).isEqualTo(UPDATED_CONTENT_1);
        assertThat(testCommunityNotice.getImg2()).isEqualTo(UPDATED_IMG_2);
        assertThat(testCommunityNotice.getImg2ContentType()).isEqualTo(UPDATED_IMG_2_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg2Title()).isEqualTo(UPDATED_IMG_2_TITLE);
        assertThat(testCommunityNotice.getContent2()).isEqualTo(UPDATED_CONTENT_2);
        assertThat(testCommunityNotice.getImg3()).isEqualTo(UPDATED_IMG_3);
        assertThat(testCommunityNotice.getImg3ContentType()).isEqualTo(UPDATED_IMG_3_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg3Title()).isEqualTo(UPDATED_IMG_3_TITLE);
        assertThat(testCommunityNotice.getContent3()).isEqualTo(UPDATED_CONTENT_3);
        assertThat(testCommunityNotice.getImg4()).isEqualTo(UPDATED_IMG_4);
        assertThat(testCommunityNotice.getImg4ContentType()).isEqualTo(UPDATED_IMG_4_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg4Title()).isEqualTo(UPDATED_IMG_4_TITLE);
        assertThat(testCommunityNotice.getContent4()).isEqualTo(UPDATED_CONTENT_4);
        assertThat(testCommunityNotice.getImg5()).isEqualTo(UPDATED_IMG_5);
        assertThat(testCommunityNotice.getImg5ContentType()).isEqualTo(UPDATED_IMG_5_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg5Title()).isEqualTo(UPDATED_IMG_5_TITLE);
        assertThat(testCommunityNotice.getContent5()).isEqualTo(UPDATED_CONTENT_5);
        assertThat(testCommunityNotice.getTail()).isEqualTo(UPDATED_TAIL);
        assertThat(testCommunityNotice.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCommunityNotice.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCommunityNotice.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityNotice.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityNotice.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void putNonExistingCommunityNotice() throws Exception {
        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();
        communityNotice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, communityNotice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityNotice))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommunityNotice() throws Exception {
        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();
        communityNotice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(communityNotice))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommunityNotice() throws Exception {
        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();
        communityNotice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityNoticeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(communityNotice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommunityNoticeWithPatch() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();

        // Update the communityNotice using partial update
        CommunityNotice partialUpdatedCommunityNotice = new CommunityNotice();
        partialUpdatedCommunityNotice.setId(communityNotice.getId());

        partialUpdatedCommunityNotice
            .img2Title(UPDATED_IMG_2_TITLE)
            .img3(UPDATED_IMG_3)
            .img3ContentType(UPDATED_IMG_3_CONTENT_TYPE)
            .img3Title(UPDATED_IMG_3_TITLE)
            .content3(UPDATED_CONTENT_3)
            .img4Title(UPDATED_IMG_4_TITLE)
            .img5(UPDATED_IMG_5)
            .img5ContentType(UPDATED_IMG_5_CONTENT_TYPE)
            .orderNum(UPDATED_ORDER_NUM);

        restCommunityNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityNotice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunityNotice))
            )
            .andExpect(status().isOk());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
        CommunityNotice testCommunityNotice = communityNoticeList.get(communityNoticeList.size() - 1);
        assertThat(testCommunityNotice.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCommunityNotice.getImg1()).isEqualTo(DEFAULT_IMG_1);
        assertThat(testCommunityNotice.getImg1ContentType()).isEqualTo(DEFAULT_IMG_1_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg1Title()).isEqualTo(DEFAULT_IMG_1_TITLE);
        assertThat(testCommunityNotice.getContent1()).isEqualTo(DEFAULT_CONTENT_1);
        assertThat(testCommunityNotice.getImg2()).isEqualTo(DEFAULT_IMG_2);
        assertThat(testCommunityNotice.getImg2ContentType()).isEqualTo(DEFAULT_IMG_2_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg2Title()).isEqualTo(UPDATED_IMG_2_TITLE);
        assertThat(testCommunityNotice.getContent2()).isEqualTo(DEFAULT_CONTENT_2);
        assertThat(testCommunityNotice.getImg3()).isEqualTo(UPDATED_IMG_3);
        assertThat(testCommunityNotice.getImg3ContentType()).isEqualTo(UPDATED_IMG_3_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg3Title()).isEqualTo(UPDATED_IMG_3_TITLE);
        assertThat(testCommunityNotice.getContent3()).isEqualTo(UPDATED_CONTENT_3);
        assertThat(testCommunityNotice.getImg4()).isEqualTo(DEFAULT_IMG_4);
        assertThat(testCommunityNotice.getImg4ContentType()).isEqualTo(DEFAULT_IMG_4_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg4Title()).isEqualTo(UPDATED_IMG_4_TITLE);
        assertThat(testCommunityNotice.getContent4()).isEqualTo(DEFAULT_CONTENT_4);
        assertThat(testCommunityNotice.getImg5()).isEqualTo(UPDATED_IMG_5);
        assertThat(testCommunityNotice.getImg5ContentType()).isEqualTo(UPDATED_IMG_5_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg5Title()).isEqualTo(DEFAULT_IMG_5_TITLE);
        assertThat(testCommunityNotice.getContent5()).isEqualTo(DEFAULT_CONTENT_5);
        assertThat(testCommunityNotice.getTail()).isEqualTo(DEFAULT_TAIL);
        assertThat(testCommunityNotice.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCommunityNotice.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCommunityNotice.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityNotice.getLastModifyDate()).isEqualTo(DEFAULT_LAST_MODIFY_DATE);
        assertThat(testCommunityNotice.getLastModifyBy()).isEqualTo(DEFAULT_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void fullUpdateCommunityNoticeWithPatch() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();

        // Update the communityNotice using partial update
        CommunityNotice partialUpdatedCommunityNotice = new CommunityNotice();
        partialUpdatedCommunityNotice.setId(communityNotice.getId());

        partialUpdatedCommunityNotice
            .title(UPDATED_TITLE)
            .img1(UPDATED_IMG_1)
            .img1ContentType(UPDATED_IMG_1_CONTENT_TYPE)
            .img1Title(UPDATED_IMG_1_TITLE)
            .content1(UPDATED_CONTENT_1)
            .img2(UPDATED_IMG_2)
            .img2ContentType(UPDATED_IMG_2_CONTENT_TYPE)
            .img2Title(UPDATED_IMG_2_TITLE)
            .content2(UPDATED_CONTENT_2)
            .img3(UPDATED_IMG_3)
            .img3ContentType(UPDATED_IMG_3_CONTENT_TYPE)
            .img3Title(UPDATED_IMG_3_TITLE)
            .content3(UPDATED_CONTENT_3)
            .img4(UPDATED_IMG_4)
            .img4ContentType(UPDATED_IMG_4_CONTENT_TYPE)
            .img4Title(UPDATED_IMG_4_TITLE)
            .content4(UPDATED_CONTENT_4)
            .img5(UPDATED_IMG_5)
            .img5ContentType(UPDATED_IMG_5_CONTENT_TYPE)
            .img5Title(UPDATED_IMG_5_TITLE)
            .content5(UPDATED_CONTENT_5)
            .tail(UPDATED_TAIL)
            .enable(UPDATED_ENABLE)
            .delFlag(UPDATED_DEL_FLAG)
            .orderNum(UPDATED_ORDER_NUM)
            .lastModifyDate(UPDATED_LAST_MODIFY_DATE)
            .lastModifyBy(UPDATED_LAST_MODIFY_BY);

        restCommunityNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommunityNotice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommunityNotice))
            )
            .andExpect(status().isOk());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
        CommunityNotice testCommunityNotice = communityNoticeList.get(communityNoticeList.size() - 1);
        assertThat(testCommunityNotice.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCommunityNotice.getImg1()).isEqualTo(UPDATED_IMG_1);
        assertThat(testCommunityNotice.getImg1ContentType()).isEqualTo(UPDATED_IMG_1_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg1Title()).isEqualTo(UPDATED_IMG_1_TITLE);
        assertThat(testCommunityNotice.getContent1()).isEqualTo(UPDATED_CONTENT_1);
        assertThat(testCommunityNotice.getImg2()).isEqualTo(UPDATED_IMG_2);
        assertThat(testCommunityNotice.getImg2ContentType()).isEqualTo(UPDATED_IMG_2_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg2Title()).isEqualTo(UPDATED_IMG_2_TITLE);
        assertThat(testCommunityNotice.getContent2()).isEqualTo(UPDATED_CONTENT_2);
        assertThat(testCommunityNotice.getImg3()).isEqualTo(UPDATED_IMG_3);
        assertThat(testCommunityNotice.getImg3ContentType()).isEqualTo(UPDATED_IMG_3_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg3Title()).isEqualTo(UPDATED_IMG_3_TITLE);
        assertThat(testCommunityNotice.getContent3()).isEqualTo(UPDATED_CONTENT_3);
        assertThat(testCommunityNotice.getImg4()).isEqualTo(UPDATED_IMG_4);
        assertThat(testCommunityNotice.getImg4ContentType()).isEqualTo(UPDATED_IMG_4_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg4Title()).isEqualTo(UPDATED_IMG_4_TITLE);
        assertThat(testCommunityNotice.getContent4()).isEqualTo(UPDATED_CONTENT_4);
        assertThat(testCommunityNotice.getImg5()).isEqualTo(UPDATED_IMG_5);
        assertThat(testCommunityNotice.getImg5ContentType()).isEqualTo(UPDATED_IMG_5_CONTENT_TYPE);
        assertThat(testCommunityNotice.getImg5Title()).isEqualTo(UPDATED_IMG_5_TITLE);
        assertThat(testCommunityNotice.getContent5()).isEqualTo(UPDATED_CONTENT_5);
        assertThat(testCommunityNotice.getTail()).isEqualTo(UPDATED_TAIL);
        assertThat(testCommunityNotice.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCommunityNotice.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCommunityNotice.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(testCommunityNotice.getLastModifyDate()).isEqualTo(UPDATED_LAST_MODIFY_DATE);
        assertThat(testCommunityNotice.getLastModifyBy()).isEqualTo(UPDATED_LAST_MODIFY_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCommunityNotice() throws Exception {
        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();
        communityNotice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommunityNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, communityNotice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityNotice))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommunityNotice() throws Exception {
        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();
        communityNotice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityNotice))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommunityNotice() throws Exception {
        int databaseSizeBeforeUpdate = communityNoticeRepository.findAll().size();
        communityNotice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommunityNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(communityNotice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommunityNotice in the database
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommunityNotice() throws Exception {
        // Initialize the database
        communityNoticeRepository.saveAndFlush(communityNotice);

        int databaseSizeBeforeDelete = communityNoticeRepository.findAll().size();

        // Delete the communityNotice
        restCommunityNoticeMockMvc
            .perform(delete(ENTITY_API_URL_ID, communityNotice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommunityNotice> communityNoticeList = communityNoticeRepository.findAll();
        assertThat(communityNoticeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
