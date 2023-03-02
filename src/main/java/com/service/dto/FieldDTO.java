package com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.domain.Field} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FieldDTO implements Serializable {

    private Long id;

    private String nameAr;

    private String nameAn;

    private FieldTypeDTO fieldType;

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

    public FieldTypeDTO getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldTypeDTO fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldDTO)) {
            return false;
        }

        FieldDTO fieldDTO = (FieldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FieldDTO{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameAn='" + getNameAn() + "'" +
            ", fieldType=" + getFieldType() +
            "}";
    }
}
