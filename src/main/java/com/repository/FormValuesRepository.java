package com.repository;

import com.domain.Field;
import com.domain.Form;
import com.domain.FormValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormValuesRepository extends JpaRepository<FormValues, Long> {
    Optional<FormValues> findByFormAndField(Form form, Field field);
}

