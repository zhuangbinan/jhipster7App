package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Visitor;
import com.mycompany.myapp.repository.VisitorRepository;
import com.mycompany.myapp.service.criteria.VisitorCriteria;
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
 * Service for executing complex queries for {@link Visitor} entities in the database.
 * The main input is a {@link VisitorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Visitor} or a {@link Page} of {@link Visitor} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VisitorQueryService extends QueryService<Visitor> {

    private final Logger log = LoggerFactory.getLogger(VisitorQueryService.class);

    private final VisitorRepository visitorRepository;

    public VisitorQueryService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    /**
     * Return a {@link List} of {@link Visitor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Visitor> findByCriteria(VisitorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Visitor> specification = createSpecification(criteria);
        return visitorRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Visitor} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Visitor> findByCriteria(VisitorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Visitor> specification = createSpecification(criteria);
        return visitorRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VisitorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Visitor> specification = createSpecification(criteria);
        return visitorRepository.count(specification);
    }

    /**
     * Function to convert {@link VisitorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Visitor> createSpecification(VisitorCriteria criteria) {
        Specification<Visitor> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Visitor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Visitor_.name));
            }
            if (criteria.getPhoneNum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNum(), Visitor_.phoneNum));
            }
            if (criteria.getCarPlateNum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCarPlateNum(), Visitor_.carPlateNum));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Visitor_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Visitor_.endTime));
            }
            if (criteria.getPasswd() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPasswd(), Visitor_.passwd));
            }
            if (criteria.getWhichEntrance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWhichEntrance(), Visitor_.whichEntrance));
            }
            if (criteria.getRoomAddrId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRoomAddrId(), root -> root.join(Visitor_.roomAddr, JoinType.LEFT).get(RoomAddr_.id))
                    );
            }
        }
        return specification;
    }
}
