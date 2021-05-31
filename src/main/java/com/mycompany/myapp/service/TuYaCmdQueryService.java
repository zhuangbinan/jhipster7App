package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.TuYaCmd;
import com.mycompany.myapp.repository.TuYaCmdRepository;
import com.mycompany.myapp.service.criteria.TuYaCmdCriteria;
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
 * Service for executing complex queries for {@link TuYaCmd} entities in the database.
 * The main input is a {@link TuYaCmdCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TuYaCmd} or a {@link Page} of {@link TuYaCmd} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TuYaCmdQueryService extends QueryService<TuYaCmd> {

    private final Logger log = LoggerFactory.getLogger(TuYaCmdQueryService.class);

    private final TuYaCmdRepository tuYaCmdRepository;

    public TuYaCmdQueryService(TuYaCmdRepository tuYaCmdRepository) {
        this.tuYaCmdRepository = tuYaCmdRepository;
    }

    /**
     * Return a {@link List} of {@link TuYaCmd} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TuYaCmd> findByCriteria(TuYaCmdCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TuYaCmd> specification = createSpecification(criteria);
        return tuYaCmdRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TuYaCmd} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TuYaCmd> findByCriteria(TuYaCmdCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TuYaCmd> specification = createSpecification(criteria);
        return tuYaCmdRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TuYaCmdCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TuYaCmd> specification = createSpecification(criteria);
        return tuYaCmdRepository.count(specification);
    }

    /**
     * Function to convert {@link TuYaCmdCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TuYaCmd> createSpecification(TuYaCmdCriteria criteria) {
        Specification<TuYaCmd> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TuYaCmd_.id));
            }
            if (criteria.getCmdName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCmdName(), TuYaCmd_.cmdName));
            }
            if (criteria.getCmdCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCmdCode(), TuYaCmd_.cmdCode));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildSpecification(criteria.getValue(), TuYaCmd_.value));
            }
            if (criteria.getCmdType() != null) {
                specification = specification.and(buildSpecification(criteria.getCmdType(), TuYaCmd_.cmdType));
            }
            if (criteria.getCreateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateTime(), TuYaCmd_.createTime));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), TuYaCmd_.enable));
            }
            if (criteria.getTuYaDeviceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTuYaDeviceId(),
                            root -> root.join(TuYaCmd_.tuYaDevice, JoinType.LEFT).get(TuYaDevice_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
