package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WamoliUserLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WamoliUserLocation.class);
        WamoliUserLocation wamoliUserLocation1 = new WamoliUserLocation();
        wamoliUserLocation1.setId(1L);
        WamoliUserLocation wamoliUserLocation2 = new WamoliUserLocation();
        wamoliUserLocation2.setId(wamoliUserLocation1.getId());
        assertThat(wamoliUserLocation1).isEqualTo(wamoliUserLocation2);
        wamoliUserLocation2.setId(2L);
        assertThat(wamoliUserLocation1).isNotEqualTo(wamoliUserLocation2);
        wamoliUserLocation1.setId(null);
        assertThat(wamoliUserLocation1).isNotEqualTo(wamoliUserLocation2);
    }
}
