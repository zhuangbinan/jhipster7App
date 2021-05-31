package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CommunityImageGroup;
import com.mycompany.myapp.repository.CommunityImageGroupRepository;
import com.mycompany.myapp.service.criteria.CommunityImageGroupCriteria;
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
 * Service for executing complex queries for {@link CommunityImageGroup} entities in the database.
 * The main input is a {@link CommunityImageGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommunityImageGroup} or a {@link Page} of {@link CommunityImageGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommunityImageGroupQueryService extends QueryService<CommunityImageGroup> {

    private final Logger log = LoggerFactory.getLogger(CommunityImageGroupQueryService.class);

    private final CommunityImageGroupRepository communityImageGroupRepository;

    public CommunityImageGroupQueryService(CommunityImageGroupRepository communityImageGroupRepository) {
        this.communityImageGroupRepository = communityImageGroupRepository;
    }

    /**
     * Return a {@link List} of {@link CommunityImageGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommunityImageGroup> findByCriteria(CommunityImageGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommunityImageGroup> specification = createSpecification(criteria);
        return communityImageGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CommunityImageGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunityImageGroup> findByCriteria(CommunityImageGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommunityImageGroup> specification = createSpecification(criteria);
        return communityImageGroupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommunityImageGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommunityImageGroup> specification = createSpecification(criteria);
        return communityImageGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link CommunityImageGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommunityImageGroup> createSpecification(CommunityImageGroupCriteria criteria) {
        Specification<CommunityImageGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommunityImageGroup_.id));
            }
            if (criteria.getImgGroupName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgGroupName(), CommunityImageGroup_.imgGroupName));
            }
            if (criteria.getOrderNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderNum(), CommunityImageGroup_.orderNum));
            }
            if (criteria.getLastModifyDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifyDate(), CommunityImageGroup_.lastModifyDate));
            }
            if (criteria.getLastModifyBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifyBy(), CommunityImageGroup_.lastModifyBy));
            }
            if (criteria.getCommunityImagesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityImagesId(),
                            root -> root.join(CommunityImageGroup_.communityImages, JoinType.LEFT).get(CommunityImages_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
