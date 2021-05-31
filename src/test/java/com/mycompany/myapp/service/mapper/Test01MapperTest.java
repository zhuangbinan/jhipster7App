package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Test01MapperTest {

    private Test01Mapper test01Mapper;

    @BeforeEach
    public void setUp() {
        test01Mapper = new Test01MapperImpl();
    }
}
