package com.web.rest;

import com.repository.LocationStatusRepository;
import com.service.LocationStatusService;
import com.service.dto.LocationStatusDTO;
import com.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.domain.LocationStatus}.
 */
@RestController
@RequestMapping("/api")
public class LocationStatusResource {

    private final Logger log = LoggerFactory.getLogger(LocationStatusResource.class);

    private static final String ENTITY_NAME = "locationStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationStatusService locationStatusService;

    private final LocationStatusRepository locationStatusRepository;

    public LocationStatusResource(LocationStatusService locationStatusService, LocationStatusRepository locationStatusRepository) {
        this.locationStatusService = locationStatusService;
        this.locationStatusRepository = locationStatusRepository;
    }

    /**
     * {@code POST  /location-statuses} : Create a new locationStatus.
     *
     * @param locationStatusDTO the locationStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationStatusDTO, or with status {@code 400 (Bad Request)} if the locationStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-statuses")
    public ResponseEntity<LocationStatusDTO> createLocationStatus(@RequestBody LocationStatusDTO locationStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save LocationStatus : {}", locationStatusDTO);
        if (locationStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new locationStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationStatusDTO result = locationStatusService.save(locationStatusDTO);
        return ResponseEntity
            .created(new URI("/api/location-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-statuses/:id} : Updates an existing locationStatus.
     *
     * @param id the id of the locationStatusDTO to save.
     * @param locationStatusDTO the locationStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationStatusDTO,
     * or with status {@code 400 (Bad Request)} if the locationStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-statuses/{id}")
    public ResponseEntity<LocationStatusDTO> updateLocationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationStatusDTO locationStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocationStatus : {}, {}", id, locationStatusDTO);
        if (locationStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationStatusDTO result = locationStatusService.update(locationStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-statuses/:id} : Partial updates given fields of an existing locationStatus, field will ignore if it is null
     *
     * @param id the id of the locationStatusDTO to save.
     * @param locationStatusDTO the locationStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationStatusDTO,
     * or with status {@code 400 (Bad Request)} if the locationStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the locationStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocationStatusDTO> partialUpdateLocationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationStatusDTO locationStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationStatus partially : {}, {}", id, locationStatusDTO);
        if (locationStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationStatusDTO> result = locationStatusService.partialUpdate(locationStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /location-statuses} : get all the locationStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationStatuses in body.
     */
    @GetMapping("/location-statuses")
    public List<LocationStatusDTO> getAllLocationStatuses() {
        log.debug("REST request to get all LocationStatuses");
        return locationStatusService.findAll();
    }

    /**
     * {@code GET  /location-statuses/:id} : get the "id" locationStatus.
     *
     * @param id the id of the locationStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-statuses/{id}")
    public ResponseEntity<LocationStatusDTO> getLocationStatus(@PathVariable Long id) {
        log.debug("REST request to get LocationStatus : {}", id);
        Optional<LocationStatusDTO> locationStatusDTO = locationStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationStatusDTO);
    }

    /**
     * {@code DELETE  /location-statuses/:id} : delete the "id" locationStatus.
     *
     * @param id the id of the locationStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-statuses/{id}")
    public ResponseEntity<Void> deleteLocationStatus(@PathVariable Long id) {
        log.debug("REST request to delete LocationStatus : {}", id);
        locationStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
