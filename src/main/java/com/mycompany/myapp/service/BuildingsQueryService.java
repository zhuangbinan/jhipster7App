package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Buildings;
import com.mycompany.myapp.repository.BuildingsRepository;
import com.mycompany.myapp.service.criteria.BuildingsCriteria;
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
 * Service for executing complex queries for {@link Buildings} entities in the database.
 * The main input is a {@link BuildingsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Buildings} or a {@link Page} of {@link Buildings} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BuildingsQueryService extends QueryService<Buildings> {

    private final Logger log = LoggerFactory.getLogger(BuildingsQueryService.class);

    private final BuildingsRepository buildingsRepository;

    public BuildingsQueryService(BuildingsRepository buildingsRepository) {
        this.buildingsRepository = buildingsRepository;
    }

    /**
     * Return a {@link List} of {@link Buildings} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Buildings> findByCriteria(BuildingsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Buildings> specification = createSpecification(criteria);
        return buildingsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Buildings} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Buildings> findByCriteria(BuildingsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Buildings> specification = createSpecification(criteria);
        return buildingsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BuildingsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Buildings> specification = createSpecification(criteria);
        return buildingsRepository.count(specification);
    }

    /**
     * Function to convert {@link BuildingsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Buildings> createSpecification(BuildingsCriteria criteria) {
        Specification<Buildings> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Buildings_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Buildings_.name));
            }
            if (criteria.getLongCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongCode(), Buildings_.longCode));
            }
            if (criteria.getFloorCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFloorCount(), Buildings_.floorCount));
            }
            if (criteria.getUnites() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnites(), Buildings_.unites));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), Buildings_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), Buildings_.latitude));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), Buildings_.enable));
            }
            if (criteria.getRoomAddrId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRoomAddrId(),
                            root -> root.join(Buildings_.roomAddrs, JoinType.LEFT).get(RoomAddr_.id)
                        )
                    );
            }
            if (criteria.getHomelandStationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHomelandStationId(),
                            root -> root.join(Buildings_.homelandStation, JoinType.LEFT).get(HomelandStation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
