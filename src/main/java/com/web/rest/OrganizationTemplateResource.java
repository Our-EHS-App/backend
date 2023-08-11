package com.web.rest;

import com.service.OrganizationTemplateService;
import com.service.dto.FormDTO;
import com.service.dto.GetAllOrgTemplatesDTO;
import com.service.dto.ImportOrgTemplateDTO;
import com.service.dto.OrganizationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/organization-template")
public class OrganizationTemplateResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationTemplateResource.class);
    private final OrganizationTemplateService organizationTemplateService;

    public OrganizationTemplateResource(OrganizationTemplateService organizationTemplateService) {
        this.organizationTemplateService = organizationTemplateService;
    }


    @PostMapping("/import")
    public void register(@RequestBody ImportOrgTemplateDTO dto){
        log.debug("REST request to register Organization");
        organizationTemplateService.importTemplateToOrg(dto);

    }

    @PostMapping("/update")
    public void update(@RequestBody ImportOrgTemplateDTO dto){
        log.debug("REST request to register Organization");
        organizationTemplateService.update(dto);
    }


    @GetMapping("/get-all-by-org-id/{id}")
    public ResponseEntity<GetAllOrgTemplatesDTO> getAll(@PathVariable Long id){
        //todo add pagination
        log.debug("REST request to get a page of Organizations");
        GetAllOrgTemplatesDTO dto = organizationTemplateService.getAllByOrgId(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/get-by-template-id/{id}")
    public ResponseEntity<FormDTO> getByTemplateId(@PathVariable Long id){
        //todo add pagination
        log.debug("REST request to get a Latest form by template id: {}", id);
        FormDTO dto = organizationTemplateService.getLatestByTemplateId(id);
        return ResponseEntity.ok().body(dto);
    }
}
