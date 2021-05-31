package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TuYaCmdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TuYaCmd.class);
        TuYaCmd tuYaCmd1 = new TuYaCmd();
        tuYaCmd1.setId(1L);
        TuYaCmd tuYaCmd2 = new TuYaCmd();
        tuYaCmd2.setId(tuYaCmd1.getId());
        assertThat(tuYaCmd1).isEqualTo(tuYaCmd2);
        tuYaCmd2.setId(2L);
        assertThat(tuYaCmd1).isNotEqualTo(tuYaCmd2);
        tuYaCmd1.setId(null);
        assertThat(tuYaCmd1).isNotEqualTo(tuYaCmd2);
    }
}
