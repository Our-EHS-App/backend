package com.service;

import com.domain.Form;
import com.service.dto.FormDTO;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.service.dto.SubmitFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.domain.Form}.
 */
public interface FormService {
    /**
     * Save a form.
     *
     * @param formDTO the entity to save.
     * @return the persisted entity.
     */
    FormDTO save(FormDTO formDTO);

    /**
     * Updates a form.
     *
     * @param formDTO the entity to update.
     * @return the persisted entity.
     */
    FormDTO update(FormDTO formDTO);

    /**
     * Partially updates a form.
     *
     * @param formDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormDTO> partialUpdate(FormDTO formDTO);

    /**
     * Get all the forms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormDTO> findAll(Pageable pageable);

    /**
     * Get the "id" form.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormDTO> findOne(Long id);

    /**
     * Delete the "id" form.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<Form> generateForm(Long orgTempId) throws ExecutionException, InterruptedException;
    void submitForm(SubmitFormDTO values);
    Page<FormDTO> getAllByOrg(Long orgId, Pageable pageable);
}
