package com.web.rest;

import com.domain.Form;
import com.repository.FormRepository;
import com.service.FormService;
import com.service.dto.FormDTO;
import com.service.dto.SubmitFormDTO;
import com.service.util.TokenUtils;
import com.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing {@link com.domain.Form}.
 */
@RestController
//todo add /forms
@RequestMapping("/api")
public class FormResource {

    private final Logger log = LoggerFactory.getLogger(FormResource.class);

    private static final String ENTITY_NAME = "form";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormService formService;

    private final FormRepository formRepository;
    private final TokenUtils tokenUtils;


    public FormResource(FormService formService, FormRepository formRepository, TokenUtils tokenUtils) {
        this.formService = formService;
        this.formRepository = formRepository;
        this.tokenUtils = tokenUtils;
    }

    /**
     * {@code POST  /forms} : Create a new form.
     *
     * @param formDTO the formDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formDTO, or with status {@code 400 (Bad Request)} if the form has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/forms")
    public ResponseEntity<FormDTO> createForm(@RequestBody FormDTO formDTO) throws URISyntaxException {
        log.debug("REST request to save Form : {}", formDTO);
        if (formDTO.getId() != null) {
            throw new BadRequestAlertException("A new form cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormDTO result = formService.save(formDTO);
        return ResponseEntity
            .created(new URI("/api/forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /forms/:id} : Updates an existing form.
     *
     * @param id the id of the formDTO to save.
     * @param formDTO the formDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formDTO,
     * or with status {@code 400 (Bad Request)} if the formDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/forms/{id}")
    public ResponseEntity<FormDTO> updateForm(@PathVariable(value = "id", required = false) final Long id, @RequestBody FormDTO formDTO)
        throws URISyntaxException {
        log.debug("REST request to update Form : {}, {}", id, formDTO);
        if (formDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormDTO result = formService.update(formDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /forms/:id} : Partial updates given fields of an existing form, field will ignore if it is null
     *
     * @param id the id of the formDTO to save.
     * @param formDTO the formDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formDTO,
     * or with status {@code 400 (Bad Request)} if the formDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/forms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormDTO> partialUpdateForm(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormDTO formDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Form partially : {}, {}", id, formDTO);
        if (formDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormDTO> result = formService.partialUpdate(formDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /forms} : get all the forms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of forms in body.
     */
    @GetMapping("/forms")
    public ResponseEntity<List<FormDTO>> getAllForms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Forms");
        Page<FormDTO> page = formService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /forms/:id} : get the "id" form.
     *
     * @param id the id of the formDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/forms/{id}")
    public ResponseEntity<FormDTO> getForm(@PathVariable Long id) {
        log.debug("REST request to get Form : {}", id);
        Optional<FormDTO> formDTO = formService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formDTO);
    }

    /**
     * {@code DELETE  /forms/:id} : delete the "id" form.
     *
     * @param id the id of the formDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/forms/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        log.debug("REST request to delete Form : {}", id);
        formService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/generate-form/{id}")
    public ResponseEntity<List<Form>> generateForm(@PathVariable("id") Long orgTempId) throws ExecutionException, InterruptedException {
        log.debug("REST request to generate Form from OrganizationTemplate id: {}", orgTempId);
        Optional<List<Form>> result = Optional.ofNullable(formService.generateForm(orgTempId));
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orgTempId.toString())
        );
    }

    @PostMapping("/submit-form")
    public void submitForm( @RequestBody SubmitFormDTO values){
        log.debug("REST request to submit Form id: {}", values.getFormId());
        formService.submitForm(values);

    }

    @GetMapping("/get-my-forms")
    public ResponseEntity<List<FormDTO>> getAllByOrg(@ParameterObject Pageable pageable, HttpServletRequest request){
        log.debug("REST request to get all my Forms");

        String orgId = tokenUtils.getOrgId(request);
        return ResponseEntity.ok().body(formService.getAllByOrg(Long.valueOf(orgId), pageable));
    }
}
