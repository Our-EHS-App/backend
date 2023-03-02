package com.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationStatusMapperTest {

    private LocationStatusMapper locationStatusMapper;

    @BeforeEach
    public void setUp() {
        locationStatusMapper = new LocationStatusMapperImpl();
    }
}
