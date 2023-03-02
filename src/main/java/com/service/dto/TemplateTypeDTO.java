package com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.domain.TemplateType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemplateTypeDTO implements Serializable {

    private Long id;

    private String nameAr;

    private String nameAn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameAn() {
        return nameAn;
    }

    public void setNameAn(String nameAn) {
        this.nameAn = nameAn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateTypeDTO)) {
            return false;
        }

        TemplateTypeDTO templateTypeDTO = (TemplateTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, templateTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemplateTypeDTO{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameAn='" + getNameAn() + "'" +
            "}";
    }
}
