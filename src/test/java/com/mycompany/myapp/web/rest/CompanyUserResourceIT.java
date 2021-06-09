package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CompanyUser;
import com.mycompany.myapp.repository.CompanyUserRepository;
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
 * Integration tests for the {@link CompanyUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyUserResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ID_CARD_NUM = "AAAAAAAAAA";
    private static final String UPDATED_ID_CARD_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUM = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DEPT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POST_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE = false;
    private static final Boolean UPDATED_ENABLE = true;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEL_FLAG = false;
    private static final Boolean UPDATED_DEL_FLAG = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/company-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyUserRepository companyUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyUserMockMvc;

    private CompanyUser companyUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyUser createEntity(EntityManager em) {
        CompanyUser companyUser = new CompanyUser()
            .userName(DEFAULT_USER_NAME)
            .idCardNum(DEFAULT_ID_CARD_NUM)
            .gender(DEFAULT_GENDER)
            .phoneNum(DEFAULT_PHONE_NUM)
            .email(DEFAULT_EMAIL)
            .deptName(DEFAULT_DEPT_NAME)
            .postName(DEFAULT_POST_NAME)
            .enable(DEFAULT_ENABLE)
            .remark(DEFAULT_REMARK)
            .delFlag(DEFAULT_DEL_FLAG)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return companyUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyUser createUpdatedEntity(EntityManager em) {
        CompanyUser companyUser = new CompanyUser()
            .userName(UPDATED_USER_NAME)
            .idCardNum(UPDATED_ID_CARD_NUM)
            .gender(UPDATED_GENDER)
            .phoneNum(UPDATED_PHONE_NUM)
            .email(UPDATED_EMAIL)
            .deptName(UPDATED_DEPT_NAME)
            .postName(UPDATED_POST_NAME)
            .enable(UPDATED_ENABLE)
            .remark(UPDATED_REMARK)
            .delFlag(UPDATED_DEL_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return companyUser;
    }

    @BeforeEach
    public void initTest() {
        companyUser = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyUser() throws Exception {
        int databaseSizeBeforeCreate = companyUserRepository.findAll().size();
        // Create the CompanyUser
        restCompanyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isCreated());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testCompanyUser.getIdCardNum()).isEqualTo(DEFAULT_ID_CARD_NUM);
        assertThat(testCompanyUser.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCompanyUser.getPhoneNum()).isEqualTo(DEFAULT_PHONE_NUM);
        assertThat(testCompanyUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompanyUser.getDeptName()).isEqualTo(DEFAULT_DEPT_NAME);
        assertThat(testCompanyUser.getPostName()).isEqualTo(DEFAULT_POST_NAME);
        assertThat(testCompanyUser.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCompanyUser.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testCompanyUser.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCompanyUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCompanyUser.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCompanyUser.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCompanyUser.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createCompanyUserWithExistingId() throws Exception {
        // Create the CompanyUser with an existing ID
        companyUser.setId(1L);

        int databaseSizeBeforeCreate = companyUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanyUsers() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        // Get all the companyUserList
        restCompanyUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].idCardNum").value(hasItem(DEFAULT_ID_CARD_NUM)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].phoneNum").value(hasItem(DEFAULT_PHONE_NUM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].deptName").value(hasItem(DEFAULT_DEPT_NAME)))
            .andExpect(jsonPath("$.[*].postName").value(hasItem(DEFAULT_POST_NAME)))
            .andExpect(jsonPath("$.[*].enable").value(hasItem(DEFAULT_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].delFlag").value(hasItem(DEFAULT_DEL_FLAG.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        // Get the companyUser
        restCompanyUserMockMvc
            .perform(get(ENTITY_API_URL_ID, companyUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyUser.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.idCardNum").value(DEFAULT_ID_CARD_NUM))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.phoneNum").value(DEFAULT_PHONE_NUM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.deptName").value(DEFAULT_DEPT_NAME))
            .andExpect(jsonPath("$.postName").value(DEFAULT_POST_NAME))
            .andExpect(jsonPath("$.enable").value(DEFAULT_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.delFlag").value(DEFAULT_DEL_FLAG.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCompanyUser() throws Exception {
        // Get the companyUser
        restCompanyUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Update the companyUser
        CompanyUser updatedCompanyUser = companyUserRepository.findById(companyUser.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyUser are not directly saved in db
        em.detach(updatedCompanyUser);
        updatedCompanyUser
            .userName(UPDATED_USER_NAME)
            .idCardNum(UPDATED_ID_CARD_NUM)
            .gender(UPDATED_GENDER)
            .phoneNum(UPDATED_PHONE_NUM)
            .email(UPDATED_EMAIL)
            .deptName(UPDATED_DEPT_NAME)
            .postName(UPDATED_POST_NAME)
            .enable(UPDATED_ENABLE)
            .remark(UPDATED_REMARK)
            .delFlag(UPDATED_DEL_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCompanyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompanyUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompanyUser))
            )
            .andExpect(status().isOk());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testCompanyUser.getIdCardNum()).isEqualTo(UPDATED_ID_CARD_NUM);
        assertThat(testCompanyUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCompanyUser.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testCompanyUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompanyUser.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testCompanyUser.getPostName()).isEqualTo(UPDATED_POST_NAME);
        assertThat(testCompanyUser.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCompanyUser.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testCompanyUser.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCompanyUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCompanyUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCompanyUser.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCompanyUser.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyUserWithPatch() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Update the companyUser using partial update
        CompanyUser partialUpdatedCompanyUser = new CompanyUser();
        partialUpdatedCompanyUser.setId(companyUser.getId());

        partialUpdatedCompanyUser
            .idCardNum(UPDATED_ID_CARD_NUM)
            .gender(UPDATED_GENDER)
            .remark(UPDATED_REMARK)
            .createdDate(UPDATED_CREATED_DATE);

        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyUser))
            )
            .andExpect(status().isOk());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testCompanyUser.getIdCardNum()).isEqualTo(UPDATED_ID_CARD_NUM);
        assertThat(testCompanyUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCompanyUser.getPhoneNum()).isEqualTo(DEFAULT_PHONE_NUM);
        assertThat(testCompanyUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCompanyUser.getDeptName()).isEqualTo(DEFAULT_DEPT_NAME);
        assertThat(testCompanyUser.getPostName()).isEqualTo(DEFAULT_POST_NAME);
        assertThat(testCompanyUser.getEnable()).isEqualTo(DEFAULT_ENABLE);
        assertThat(testCompanyUser.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testCompanyUser.getDelFlag()).isEqualTo(DEFAULT_DEL_FLAG);
        assertThat(testCompanyUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCompanyUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCompanyUser.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCompanyUser.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCompanyUserWithPatch() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();

        // Update the companyUser using partial update
        CompanyUser partialUpdatedCompanyUser = new CompanyUser();
        partialUpdatedCompanyUser.setId(companyUser.getId());

        partialUpdatedCompanyUser
            .userName(UPDATED_USER_NAME)
            .idCardNum(UPDATED_ID_CARD_NUM)
            .gender(UPDATED_GENDER)
            .phoneNum(UPDATED_PHONE_NUM)
            .email(UPDATED_EMAIL)
            .deptName(UPDATED_DEPT_NAME)
            .postName(UPDATED_POST_NAME)
            .enable(UPDATED_ENABLE)
            .remark(UPDATED_REMARK)
            .delFlag(UPDATED_DEL_FLAG)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyUser))
            )
            .andExpect(status().isOk());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
        CompanyUser testCompanyUser = companyUserList.get(companyUserList.size() - 1);
        assertThat(testCompanyUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testCompanyUser.getIdCardNum()).isEqualTo(UPDATED_ID_CARD_NUM);
        assertThat(testCompanyUser.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCompanyUser.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testCompanyUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCompanyUser.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testCompanyUser.getPostName()).isEqualTo(UPDATED_POST_NAME);
        assertThat(testCompanyUser.getEnable()).isEqualTo(UPDATED_ENABLE);
        assertThat(testCompanyUser.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testCompanyUser.getDelFlag()).isEqualTo(UPDATED_DEL_FLAG);
        assertThat(testCompanyUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCompanyUser.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCompanyUser.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCompanyUser.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyUser() throws Exception {
        int databaseSizeBeforeUpdate = companyUserRepository.findAll().size();
        companyUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companyUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyUser in the database
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyUser() throws Exception {
        // Initialize the database
        companyUserRepository.saveAndFlush(companyUser);

        int databaseSizeBeforeDelete = companyUserRepository.findAll().size();

        // Delete the companyUser
        restCompanyUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyUser> companyUserList = companyUserRepository.findAll();
        assertThat(companyUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
