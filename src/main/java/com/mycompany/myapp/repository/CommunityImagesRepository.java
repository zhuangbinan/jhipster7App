package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityImages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommunityImages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityImagesRepository extends JpaRepository<CommunityImages, Long>, JpaSpecificationExecutor<CommunityImages> {}
