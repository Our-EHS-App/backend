package com.service.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class GetAllOrgTemplatesDTO {
    private OrganizationDTO organizationDTO;
    private Page<TemplateLocationsDTO> templateLocationsDTOS;
    private Long total;

    public GetAllOrgTemplatesDTO(OrganizationDTO organizationDTO, Page<TemplateLocationsDTO> templateLocationsDTOS) {
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

    public Page<TemplateLocationsDTO> getTemplateLocationsDTOS() {
        return templateLocationsDTOS;
    }

    public void setTemplateLocationsDTOS(Page<TemplateLocationsDTO> templateLocationsDTOS) {
        this.templateLocationsDTOS = templateLocationsDTOS;
    }
}
