package com.service.mapper;

import com.domain.Category;
import com.domain.Field;
import com.domain.Template;
import com.domain.TemplateType;
import com.service.dto.CategoryDTO;
import com.service.dto.FieldDTO;
import com.service.dto.TemplateDTO;
import com.service.dto.TemplateTypeDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Template} and its DTO {@link TemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface TemplateMapper extends EntityMapper<TemplateDTO, Template> {
    @Mapping(target = "templateType", source = "templateType", qualifiedByName = "templateTypeId")
    @Mapping(target = "subCategory", source = "subCategory", qualifiedByName = "categoryId")
    @Mapping(target = "fields", source = "fields", qualifiedByName = "fieldIdSet")
//    @Mapping(target = "fields", ignore = true)
    TemplateDTO toDto(Template s);

    @Mapping(target = "removeField", ignore = true)
    Template toEntity(TemplateDTO templateDTO);

    @Named("templateTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TemplateTypeDTO toDtoTemplateTypeId(TemplateType templateType);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("fieldId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nameAr", source = "nameAr")
    @Mapping(target = "nameEn", source = "nameEn")
    @Mapping(target = "fieldType", source = "fieldType")
    FieldDTO toDtoFieldId(Field field);

    @Named("fieldIdSet")
    default Set<FieldDTO> toDtoFieldIdSet(Set<Field> field) {
        return field.stream().map(this::toDtoFieldId).collect(Collectors.toSet());
    }
}
