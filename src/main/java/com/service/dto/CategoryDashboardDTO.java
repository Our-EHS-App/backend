package com.service.dto;

public class CategoryDashboardDTO {
    private Long categoryId;
    private Long formCount;

    private Double percentage;

    public CategoryDashboardDTO(Long categoryId, Long formCount) {
        this.categoryId = categoryId;
        this.formCount = formCount;
    }

    public CategoryDashboardDTO(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getFormCount() {
        return formCount;
    }

    public void setFormCount(Long formCount) {
        this.formCount = formCount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
