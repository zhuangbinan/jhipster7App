package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Test01Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Test01.class);
        Test01 test011 = new Test01();
        test011.setId(1L);
        Test01 test012 = new Test01();
        test012.setId(test011.getId());
        assertThat(test011).isEqualTo(test012);
        test012.setId(2L);
        assertThat(test011).isNotEqualTo(test012);
        test011.setId(null);
        assertThat(test011).isNotEqualTo(test012);
    }
}
