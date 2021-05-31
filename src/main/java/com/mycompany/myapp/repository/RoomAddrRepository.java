package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RoomAddr;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RoomAddr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomAddrRepository extends JpaRepository<RoomAddr, Long>, JpaSpecificationExecutor<RoomAddr> {}
