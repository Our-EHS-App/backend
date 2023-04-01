package com.service.dto;

import java.util.List;

public class TemplateLocationsDTO {
    private TemplateDTO templateDTO;
    private List<LocationDTO> locationDTOS;

    public TemplateLocationsDTO(TemplateDTO templateDTO, List<LocationDTO> locationDTOS) {
        this.templateDTO = templateDTO;
        this.locationDTOS = locationDTOS;
    }

    public TemplateLocationsDTO() {
    }

    public TemplateDTO getTemplateDTO() {
        return templateDTO;
    }

    public void setTemplateDTO(TemplateDTO templateDTO) {
        this.templateDTO = templateDTO;
    }

    public List<LocationDTO> getLocationDTOS() {
        return locationDTOS;
    }

    public void setLocationDTOS(List<LocationDTO> locationDTOS) {
        this.locationDTOS = locationDTOS;
    }
}
