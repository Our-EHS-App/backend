package com.web.rest;

import com.repository.FormStatusRepository;
import com.service.FormStatusService;
import com.service.dto.FormStatusDTO;
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
 * REST controller for managing {@link com.domain.FormStatus}.
 */
@RestController
@RequestMapping("/api")
public class FormStatusResource {

    private final Logger log = LoggerFactory.getLogger(FormStatusResource.class);

    private static final String ENTITY_NAME = "formStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormStatusService formStatusService;

    private final FormStatusRepository formStatusRepository;

    public FormStatusResource(FormStatusService formStatusService, FormStatusRepository formStatusRepository) {
        this.formStatusService = formStatusService;
        this.formStatusRepository = formStatusRepository;
    }

    /**
     * {@code POST  /form-statuses} : Create a new formStatus.
     *
     * @param formStatusDTO the formStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formStatusDTO, or with status {@code 400 (Bad Request)} if the formStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/form-statuses")
    public ResponseEntity<FormStatusDTO> createFormStatus(@RequestBody FormStatusDTO formStatusDTO) throws URISyntaxException {
        log.debug("REST request to save FormStatus : {}", formStatusDTO);
        if (formStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new formStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormStatusDTO result = formStatusService.save(formStatusDTO);
        return ResponseEntity
            .created(new URI("/api/form-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /form-statuses/:id} : Updates an existing formStatus.
     *
     * @param id the id of the formStatusDTO to save.
     * @param formStatusDTO the formStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formStatusDTO,
     * or with status {@code 400 (Bad Request)} if the formStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/form-statuses/{id}")
    public ResponseEntity<FormStatusDTO> updateFormStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormStatusDTO formStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormStatus : {}, {}", id, formStatusDTO);
        if (formStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormStatusDTO result = formStatusService.update(formStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /form-statuses/:id} : Partial updates given fields of an existing formStatus, field will ignore if it is null
     *
     * @param id the id of the formStatusDTO to save.
     * @param formStatusDTO the formStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formStatusDTO,
     * or with status {@code 400 (Bad Request)} if the formStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/form-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormStatusDTO> partialUpdateFormStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormStatusDTO formStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormStatus partially : {}, {}", id, formStatusDTO);
        if (formStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormStatusDTO> result = formStatusService.partialUpdate(formStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /form-statuses} : get all the formStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formStatuses in body.
     */
    @GetMapping("/form-statuses")
    public List<FormStatusDTO> getAllFormStatuses() {
        log.debug("REST request to get all FormStatuses");
        return formStatusService.findAll();
    }

    /**
     * {@code GET  /form-statuses/:id} : get the "id" formStatus.
     *
     * @param id the id of the formStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/form-statuses/{id}")
    public ResponseEntity<FormStatusDTO> getFormStatus(@PathVariable Long id) {
        log.debug("REST request to get FormStatus : {}", id);
        Optional<FormStatusDTO> formStatusDTO = formStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formStatusDTO);
    }

    /**
     * {@code DELETE  /form-statuses/:id} : delete the "id" formStatus.
     *
     * @param id the id of the formStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/form-statuses/{id}")
    public ResponseEntity<Void> deleteFormStatus(@PathVariable Long id) {
        log.debug("REST request to delete FormStatus : {}", id);
        formStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
