package com.service.impl;

import com.domain.Field;
import com.repository.FieldRepository;
import com.service.FieldService;
import com.service.dto.FieldDTO;
import com.service.mapper.FieldMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Field}.
 */
@Service
@Transactional
public class FieldServiceImpl implements FieldService {

    private final Logger log = LoggerFactory.getLogger(FieldServiceImpl.class);

    private final FieldRepository fieldRepository;

    private final FieldMapper fieldMapper;

    public FieldServiceImpl(FieldRepository fieldRepository, FieldMapper fieldMapper) {
        this.fieldRepository = fieldRepository;
        this.fieldMapper = fieldMapper;
    }

    @Override
    public FieldDTO save(FieldDTO fieldDTO) {
        log.debug("Request to save Field : {}", fieldDTO);
        Field field = fieldMapper.toEntity(fieldDTO);
        field = fieldRepository.save(field);
        return fieldMapper.toDto(field);
    }

    @Override
    public FieldDTO update(FieldDTO fieldDTO) {
        log.debug("Request to update Field : {}", fieldDTO);
        Field field = fieldMapper.toEntity(fieldDTO);
        field = fieldRepository.save(field);
        return fieldMapper.toDto(field);
    }

    @Override
    public Optional<FieldDTO> partialUpdate(FieldDTO fieldDTO) {
        log.debug("Request to partially update Field : {}", fieldDTO);

        return fieldRepository
            .findById(fieldDTO.getId())
            .map(existingField -> {
                fieldMapper.partialUpdate(existingField, fieldDTO);

                return existingField;
            })
            .map(fieldRepository::save)
            .map(fieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FieldDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fields");
        return fieldRepository.findAll(pageable).map(fieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldDTO> findOne(Long id) {
        log.debug("Request to get Field : {}", id);
        return fieldRepository.findById(id).map(fieldMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Field : {}", id);
        fieldRepository.deleteById(id);
    }
}
