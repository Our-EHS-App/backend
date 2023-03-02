package com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationTypeMapperTest {

    private LocationTypeMapper locationTypeMapper;

    @BeforeEach
    public void setUp() {
        locationTypeMapper = new LocationTypeMapperImpl();
    }
}
