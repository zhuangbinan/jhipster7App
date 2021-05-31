package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityImageGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunityImageGroup.class);
        CommunityImageGroup communityImageGroup1 = new CommunityImageGroup();
        communityImageGroup1.setId(1L);
        CommunityImageGroup communityImageGroup2 = new CommunityImageGroup();
        communityImageGroup2.setId(communityImageGroup1.getId());
        assertThat(communityImageGroup1).isEqualTo(communityImageGroup2);
        communityImageGroup2.setId(2L);
        assertThat(communityImageGroup1).isNotEqualTo(communityImageGroup2);
        communityImageGroup1.setId(null);
        assertThat(communityImageGroup1).isNotEqualTo(communityImageGroup2);
    }
}
