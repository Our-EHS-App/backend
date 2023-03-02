package com.service.impl;

import com.domain.LocationStatus;
import com.repository.LocationStatusRepository;
import com.service.LocationStatusService;
import com.service.dto.LocationStatusDTO;
import com.service.mapper.LocationStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationStatus}.
 */
@Service
@Transactional
public class LocationStatusServiceImpl implements LocationStatusService {

    private final Logger log = LoggerFactory.getLogger(LocationStatusServiceImpl.class);

    private final LocationStatusRepository locationStatusRepository;

    private final LocationStatusMapper locationStatusMapper;

    public LocationStatusServiceImpl(LocationStatusRepository locationStatusRepository, LocationStatusMapper locationStatusMapper) {
        this.locationStatusRepository = locationStatusRepository;
        this.locationStatusMapper = locationStatusMapper;
    }

    @Override
    public LocationStatusDTO save(LocationStatusDTO locationStatusDTO) {
        log.debug("Request to save LocationStatus : {}", locationStatusDTO);
        LocationStatus locationStatus = locationStatusMapper.toEntity(locationStatusDTO);
        locationStatus = locationStatusRepository.save(locationStatus);
        return locationStatusMapper.toDto(locationStatus);
    }

    @Override
    public LocationStatusDTO update(LocationStatusDTO locationStatusDTO) {
        log.debug("Request to update LocationStatus : {}", locationStatusDTO);
        LocationStatus locationStatus = locationStatusMapper.toEntity(locationStatusDTO);
        locationStatus = locationStatusRepository.save(locationStatus);
        return locationStatusMapper.toDto(locationStatus);
    }

    @Override
    public Optional<LocationStatusDTO> partialUpdate(LocationStatusDTO locationStatusDTO) {
        log.debug("Request to partially update LocationStatus : {}", locationStatusDTO);

        return locationStatusRepository
            .findById(locationStatusDTO.getId())
            .map(existingLocationStatus -> {
                locationStatusMapper.partialUpdate(existingLocationStatus, locationStatusDTO);

                return existingLocationStatus;
            })
            .map(locationStatusRepository::save)
            .map(locationStatusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationStatusDTO> findAll() {
        log.debug("Request to get all LocationStatuses");
        return locationStatusRepository
            .findAll()
            .stream()
            .map(locationStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationStatusDTO> findOne(Long id) {
        log.debug("Request to get LocationStatus : {}", id);
        return locationStatusRepository.findById(id).map(locationStatusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LocationStatus : {}", id);
        locationStatusRepository.deleteById(id);
    }
}
