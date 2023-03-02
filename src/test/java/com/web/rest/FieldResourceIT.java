package com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.IntegrationTest;
import com.domain.Field;
import com.repository.FieldRepository;
import com.service.dto.FieldDTO;
import com.service.mapper.FieldMapper;
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
 * Integration tests for the {@link FieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FieldResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldMapper fieldMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFieldMockMvc;

    private Field field;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Field createEntity(EntityManager em) {
        Field field = new Field().nameAr(DEFAULT_NAME_AR).nameAn(DEFAULT_NAME_AN);
        return field;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Field createUpdatedEntity(EntityManager em) {
        Field field = new Field().nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);
        return field;
    }

    @BeforeEach
    public void initTest() {
        field = createEntity(em);
    }

    @Test
    @Transactional
    void createField() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();
        // Create the Field
        FieldDTO fieldDTO = fieldMapper.toDto(field);
        restFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldDTO)))
            .andExpect(status().isCreated());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate + 1);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testField.getNameAn()).isEqualTo(DEFAULT_NAME_AN);
    }

    @Test
    @Transactional
    void createFieldWithExistingId() throws Exception {
        // Create the Field with an existing ID
        field.setId(1L);
        FieldDTO fieldDTO = fieldMapper.toDto(field);

        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFields() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList
        restFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameAn").value(hasItem(DEFAULT_NAME_AN)));
    }

    @Test
    @Transactional
    void getField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get the field
        restFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(field.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameAn").value(DEFAULT_NAME_AN));
    }

    @Test
    @Transactional
    void getNonExistingField() throws Exception {
        // Get the field
        restFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field
        Field updatedField = fieldRepository.findById(field.getId()).get();
        // Disconnect from session so that the updates on updatedField are not directly saved in db
        em.detach(updatedField);
        updatedField.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);
        FieldDTO fieldDTO = fieldMapper.toDto(updatedField);

        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldDTO))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testField.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void putNonExistingField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // Create the Field
        FieldDTO fieldDTO = fieldMapper.toDto(field);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // Create the Field
        FieldDTO fieldDTO = fieldMapper.toDto(field);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // Create the Field
        FieldDTO fieldDTO = fieldMapper.toDto(field);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFieldWithPatch() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field using partial update
        Field partialUpdatedField = new Field();
        partialUpdatedField.setId(field.getId());

        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedField))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testField.getNameAn()).isEqualTo(DEFAULT_NAME_AN);
    }

    @Test
    @Transactional
    void fullUpdateFieldWithPatch() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field using partial update
        Field partialUpdatedField = new Field();
        partialUpdatedField.setId(field.getId());

        partialUpdatedField.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);

        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedField))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testField.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void patchNonExistingField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // Create the Field
        FieldDTO fieldDTO = fieldMapper.toDto(field);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fieldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // Create the Field
        FieldDTO fieldDTO = fieldMapper.toDto(field);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // Create the Field
        FieldDTO fieldDTO = fieldMapper.toDto(field);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeDelete = fieldRepository.findAll().size();

        // Delete the field
        restFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, field.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
