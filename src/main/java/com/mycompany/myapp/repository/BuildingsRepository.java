package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Buildings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Buildings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BuildingsRepository extends JpaRepository<Buildings, Long>, JpaSpecificationExecutor<Buildings> {}
