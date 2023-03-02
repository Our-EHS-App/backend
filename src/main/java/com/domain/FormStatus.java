package com.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FormStatus.
 */
@Entity
@Table(name = "form_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormStatus implements Serializable {

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAr() {
        return this.nameAr;
    }

    public FormStatus nameAr(String nameAr) {
        this.setNameAr(nameAr);
        return this;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameAn() {
        return this.nameAn;
    }

    public FormStatus nameAn(String nameAn) {
        this.setNameAn(nameAn);
        return this;
    }

    public void setNameAn(String nameAn) {
        this.nameAn = nameAn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormStatus)) {
            return false;
        }
        return id != null && id.equals(((FormStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormStatus{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameAn='" + getNameAn() + "'" +
            "}";
    }
}
