package com.service.mapper;

import com.domain.Form;
import com.domain.FormStatus;
import com.domain.Template;
import com.service.dto.FormDTO;
import com.service.dto.FormStatusDTO;
import com.service.dto.TemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Form} and its DTO {@link FormDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormMapper extends EntityMapper<FormDTO, Form> {
    @Mapping(target = "listStatus", source = "listStatus", qualifiedByName = "formStatusId")
    @Mapping(target = "template", source = "template", qualifiedByName = "templateId")
    FormDTO toDto(Form s);

    @Named("formStatusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormStatusDTO toDtoFormStatusId(FormStatus formStatus);

    @Named("templateId")
//    @BeanMapping(ignoreByDefault = true)
    TemplateDTO toDtoTemplateId(Template template);
}
