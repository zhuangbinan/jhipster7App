package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CompanyPost;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompanyPost entity.
 */
@Repository
public interface CompanyPostRepository extends JpaRepository<CompanyPost, Long>, JpaSpecificationExecutor<CompanyPost> {
    @Query(
        value = "select distinct companyPost from CompanyPost companyPost left join fetch companyPost.wamoliUsers",
        countQuery = "select count(distinct companyPost) from CompanyPost companyPost"
    )
    Page<CompanyPost> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct companyPost from CompanyPost companyPost left join fetch companyPost.wamoliUsers")
    List<CompanyPost> findAllWithEagerRelationships();

    @Query("select companyPost from CompanyPost companyPost left join fetch companyPost.wamoliUsers where companyPost.id =:id")
    Optional<CompanyPost> findOneWithEagerRelationships(@Param("id") Long id);
}
