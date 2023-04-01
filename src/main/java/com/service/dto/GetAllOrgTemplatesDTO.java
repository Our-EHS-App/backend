package com.service.dto;

import java.util.List;

public class GetAllOrgTemplatesDTO {
    private OrganizationDTO organizationDTO;
    private List<TemplateLocationsDTO> templateLocationsDTOS;

    public GetAllOrgTemplatesDTO(OrganizationDTO organizationDTO, List<TemplateLocationsDTO> templateLocationsDTOS) {
        this.organizationDTO = organizationDTO;
        this.templateLocationsDTOS = templateLocationsDTOS;
    }

    public GetAllOrgTemplatesDTO() {
    }

    public OrganizationDTO getOrganizationDTO() {
        return organizationDTO;
    }

    public void setOrganizationDTO(OrganizationDTO organizationDTO) {
        this.organizationDTO = organizationDTO;
    }

    public List<TemplateLocationsDTO> getTemplateLocationsDTOS() {
        return templateLocationsDTOS;
    }

    public void setTemplateLocationsDTOS(List<TemplateLocationsDTO> templateLocationsDTOS) {
        this.templateLocationsDTOS = templateLocationsDTOS;
    }
}
