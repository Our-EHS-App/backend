package com.service;

import com.service.dto.LocationStatusDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.domain.LocationStatus}.
 */
public interface LocationStatusService {
    /**
     * Save a locationStatus.
     *
     * @param locationStatusDTO the entity to save.
     * @return the persisted entity.
     */
    LocationStatusDTO save(LocationStatusDTO locationStatusDTO);

    /**
     * Updates a locationStatus.
     *
     * @param locationStatusDTO the entity to update.
     * @return the persisted entity.
     */
    LocationStatusDTO update(LocationStatusDTO locationStatusDTO);

    /**
     * Partially updates a locationStatus.
     *
     * @param locationStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LocationStatusDTO> partialUpdate(LocationStatusDTO locationStatusDTO);

    /**
     * Get all the locationStatuses.
     *
     * @return the list of entities.
     */
    List<LocationStatusDTO> findAll();

    /**
     * Get the "id" locationStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LocationStatusDTO> findOne(Long id);

    /**
     * Delete the "id" locationStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
