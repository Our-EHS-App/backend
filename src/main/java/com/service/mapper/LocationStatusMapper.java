package com.service.mapper;

import com.domain.LocationStatus;
import com.service.dto.LocationStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LocationStatus} and its DTO {@link LocationStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationStatusMapper extends EntityMapper<LocationStatusDTO, LocationStatus> {}
