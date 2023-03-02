package com.service;

import com.service.dto.FieldTypeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.domain.FieldType}.
 */
public interface FieldTypeService {
    /**
     * Save a fieldType.
     *
     * @param fieldTypeDTO the entity to save.
     * @return the persisted entity.
     */
    FieldTypeDTO save(FieldTypeDTO fieldTypeDTO);

    /**
     * Updates a fieldType.
     *
     * @param fieldTypeDTO the entity to update.
     * @return the persisted entity.
     */
    FieldTypeDTO update(FieldTypeDTO fieldTypeDTO);

    /**
     * Partially updates a fieldType.
     *
     * @param fieldTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FieldTypeDTO> partialUpdate(FieldTypeDTO fieldTypeDTO);

    /**
     * Get all the fieldTypes.
     *
     * @return the list of entities.
     */
    List<FieldTypeDTO> findAll();

    /**
     * Get the "id" fieldType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FieldTypeDTO> findOne(Long id);

    /**
     * Delete the "id" fieldType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
