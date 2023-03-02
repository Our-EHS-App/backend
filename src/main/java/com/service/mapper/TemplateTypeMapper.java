package com.service.mapper;

import com.domain.TemplateType;
import com.service.dto.TemplateTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TemplateType} and its DTO {@link TemplateTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TemplateTypeMapper extends EntityMapper<TemplateTypeDTO, TemplateType> {}
