package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityImageGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommunityImageGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityImageGroupRepository
    extends JpaRepository<CommunityImageGroup, Long>, JpaSpecificationExecutor<CommunityImageGroup> {}
