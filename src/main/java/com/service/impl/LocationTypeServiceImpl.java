package com.service.impl;

import com.domain.LocationType;
import com.repository.LocationTypeRepository;
import com.service.LocationTypeService;
import com.service.dto.LocationTypeDTO;
import com.service.mapper.LocationTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationType}.
 */
@Service
@Transactional
public class LocationTypeServiceImpl implements LocationTypeService {

    private final Logger log = LoggerFactory.getLogger(LocationTypeServiceImpl.class);

    private final LocationTypeRepository locationTypeRepository;

    private final LocationTypeMapper locationTypeMapper;

    public LocationTypeServiceImpl(LocationTypeRepository locationTypeRepository, LocationTypeMapper locationTypeMapper) {
        this.locationTypeRepository = locationTypeRepository;
        this.locationTypeMapper = locationTypeMapper;
    }

    @Override
    public LocationTypeDTO save(LocationTypeDTO locationTypeDTO) {
        log.debug("Request to save LocationType : {}", locationTypeDTO);
        LocationType locationType = locationTypeMapper.toEntity(locationTypeDTO);
        locationType = locationTypeRepository.save(locationType);
        return locationTypeMapper.toDto(locationType);
    }

    @Override
    public LocationTypeDTO update(LocationTypeDTO locationTypeDTO) {
        log.debug("Request to update LocationType : {}", locationTypeDTO);
        LocationType locationType = locationTypeMapper.toEntity(locationTypeDTO);
        locationType = locationTypeRepository.save(locationType);
        return locationTypeMapper.toDto(locationType);
    }

    @Override
    public Optional<LocationTypeDTO> partialUpdate(LocationTypeDTO locationTypeDTO) {
        log.debug("Request to partially update LocationType : {}", locationTypeDTO);

        return locationTypeRepository
            .findById(locationTypeDTO.getId())
            .map(existingLocationType -> {
                locationTypeMapper.partialUpdate(existingLocationType, locationTypeDTO);

                return existingLocationType;
            })
            .map(locationTypeRepository::save)
            .map(locationTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationTypeDTO> findAll() {
        log.debug("Request to get all LocationTypes");
        return locationTypeRepository.findAll().stream().map(locationTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationTypeDTO> findOne(Long id) {
        log.debug("Request to get LocationType : {}", id);
        return locationTypeRepository.findById(id).map(locationTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LocationType : {}", id);
        locationTypeRepository.deleteById(id);
    }
}
