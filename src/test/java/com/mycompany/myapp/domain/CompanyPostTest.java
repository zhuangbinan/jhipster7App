package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyPostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyPost.class);
        CompanyPost companyPost1 = new CompanyPost();
        companyPost1.setId(1L);
        CompanyPost companyPost2 = new CompanyPost();
        companyPost2.setId(companyPost1.getId());
        assertThat(companyPost1).isEqualTo(companyPost2);
        companyPost2.setId(2L);
        assertThat(companyPost1).isNotEqualTo(companyPost2);
        companyPost1.setId(null);
        assertThat(companyPost1).isNotEqualTo(companyPost2);
    }
}
