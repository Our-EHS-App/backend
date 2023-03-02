package com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationStatusDTO.class);
        LocationStatusDTO locationStatusDTO1 = new LocationStatusDTO();
        locationStatusDTO1.setId(1L);
        LocationStatusDTO locationStatusDTO2 = new LocationStatusDTO();
        assertThat(locationStatusDTO1).isNotEqualTo(locationStatusDTO2);
        locationStatusDTO2.setId(locationStatusDTO1.getId());
        assertThat(locationStatusDTO1).isEqualTo(locationStatusDTO2);
        locationStatusDTO2.setId(2L);
        assertThat(locationStatusDTO1).isNotEqualTo(locationStatusDTO2);
        locationStatusDTO1.setId(null);
        assertThat(locationStatusDTO1).isNotEqualTo(locationStatusDTO2);
    }
}
