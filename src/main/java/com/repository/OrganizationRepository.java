package com.repository;

import com.domain.Organization;
import com.domain.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Template entity.
 *
 * When extending this class, extend TemplateRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface OrganizationRepository extends  JpaRepository<Organization, Long> {
    boolean existsByEmail(String email);

}
