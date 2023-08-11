package com.service.impl;

import com.domain.Field;
import com.domain.Location;
import com.domain.Template;
import com.repository.TemplateRepository;
import com.service.TemplateService;
import com.service.dto.FieldDTO;
import com.service.dto.TemplateDTO;
import com.service.mapper.FieldMapper;
import com.service.mapper.TemplateMapper;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.web.rest.errors.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Template}.
 */
@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

    private final Logger log = LoggerFactory.getLogger(TemplateServiceImpl.class);

    private final TemplateRepository templateRepository;

    private final TemplateMapper templateMapper;
    private final FieldMapper fieldMapper;
    private final FieldServiceImpl fieldService;


    public TemplateServiceImpl(TemplateRepository templateRepository, TemplateMapper templateMapper, FieldMapper fieldMapper, FieldServiceImpl fieldService) {
        this.templateRepository = templateRepository;
        this.templateMapper = templateMapper;
        this.fieldMapper = fieldMapper;
        this.fieldService = fieldService;
    }

    @Override
    public TemplateDTO save(TemplateDTO templateDTO) {
        log.debug("Request to save Template : {}", templateDTO);
        // todo must have at least 1 fields
        Set<FieldDTO> fieldSet = templateDTO
            .getFields()
            .stream().map(fieldService::save)
            .collect(Collectors.toSet());
        templateDTO.setFields(fieldSet);
        Template template = templateMapper.toEntity(templateDTO);
//        template.setFields(fieldSet);
        template = templateRepository.saveAndFlush(template);
        return templateMapper.toDto(template);
    }

    @Override
    public TemplateDTO update(TemplateDTO templateDTO) {
        log.debug("Request to update Template : {}", templateDTO);
        Template template = templateMapper.toEntity(templateDTO);
        template = templateRepository.save(template);
        return templateMapper.toDto(template);
    }

    @Override
    public Optional<TemplateDTO> partialUpdate(TemplateDTO templateDTO) {
        log.debug("Request to partially update Template : {}", templateDTO);

        return templateRepository
            .findById(templateDTO.getId())
            .map(existingTemplate -> {
                templateMapper.partialUpdate(existingTemplate, templateDTO);

                return existingTemplate;
            })
            .map(templateRepository::save)
            .map(templateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Templates");
        return templateRepository.findAll(pageable).map(templateMapper::toDto);
    }

    public Page<TemplateDTO> findAllWithEagerRelationships(Pageable pageable) {
        return templateRepository.findAllWithEagerRelationships(pageable).map(templateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TemplateDTO> findOne(Long id) {
        log.debug("Request to get Template : {}", id);
        return templateRepository.findOneWithEagerRelationships(id).map(templateMapper::toDto);
    }

    public Template getById(Long id){
        if(Objects.nonNull(id))
        return templateRepository.findById(id)
            .orElseThrow(()-> new CustomException("Template not found!","النموذح غير موجود","not.found"));
        throw new CustomException("Template not found!","النموذح غير موجود","not.found");
    }



    @Override
    public void delete(Long id) {
        log.debug("Request to delete Template : {}", id);
        templateRepository.deleteById(id);
    }
}
