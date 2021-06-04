package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CompanyPost;
import com.mycompany.myapp.repository.CompanyPostRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompanyPost}.
 */
@Service
@Transactional
public class CompanyPostService {

    private final Logger log = LoggerFactory.getLogger(CompanyPostService.class);

    private final CompanyPostRepository companyPostRepository;

    public CompanyPostService(CompanyPostRepository companyPostRepository) {
        this.companyPostRepository = companyPostRepository;
    }

    /**
     * Save a companyPost.
     *
     * @param companyPost the entity to save.
     * @return the persisted entity.
     */
    public CompanyPost save(CompanyPost companyPost) {
        log.debug("Request to save CompanyPost : {}", companyPost);
        return companyPostRepository.save(companyPost);
    }

    /**
     * Partially update a companyPost.
     *
     * @param companyPost the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyPost> partialUpdate(CompanyPost companyPost) {
        log.debug("Request to partially update CompanyPost : {}", companyPost);

        return companyPostRepository
            .findById(companyPost.getId())
            .map(
                existingCompanyPost -> {
                    if (companyPost.getPostCode() != null) {
                        existingCompanyPost.setPostCode(companyPost.getPostCode());
                    }
                    if (companyPost.getPostName() != null) {
                        existingCompanyPost.setPostName(companyPost.getPostName());
                    }
                    if (companyPost.getOrderNum() != null) {
                        existingCompanyPost.setOrderNum(companyPost.getOrderNum());
                    }
                    if (companyPost.getRemark() != null) {
                        existingCompanyPost.setRemark(companyPost.getRemark());
                    }
                    if (companyPost.getEnable() != null) {
                        existingCompanyPost.setEnable(companyPost.getEnable());
                    }
                    if (companyPost.getCreateBy() != null) {
                        existingCompanyPost.setCreateBy(companyPost.getCreateBy());
                    }
                    if (companyPost.getCreateDate() != null) {
                        existingCompanyPost.setCreateDate(companyPost.getCreateDate());
                    }
                    if (companyPost.getLastModifyBy() != null) {
                        existingCompanyPost.setLastModifyBy(companyPost.getLastModifyBy());
                    }
                    if (companyPost.getLastModifyDate() != null) {
                        existingCompanyPost.setLastModifyDate(companyPost.getLastModifyDate());
                    }

                    return existingCompanyPost;
                }
            )
            .map(companyPostRepository::save);
    }

    /**
     * Get all the companyPosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyPost> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyPosts");
        return companyPostRepository.findAll(pageable);
    }

    /**
     * Get all the companyPosts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CompanyPost> findAllWithEagerRelationships(Pageable pageable) {
        return companyPostRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one companyPost by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyPost> findOne(Long id) {
        log.debug("Request to get CompanyPost : {}", id);
        return companyPostRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the companyPost by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyPost : {}", id);
        companyPostRepository.deleteById(id);
    }
}
