package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CommunityImages;
import com.mycompany.myapp.repository.CommunityImagesRepository;
import com.mycompany.myapp.service.criteria.CommunityImagesCriteria;
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
 * Service for executing complex queries for {@link CommunityImages} entities in the database.
 * The main input is a {@link CommunityImagesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommunityImages} or a {@link Page} of {@link CommunityImages} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommunityImagesQueryService extends QueryService<CommunityImages> {

    private final Logger log = LoggerFactory.getLogger(CommunityImagesQueryService.class);

    private final CommunityImagesRepository communityImagesRepository;

    public CommunityImagesQueryService(CommunityImagesRepository communityImagesRepository) {
        this.communityImagesRepository = communityImagesRepository;
    }

    /**
     * Return a {@link List} of {@link CommunityImages} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommunityImages> findByCriteria(CommunityImagesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommunityImages> specification = createSpecification(criteria);
        return communityImagesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CommunityImages} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunityImages> findByCriteria(CommunityImagesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommunityImages> specification = createSpecification(criteria);
        return communityImagesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommunityImagesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommunityImages> specification = createSpecification(criteria);
        return communityImagesRepository.count(specification);
    }

    /**
     * Function to convert {@link CommunityImagesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommunityImages> createSpecification(CommunityImagesCriteria criteria) {
        Specification<CommunityImages> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommunityImages_.id));
            }
            if (criteria.getImgTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgTitle(), CommunityImages_.imgTitle));
            }
            if (criteria.getImgDesc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgDesc(), CommunityImages_.imgDesc));
            }
            if (criteria.getOrderNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderNum(), CommunityImages_.orderNum));
            }
            if (criteria.getLastModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifyDate(), CommunityImages_.lastModifyDate));
            }
            if (criteria.getLastModifyBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifyBy(), CommunityImages_.lastModifyBy));
            }
            if (criteria.getCommunityImageGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityImageGroupId(),
                            root -> root.join(CommunityImages_.communityImageGroup, JoinType.LEFT).get(CommunityImageGroup_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
