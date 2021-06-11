package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WamoliUser;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WamoliUser entity.
 */
@Repository
public interface WamoliUserRepository extends JpaRepository<WamoliUser, Long>, JpaSpecificationExecutor<WamoliUser> {
    @Query(
        value = "select distinct wamoliUser from WamoliUser wamoliUser left join fetch wamoliUser.roomAddrs left join fetch wamoliUser.companyDepts",
        countQuery = "select count(distinct wamoliUser) from WamoliUser wamoliUser"
    )
    Page<WamoliUser> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct wamoliUser from WamoliUser wamoliUser left join fetch wamoliUser.roomAddrs left join fetch wamoliUser.companyDepts"
    )
    List<WamoliUser> findAllWithEagerRelationships();

    @Query(
        "select wamoliUser from WamoliUser wamoliUser left join fetch wamoliUser.roomAddrs left join fetch wamoliUser.companyDepts where wamoliUser.id =:id"
    )
    Optional<WamoliUser> findOneWithEagerRelationships(@Param("id") Long id);

    Optional<WamoliUser> findOneByEmailAndPhoneNumAndUserIsNotNullAndEnableIsFalse(String email, String phoneNum);

    int deleteByPhoneNum(String phoneNum);
}
