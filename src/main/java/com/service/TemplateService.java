package com.service;

import com.service.dto.TemplateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.domain.Template}.
 */
public interface TemplateService {
    /**
     * Save a template.
     *
     * @param templateDTO the entity to save.
     * @return the persisted entity.
     */
    TemplateDTO save(TemplateDTO templateDTO);

    /**
     * Updates a template.
     *
     * @param templateDTO the entity to update.
     * @return the persisted entity.
     */
    TemplateDTO update(TemplateDTO templateDTO);

    /**
     * Partially updates a template.
     *
     * @param templateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TemplateDTO> partialUpdate(TemplateDTO templateDTO);

    /**
     * Get all the templates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TemplateDTO> findAll(Pageable pageable);

    /**
     * Get all the templates with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TemplateDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" template.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TemplateDTO> findOne(Long id);

    /**
     * Delete the "id" template.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
