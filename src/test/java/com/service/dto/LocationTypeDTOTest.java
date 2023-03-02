package com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationTypeDTO.class);
        LocationTypeDTO locationTypeDTO1 = new LocationTypeDTO();
        locationTypeDTO1.setId(1L);
        LocationTypeDTO locationTypeDTO2 = new LocationTypeDTO();
        assertThat(locationTypeDTO1).isNotEqualTo(locationTypeDTO2);
        locationTypeDTO2.setId(locationTypeDTO1.getId());
        assertThat(locationTypeDTO1).isEqualTo(locationTypeDTO2);
        locationTypeDTO2.setId(2L);
        assertThat(locationTypeDTO1).isNotEqualTo(locationTypeDTO2);
        locationTypeDTO1.setId(null);
        assertThat(locationTypeDTO1).isNotEqualTo(locationTypeDTO2);
    }
}
