package com.web.rest;

import com.repository.FieldTypeRepository;
import com.service.FieldTypeService;
import com.service.dto.FieldTypeDTO;
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
 * REST controller for managing {@link com.domain.FieldType}.
 */
@RestController
@RequestMapping("/api")
public class FieldTypeResource {

    private final Logger log = LoggerFactory.getLogger(FieldTypeResource.class);

    private static final String ENTITY_NAME = "fieldType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FieldTypeService fieldTypeService;

    private final FieldTypeRepository fieldTypeRepository;

    public FieldTypeResource(FieldTypeService fieldTypeService, FieldTypeRepository fieldTypeRepository) {
        this.fieldTypeService = fieldTypeService;
        this.fieldTypeRepository = fieldTypeRepository;
    }

    /**
     * {@code POST  /field-types} : Create a new fieldType.
     *
     * @param fieldTypeDTO the fieldTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldTypeDTO, or with status {@code 400 (Bad Request)} if the fieldType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/field-types")
    public ResponseEntity<FieldTypeDTO> createFieldType(@RequestBody FieldTypeDTO fieldTypeDTO) throws URISyntaxException {
        log.debug("REST request to save FieldType : {}", fieldTypeDTO);
        if (fieldTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new fieldType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FieldTypeDTO result = fieldTypeService.save(fieldTypeDTO);
        return ResponseEntity
            .created(new URI("/api/field-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /field-types/:id} : Updates an existing fieldType.
     *
     * @param id the id of the fieldTypeDTO to save.
     * @param fieldTypeDTO the fieldTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fieldTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/field-types/{id}")
    public ResponseEntity<FieldTypeDTO> updateFieldType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FieldTypeDTO fieldTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FieldType : {}, {}", id, fieldTypeDTO);
        if (fieldTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FieldTypeDTO result = fieldTypeService.update(fieldTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fieldTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /field-types/:id} : Partial updates given fields of an existing fieldType, field will ignore if it is null
     *
     * @param id the id of the fieldTypeDTO to save.
     * @param fieldTypeDTO the fieldTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fieldTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fieldTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fieldTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/field-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FieldTypeDTO> partialUpdateFieldType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FieldTypeDTO fieldTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FieldType partially : {}, {}", id, fieldTypeDTO);
        if (fieldTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fieldTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FieldTypeDTO> result = fieldTypeService.partialUpdate(fieldTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fieldTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /field-types} : get all the fieldTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fieldTypes in body.
     */
    @GetMapping("/field-types")
    public List<FieldTypeDTO> getAllFieldTypes() {
        log.debug("REST request to get all FieldTypes");
        return fieldTypeService.findAll();
    }

    /**
     * {@code GET  /field-types/:id} : get the "id" fieldType.
     *
     * @param id the id of the fieldTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/field-types/{id}")
    public ResponseEntity<FieldTypeDTO> getFieldType(@PathVariable Long id) {
        log.debug("REST request to get FieldType : {}", id);
        Optional<FieldTypeDTO> fieldTypeDTO = fieldTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fieldTypeDTO);
    }

    /**
     * {@code DELETE  /field-types/:id} : delete the "id" fieldType.
     *
     * @param id the id of the fieldTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/field-types/{id}")
    public ResponseEntity<Void> deleteFieldType(@PathVariable Long id) {
        log.debug("REST request to delete FieldType : {}", id);
        fieldTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
