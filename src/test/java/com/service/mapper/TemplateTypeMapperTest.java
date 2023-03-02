package com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TemplateTypeMapperTest {

    private TemplateTypeMapper templateTypeMapper;

    @BeforeEach
    public void setUp() {
        templateTypeMapper = new TemplateTypeMapperImpl();
    }
}
