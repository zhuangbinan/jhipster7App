package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.HomelandStation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HomelandStation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HomelandStationRepository extends JpaRepository<HomelandStation, Long>, JpaSpecificationExecutor<HomelandStation> {}
