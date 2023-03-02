package com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemplateTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemplateTypeDTO.class);
        TemplateTypeDTO templateTypeDTO1 = new TemplateTypeDTO();
        templateTypeDTO1.setId(1L);
        TemplateTypeDTO templateTypeDTO2 = new TemplateTypeDTO();
        assertThat(templateTypeDTO1).isNotEqualTo(templateTypeDTO2);
        templateTypeDTO2.setId(templateTypeDTO1.getId());
        assertThat(templateTypeDTO1).isEqualTo(templateTypeDTO2);
        templateTypeDTO2.setId(2L);
        assertThat(templateTypeDTO1).isNotEqualTo(templateTypeDTO2);
        templateTypeDTO1.setId(null);
        assertThat(templateTypeDTO1).isNotEqualTo(templateTypeDTO2);
    }
}
