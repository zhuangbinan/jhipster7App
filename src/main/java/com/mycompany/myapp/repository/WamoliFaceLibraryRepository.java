package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WamoliFaceLibrary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WamoliFaceLibrary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WamoliFaceLibraryRepository extends JpaRepository<WamoliFaceLibrary, Long> {}
