package com.service;

import com.service.dto.TemplateTypeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.domain.TemplateType}.
 */
public interface TemplateTypeService {
    /**
     * Save a templateType.
     *
     * @param templateTypeDTO the entity to save.
     * @return the persisted entity.
     */
    TemplateTypeDTO save(TemplateTypeDTO templateTypeDTO);

    /**
     * Updates a templateType.
     *
     * @param templateTypeDTO the entity to update.
     * @return the persisted entity.
     */
    TemplateTypeDTO update(TemplateTypeDTO templateTypeDTO);

    /**
     * Partially updates a templateType.
     *
     * @param templateTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TemplateTypeDTO> partialUpdate(TemplateTypeDTO templateTypeDTO);

    /**
     * Get all the templateTypes.
     *
     * @return the list of entities.
     */
    List<TemplateTypeDTO> findAll();

    /**
     * Get the "id" templateType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TemplateTypeDTO> findOne(Long id);

    /**
     * Delete the "id" templateType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
