package com.service.mapper;

import com.domain.Field;
import com.domain.FieldType;
import com.service.dto.FieldDTO;
import com.service.dto.FieldTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Field} and its DTO {@link FieldDTO}.
 */
@Mapper(componentModel = "spring")
public interface FieldMapper extends EntityMapper<FieldDTO, Field> {
    @Mapping(target = "fieldType", source = "fieldType", qualifiedByName = "fieldTypeId")
    FieldDTO toDto(Field s);

    @Named("fieldTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FieldTypeDTO toDtoFieldTypeId(FieldType fieldType);
}
