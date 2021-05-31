package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.HomelandStation;
import com.mycompany.myapp.repository.HomelandStationRepository;
import com.mycompany.myapp.service.criteria.HomelandStationCriteria;
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
 * Service for executing complex queries for {@link HomelandStation} entities in the database.
 * The main input is a {@link HomelandStationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HomelandStation} or a {@link Page} of {@link HomelandStation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HomelandStationQueryService extends QueryService<HomelandStation> {

    private final Logger log = LoggerFactory.getLogger(HomelandStationQueryService.class);

    private final HomelandStationRepository homelandStationRepository;

    public HomelandStationQueryService(HomelandStationRepository homelandStationRepository) {
        this.homelandStationRepository = homelandStationRepository;
    }

    /**
     * Return a {@link List} of {@link HomelandStation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HomelandStation> findByCriteria(HomelandStationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HomelandStation> specification = createSpecification(criteria);
        return homelandStationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HomelandStation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HomelandStation> findByCriteria(HomelandStationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HomelandStation> specification = createSpecification(criteria);
        return homelandStationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HomelandStationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HomelandStation> specification = createSpecification(criteria);
        return homelandStationRepository.count(specification);
    }

    /**
     * Function to convert {@link HomelandStationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HomelandStation> createSpecification(HomelandStationCriteria criteria) {
        Specification<HomelandStation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HomelandStation_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HomelandStation_.name));
            }
            if (criteria.getLongCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongCode(), HomelandStation_.longCode));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), HomelandStation_.address));
            }
            if (criteria.getLivingPopulation() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLivingPopulation(), HomelandStation_.livingPopulation));
            }
            if (criteria.getBuildingCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBuildingCount(), HomelandStation_.buildingCount));
            }
            if (criteria.getEntranceCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntranceCount(), HomelandStation_.entranceCount));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), HomelandStation_.longitude));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), HomelandStation_.latitude));
            }
            if (criteria.getBuildingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBuildingsId(),
                            root -> root.join(HomelandStation_.buildings, JoinType.LEFT).get(Buildings_.id)
                        )
                    );
            }
            if (criteria.getCommunityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommunityId(),
                            root -> root.join(HomelandStation_.community, JoinType.LEFT).get(Community_.id)
                        )
                    );
            }
            if (criteria.getCompanyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyId(),
                            root -> root.join(HomelandStation_.company, JoinType.LEFT).get(Company_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
