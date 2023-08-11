package com.repository;

import com.domain.Form;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    // todo add pagination
    List<Form> findAllByOrganizationTemplate_Organization_Id(Long id);
}
