package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CompanyPost;
import com.mycompany.myapp.repository.CompanyPostRepository;
import com.mycompany.myapp.service.criteria.CompanyPostCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CompanyPost} entities in the database.
 * The main input is a {@link CompanyPostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompanyPost} or a {@link Page} of {@link CompanyPost} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompanyPostQueryService extends QueryService<CompanyPost> {

    private final Logger log = LoggerFactory.getLogger(CompanyPostQueryService.class);

    private final CompanyPostRepository companyPostRepository;

    public CompanyPostQueryService(CompanyPostRepository companyPostRepository) {
        this.companyPostRepository = companyPostRepository;
    }

    /**
     * Return a {@link List} of {@link CompanyPost} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompanyPost> findByCriteria(CompanyPostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CompanyPost> specification = createSpecification(criteria);
        return companyPostRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CompanyPost} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyPost> findByCriteria(CompanyPostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CompanyPost> specification = createSpecification(criteria);
        return companyPostRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompanyPostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CompanyPost> specification = createSpecification(criteria);
        return companyPostRepository.count(specification);
    }

    /**
     * Function to convert {@link CompanyPostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CompanyPost> createSpecification(CompanyPostCriteria criteria) {
        Specification<CompanyPost> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CompanyPost_.id));
            }
            if (criteria.getPostCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostCode(), CompanyPost_.postCode));
            }
            if (criteria.getPostName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostName(), CompanyPost_.postName));
            }
            if (criteria.getOrderNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderNum(), CompanyPost_.orderNum));
            }
            if (criteria.getRemark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemark(), CompanyPost_.remark));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), CompanyPost_.enable));
            }
            if (criteria.getCreateBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreateBy(), CompanyPost_.createBy));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), CompanyPost_.createDate));
            }
            if (criteria.getLastModifyBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifyBy(), CompanyPost_.lastModifyBy));
            }
            if (criteria.getLastModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifyDate(), CompanyPost_.lastModifyDate));
            }
            if (criteria.getCompanyUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyUserId(),
                            root -> root.join(CompanyPost_.companyUsers, JoinType.LEFT).get(CompanyUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
