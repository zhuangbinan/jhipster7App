package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TuYaCmd;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TuYaCmd entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TuYaCmdRepository extends JpaRepository<TuYaCmd, Long>, JpaSpecificationExecutor<TuYaCmd> {}
