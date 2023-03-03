package com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.IntegrationTest;
import com.domain.FormStatus;
import com.repository.FormStatusRepository;
import com.service.dto.FormStatusDTO;
import com.service.mapper.FormStatusMapper;
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
 * Integration tests for the {@link FormStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormStatusResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/form-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormStatusRepository formStatusRepository;

    @Autowired
    private FormStatusMapper formStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormStatusMockMvc;

    private FormStatus formStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormStatus createEntity(EntityManager em) {
        FormStatus formStatus = new FormStatus().nameAr(DEFAULT_NAME_AR).nameEn(DEFAULT_NAME_EN);
        return formStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormStatus createUpdatedEntity(EntityManager em) {
        FormStatus formStatus = new FormStatus().nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);
        return formStatus;
    }

    @BeforeEach
    public void initTest() {
        formStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createFormStatus() throws Exception {
        int databaseSizeBeforeCreate = formStatusRepository.findAll().size();
        // Create the FormStatus
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(formStatus);
        restFormStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeCreate + 1);
        FormStatus testFormStatus = formStatusList.get(formStatusList.size() - 1);
        assertThat(testFormStatus.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testFormStatus.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void createFormStatusWithExistingId() throws Exception {
        // Create the FormStatus with an existing ID
        formStatus.setId(1L);
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(formStatus);

        int databaseSizeBeforeCreate = formStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormStatuses() throws Exception {
        // Initialize the database
        formStatusRepository.saveAndFlush(formStatus);

        // Get all the formStatusList
        restFormStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)));
    }

    @Test
    @Transactional
    void getFormStatus() throws Exception {
        // Initialize the database
        formStatusRepository.saveAndFlush(formStatus);

        // Get the formStatus
        restFormStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, formStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formStatus.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN));
    }

    @Test
    @Transactional
    void getNonExistingFormStatus() throws Exception {
        // Get the formStatus
        restFormStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormStatus() throws Exception {
        // Initialize the database
        formStatusRepository.saveAndFlush(formStatus);

        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();

        // Update the formStatus
        FormStatus updatedFormStatus = formStatusRepository.findById(formStatus.getId()).get();
        // Disconnect from session so that the updates on updatedFormStatus are not directly saved in db
        em.detach(updatedFormStatus);
        updatedFormStatus.nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(updatedFormStatus);

        restFormStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
        FormStatus testFormStatus = formStatusList.get(formStatusList.size() - 1);
        assertThat(testFormStatus.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testFormStatus.getNameEn()).isEqualTo(UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void putNonExistingFormStatus() throws Exception {
        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();
        formStatus.setId(count.incrementAndGet());

        // Create the FormStatus
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(formStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormStatus() throws Exception {
        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();
        formStatus.setId(count.incrementAndGet());

        // Create the FormStatus
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(formStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormStatus() throws Exception {
        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();
        formStatus.setId(count.incrementAndGet());

        // Create the FormStatus
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(formStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormStatusWithPatch() throws Exception {
        // Initialize the database
        formStatusRepository.saveAndFlush(formStatus);

        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();

        // Update the formStatus using partial update
        FormStatus partialUpdatedFormStatus = new FormStatus();
        partialUpdatedFormStatus.setId(formStatus.getId());

        restFormStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormStatus))
            )
            .andExpect(status().isOk());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
        FormStatus testFormStatus = formStatusList.get(formStatusList.size() - 1);
        assertThat(testFormStatus.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testFormStatus.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void fullUpdateFormStatusWithPatch() throws Exception {
        // Initialize the database
        formStatusRepository.saveAndFlush(formStatus);

        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();

        // Update the formStatus using partial update
        FormStatus partialUpdatedFormStatus = new FormStatus();
        partialUpdatedFormStatus.setId(formStatus.getId());

        partialUpdatedFormStatus.nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);

        restFormStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormStatus))
            )
            .andExpect(status().isOk());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
        FormStatus testFormStatus = formStatusList.get(formStatusList.size() - 1);
        assertThat(testFormStatus.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testFormStatus.getNameEn()).isEqualTo(UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void patchNonExistingFormStatus() throws Exception {
        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();
        formStatus.setId(count.incrementAndGet());

        // Create the FormStatus
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(formStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormStatus() throws Exception {
        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();
        formStatus.setId(count.incrementAndGet());

        // Create the FormStatus
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(formStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormStatus() throws Exception {
        int databaseSizeBeforeUpdate = formStatusRepository.findAll().size();
        formStatus.setId(count.incrementAndGet());

        // Create the FormStatus
        FormStatusDTO formStatusDTO = formStatusMapper.toDto(formStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormStatus in the database
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormStatus() throws Exception {
        // Initialize the database
        formStatusRepository.saveAndFlush(formStatus);

        int databaseSizeBeforeDelete = formStatusRepository.findAll().size();

        // Delete the formStatus
        restFormStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, formStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormStatus> formStatusList = formStatusRepository.findAll();
        assertThat(formStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
