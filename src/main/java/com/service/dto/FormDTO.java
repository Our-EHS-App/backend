package com.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.domain.Form} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormDTO implements Serializable {

    private Long id;

    private String nameAr;

    private String nameEn;

    private FormStatusDTO listStatus;

    private TemplateDTO template;

    private Instant createdDate;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

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

    public FormStatusDTO getListStatus() {
        return listStatus;
    }

    public void setListStatus(FormStatusDTO listStatus) {
        this.listStatus = listStatus;
    }

    public TemplateDTO getTemplate() {
        return template;
    }

    public void setTemplate(TemplateDTO template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormDTO)) {
            return false;
        }

        FormDTO formDTO = (FormDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormDTO{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", listStatus=" + getListStatus() +
            ", template=" + getTemplate() +
            "}";
    }
}
