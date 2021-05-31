package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Test01;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Test01 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Test01Repository extends JpaRepository<Test01, Long>, JpaSpecificationExecutor<Test01> {}
