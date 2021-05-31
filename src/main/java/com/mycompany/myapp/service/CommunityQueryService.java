package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Community;
import com.mycompany.myapp.repository.CommunityRepository;
import com.mycompany.myapp.service.criteria.CommunityCriteria;
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
 * Service for executing complex queries for {@link Community} entities in the database.
 * The main input is a {@link CommunityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Community} or a {@link Page} of {@link Community} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommunityQueryService extends QueryService<Community> {

    private final Logger log = LoggerFactory.getLogger(CommunityQueryService.class);

    private final CommunityRepository communityRepository;

    public CommunityQueryService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    /**
     * Return a {@link List} of {@link Community} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Community> findByCriteria(CommunityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Community> specification = createSpecification(criteria);
        return communityRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Community} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Community> findByCriteria(CommunityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Community> specification = createSpecification(criteria);
        return communityRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommunityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Community> specification = createSpecification(criteria);
        return communityRepository.count(specification);
    }

    /**
     * Function to convert {@link CommunityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Community> createSpecification(CommunityCriteria criteria) {
        Specification<Community> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Community_.id));
            }
            if (criteria.getCname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCname(), Community_.cname));
            }
            if (criteria.getManagerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getManagerName(), Community_.managerName));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Community_.address));
            }
            if (criteria.getTel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTel(), Community_.tel));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Community_.email));
            }
            if (criteria.getOfficeHours() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOfficeHours(), Community_.officeHours));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Community_.description));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSource(), Community_.source));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getParentId(), Community_.parentId));
            }
            if (criteria.getAncestors() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAncestors(), Community_.ancestors));
            }
            if (criteria.getLongCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongCode(), Community_.longCode));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), Community_.enable));
            }
            if (criteria.getDelFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getDelFlag(), Community_.delFlag));
            }
            if (criteria.getOrderNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderNum(), Community_.orderNum));
            }
            if (criteria.getLastModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifyDate(), Community_.lastModifyDate));
            }
            if (criteria.getLastModifyBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifyBy(), Community_.lastModifyBy));
            }
            if (criteria.getCommunityNoticeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityNoticeId(),
                            root -> root.join(Community_.communityNotices, JoinType.LEFT).get(CommunityNotice_.id)
                        )
                    );
            }
            if (criteria.getCommunityLeaderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityLeaderId(),
                            root -> root.join(Community_.communityLeaders, JoinType.LEFT).get(CommunityLeader_.id)
                        )
                    );
            }
            if (criteria.getHomelandStationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHomelandStationId(),
                            root -> root.join(Community_.homelandStations, JoinType.LEFT).get(HomelandStation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
