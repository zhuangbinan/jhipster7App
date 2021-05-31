package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WamoliUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WamoliUser.class);
        WamoliUser wamoliUser1 = new WamoliUser();
        wamoliUser1.setId(1L);
        WamoliUser wamoliUser2 = new WamoliUser();
        wamoliUser2.setId(wamoliUser1.getId());
        assertThat(wamoliUser1).isEqualTo(wamoliUser2);
        wamoliUser2.setId(2L);
        assertThat(wamoliUser1).isNotEqualTo(wamoliUser2);
        wamoliUser1.setId(null);
        assertThat(wamoliUser1).isNotEqualTo(wamoliUser2);
    }
}
