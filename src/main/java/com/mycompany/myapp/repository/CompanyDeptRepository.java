package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CompanyDept;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompanyDept entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyDeptRepository extends JpaRepository<CompanyDept, Long>, JpaSpecificationExecutor<CompanyDept> {}
