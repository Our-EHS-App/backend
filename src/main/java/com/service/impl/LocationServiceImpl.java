package com.service.impl;

import com.domain.Location;
import com.domain.Organization;
import com.repository.LocationRepository;
import com.service.LocationService;
import com.service.OrganizationService;
import com.service.dto.LocationDTO;
import com.service.mapper.LocationMapper;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.service.util.TokenUtils;
import com.web.rest.errors.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;
    private final TokenUtils tokenUtils;
    private final OrganizationService organizationService;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper, TokenUtils tokenUtils, OrganizationService organizationService) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.tokenUtils = tokenUtils;
        this.organizationService = organizationService;
    }

    @Override
    public LocationDTO save(LocationDTO locationDTO, HttpServletRequest request) {
        log.debug("Request to save Location : {}", locationDTO);
        locationDTO.setOrganizationId(Long.valueOf(tokenUtils.getOrgId(request)));
        Organization organization = organizationService.getById(locationDTO.getOrganizationId());
        Location location = locationMapper.toEntity(locationDTO);
        location.setOrganization(organization);
        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public LocationDTO update(LocationDTO locationDTO) {
        log.debug("Request to update Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public Optional<LocationDTO> partialUpdate(LocationDTO locationDTO) {
        log.debug("Request to partially update Location : {}", locationDTO);

        return locationRepository
            .findById(locationDTO.getId())
            .map(existingLocation -> {
                locationMapper.partialUpdate(existingLocation, locationDTO);

                return existingLocation;
            })
            .map(locationRepository::save)
            .map(locationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Locations");
        return locationRepository.findAll(pageable).map(locationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id).map(locationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }

    public Location getById(Long id){
        if(Objects.nonNull(id))
            return locationRepository.findById(id)
                .orElseThrow(()-> new CustomException("Location not found!","الموقع غير موجود","not.found"));
        throw new CustomException("Location not found!","الموقع غير موجود","not.found");
    }

    public Set<Location> getByIds(Set<Long> ids){
        return ids
            .stream()
            .filter(Objects::nonNull)
            .map(this::getById)
            .collect(Collectors.toSet());
    }
}
