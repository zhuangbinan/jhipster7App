package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BuildingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Buildings.class);
        Buildings buildings1 = new Buildings();
        buildings1.setId(1L);
        Buildings buildings2 = new Buildings();
        buildings2.setId(buildings1.getId());
        assertThat(buildings1).isEqualTo(buildings2);
        buildings2.setId(2L);
        assertThat(buildings1).isNotEqualTo(buildings2);
        buildings1.setId(null);
        assertThat(buildings1).isNotEqualTo(buildings2);
    }
}
