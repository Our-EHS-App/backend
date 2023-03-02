package com.service.mapper;

import com.domain.Location;
import com.domain.LocationStatus;
import com.domain.LocationType;
import com.service.dto.LocationDTO;
import com.service.dto.LocationStatusDTO;
import com.service.dto.LocationTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "locationType", source = "locationType", qualifiedByName = "locationTypeId")
    @Mapping(target = "locationStatus", source = "locationStatus", qualifiedByName = "locationStatusId")
    LocationDTO toDto(Location s);

    @Named("locationTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationTypeDTO toDtoLocationTypeId(LocationType locationType);

    @Named("locationStatusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationStatusDTO toDtoLocationStatusId(LocationStatus locationStatus);
}
