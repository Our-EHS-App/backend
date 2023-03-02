package com.repository;

import com.domain.Form;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {}
