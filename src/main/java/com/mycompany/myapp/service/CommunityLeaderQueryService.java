package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CommunityLeader;
import com.mycompany.myapp.repository.CommunityLeaderRepository;
import com.mycompany.myapp.service.criteria.CommunityLeaderCriteria;
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
 * Service for executing complex queries for {@link CommunityLeader} entities in the database.
 * The main input is a {@link CommunityLeaderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommunityLeader} or a {@link Page} of {@link CommunityLeader} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommunityLeaderQueryService extends QueryService<CommunityLeader> {

    private final Logger log = LoggerFactory.getLogger(CommunityLeaderQueryService.class);

    private final CommunityLeaderRepository communityLeaderRepository;

    public CommunityLeaderQueryService(CommunityLeaderRepository communityLeaderRepository) {
        this.communityLeaderRepository = communityLeaderRepository;
    }

    /**
     * Return a {@link List} of {@link CommunityLeader} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommunityLeader> findByCriteria(CommunityLeaderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommunityLeader> specification = createSpecification(criteria);
        return communityLeaderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CommunityLeader} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunityLeader> findByCriteria(CommunityLeaderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommunityLeader> specification = createSpecification(criteria);
        return communityLeaderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommunityLeaderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommunityLeader> specification = createSpecification(criteria);
        return communityLeaderRepository.count(specification);
    }

    /**
     * Function to convert {@link CommunityLeaderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommunityLeader> createSpecification(CommunityLeaderCriteria criteria) {
        Specification<CommunityLeader> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommunityLeader_.id));
            }
            if (criteria.getRealName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRealName(), CommunityLeader_.realName));
            }
            if (criteria.getTel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTel(), CommunityLeader_.tel));
            }
            if (criteria.getJobTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobTitle(), CommunityLeader_.jobTitle));
            }
            if (criteria.getJobDesc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobDesc(), CommunityLeader_.jobDesc));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), CommunityLeader_.enable));
            }
            if (criteria.getDelFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getDelFlag(), CommunityLeader_.delFlag));
            }
            if (criteria.getOrderNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderNum(), CommunityLeader_.orderNum));
            }
            if (criteria.getLastModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifyDate(), CommunityLeader_.lastModifyDate));
            }
            if (criteria.getLastModifyBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifyBy(), CommunityLeader_.lastModifyBy));
            }
            if (criteria.getCommunityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityId(),
                            root -> root.join(CommunityLeader_.community, JoinType.LEFT).get(Community_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
