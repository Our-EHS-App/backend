package com.web.rest;

import com.service.OrganizationTemplateService;
import com.service.dto.FormDTO;
import com.service.dto.GetAllOrgTemplatesDTO;
import com.service.dto.ImportOrgTemplateDTO;
import com.service.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/organization-template")
public class OrganizationTemplateResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationTemplateResource.class);
    private final OrganizationTemplateService organizationTemplateService;
    private final TokenUtils tokenUtils;

    public OrganizationTemplateResource(OrganizationTemplateService organizationTemplateService, TokenUtils tokenUtils) {
        this.organizationTemplateService = organizationTemplateService;
        this.tokenUtils = tokenUtils;
    }


    @PostMapping("/import")
    public void register(@RequestBody ImportOrgTemplateDTO dto, HttpServletRequest request){
        log.debug("REST request to register Organization");
        organizationTemplateService.importTemplateToOrg(dto, request);

    }

    @PostMapping("/update")
    public void update(@RequestBody ImportOrgTemplateDTO dto){
        log.debug("REST request to register Organization");
        organizationTemplateService.update(dto);
    }


    @GetMapping("/get-my_templates")
    public ResponseEntity<GetAllOrgTemplatesDTO> getAll(@ParameterObject Pageable pageable, HttpServletRequest request){
        //todo add pagination
        log.debug("REST request to get a page of Organizations");
        String orgId = tokenUtils.getOrgId(request);
        GetAllOrgTemplatesDTO dto = organizationTemplateService.getAllByOrgId(Long.parseLong(orgId), pageable);
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
