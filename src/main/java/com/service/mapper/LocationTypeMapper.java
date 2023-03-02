package com.service.mapper;

import com.domain.LocationType;
import com.service.dto.LocationTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LocationType} and its DTO {@link LocationTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationTypeMapper extends EntityMapper<LocationTypeDTO, LocationType> {}
