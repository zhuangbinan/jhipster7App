package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TuYaDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TuYaDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TuYaDeviceRepository extends JpaRepository<TuYaDevice, Long>, JpaSpecificationExecutor<TuYaDevice> {}
