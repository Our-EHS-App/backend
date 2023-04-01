package com.repository;

import com.domain.OrganizationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Template entity.
 *
 * When extending this class, extend TemplateRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface OrganizationTemplateRepository extends  JpaRepository<OrganizationTemplate, Long> {

    List<OrganizationTemplate> findAllByOrganization_id(Long id);
}
