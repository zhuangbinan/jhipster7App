package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Test01;
import com.mycompany.myapp.repository.Test01Repository;
import com.mycompany.myapp.service.criteria.Test01Criteria;
import com.mycompany.myapp.service.dto.Test01DTO;
import com.mycompany.myapp.service.mapper.Test01Mapper;
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
 * Service for executing complex queries for {@link Test01} entities in the database.
 * The main input is a {@link Test01Criteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Test01DTO} or a {@link Page} of {@link Test01DTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class Test01QueryService extends QueryService<Test01> {

    private final Logger log = LoggerFactory.getLogger(Test01QueryService.class);

    private final Test01Repository test01Repository;

    private final Test01Mapper test01Mapper;

    public Test01QueryService(Test01Repository test01Repository, Test01Mapper test01Mapper) {
        this.test01Repository = test01Repository;
        this.test01Mapper = test01Mapper;
    }

    /**
     * Return a {@link List} of {@link Test01DTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Test01DTO> findByCriteria(Test01Criteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Test01> specification = createSpecification(criteria);
        return test01Mapper.toDto(test01Repository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link Test01DTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Test01DTO> findByCriteria(Test01Criteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Test01> specification = createSpecification(criteria);
        return test01Repository.findAll(specification, page).map(test01Mapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Test01Criteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Test01> specification = createSpecification(criteria);
        return test01Repository.count(specification);
    }

    /**
     * Function to convert {@link Test01Criteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Test01> createSpecification(Test01Criteria criteria) {
        Specification<Test01> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Test01_.id));
            }
        }
        return specification;
    }
}
