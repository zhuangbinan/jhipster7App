package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityLeaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunityLeader.class);
        CommunityLeader communityLeader1 = new CommunityLeader();
        communityLeader1.setId(1L);
        CommunityLeader communityLeader2 = new CommunityLeader();
        communityLeader2.setId(communityLeader1.getId());
        assertThat(communityLeader1).isEqualTo(communityLeader2);
        communityLeader2.setId(2L);
        assertThat(communityLeader1).isNotEqualTo(communityLeader2);
        communityLeader1.setId(null);
        assertThat(communityLeader1).isNotEqualTo(communityLeader2);
    }
}
