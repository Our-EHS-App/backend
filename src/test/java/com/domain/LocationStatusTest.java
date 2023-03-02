package com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationStatus.class);
        LocationStatus locationStatus1 = new LocationStatus();
        locationStatus1.setId(1L);
        LocationStatus locationStatus2 = new LocationStatus();
        locationStatus2.setId(locationStatus1.getId());
        assertThat(locationStatus1).isEqualTo(locationStatus2);
        locationStatus2.setId(2L);
        assertThat(locationStatus1).isNotEqualTo(locationStatus2);
        locationStatus1.setId(null);
        assertThat(locationStatus1).isNotEqualTo(locationStatus2);
    }
}
