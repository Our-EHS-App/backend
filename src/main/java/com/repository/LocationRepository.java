package com.repository;

import com.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByOrganization_id(Long orgId);
    Page<Location> findAllByOrganization_id(Long orgId, Pageable pageable);
}
