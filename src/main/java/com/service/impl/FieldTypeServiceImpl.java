package com.service.impl;

import com.domain.FieldType;
import com.repository.FieldTypeRepository;
import com.service.FieldTypeService;
import com.service.dto.FieldTypeDTO;
import com.service.mapper.FieldTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FieldType}.
 */
@Service
@Transactional
public class FieldTypeServiceImpl implements FieldTypeService {

    private final Logger log = LoggerFactory.getLogger(FieldTypeServiceImpl.class);

    private final FieldTypeRepository fieldTypeRepository;

    private final FieldTypeMapper fieldTypeMapper;

    public FieldTypeServiceImpl(FieldTypeRepository fieldTypeRepository, FieldTypeMapper fieldTypeMapper) {
        this.fieldTypeRepository = fieldTypeRepository;
        this.fieldTypeMapper = fieldTypeMapper;
    }

    @Override
    public FieldTypeDTO save(FieldTypeDTO fieldTypeDTO) {
        log.debug("Request to save FieldType : {}", fieldTypeDTO);
        FieldType fieldType = fieldTypeMapper.toEntity(fieldTypeDTO);
        fieldType = fieldTypeRepository.save(fieldType);
        return fieldTypeMapper.toDto(fieldType);
    }

    @Override
    public FieldTypeDTO update(FieldTypeDTO fieldTypeDTO) {
        log.debug("Request to update FieldType : {}", fieldTypeDTO);
        FieldType fieldType = fieldTypeMapper.toEntity(fieldTypeDTO);
        fieldType = fieldTypeRepository.save(fieldType);
        return fieldTypeMapper.toDto(fieldType);
    }

    @Override
    public Optional<FieldTypeDTO> partialUpdate(FieldTypeDTO fieldTypeDTO) {
        log.debug("Request to partially update FieldType : {}", fieldTypeDTO);

        return fieldTypeRepository
            .findById(fieldTypeDTO.getId())
            .map(existingFieldType -> {
                fieldTypeMapper.partialUpdate(existingFieldType, fieldTypeDTO);

                return existingFieldType;
            })
            .map(fieldTypeRepository::save)
            .map(fieldTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FieldTypeDTO> findAll() {
        log.debug("Request to get all FieldTypes");
        return fieldTypeRepository.findAll().stream().map(fieldTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldTypeDTO> findOne(Long id) {
        log.debug("Request to get FieldType : {}", id);
        return fieldTypeRepository.findById(id).map(fieldTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FieldType : {}", id);
        fieldTypeRepository.deleteById(id);
    }


    enum FormType{

    }
}
