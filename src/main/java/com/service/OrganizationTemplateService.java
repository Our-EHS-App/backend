package com.service;

import com.domain.Location;
import com.domain.Organization;
import com.domain.OrganizationTemplate;
import com.domain.Template;
import com.repository.OrganizationTemplateRepository;
import com.service.dto.*;
import com.service.impl.FormServiceImpl;
import com.service.impl.LocationServiceImpl;
import com.service.impl.TemplateServiceImpl;
import com.service.mapper.LocationMapper;
import com.service.mapper.TemplateMapper;
import com.web.rest.errors.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;

@Service
public class OrganizationTemplateService {
    private final Logger log = LoggerFactory.getLogger(OrganizationTemplateService.class);
    private final OrganizationTemplateRepository organizationTemplateRepository;
    private final OrganizationService organizationService;
    private final TemplateServiceImpl templateService;
    private final LocationServiceImpl locationService;
    private final FormServiceImpl formService;
    private final TemplateMapper templateMapper;
    private final LocationMapper locationMapper;

    public OrganizationTemplateService(OrganizationTemplateRepository organizationTemplateRepository, OrganizationService organizationService, TemplateServiceImpl templateService, LocationServiceImpl locationService, FormServiceImpl formService, TemplateMapper templateMapper, LocationMapper locationMapper) {
        this.organizationTemplateRepository = organizationTemplateRepository;
        this.organizationService = organizationService;
        this.templateService = templateService;
        this.locationService = locationService;
        this.formService = formService;
        this.templateMapper = templateMapper;
        this.locationMapper = locationMapper;
    }

    @Transactional
    public void importTemplateToOrg(ImportOrgTemplateDTO dto)  {
        try{
            Set<Location> locations = checkLocationBelongToOrg(dto);
        Organization organization = organizationService.getById(dto.getOrgId());
        Template template = templateService.getById(dto.getTemplateId());
        OrganizationTemplate organizationTemplate = new OrganizationTemplate(organization, template, locations);
        organizationTemplateRepository.saveAndFlush(organizationTemplate);
        }catch (CustomException ex){
            throw ex;
        }
        //todo throw exception message that cant link location with the same template
        catch (Exception e){
            throw new CustomException("Can not import template!","لا يمكن ربط المنشأة مع النموذج","Cant.import");
        }
        //todo generate form then return it
    }
    @Transactional
    public void update(ImportOrgTemplateDTO dto){
        OrganizationTemplate organizationTemplate = organizationTemplateRepository.findByOrganization_IdAndTemplate_Id(dto.getOrgId(), dto.getTemplateId())
            .orElseThrow(() -> new CustomException("Not found!","غير موجود!","not.found"));
        Set<Location> locations = checkLocationBelongToOrg(dto);
        organizationTemplate.setLocations(locations);
        organizationTemplateRepository.saveAndFlush(organizationTemplate);
    }

    public GetAllOrgTemplatesDTO getAllByOrgId(Long id){
        List<OrganizationTemplate> list = organizationTemplateRepository.findAllByOrganization_id(id);
        GetAllOrgTemplatesDTO dto = new GetAllOrgTemplatesDTO();
        List<TemplateLocationsDTO> templateLocationsDTOS = list.stream()
            .map(i ->{
                TemplateDTO templateDTO = templateMapper.toDto(i.getTemplate());
                List<LocationDTO> locationDTO = i.getLocations()
                    .stream()
                    .map(locationMapper::toDto)
                    .collect(Collectors.toList());

                return  new TemplateLocationsDTO(templateDTO,locationDTO);
            })
                .collect(Collectors.toList());
        dto.setTemplateLocationsDTOS(templateLocationsDTOS);
        dto.setOrganizationDTO(organizationService.getByIdToDto(id));

        return dto;
    }

    private Set<Location> checkLocationBelongToOrg(ImportOrgTemplateDTO dto){
         return dto.getLocationIds()
            .stream()
            .map(locationService::getById)
            .filter(location -> location.getOrganization().getId().equals(dto.getOrgId()))
            .collect(Collectors.toSet());
    }
    //todo add location to OrganizationTemplate

    public FormDTO getLatestByTemplateId(Long templateId){
        return formService.findLatestByTemplateId(templateId);
    }
}
