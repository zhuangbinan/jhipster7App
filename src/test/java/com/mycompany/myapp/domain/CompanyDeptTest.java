package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyDeptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyDept.class);
        CompanyDept companyDept1 = new CompanyDept();
        companyDept1.setId(1L);
        CompanyDept companyDept2 = new CompanyDept();
        companyDept2.setId(companyDept1.getId());
        assertThat(companyDept1).isEqualTo(companyDept2);
        companyDept2.setId(2L);
        assertThat(companyDept1).isNotEqualTo(companyDept2);
        companyDept1.setId(null);
        assertThat(companyDept1).isNotEqualTo(companyDept2);
    }
}
