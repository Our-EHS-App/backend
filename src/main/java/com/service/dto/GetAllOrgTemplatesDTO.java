package com.service.dto;

import java.util.List;

public class GetAllOrgTemplatesDTO {
    private OrganizationDTO organizationDTO;
    private List<TemplateLocationsDTO> templateLocationsDTOS;
    private Long total;

    public GetAllOrgTemplatesDTO(OrganizationDTO organizationDTO, List<TemplateLocationsDTO> templateLocationsDTOS) {
        this.organizationDTO = organizationDTO;
        this.templateLocationsDTOS = templateLocationsDTOS;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
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
