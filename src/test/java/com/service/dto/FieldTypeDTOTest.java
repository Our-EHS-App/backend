package com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FieldTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldTypeDTO.class);
        FieldTypeDTO fieldTypeDTO1 = new FieldTypeDTO();
        fieldTypeDTO1.setId(1L);
        FieldTypeDTO fieldTypeDTO2 = new FieldTypeDTO();
        assertThat(fieldTypeDTO1).isNotEqualTo(fieldTypeDTO2);
        fieldTypeDTO2.setId(fieldTypeDTO1.getId());
        assertThat(fieldTypeDTO1).isEqualTo(fieldTypeDTO2);
        fieldTypeDTO2.setId(2L);
        assertThat(fieldTypeDTO1).isNotEqualTo(fieldTypeDTO2);
        fieldTypeDTO1.setId(null);
        assertThat(fieldTypeDTO1).isNotEqualTo(fieldTypeDTO2);
    }
}
