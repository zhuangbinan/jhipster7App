package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Test01DTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(Test01DTO.class);
        Test01DTO test01DTO1 = new Test01DTO();
        test01DTO1.setId(1L);
        Test01DTO test01DTO2 = new Test01DTO();
        assertThat(test01DTO1).isNotEqualTo(test01DTO2);
        test01DTO2.setId(test01DTO1.getId());
        assertThat(test01DTO1).isEqualTo(test01DTO2);
        test01DTO2.setId(2L);
        assertThat(test01DTO1).isNotEqualTo(test01DTO2);
        test01DTO1.setId(null);
        assertThat(test01DTO1).isNotEqualTo(test01DTO2);
    }
}
