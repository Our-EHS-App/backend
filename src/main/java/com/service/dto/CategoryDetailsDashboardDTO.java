package com.service.dto;

public class CategoryDetailsDashboardDTO {
    private CategoryDTO categoryDTO;
    private Long formCount;



    public Long getFormCount() {
        return formCount;
    }

    public void setFormCount(Long formCount) {
        this.formCount = formCount;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }
}
