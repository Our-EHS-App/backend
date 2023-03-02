package com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FieldTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldType.class);
        FieldType fieldType1 = new FieldType();
        fieldType1.setId(1L);
        FieldType fieldType2 = new FieldType();
        fieldType2.setId(fieldType1.getId());
        assertThat(fieldType1).isEqualTo(fieldType2);
        fieldType2.setId(2L);
        assertThat(fieldType1).isNotEqualTo(fieldType2);
        fieldType1.setId(null);
        assertThat(fieldType1).isNotEqualTo(fieldType2);
    }
}
