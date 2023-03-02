package com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.domain.Category} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryDTO implements Serializable {

    private Long id;

    private String nameAr;

    private String nameEn;

    private Boolean isSubCategory;

    private CategoryDTO parent;

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

    public Boolean getIsSubCategory() {
        return isSubCategory;
    }

    public void setIsSubCategory(Boolean isSubCategory) {
        this.isSubCategory = isSubCategory;
    }

    public CategoryDTO getParent() {
        return parent;
    }

    public void setParent(CategoryDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDTO)) {
            return false;
        }

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryDTO{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", isSubCategory='" + getIsSubCategory() + "'" +
            ", parent=" + getParent() +
            "}";
    }
}
