package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WamoliFaceLibraryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WamoliFaceLibrary.class);
        WamoliFaceLibrary wamoliFaceLibrary1 = new WamoliFaceLibrary();
        wamoliFaceLibrary1.setId(1L);
        WamoliFaceLibrary wamoliFaceLibrary2 = new WamoliFaceLibrary();
        wamoliFaceLibrary2.setId(wamoliFaceLibrary1.getId());
        assertThat(wamoliFaceLibrary1).isEqualTo(wamoliFaceLibrary2);
        wamoliFaceLibrary2.setId(2L);
        assertThat(wamoliFaceLibrary1).isNotEqualTo(wamoliFaceLibrary2);
        wamoliFaceLibrary1.setId(null);
        assertThat(wamoliFaceLibrary1).isNotEqualTo(wamoliFaceLibrary2);
    }
}
