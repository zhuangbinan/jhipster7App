package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TuYaDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TuYaDevice.class);
        TuYaDevice tuYaDevice1 = new TuYaDevice();
        tuYaDevice1.setId(1L);
        TuYaDevice tuYaDevice2 = new TuYaDevice();
        tuYaDevice2.setId(tuYaDevice1.getId());
        assertThat(tuYaDevice1).isEqualTo(tuYaDevice2);
        tuYaDevice2.setId(2L);
        assertThat(tuYaDevice1).isNotEqualTo(tuYaDevice2);
        tuYaDevice1.setId(null);
        assertThat(tuYaDevice1).isNotEqualTo(tuYaDevice2);
    }
}
