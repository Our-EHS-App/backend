package com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.IntegrationTest;
import com.domain.FieldType;
import com.repository.FieldTypeRepository;
import com.service.dto.FieldTypeDTO;
import com.service.mapper.FieldTypeMapper;
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
 * Integration tests for the {@link FieldTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FieldTypeResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/field-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FieldTypeRepository fieldTypeRepository;

    @Autowired
    private FieldTypeMapper fieldTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFieldTypeMockMvc;

    private FieldType fieldType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldType createEntity(EntityManager em) {
        FieldType fieldType = new FieldType().nameAr(DEFAULT_NAME_AR).nameAn(DEFAULT_NAME_AN);
        return fieldType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldType createUpdatedEntity(EntityManager em) {
        FieldType fieldType = new FieldType().nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);
        return fieldType;
    }

    @BeforeEach
    public void initTest() {
        fieldType = createEntity(em);
    }

    @Test
    @Transactional
    void createFieldType() throws Exception {
        int databaseSizeBeforeCreate = fieldTypeRepository.findAll().size();
        // Create the FieldType
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(fieldType);
        restFieldTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FieldType testFieldType = fieldTypeList.get(fieldTypeList.size() - 1);
        assertThat(testFieldType.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testFieldType.getNameAn()).isEqualTo(DEFAULT_NAME_AN);
    }

    @Test
    @Transactional
    void createFieldTypeWithExistingId() throws Exception {
        // Create the FieldType with an existing ID
        fieldType.setId(1L);
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(fieldType);

        int databaseSizeBeforeCreate = fieldTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFieldTypes() throws Exception {
        // Initialize the database
        fieldTypeRepository.saveAndFlush(fieldType);

        // Get all the fieldTypeList
        restFieldTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fieldType.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameAn").value(hasItem(DEFAULT_NAME_AN)));
    }

    @Test
    @Transactional
    void getFieldType() throws Exception {
        // Initialize the database
        fieldTypeRepository.saveAndFlush(fieldType);

        // Get the fieldType
        restFieldTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fieldType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fieldType.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameAn").value(DEFAULT_NAME_AN));
    }

    @Test
    @Transactional
    void getNonExistingFieldType() throws Exception {
        // Get the fieldType
        restFieldTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFieldType() throws Exception {
        // Initialize the database
        fieldTypeRepository.saveAndFlush(fieldType);

        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();

        // Update the fieldType
        FieldType updatedFieldType = fieldTypeRepository.findById(fieldType.getId()).get();
        // Disconnect from session so that the updates on updatedFieldType are not directly saved in db
        em.detach(updatedFieldType);
        updatedFieldType.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(updatedFieldType);

        restFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fieldTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
        FieldType testFieldType = fieldTypeList.get(fieldTypeList.size() - 1);
        assertThat(testFieldType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testFieldType.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void putNonExistingFieldType() throws Exception {
        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();
        fieldType.setId(count.incrementAndGet());

        // Create the FieldType
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(fieldType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fieldTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFieldType() throws Exception {
        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();
        fieldType.setId(count.incrementAndGet());

        // Create the FieldType
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(fieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFieldType() throws Exception {
        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();
        fieldType.setId(count.incrementAndGet());

        // Create the FieldType
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(fieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFieldTypeWithPatch() throws Exception {
        // Initialize the database
        fieldTypeRepository.saveAndFlush(fieldType);

        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();

        // Update the fieldType using partial update
        FieldType partialUpdatedFieldType = new FieldType();
        partialUpdatedFieldType.setId(fieldType.getId());

        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFieldType))
            )
            .andExpect(status().isOk());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
        FieldType testFieldType = fieldTypeList.get(fieldTypeList.size() - 1);
        assertThat(testFieldType.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testFieldType.getNameAn()).isEqualTo(DEFAULT_NAME_AN);
    }

    @Test
    @Transactional
    void fullUpdateFieldTypeWithPatch() throws Exception {
        // Initialize the database
        fieldTypeRepository.saveAndFlush(fieldType);

        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();

        // Update the fieldType using partial update
        FieldType partialUpdatedFieldType = new FieldType();
        partialUpdatedFieldType.setId(fieldType.getId());

        partialUpdatedFieldType.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);

        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFieldType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFieldType))
            )
            .andExpect(status().isOk());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
        FieldType testFieldType = fieldTypeList.get(fieldTypeList.size() - 1);
        assertThat(testFieldType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testFieldType.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void patchNonExistingFieldType() throws Exception {
        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();
        fieldType.setId(count.incrementAndGet());

        // Create the FieldType
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(fieldType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fieldTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFieldType() throws Exception {
        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();
        fieldType.setId(count.incrementAndGet());

        // Create the FieldType
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(fieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFieldType() throws Exception {
        int databaseSizeBeforeUpdate = fieldTypeRepository.findAll().size();
        fieldType.setId(count.incrementAndGet());

        // Create the FieldType
        FieldTypeDTO fieldTypeDTO = fieldTypeMapper.toDto(fieldType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fieldTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FieldType in the database
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFieldType() throws Exception {
        // Initialize the database
        fieldTypeRepository.saveAndFlush(fieldType);

        int databaseSizeBeforeDelete = fieldTypeRepository.findAll().size();

        // Delete the fieldType
        restFieldTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fieldType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FieldType> fieldTypeList = fieldTypeRepository.findAll();
        assertThat(fieldTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
