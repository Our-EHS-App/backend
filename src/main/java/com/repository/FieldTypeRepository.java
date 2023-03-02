package com.repository;

import com.domain.FieldType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FieldType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldTypeRepository extends JpaRepository<FieldType, Long> {}
