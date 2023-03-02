package com.web.rest;

import com.repository.TemplateTypeRepository;
import com.service.TemplateTypeService;
import com.service.dto.TemplateTypeDTO;
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
 * REST controller for managing {@link com.domain.TemplateType}.
 */
@RestController
@RequestMapping("/api")
public class TemplateTypeResource {

    private final Logger log = LoggerFactory.getLogger(TemplateTypeResource.class);

    private static final String ENTITY_NAME = "templateType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplateTypeService templateTypeService;

    private final TemplateTypeRepository templateTypeRepository;

    public TemplateTypeResource(TemplateTypeService templateTypeService, TemplateTypeRepository templateTypeRepository) {
        this.templateTypeService = templateTypeService;
        this.templateTypeRepository = templateTypeRepository;
    }

    /**
     * {@code POST  /template-types} : Create a new templateType.
     *
     * @param templateTypeDTO the templateTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new templateTypeDTO, or with status {@code 400 (Bad Request)} if the templateType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/template-types")
    public ResponseEntity<TemplateTypeDTO> createTemplateType(@RequestBody TemplateTypeDTO templateTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TemplateType : {}", templateTypeDTO);
        if (templateTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new templateType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TemplateTypeDTO result = templateTypeService.save(templateTypeDTO);
        return ResponseEntity
            .created(new URI("/api/template-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /template-types/:id} : Updates an existing templateType.
     *
     * @param id the id of the templateTypeDTO to save.
     * @param templateTypeDTO the templateTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateTypeDTO,
     * or with status {@code 400 (Bad Request)} if the templateTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the templateTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/template-types/{id}")
    public ResponseEntity<TemplateTypeDTO> updateTemplateType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TemplateTypeDTO templateTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TemplateType : {}, {}", id, templateTypeDTO);
        if (templateTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TemplateTypeDTO result = templateTypeService.update(templateTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, templateTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /template-types/:id} : Partial updates given fields of an existing templateType, field will ignore if it is null
     *
     * @param id the id of the templateTypeDTO to save.
     * @param templateTypeDTO the templateTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated templateTypeDTO,
     * or with status {@code 400 (Bad Request)} if the templateTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the templateTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the templateTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/template-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TemplateTypeDTO> partialUpdateTemplateType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TemplateTypeDTO templateTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TemplateType partially : {}, {}", id, templateTypeDTO);
        if (templateTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, templateTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TemplateTypeDTO> result = templateTypeService.partialUpdate(templateTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, templateTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /template-types} : get all the templateTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templateTypes in body.
     */
    @GetMapping("/template-types")
    public List<TemplateTypeDTO> getAllTemplateTypes() {
        log.debug("REST request to get all TemplateTypes");
        return templateTypeService.findAll();
    }

    /**
     * {@code GET  /template-types/:id} : get the "id" templateType.
     *
     * @param id the id of the templateTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the templateTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/template-types/{id}")
    public ResponseEntity<TemplateTypeDTO> getTemplateType(@PathVariable Long id) {
        log.debug("REST request to get TemplateType : {}", id);
        Optional<TemplateTypeDTO> templateTypeDTO = templateTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(templateTypeDTO);
    }

    /**
     * {@code DELETE  /template-types/:id} : delete the "id" templateType.
     *
     * @param id the id of the templateTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/template-types/{id}")
    public ResponseEntity<Void> deleteTemplateType(@PathVariable Long id) {
        log.debug("REST request to delete TemplateType : {}", id);
        templateTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
