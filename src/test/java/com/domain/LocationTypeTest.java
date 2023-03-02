package com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationType.class);
        LocationType locationType1 = new LocationType();
        locationType1.setId(1L);
        LocationType locationType2 = new LocationType();
        locationType2.setId(locationType1.getId());
        assertThat(locationType1).isEqualTo(locationType2);
        locationType2.setId(2L);
        assertThat(locationType1).isNotEqualTo(locationType2);
        locationType1.setId(null);
        assertThat(locationType1).isNotEqualTo(locationType2);
    }
}
