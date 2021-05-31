package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WamoliUserLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WamoliUserLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WamoliUserLocationRepository extends JpaRepository<WamoliUserLocation, Long> {}
