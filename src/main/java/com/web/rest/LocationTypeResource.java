package com.web.rest;

import com.repository.LocationTypeRepository;
import com.service.LocationTypeService;
import com.service.dto.LocationTypeDTO;
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
 * REST controller for managing {@link com.domain.LocationType}.
 */
@RestController
@RequestMapping("/api")
public class LocationTypeResource {

    private final Logger log = LoggerFactory.getLogger(LocationTypeResource.class);

    private static final String ENTITY_NAME = "locationType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationTypeService locationTypeService;

    private final LocationTypeRepository locationTypeRepository;

    public LocationTypeResource(LocationTypeService locationTypeService, LocationTypeRepository locationTypeRepository) {
        this.locationTypeService = locationTypeService;
        this.locationTypeRepository = locationTypeRepository;
    }

    /**
     * {@code POST  /location-types} : Create a new locationType.
     *
     * @param locationTypeDTO the locationTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationTypeDTO, or with status {@code 400 (Bad Request)} if the locationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-types")
    public ResponseEntity<LocationTypeDTO> createLocationType(@RequestBody LocationTypeDTO locationTypeDTO) throws URISyntaxException {
        log.debug("REST request to save LocationType : {}", locationTypeDTO);
        if (locationTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new locationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationTypeDTO result = locationTypeService.save(locationTypeDTO);
        return ResponseEntity
            .created(new URI("/api/location-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-types/:id} : Updates an existing locationType.
     *
     * @param id the id of the locationTypeDTO to save.
     * @param locationTypeDTO the locationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the locationTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-types/{id}")
    public ResponseEntity<LocationTypeDTO> updateLocationType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationTypeDTO locationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LocationType : {}, {}", id, locationTypeDTO);
        if (locationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationTypeDTO result = locationTypeService.update(locationTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-types/:id} : Partial updates given fields of an existing locationType, field will ignore if it is null
     *
     * @param id the id of the locationTypeDTO to save.
     * @param locationTypeDTO the locationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the locationTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the locationTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocationTypeDTO> partialUpdateLocationType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationTypeDTO locationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationType partially : {}, {}", id, locationTypeDTO);
        if (locationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationTypeDTO> result = locationTypeService.partialUpdate(locationTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /location-types} : get all the locationTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationTypes in body.
     */
    @GetMapping("/location-types")
    public List<LocationTypeDTO> getAllLocationTypes() {
        log.debug("REST request to get all LocationTypes");
        return locationTypeService.findAll();
    }

    /**
     * {@code GET  /location-types/:id} : get the "id" locationType.
     *
     * @param id the id of the locationTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-types/{id}")
    public ResponseEntity<LocationTypeDTO> getLocationType(@PathVariable Long id) {
        log.debug("REST request to get LocationType : {}", id);
        Optional<LocationTypeDTO> locationTypeDTO = locationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationTypeDTO);
    }

    /**
     * {@code DELETE  /location-types/:id} : delete the "id" locationType.
     *
     * @param id the id of the locationTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-types/{id}")
    public ResponseEntity<Void> deleteLocationType(@PathVariable Long id) {
        log.debug("REST request to delete LocationType : {}", id);
        locationTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
