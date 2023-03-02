package com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.IntegrationTest;
import com.domain.LocationStatus;
import com.repository.LocationStatusRepository;
import com.service.dto.LocationStatusDTO;
import com.service.mapper.LocationStatusMapper;
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
 * Integration tests for the {@link LocationStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationStatusResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/location-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationStatusRepository locationStatusRepository;

    @Autowired
    private LocationStatusMapper locationStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationStatusMockMvc;

    private LocationStatus locationStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationStatus createEntity(EntityManager em) {
        LocationStatus locationStatus = new LocationStatus().nameAr(DEFAULT_NAME_AR).nameAn(DEFAULT_NAME_AN);
        return locationStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationStatus createUpdatedEntity(EntityManager em) {
        LocationStatus locationStatus = new LocationStatus().nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);
        return locationStatus;
    }

    @BeforeEach
    public void initTest() {
        locationStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationStatus() throws Exception {
        int databaseSizeBeforeCreate = locationStatusRepository.findAll().size();
        // Create the LocationStatus
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(locationStatus);
        restLocationStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeCreate + 1);
        LocationStatus testLocationStatus = locationStatusList.get(locationStatusList.size() - 1);
        assertThat(testLocationStatus.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testLocationStatus.getNameAn()).isEqualTo(DEFAULT_NAME_AN);
    }

    @Test
    @Transactional
    void createLocationStatusWithExistingId() throws Exception {
        // Create the LocationStatus with an existing ID
        locationStatus.setId(1L);
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(locationStatus);

        int databaseSizeBeforeCreate = locationStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationStatuses() throws Exception {
        // Initialize the database
        locationStatusRepository.saveAndFlush(locationStatus);

        // Get all the locationStatusList
        restLocationStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameAn").value(hasItem(DEFAULT_NAME_AN)));
    }

    @Test
    @Transactional
    void getLocationStatus() throws Exception {
        // Initialize the database
        locationStatusRepository.saveAndFlush(locationStatus);

        // Get the locationStatus
        restLocationStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, locationStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationStatus.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameAn").value(DEFAULT_NAME_AN));
    }

    @Test
    @Transactional
    void getNonExistingLocationStatus() throws Exception {
        // Get the locationStatus
        restLocationStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocationStatus() throws Exception {
        // Initialize the database
        locationStatusRepository.saveAndFlush(locationStatus);

        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();

        // Update the locationStatus
        LocationStatus updatedLocationStatus = locationStatusRepository.findById(locationStatus.getId()).get();
        // Disconnect from session so that the updates on updatedLocationStatus are not directly saved in db
        em.detach(updatedLocationStatus);
        updatedLocationStatus.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(updatedLocationStatus);

        restLocationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
        LocationStatus testLocationStatus = locationStatusList.get(locationStatusList.size() - 1);
        assertThat(testLocationStatus.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testLocationStatus.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void putNonExistingLocationStatus() throws Exception {
        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();
        locationStatus.setId(count.incrementAndGet());

        // Create the LocationStatus
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(locationStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationStatus() throws Exception {
        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();
        locationStatus.setId(count.incrementAndGet());

        // Create the LocationStatus
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(locationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationStatus() throws Exception {
        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();
        locationStatus.setId(count.incrementAndGet());

        // Create the LocationStatus
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(locationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationStatusWithPatch() throws Exception {
        // Initialize the database
        locationStatusRepository.saveAndFlush(locationStatus);

        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();

        // Update the locationStatus using partial update
        LocationStatus partialUpdatedLocationStatus = new LocationStatus();
        partialUpdatedLocationStatus.setId(locationStatus.getId());

        partialUpdatedLocationStatus.nameAr(UPDATED_NAME_AR);

        restLocationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationStatus))
            )
            .andExpect(status().isOk());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
        LocationStatus testLocationStatus = locationStatusList.get(locationStatusList.size() - 1);
        assertThat(testLocationStatus.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testLocationStatus.getNameAn()).isEqualTo(DEFAULT_NAME_AN);
    }

    @Test
    @Transactional
    void fullUpdateLocationStatusWithPatch() throws Exception {
        // Initialize the database
        locationStatusRepository.saveAndFlush(locationStatus);

        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();

        // Update the locationStatus using partial update
        LocationStatus partialUpdatedLocationStatus = new LocationStatus();
        partialUpdatedLocationStatus.setId(locationStatus.getId());

        partialUpdatedLocationStatus.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);

        restLocationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationStatus))
            )
            .andExpect(status().isOk());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
        LocationStatus testLocationStatus = locationStatusList.get(locationStatusList.size() - 1);
        assertThat(testLocationStatus.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testLocationStatus.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void patchNonExistingLocationStatus() throws Exception {
        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();
        locationStatus.setId(count.incrementAndGet());

        // Create the LocationStatus
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(locationStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationStatus() throws Exception {
        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();
        locationStatus.setId(count.incrementAndGet());

        // Create the LocationStatus
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(locationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationStatus() throws Exception {
        int databaseSizeBeforeUpdate = locationStatusRepository.findAll().size();
        locationStatus.setId(count.incrementAndGet());

        // Create the LocationStatus
        LocationStatusDTO locationStatusDTO = locationStatusMapper.toDto(locationStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationStatus in the database
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationStatus() throws Exception {
        // Initialize the database
        locationStatusRepository.saveAndFlush(locationStatus);

        int databaseSizeBeforeDelete = locationStatusRepository.findAll().size();

        // Delete the locationStatus
        restLocationStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationStatus> locationStatusList = locationStatusRepository.findAll();
        assertThat(locationStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
