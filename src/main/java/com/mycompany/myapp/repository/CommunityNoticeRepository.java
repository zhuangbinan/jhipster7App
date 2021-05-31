package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityNotice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommunityNotice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityNoticeRepository extends JpaRepository<CommunityNotice, Long>, JpaSpecificationExecutor<CommunityNotice> {}
