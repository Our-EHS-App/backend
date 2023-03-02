package com.service;

import com.service.dto.FormStatusDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.domain.FormStatus}.
 */
public interface FormStatusService {
    /**
     * Save a formStatus.
     *
     * @param formStatusDTO the entity to save.
     * @return the persisted entity.
     */
    FormStatusDTO save(FormStatusDTO formStatusDTO);

    /**
     * Updates a formStatus.
     *
     * @param formStatusDTO the entity to update.
     * @return the persisted entity.
     */
    FormStatusDTO update(FormStatusDTO formStatusDTO);

    /**
     * Partially updates a formStatus.
     *
     * @param formStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormStatusDTO> partialUpdate(FormStatusDTO formStatusDTO);

    /**
     * Get all the formStatuses.
     *
     * @return the list of entities.
     */
    List<FormStatusDTO> findAll();

    /**
     * Get the "id" formStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormStatusDTO> findOne(Long id);

    /**
     * Delete the "id" formStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
