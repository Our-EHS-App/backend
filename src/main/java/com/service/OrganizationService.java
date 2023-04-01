package com.service;

import com.domain.Organization;
import com.domain.OrganizationType;
import com.repository.OrganizationRepository;
import com.repository.OrganizationTypeRepository;
import com.service.dto.OrganizationDTO;
import com.web.rest.errors.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class OrganizationService {
    private final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    public final OrganizationRepository organizationRepository;
    public final OrganizationTypeRepository organizationTypeRepository;
    public final UserService userService;


    public OrganizationService(OrganizationRepository organizationRepository, OrganizationTypeRepository organizationTypeRepository, UserService userService) {
        this.organizationRepository = organizationRepository;
        this.organizationTypeRepository = organizationTypeRepository;
        this.userService = userService;
    }

    @Transactional
    public OrganizationDTO register(OrganizationDTO dto){
        checkOrgExists(dto);
        OrganizationType organizationType = organizationTypeRepository.findById(dto.getOrganizationTypeId())
            .orElseThrow(()->  new CustomException("Organization Type not found","نوع المنشأة غير موجود","not.found"));
        Organization organization = new Organization(dto.getNameAr(), dto.getNameEn(), dto.getEmail(), organizationType);
        organization = organizationRepository.saveAndFlush(organization);
        dto.getMainUser().setOrganizationId(organization.getId());
        userService.registerUser(dto.getMainUser(), dto.getMainUser().getPassword(), organization);
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

    public Organization getById(Long id){
        if(Objects.isNull(id)) {
            throw new CustomException("Organization not found!","المنشأة غير موجودة!","not.found");
        }
        return organizationRepository.findById(id)
            .orElseThrow(()->new CustomException("Organization not found!","المنشأة غير موجودة!","not.found"));
    }

    public OrganizationDTO getByIdToDto(Long id){
        Organization org = getById(id);
        return new OrganizationDTO(
            org.getId(), org.getNameAr(), org.getNameEn(), org.getEmail(), org.getOrganizationType().getId()
        );
    }

}
