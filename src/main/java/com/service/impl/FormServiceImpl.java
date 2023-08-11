package com.service.impl;

import com.domain.*;
import com.repository.FieldRepository;
import com.repository.FormRepository;
import com.repository.FormValuesRepository;
import com.repository.OrganizationTemplateRepository;
import com.service.FormService;
import com.service.dto.FieldValueDTO;
import com.service.dto.FormDTO;
import com.service.dto.SubmitFormDTO;
import com.service.mapper.FormMapper;

import java.util.*;
import java.util.stream.Collectors;

import com.web.rest.errors.CustomException;
import io.undertow.server.handlers.form.FormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.config.Constants.*;

/**
 * Service Implementation for managing {@link Form}.
 */
@Service
@Transactional
public class FormServiceImpl implements FormService {

    private final Logger log = LoggerFactory.getLogger(FormServiceImpl.class);
    private final FormRepository formRepository;
    private final FieldRepository fieldRepository;

    private final FormMapper formMapper;
    private final OrganizationTemplateRepository organizationTemplateRepository;
    private final FormValuesRepository formValuesRepository;

    public FormServiceImpl(FormRepository formRepository, FieldRepository fieldRepository, FormMapper formMapper, OrganizationTemplateRepository organizationTemplateRepository, FormValuesRepository formValuesRepository) {
        this.formRepository = formRepository;
        this.fieldRepository = fieldRepository;
        this.formMapper = formMapper;
        this.organizationTemplateRepository = organizationTemplateRepository;
        this.formValuesRepository = formValuesRepository;
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
        return formRepository.findAll(pageable).map(formMapper::toDto)
            .map(this::getValues)
            ;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormDTO> findOne(Long id) {
        log.debug("Request to get Form : {}", id);
        return formRepository.findById(id)
            .map(formMapper::toDto)
            .map(this::getValues);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Form : {}", id);
        formRepository.deleteById(id);
    }

    public List<Form> generateForm(Long orgTempId) {
        OrganizationTemplate organizationTemplate = organizationTemplateRepository.findById(orgTempId)
            .orElseThrow(() -> new CustomException("Not found!", "غير موجود!", "not.found"));
        return generateForm(organizationTemplate);
    }

    private List<Form> generateForm(OrganizationTemplate organizationTemplate) {
        return organizationTemplate.getLocations()
            .stream().map(location -> {
                Form form = new Form();
                form.setTemplate(organizationTemplate.getTemplate());
                form.setLocation(location);
                form.setOrganizationTemplate(organizationTemplate);
                form.setNameAr(organizationTemplate.getTemplate().getTitleAr());
                form.setNameEn(organizationTemplate.getTemplate().getTitleEn());
                // todo status enum
                FormStatus formStatus = new FormStatus();
                form.setListStatus(formStatus.id(1L));
                return formRepository.saveAndFlush(form);
            })
            .collect(Collectors.toList());
    }

    public void submitForm(SubmitFormDTO values) {
        // todo cant submit more than one
        Form form = formRepository.findByIdAndListStatus_Id(values.getFormId(), 1L) // Active status
            .orElseThrow(() -> new CustomException("Form not found!", "النموذج غير موجود!", "not.found"));
        // todo find duplicate ids
        List<Long> fieldIds = form.getTemplate()
            .getFields()
            .stream()
            .map(Field::getId)
            .collect(Collectors.toList());

        // todo check mandatory fields
        values.getValues().stream()
            .map(FieldValueDTO::getFieldId)
            .allMatch(id -> fieldIds.contains(id));

        //todo check answer match field type
        List<FormValues> formValues = values.getValues().stream()
            .map(value -> toEntity(value, form))
            .collect(Collectors.toList());
        FormStatus formStatus = new FormStatus();
        form.setListStatus(formStatus.id(4L)); //Submitted status
    }

    private FormValues toEntity(FieldValueDTO value, Form form) {
        Field field = fieldRepository.findById(value.getFieldId())
            .orElseThrow(() -> new CustomException("Field not found!", "الخانة غير موجودة!", "not.found"));
        validateValue(field, value.getValue());
        FormValues formValues = formValuesRepository.findByFormAndField(form, field)
            .orElse(new FormValues());
        formValues.setForm(form);
        formValues.setField(field);
        formValues.setValue(value.getValue());
        return formValuesRepository.save(formValues);
    }

    private void validateValue(Field field, String value) {
        if (field.getFieldType().getNameEn().equalsIgnoreCase("number")) {
            if (!value.matches(NUMBER_REGEX))
                throw new CustomException("Invalid value", "القيمة غير صحيحة", "invalid.value");
        }
        if (field.getFieldType().getNameEn().equalsIgnoreCase("Checkbox")) {
            if (!value.matches(CHECKBOX_REGEX))
                throw new CustomException("Invalid value", "القيمة غير صحيحة", "invalid.value");
        }
    }

    @Scheduled(cron = "0 * 0/5 * * ?")
    public void generateForms() {
        log.info("Generate form job started");
        // todo dont duplicate
        List<OrganizationTemplate> organizationTemplateList = organizationTemplateRepository.findAll();
        List<Form> forms = organizationTemplateList
            .stream()
            .map(this::generateForm)
            .flatMap(java.util.Collection::stream)
            .collect(Collectors.toList());
        log.info("# of form generated: {}", forms.size());
        log.info("Generate form job finished");
        // todo send notification

    }

    public List<FormDTO> getAllByOrg(Long id) {
        return formRepository.findAllByOrganizationTemplate_Organization_Id(id)
            .stream()
            .map(formMapper::toDto)
            .map(this::getValues)
            .collect(Collectors.toList());

    }

    private FormDTO getValues(FormDTO dto){

        dto.getTemplate().getFields().forEach(f ->{
            formValuesRepository
                .findByForm_idAndField_id(dto.getId(), f.getId())
                .ifPresent(formValues -> f.setValue(formValues.getValue()));
        });
        return dto;
    }
}
