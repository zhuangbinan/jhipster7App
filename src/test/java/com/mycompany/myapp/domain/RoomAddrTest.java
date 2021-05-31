package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomAddrTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomAddr.class);
        RoomAddr roomAddr1 = new RoomAddr();
        roomAddr1.setId(1L);
        RoomAddr roomAddr2 = new RoomAddr();
        roomAddr2.setId(roomAddr1.getId());
        assertThat(roomAddr1).isEqualTo(roomAddr2);
        roomAddr2.setId(2L);
        assertThat(roomAddr1).isNotEqualTo(roomAddr2);
        roomAddr1.setId(null);
        assertThat(roomAddr1).isNotEqualTo(roomAddr2);
    }
}
