package com.repository;

import com.domain.LocationType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LocationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationTypeRepository extends JpaRepository<LocationType, Long> {}
