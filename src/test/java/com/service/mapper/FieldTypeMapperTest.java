package com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldTypeMapperTest {

    private FieldTypeMapper fieldTypeMapper;

    @BeforeEach
    public void setUp() {
        fieldTypeMapper = new FieldTypeMapperImpl();
    }
}
