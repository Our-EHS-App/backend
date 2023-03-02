package com.repository;

import com.domain.LocationStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LocationStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationStatusRepository extends JpaRepository<LocationStatus, Long> {}
