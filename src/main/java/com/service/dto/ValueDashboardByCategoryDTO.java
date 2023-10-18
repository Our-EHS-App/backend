package com.service.dto;

import javax.validation.constraints.DecimalMin;
import java.text.DecimalFormat;

public class ValueDashboardByCategoryDTO {
    private Long categoryId;
    private Double percentage;

    public ValueDashboardByCategoryDTO(Long categoryId, Double percentage) {
        this.categoryId = categoryId;
        DecimalFormat df = new DecimalFormat("0.00");
        this.percentage = Double.valueOf(df.format(percentage));
    }

    public ValueDashboardByCategoryDTO(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
