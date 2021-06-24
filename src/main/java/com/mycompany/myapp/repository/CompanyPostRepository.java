package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CompanyPost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompanyPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyPostRepository extends JpaRepository<CompanyPost, Long>, JpaSpecificationExecutor<CompanyPost> {}
