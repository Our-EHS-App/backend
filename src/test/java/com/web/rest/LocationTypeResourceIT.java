package com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.IntegrationTest;
import com.domain.LocationType;
import com.repository.LocationTypeRepository;
import com.service.dto.LocationTypeDTO;
import com.service.mapper.LocationTypeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LocationTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationTypeResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/location-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Autowired
    private LocationTypeMapper locationTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationTypeMockMvc;

    private LocationType locationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationType createEntity(EntityManager em) {
        LocationType locationType = new LocationType().nameAr(DEFAULT_NAME_AR).nameEn(DEFAULT_NAME_EN);
        return locationType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationType createUpdatedEntity(EntityManager em) {
        LocationType locationType = new LocationType().nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);
        return locationType;
    }

    @BeforeEach
    public void initTest() {
        locationType = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationType() throws Exception {
        int databaseSizeBeforeCreate = locationTypeRepository.findAll().size();
        // Create the LocationType
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(locationType);
        restLocationTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LocationType testLocationType = locationTypeList.get(locationTypeList.size() - 1);
        assertThat(testLocationType.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testLocationType.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void createLocationTypeWithExistingId() throws Exception {
        // Create the LocationType with an existing ID
        locationType.setId(1L);
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(locationType);

        int databaseSizeBeforeCreate = locationTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationTypes() throws Exception {
        // Initialize the database
        locationTypeRepository.saveAndFlush(locationType);

        // Get all the locationTypeList
        restLocationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)));
    }

    @Test
    @Transactional
    void getLocationType() throws Exception {
        // Initialize the database
        locationTypeRepository.saveAndFlush(locationType);

        // Get the locationType
        restLocationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, locationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationType.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN));
    }

    @Test
    @Transactional
    void getNonExistingLocationType() throws Exception {
        // Get the locationType
        restLocationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocationType() throws Exception {
        // Initialize the database
        locationTypeRepository.saveAndFlush(locationType);

        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();

        // Update the locationType
        LocationType updatedLocationType = locationTypeRepository.findById(locationType.getId()).get();
        // Disconnect from session so that the updates on updatedLocationType are not directly saved in db
        em.detach(updatedLocationType);
        updatedLocationType.nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(updatedLocationType);

        restLocationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
        LocationType testLocationType = locationTypeList.get(locationTypeList.size() - 1);
        assertThat(testLocationType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testLocationType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void putNonExistingLocationType() throws Exception {
        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();
        locationType.setId(count.incrementAndGet());

        // Create the LocationType
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(locationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationType() throws Exception {
        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();
        locationType.setId(count.incrementAndGet());

        // Create the LocationType
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(locationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationType() throws Exception {
        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();
        locationType.setId(count.incrementAndGet());

        // Create the LocationType
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(locationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationTypeWithPatch() throws Exception {
        // Initialize the database
        locationTypeRepository.saveAndFlush(locationType);

        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();

        // Update the locationType using partial update
        LocationType partialUpdatedLocationType = new LocationType();
        partialUpdatedLocationType.setId(locationType.getId());

        partialUpdatedLocationType.nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);

        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationType))
            )
            .andExpect(status().isOk());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
        LocationType testLocationType = locationTypeList.get(locationTypeList.size() - 1);
        assertThat(testLocationType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testLocationType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void fullUpdateLocationTypeWithPatch() throws Exception {
        // Initialize the database
        locationTypeRepository.saveAndFlush(locationType);

        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();

        // Update the locationType using partial update
        LocationType partialUpdatedLocationType = new LocationType();
        partialUpdatedLocationType.setId(locationType.getId());

        partialUpdatedLocationType.nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);

        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationType))
            )
            .andExpect(status().isOk());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
        LocationType testLocationType = locationTypeList.get(locationTypeList.size() - 1);
        assertThat(testLocationType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testLocationType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void patchNonExistingLocationType() throws Exception {
        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();
        locationType.setId(count.incrementAndGet());

        // Create the LocationType
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(locationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationType() throws Exception {
        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();
        locationType.setId(count.incrementAndGet());

        // Create the LocationType
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(locationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationType() throws Exception {
        int databaseSizeBeforeUpdate = locationTypeRepository.findAll().size();
        locationType.setId(count.incrementAndGet());

        // Create the LocationType
        LocationTypeDTO locationTypeDTO = locationTypeMapper.toDto(locationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationType in the database
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationType() throws Exception {
        // Initialize the database
        locationTypeRepository.saveAndFlush(locationType);

        int databaseSizeBeforeDelete = locationTypeRepository.findAll().size();

        // Delete the locationType
        restLocationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationType> locationTypeList = locationTypeRepository.findAll();
        assertThat(locationTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
