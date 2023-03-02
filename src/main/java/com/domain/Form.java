package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Form.
 */
@Entity
@Table(name = "form")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "name_an")
    private String nameAn;

    @ManyToOne
    private FormStatus listStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "forms", "templateType", "subCategory", "fields" }, allowSetters = true)
    private Template template;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Form id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public Form nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameAn() {
        return this.nameAn;
    }

    public Form nameAn(String nameAn) {
        this.setNameAn(nameAn);
        return this;
    }

    public void setNameAn(String nameAn) {
        this.nameAn = nameAn;
    }

    public FormStatus getListStatus() {
        return this.listStatus;
    }

    public void setListStatus(FormStatus formStatus) {
        this.listStatus = formStatus;
    }

    public Form listStatus(FormStatus formStatus) {
        this.setListStatus(formStatus);
        return this;
    }

    public Template getTemplate() {
        return this.template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Form template(Template template) {
        this.setTemplate(template);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Form)) {
            return false;
        }
        return id != null && id.equals(((Form) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Form{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameAn='" + getNameAn() + "'" +
            "}";
    }
}
