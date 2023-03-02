package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Template.
 */
@Entity
@Table(name = "template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title_ar")
    private String titleAr;

    @Column(name = "title_en")
    private String titleEn;

    @Column(name = "duration")
    private String duration;

    @OneToMany(mappedBy = "template")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "listStatus", "template" }, allowSetters = true)
    private Set<Form> forms = new HashSet<>();

    @ManyToOne
    private TemplateType templateType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Category subCategory;

    @ManyToMany
    @JoinTable(
        name = "template_field",
        joinColumns = @JoinColumn(name = "template_id"),
        inverseJoinColumns = @JoinColumn(name = "field_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fieldType", "templates" }, allowSetters = true)
    private Set<Field> fields = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Template id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleAr() {
        return this.titleAr;
    }

    public Template titleAr(String titleAr) {
        this.setTitleAr(titleAr);
        return this;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleEn() {
        return this.titleEn;
    }

    public Template titleEn(String titleEn) {
        this.setTitleEn(titleEn);
        return this;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getDuration() {
        return this.duration;
    }

    public Template duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Set<Form> getForms() {
        return this.forms;
    }

    public void setForms(Set<Form> forms) {
        if (this.forms != null) {
            this.forms.forEach(i -> i.setTemplate(null));
        }
        if (forms != null) {
            forms.forEach(i -> i.setTemplate(this));
        }
        this.forms = forms;
    }

    public Template forms(Set<Form> forms) {
        this.setForms(forms);
        return this;
    }

    public Template addForm(Form form) {
        this.forms.add(form);
        form.setTemplate(this);
        return this;
    }

    public Template removeForm(Form form) {
        this.forms.remove(form);
        form.setTemplate(null);
        return this;
    }

    public TemplateType getTemplateType() {
        return this.templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public Template templateType(TemplateType templateType) {
        this.setTemplateType(templateType);
        return this;
    }

    public Category getSubCategory() {
        return this.subCategory;
    }

    public void setSubCategory(Category category) {
        this.subCategory = category;
    }

    public Template subCategory(Category category) {
        this.setSubCategory(category);
        return this;
    }

    public Set<Field> getFields() {
        return this.fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }

    public Template fields(Set<Field> fields) {
        this.setFields(fields);
        return this;
    }

    public Template addField(Field field) {
        this.fields.add(field);
        field.getTemplates().add(this);
        return this;
    }

    public Template removeField(Field field) {
        this.fields.remove(field);
        field.getTemplates().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Template)) {
            return false;
        }
        return id != null && id.equals(((Template) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Template{" +
            "id=" + getId() +
            ", titleAr='" + getTitleAr() + "'" +
            ", titleEn='" + getTitleEn() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
