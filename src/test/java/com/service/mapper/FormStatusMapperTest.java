package com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormStatusMapperTest {

    private FormStatusMapper formStatusMapper;

    @BeforeEach
    public void setUp() {
        formStatusMapper = new FormStatusMapperImpl();
    }
}
