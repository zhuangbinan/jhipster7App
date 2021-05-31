package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityNoticeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunityNotice.class);
        CommunityNotice communityNotice1 = new CommunityNotice();
        communityNotice1.setId(1L);
        CommunityNotice communityNotice2 = new CommunityNotice();
        communityNotice2.setId(communityNotice1.getId());
        assertThat(communityNotice1).isEqualTo(communityNotice2);
        communityNotice2.setId(2L);
        assertThat(communityNotice1).isNotEqualTo(communityNotice2);
        communityNotice1.setId(null);
        assertThat(communityNotice1).isNotEqualTo(communityNotice2);
    }
}
