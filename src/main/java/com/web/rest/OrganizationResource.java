package com.web.rest;

import com.service.dto.OrganizationDTO;
import com.service.OrganizationService;
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
@RequestMapping("/api/organizations")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);
    private final OrganizationService organizationService;

    public OrganizationResource(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping("/register")
    // todo accept anonymous
    public ResponseEntity<OrganizationDTO> register(@RequestBody OrganizationDTO dto){
        log.debug("REST request to register Organization");
        OrganizationDTO result=organizationService.register(dto);
        return ok().body(result);
    }



    @GetMapping
    public ResponseEntity<List<OrganizationDTO>> getAll(@ParameterObject Pageable pageable){
        log.debug("REST request to get a page of Organizations");
        Page<OrganizationDTO> page = organizationService.getAllOrgs(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
