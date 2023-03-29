package com.repository;

import com.domain.OrganizationType;
import com.domain.TemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TemplateType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, Long> {

}
