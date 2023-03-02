package com.service.impl;

import com.domain.Form;
import com.repository.FormRepository;
import com.service.FormService;
import com.service.dto.FormDTO;
import com.service.mapper.FormMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Form}.
 */
@Service
@Transactional
public class FormServiceImpl implements FormService {

    private final Logger log = LoggerFactory.getLogger(FormServiceImpl.class);

    private final FormRepository formRepository;

    private final FormMapper formMapper;

    public FormServiceImpl(FormRepository formRepository, FormMapper formMapper) {
        this.formRepository = formRepository;
        this.formMapper = formMapper;
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
}
