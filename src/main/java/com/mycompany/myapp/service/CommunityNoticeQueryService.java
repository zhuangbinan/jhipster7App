package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CommunityNotice;
import com.mycompany.myapp.repository.CommunityNoticeRepository;
import com.mycompany.myapp.service.criteria.CommunityNoticeCriteria;
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
 * Service for executing complex queries for {@link CommunityNotice} entities in the database.
 * The main input is a {@link CommunityNoticeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommunityNotice} or a {@link Page} of {@link CommunityNotice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommunityNoticeQueryService extends QueryService<CommunityNotice> {

    private final Logger log = LoggerFactory.getLogger(CommunityNoticeQueryService.class);

    private final CommunityNoticeRepository communityNoticeRepository;

    public CommunityNoticeQueryService(CommunityNoticeRepository communityNoticeRepository) {
        this.communityNoticeRepository = communityNoticeRepository;
    }

    /**
     * Return a {@link List} of {@link CommunityNotice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommunityNotice> findByCriteria(CommunityNoticeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommunityNotice> specification = createSpecification(criteria);
        return communityNoticeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CommunityNotice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunityNotice> findByCriteria(CommunityNoticeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommunityNotice> specification = createSpecification(criteria);
        return communityNoticeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommunityNoticeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommunityNotice> specification = createSpecification(criteria);
        return communityNoticeRepository.count(specification);
    }

    /**
     * Function to convert {@link CommunityNoticeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommunityNotice> createSpecification(CommunityNoticeCriteria criteria) {
        Specification<CommunityNotice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommunityNotice_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), CommunityNotice_.title));
            }
            if (criteria.getImg1Title() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg1Title(), CommunityNotice_.img1Title));
            }
            if (criteria.getImg2Title() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg2Title(), CommunityNotice_.img2Title));
            }
            if (criteria.getImg3Title() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg3Title(), CommunityNotice_.img3Title));
            }
            if (criteria.getImg4Title() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg4Title(), CommunityNotice_.img4Title));
            }
            if (criteria.getImg5Title() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg5Title(), CommunityNotice_.img5Title));
            }
            if (criteria.getTail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTail(), CommunityNotice_.tail));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), CommunityNotice_.enable));
            }
            if (criteria.getDelFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getDelFlag(), CommunityNotice_.delFlag));
            }
            if (criteria.getOrderNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderNum(), CommunityNotice_.orderNum));
            }
            if (criteria.getLastModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifyDate(), CommunityNotice_.lastModifyDate));
            }
            if (criteria.getLastModifyBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifyBy(), CommunityNotice_.lastModifyBy));
            }
            if (criteria.getCommunityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityId(),
                            root -> root.join(CommunityNotice_.community, JoinType.LEFT).get(Community_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
