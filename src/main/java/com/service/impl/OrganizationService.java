package com.service.impl;

import com.domain.Organization;
import com.domain.OrganizationType;
import com.repository.OrganizationRepository;
import com.repository.OrganizationTypeRepository;
import com.service.EmailAlreadyUsedException;
import com.service.dto.OrganizationDTO;
import com.web.rest.OrganizationResource;
import com.web.rest.errors.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService {
    private final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    public final OrganizationRepository organizationRepository;
    public final OrganizationTypeRepository organizationTypeRepository;


    public OrganizationService(OrganizationRepository organizationRepository, OrganizationTypeRepository organizationTypeRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationTypeRepository = organizationTypeRepository;
    }

    @Transactional
    public OrganizationDTO register(OrganizationDTO dto){
        checkOrgExists(dto);
        OrganizationType organizationType = organizationTypeRepository.findById(dto.getOrganizationTypeId())
            .orElseThrow(()->  new CustomException("Organization Type not found","نوع المنشأة غير موجود","not.found"));
        Organization organization = new Organization(dto.getNameAr(), dto.getNameEn(), dto.getEmail(), organizationType);
        organization = organizationRepository.saveAndFlush(organization);
        dto.setId(organization.getId());;
        return dto;
    }

    private void checkOrgExists(OrganizationDTO dto){
        if(organizationRepository.existsByEmail(dto.getEmail()))
            throw new EmailAlreadyUsedException();
    }

    public Page<OrganizationDTO> getAllOrgs(Pageable pageable){
        return organizationRepository.findAll(pageable).map(org -> new OrganizationDTO(
            org.getId(), org.getNameAr(), org.getNameEn(), org.getEmail(), org.getOrganizationType().getId()
        ));
    }
}
