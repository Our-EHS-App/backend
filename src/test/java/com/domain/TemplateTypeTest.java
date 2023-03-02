package com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemplateTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemplateType.class);
        TemplateType templateType1 = new TemplateType();
        templateType1.setId(1L);
        TemplateType templateType2 = new TemplateType();
        templateType2.setId(templateType1.getId());
        assertThat(templateType1).isEqualTo(templateType2);
        templateType2.setId(2L);
        assertThat(templateType1).isNotEqualTo(templateType2);
        templateType1.setId(null);
        assertThat(templateType1).isNotEqualTo(templateType2);
    }
}
