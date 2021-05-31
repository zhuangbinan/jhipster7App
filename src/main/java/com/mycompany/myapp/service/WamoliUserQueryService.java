package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.WamoliUser;
import com.mycompany.myapp.repository.WamoliUserRepository;
import com.mycompany.myapp.service.criteria.WamoliUserCriteria;
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
 * Service for executing complex queries for {@link WamoliUser} entities in the database.
 * The main input is a {@link WamoliUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WamoliUser} or a {@link Page} of {@link WamoliUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WamoliUserQueryService extends QueryService<WamoliUser> {

    private final Logger log = LoggerFactory.getLogger(WamoliUserQueryService.class);

    private final WamoliUserRepository wamoliUserRepository;

    public WamoliUserQueryService(WamoliUserRepository wamoliUserRepository) {
        this.wamoliUserRepository = wamoliUserRepository;
    }

    /**
     * Return a {@link List} of {@link WamoliUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WamoliUser> findByCriteria(WamoliUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WamoliUser> specification = createSpecification(criteria);
        return wamoliUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link WamoliUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WamoliUser> findByCriteria(WamoliUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WamoliUser> specification = createSpecification(criteria);
        return wamoliUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WamoliUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WamoliUser> specification = createSpecification(criteria);
        return wamoliUserRepository.count(specification);
    }

    /**
     * Function to convert {@link WamoliUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WamoliUser> createSpecification(WamoliUserCriteria criteria) {
        Specification<WamoliUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WamoliUser_.id));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), WamoliUser_.userName));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGender(), WamoliUser_.gender));
            }
            if (criteria.getPhoneNum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNum(), WamoliUser_.phoneNum));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), WamoliUser_.email));
            }
            if (criteria.getUnitAddr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnitAddr(), WamoliUser_.unitAddr));
            }
            if (criteria.getRoomAddr() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRoomAddr(), WamoliUser_.roomAddr));
            }
            if (criteria.getIdCardNum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdCardNum(), WamoliUser_.idCardNum));
            }
            if (criteria.getIdCardType() != null) {
                specification = specification.and(buildSpecification(criteria.getIdCardType(), WamoliUser_.idCardType));
            }
            if (criteria.getUserType() != null) {
                specification = specification.and(buildSpecification(criteria.getUserType(), WamoliUser_.userType));
            }
            if (criteria.getEnable() != null) {
                specification = specification.and(buildSpecification(criteria.getEnable(), WamoliUser_.enable));
            }
            if (criteria.getIsManager() != null) {
                specification = specification.and(buildSpecification(criteria.getIsManager(), WamoliUser_.isManager));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), WamoliUser_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), WamoliUser_.lastModifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(WamoliUser_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getRoomAddrId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRoomAddrId(),
                            root -> root.join(WamoliUser_.roomAddrs, JoinType.LEFT).get(RoomAddr_.id)
                        )
                    );
            }
            if (criteria.getCompanyDeptId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCompanyDeptId(),
                            root -> root.join(WamoliUser_.companyDepts, JoinType.LEFT).get(CompanyDept_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
