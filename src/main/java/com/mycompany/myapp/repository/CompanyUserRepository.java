package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CompanyUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the CompanyUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {
    @Query(
        value = "select distinct companyUser from CompanyUser companyUser left join fetch companyUser.companyDepts left join fetch companyUser.companyPosts",
        countQuery = "select count(distinct companyUser) from CompanyUser companyUser"
    )
    Page<CompanyUser> findAllWithEagerRelationships(Pageable pageable);

    Optional<CompanyUser> findByIdAndDelFlagIsFalse(Long id);

    @Query(
        "select companyUser from CompanyUser companyUser left join fetch companyUser.companyDepts left join fetch companyUser.companyPosts where companyUser.id =:id"
    )
    Optional<CompanyUser> findOneWithEagerRelationships(@Param("id") Long id);
}
