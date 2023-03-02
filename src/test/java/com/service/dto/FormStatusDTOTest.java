package com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormStatusDTO.class);
        FormStatusDTO formStatusDTO1 = new FormStatusDTO();
        formStatusDTO1.setId(1L);
        FormStatusDTO formStatusDTO2 = new FormStatusDTO();
        assertThat(formStatusDTO1).isNotEqualTo(formStatusDTO2);
        formStatusDTO2.setId(formStatusDTO1.getId());
        assertThat(formStatusDTO1).isEqualTo(formStatusDTO2);
        formStatusDTO2.setId(2L);
        assertThat(formStatusDTO1).isNotEqualTo(formStatusDTO2);
        formStatusDTO1.setId(null);
        assertThat(formStatusDTO1).isNotEqualTo(formStatusDTO2);
    }
}
