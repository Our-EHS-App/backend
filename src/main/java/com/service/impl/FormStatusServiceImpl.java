package com.service.impl;

import com.domain.FormStatus;
import com.repository.FormStatusRepository;
import com.service.FormStatusService;
import com.service.dto.FormStatusDTO;
import com.service.mapper.FormStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FormStatus}.
 */
@Service
@Transactional
public class FormStatusServiceImpl implements FormStatusService {

    private final Logger log = LoggerFactory.getLogger(FormStatusServiceImpl.class);

    private final FormStatusRepository formStatusRepository;

    private final FormStatusMapper formStatusMapper;

    public FormStatusServiceImpl(FormStatusRepository formStatusRepository, FormStatusMapper formStatusMapper) {
        this.formStatusRepository = formStatusRepository;
        this.formStatusMapper = formStatusMapper;
    }

    @Override
    public FormStatusDTO save(FormStatusDTO formStatusDTO) {
        log.debug("Request to save FormStatus : {}", formStatusDTO);
        FormStatus formStatus = formStatusMapper.toEntity(formStatusDTO);
        formStatus = formStatusRepository.save(formStatus);
        return formStatusMapper.toDto(formStatus);
    }

    @Override
    public FormStatusDTO update(FormStatusDTO formStatusDTO) {
        log.debug("Request to update FormStatus : {}", formStatusDTO);
        FormStatus formStatus = formStatusMapper.toEntity(formStatusDTO);
        formStatus = formStatusRepository.save(formStatus);
        return formStatusMapper.toDto(formStatus);
    }

    @Override
    public Optional<FormStatusDTO> partialUpdate(FormStatusDTO formStatusDTO) {
        log.debug("Request to partially update FormStatus : {}", formStatusDTO);

        return formStatusRepository
            .findById(formStatusDTO.getId())
            .map(existingFormStatus -> {
                formStatusMapper.partialUpdate(existingFormStatus, formStatusDTO);

                return existingFormStatus;
            })
            .map(formStatusRepository::save)
            .map(formStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormStatusDTO> findAll() {
        log.debug("Request to get all FormStatuses");
        return formStatusRepository.findAll().stream().map(formStatusMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormStatusDTO> findOne(Long id) {
        log.debug("Request to get FormStatus : {}", id);
        return formStatusRepository.findById(id).map(formStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormStatus : {}", id);
        formStatusRepository.deleteById(id);
    }
}
