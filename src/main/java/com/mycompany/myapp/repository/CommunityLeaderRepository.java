package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityLeader;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommunityLeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityLeaderRepository extends JpaRepository<CommunityLeader, Long>, JpaSpecificationExecutor<CommunityLeader> {}
