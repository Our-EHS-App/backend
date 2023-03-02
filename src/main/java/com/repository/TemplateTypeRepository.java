package com.repository;

import com.domain.TemplateType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TemplateType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemplateTypeRepository extends JpaRepository<TemplateType, Long> {}
