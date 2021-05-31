package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HomelandStationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomelandStation.class);
        HomelandStation homelandStation1 = new HomelandStation();
        homelandStation1.setId(1L);
        HomelandStation homelandStation2 = new HomelandStation();
        homelandStation2.setId(homelandStation1.getId());
        assertThat(homelandStation1).isEqualTo(homelandStation2);
        homelandStation2.setId(2L);
        assertThat(homelandStation1).isNotEqualTo(homelandStation2);
        homelandStation1.setId(null);
        assertThat(homelandStation1).isNotEqualTo(homelandStation2);
    }
}
