package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TuYaDevice;
import com.mycompany.myapp.repository.TuYaDeviceRepository;
import com.mycompany.myapp.service.criteria.TuYaDeviceCriteria;
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
 * Service for executing complex queries for {@link TuYaDevice} entities in the database.
 * The main input is a {@link TuYaDeviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TuYaDevice} or a {@link Page} of {@link TuYaDevice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TuYaDeviceQueryService extends QueryService<TuYaDevice> {

    private final Logger log = LoggerFactory.getLogger(TuYaDeviceQueryService.class);

    private final TuYaDeviceRepository tuYaDeviceRepository;

    public TuYaDeviceQueryService(TuYaDeviceRepository tuYaDeviceRepository) {
        this.tuYaDeviceRepository = tuYaDeviceRepository;
    }

    /**
     * Return a {@link List} of {@link TuYaDevice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TuYaDevice> findByCriteria(TuYaDeviceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TuYaDevice> specification = createSpecification(criteria);
        return tuYaDeviceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TuYaDevice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TuYaDevice> findByCriteria(TuYaDeviceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TuYaDevice> specification = createSpecification(criteria);
        return tuYaDeviceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TuYaDeviceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TuYaDevice> specification = createSpecification(criteria);
        return tuYaDeviceRepository.count(specification);
    }

    /**
     * Function to convert {@link TuYaDeviceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TuYaDevice> createSpecification(TuYaDeviceCriteria criteria) {
        Specification<TuYaDevice> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TuYaDevice_.id));
            }
            if (criteria.getDeviceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceName(), TuYaDevice_.deviceName));
            }
            if (criteria.getLongCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongCode(), TuYaDevice_.longCode));
            }
            if (criteria.getTyDeviceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTyDeviceId(), TuYaDevice_.tyDeviceId));
            }
            if (criteria.getDeviceType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeviceType(), TuYaDevice_.deviceType));
            }
            if (criteria.getCmdCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCmdCode(), TuYaDevice_.cmdCode));
            }
            if (criteria.getCreateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateTime(), TuYaDevice_.createTime));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), TuYaDevice_.enable));
            }
            if (criteria.getTuYaCmdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTuYaCmdId(), root -> root.join(TuYaDevice_.tuYaCmds, JoinType.LEFT).get(TuYaCmd_.id))
                    );
            }
            if (criteria.getRoomAddrId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRoomAddrId(),
                            root -> root.join(TuYaDevice_.roomAddr, JoinType.LEFT).get(RoomAddr_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
