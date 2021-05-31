package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CompanyDept;
import com.mycompany.myapp.repository.CompanyDeptRepository;
import com.mycompany.myapp.service.criteria.CompanyDeptCriteria;
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
 * Service for executing complex queries for {@link CompanyDept} entities in the database.
 * The main input is a {@link CompanyDeptCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompanyDept} or a {@link Page} of {@link CompanyDept} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompanyDeptQueryService extends QueryService<CompanyDept> {

    private final Logger log = LoggerFactory.getLogger(CompanyDeptQueryService.class);

    private final CompanyDeptRepository companyDeptRepository;

    public CompanyDeptQueryService(CompanyDeptRepository companyDeptRepository) {
        this.companyDeptRepository = companyDeptRepository;
    }

    /**
     * Return a {@link List} of {@link CompanyDept} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompanyDept> findByCriteria(CompanyDeptCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CompanyDept> specification = createSpecification(criteria);
        return companyDeptRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CompanyDept} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyDept> findByCriteria(CompanyDeptCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CompanyDept> specification = createSpecification(criteria);
        return companyDeptRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompanyDeptCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CompanyDept> specification = createSpecification(criteria);
        return companyDeptRepository.count(specification);
    }

    /**
     * Function to convert {@link CompanyDeptCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CompanyDept> createSpecification(CompanyDeptCriteria criteria) {
        Specification<CompanyDept> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CompanyDept_.id));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getParentId(), CompanyDept_.parentId));
            }
            if (criteria.getAncestors() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAncestors(), CompanyDept_.ancestors));
            }
            if (criteria.getDeptName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeptName(), CompanyDept_.deptName));
            }
            if (criteria.getOrderNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderNum(), CompanyDept_.orderNum));
            }
            if (criteria.getLeaderName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaderName(), CompanyDept_.leaderName));
            }
            if (criteria.getTel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTel(), CompanyDept_.tel));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CompanyDept_.email));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), CompanyDept_.enable));
            }
            if (criteria.getDelFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getDelFlag(), CompanyDept_.delFlag));
            }
            if (criteria.getCreateBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreateBy(), CompanyDept_.createBy));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), CompanyDept_.createDate));
            }
            if (criteria.getLastModifyBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifyBy(), CompanyDept_.lastModifyBy));
            }
            if (criteria.getLastModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifyDate(), CompanyDept_.lastModifyDate));
            }
            if (criteria.getCompanyPostId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyPostId(),
                            root -> root.join(CompanyDept_.companyPosts, JoinType.LEFT).get(CompanyPost_.id)
                        )
                    );
            }
            if (criteria.getWamoliUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWamoliUserId(),
                            root -> root.join(CompanyDept_.wamoliUsers, JoinType.LEFT).get(WamoliUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
