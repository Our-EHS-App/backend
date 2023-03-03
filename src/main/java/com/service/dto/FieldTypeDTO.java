package com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.domain.FieldType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FieldTypeDTO implements Serializable {

    private Long id;

    private String nameAr;

    private String nameEn;

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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldTypeDTO)) {
            return false;
        }

        FieldTypeDTO fieldTypeDTO = (FieldTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fieldTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FieldTypeDTO{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            "}";
    }
}
