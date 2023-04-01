package com.service.dto;

import java.util.Set;

public class ImportOrgTemplateDTO {
    private Long orgId;
    private Long templateId;
    private Set<Long> locationIds;

    public ImportOrgTemplateDTO() {
    }

    public ImportOrgTemplateDTO(Long orgId, Long templateId, Set<Long> locationIds) {
        this.orgId = orgId;
        this.templateId = templateId;
        this.locationIds = locationIds;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Set<Long> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(Set<Long> locationIds) {
        this.locationIds = locationIds;
    }
}
