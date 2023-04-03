package com.service.impl;

import com.domain.Form;
import com.domain.FormStatus;
import com.domain.OrganizationTemplate;
import com.repository.FormRepository;
import com.repository.OrganizationTemplateRepository;
import com.service.FormService;
import com.service.dto.FormDTO;
import com.service.mapper.FormMapper;

import java.util.*;
import java.util.stream.Collectors;

import com.web.rest.errors.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

/**
 * Service Implementation for managing {@link Form}.
 */
@Service
@Transactional
public class FormServiceImpl implements FormService {

    private final Logger log = LoggerFactory.getLogger(FormServiceImpl.class);

    private final FormRepository formRepository;

    private final FormMapper formMapper;
    private final OrganizationTemplateRepository organizationTemplateRepository;

    public FormServiceImpl(FormRepository formRepository, FormMapper formMapper, OrganizationTemplateRepository organizationTemplateRepository) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
        this.organizationTemplateRepository = organizationTemplateRepository;
    }

    @Override
    public FormDTO save(FormDTO formDTO) {
        log.debug("Request to save Form : {}", formDTO);
        Form form = formMapper.toEntity(formDTO);
        form = formRepository.save(form);
        return formMapper.toDto(form);
    }

    @Override
    public FormDTO update(FormDTO formDTO) {
        log.debug("Request to update Form : {}", formDTO);
        Form form = formMapper.toEntity(formDTO);
        form = formRepository.save(form);
        return formMapper.toDto(form);
    }

    @Override
    public Optional<FormDTO> partialUpdate(FormDTO formDTO) {
        log.debug("Request to partially update Form : {}", formDTO);

        return formRepository
            .findById(formDTO.getId())
            .map(existingForm -> {
                formMapper.partialUpdate(existingForm, formDTO);

                return existingForm;
            })
            .map(formRepository::save)
            .map(formMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Forms");
        return formRepository.findAll(pageable).map(formMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormDTO> findOne(Long id) {
        log.debug("Request to get Form : {}", id);
        return formRepository.findById(id).map(formMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Form : {}", id);
        formRepository.deleteById(id);
    }

    public List<Form> generateForm(Long orgTempId){
        OrganizationTemplate organizationTemplate = organizationTemplateRepository.findById(orgTempId)
            .orElseThrow(() -> new CustomException("Not found!","غير موجود!","not.found"));
        return generateForm(organizationTemplate);
    }

    private List<Form> generateForm(OrganizationTemplate organizationTemplate ){
        return organizationTemplate.getLocations()
            .stream().map(location -> {
        Form form = new Form();
        form.setTemplate(organizationTemplate.getTemplate());
        form.setLocation(location);
        form.setOrganizationTemplate(organizationTemplate);
        form.setNameAr(organizationTemplate.getTemplate().getTitleAr());
        form.setNameEn(organizationTemplate.getTemplate().getTitleEn());
        FormStatus formStatus = new FormStatus();
        form.setListStatus(formStatus.id(1L));
        return formRepository.saveAndFlush(form);
            })
            .collect(Collectors.toList());
    }

    @Scheduled(cron="0 * 0/5 * * ?")
    public void generateForms(){
        log.info("Generate form job started");
        log.info("Generate form job started");
        List<OrganizationTemplate> organizationTemplateList = organizationTemplateRepository.findAll();
        List<Form> forms = organizationTemplateList
            .stream()
            .map(this::generateForm)
            .flatMap(java.util.Collection::stream)
            .collect(Collectors.toList());
        log.info("# of form generated: {}", forms.size());
        log.info("Generate form job finished");
    }
}
