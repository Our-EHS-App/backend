package com.service.impl;

import com.domain.TemplateType;
import com.repository.TemplateTypeRepository;
import com.service.TemplateTypeService;
import com.service.dto.TemplateTypeDTO;
import com.service.mapper.TemplateTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TemplateType}.
 */
@Service
@Transactional
public class TemplateTypeServiceImpl implements TemplateTypeService {

    private final Logger log = LoggerFactory.getLogger(TemplateTypeServiceImpl.class);

    private final TemplateTypeRepository templateTypeRepository;

    private final TemplateTypeMapper templateTypeMapper;

    public TemplateTypeServiceImpl(TemplateTypeRepository templateTypeRepository, TemplateTypeMapper templateTypeMapper) {
        this.templateTypeRepository = templateTypeRepository;
        this.templateTypeMapper = templateTypeMapper;
    }

    @Override
    public TemplateTypeDTO save(TemplateTypeDTO templateTypeDTO) {
        log.debug("Request to save TemplateType : {}", templateTypeDTO);
        TemplateType templateType = templateTypeMapper.toEntity(templateTypeDTO);
        templateType = templateTypeRepository.save(templateType);
        return templateTypeMapper.toDto(templateType);
    }

    @Override
    public TemplateTypeDTO update(TemplateTypeDTO templateTypeDTO) {
        log.debug("Request to update TemplateType : {}", templateTypeDTO);
        TemplateType templateType = templateTypeMapper.toEntity(templateTypeDTO);
        templateType = templateTypeRepository.save(templateType);
        return templateTypeMapper.toDto(templateType);
    }

    @Override
    public Optional<TemplateTypeDTO> partialUpdate(TemplateTypeDTO templateTypeDTO) {
        log.debug("Request to partially update TemplateType : {}", templateTypeDTO);

        return templateTypeRepository
            .findById(templateTypeDTO.getId())
            .map(existingTemplateType -> {
                templateTypeMapper.partialUpdate(existingTemplateType, templateTypeDTO);

                return existingTemplateType;
            })
            .map(templateTypeRepository::save)
            .map(templateTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TemplateTypeDTO> findAll() {
        log.debug("Request to get all TemplateTypes");
        return templateTypeRepository.findAll().stream().map(templateTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TemplateTypeDTO> findOne(Long id) {
        log.debug("Request to get TemplateType : {}", id);
        return templateTypeRepository.findById(id).map(templateTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TemplateType : {}", id);
        templateTypeRepository.deleteById(id);
    }
}
