package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityImagesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunityImages.class);
        CommunityImages communityImages1 = new CommunityImages();
        communityImages1.setId(1L);
        CommunityImages communityImages2 = new CommunityImages();
        communityImages2.setId(communityImages1.getId());
        assertThat(communityImages1).isEqualTo(communityImages2);
        communityImages2.setId(2L);
        assertThat(communityImages1).isNotEqualTo(communityImages2);
        communityImages1.setId(null);
        assertThat(communityImages1).isNotEqualTo(communityImages2);
    }
}
