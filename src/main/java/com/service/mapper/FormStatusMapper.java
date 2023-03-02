package com.service.mapper;

import com.domain.FormStatus;
import com.service.dto.FormStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormStatus} and its DTO {@link FormStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormStatusMapper extends EntityMapper<FormStatusDTO, FormStatus> {}
