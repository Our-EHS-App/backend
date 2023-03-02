package com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormStatus.class);
        FormStatus formStatus1 = new FormStatus();
        formStatus1.setId(1L);
        FormStatus formStatus2 = new FormStatus();
        formStatus2.setId(formStatus1.getId());
        assertThat(formStatus1).isEqualTo(formStatus2);
        formStatus2.setId(2L);
        assertThat(formStatus1).isNotEqualTo(formStatus2);
        formStatus1.setId(null);
        assertThat(formStatus1).isNotEqualTo(formStatus2);
    }
}
