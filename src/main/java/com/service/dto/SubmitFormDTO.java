package com.service.dto;

import java.util.List;

public class SubmitFormDTO {
    private Long formId;
    private List<FieldValueDTO> values;

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public List<FieldValueDTO> getValues() {
        return values;
    }

    public void setValues(List<FieldValueDTO> values) {
        this.values = values;
    }
}
