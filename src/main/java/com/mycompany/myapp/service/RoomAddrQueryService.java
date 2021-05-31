package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.RoomAddr;
import com.mycompany.myapp.repository.RoomAddrRepository;
import com.mycompany.myapp.service.criteria.RoomAddrCriteria;
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
 * Service for executing complex queries for {@link RoomAddr} entities in the database.
 * The main input is a {@link RoomAddrCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RoomAddr} or a {@link Page} of {@link RoomAddr} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoomAddrQueryService extends QueryService<RoomAddr> {

    private final Logger log = LoggerFactory.getLogger(RoomAddrQueryService.class);

    private final RoomAddrRepository roomAddrRepository;

    public RoomAddrQueryService(RoomAddrRepository roomAddrRepository) {
        this.roomAddrRepository = roomAddrRepository;
    }

    /**
     * Return a {@link List} of {@link RoomAddr} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RoomAddr> findByCriteria(RoomAddrCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RoomAddr> specification = createSpecification(criteria);
        return roomAddrRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RoomAddr} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RoomAddr> findByCriteria(RoomAddrCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RoomAddr> specification = createSpecification(criteria);
        return roomAddrRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoomAddrCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RoomAddr> specification = createSpecification(criteria);
        return roomAddrRepository.count(specification);
    }

    /**
     * Function to convert {@link RoomAddrCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RoomAddr> createSpecification(RoomAddrCriteria criteria) {
        Specification<RoomAddr> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RoomAddr_.id));
            }
            if (criteria.getRoomNum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoomNum(), RoomAddr_.roomNum));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), RoomAddr_.unit));
            }
            if (criteria.getRoomType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRoomType(), RoomAddr_.roomType));
            }
            if (criteria.getRoomArea() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRoomArea(), RoomAddr_.roomArea));
            }
            if (criteria.getUsed() != null) {
                specification = specification.and(buildSpecification(criteria.getUsed(), RoomAddr_.used));
            }
            if (criteria.getAutoControl() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoControl(), RoomAddr_.autoControl));
            }
            if (criteria.getLongCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongCode(), RoomAddr_.longCode));
            }
            if (criteria.getVisitorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVisitorId(), root -> root.join(RoomAddr_.visitors, JoinType.LEFT).get(Visitor_.id))
                    );
            }
            if (criteria.getBuildingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBuildingsId(),
                            root -> root.join(RoomAddr_.buildings, JoinType.LEFT).get(Buildings_.id)
                        )
                    );
            }
            if (criteria.getWamoliUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWamoliUserId(),
                            root -> root.join(RoomAddr_.wamoliUsers, JoinType.LEFT).get(WamoliUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
