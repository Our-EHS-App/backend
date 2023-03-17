package com.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.domain.Template} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateDTO implements Serializable {

    private Long id;

    private String titleAr;

    private String titleEn;

    private String frequency;

    private TemplateTypeDTO templateType;

    private CategoryDTO subCategory;

    private Set<FieldDTO> fields = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public TemplateTypeDTO getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateTypeDTO templateType) {
        this.templateType = templateType;
    }

    public CategoryDTO getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(CategoryDTO subCategory) {
        this.subCategory = subCategory;
    }

    public Set<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(Set<FieldDTO> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateDTO)) {
            return false;
        }

        TemplateDTO templateDTO = (TemplateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, templateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateDTO{" +
            "id=" + getId() +
            ", titleAr='" + getTitleAr() + "'" +
            ", titleEn='" + getTitleEn() + "'" +
            ", duration='" + getFrequency() + "'" +
            ", templateType=" + getTemplateType() +
            ", subCategory=" + getSubCategory() +
            ", fields=" + getFields() +
            "}";
    }
}
