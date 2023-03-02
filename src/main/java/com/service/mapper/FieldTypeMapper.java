package com.service.mapper;

import com.domain.FieldType;
import com.service.dto.FieldTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FieldType} and its DTO {@link FieldTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface FieldTypeMapper extends EntityMapper<FieldTypeDTO, FieldType> {}
