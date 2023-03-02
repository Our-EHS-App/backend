package com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.IntegrationTest;
import com.domain.TemplateType;
import com.repository.TemplateTypeRepository;
import com.service.dto.TemplateTypeDTO;
import com.service.mapper.TemplateTypeMapper;
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
 * Integration tests for the {@link TemplateTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TemplateTypeResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/template-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemplateTypeRepository templateTypeRepository;

    @Autowired
    private TemplateTypeMapper templateTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplateTypeMockMvc;

    private TemplateType templateType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateType createEntity(EntityManager em) {
        TemplateType templateType = new TemplateType().nameAr(DEFAULT_NAME_AR).nameAn(DEFAULT_NAME_AN);
        return templateType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TemplateType createUpdatedEntity(EntityManager em) {
        TemplateType templateType = new TemplateType().nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);
        return templateType;
    }

    @BeforeEach
    public void initTest() {
        templateType = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplateType() throws Exception {
        int databaseSizeBeforeCreate = templateTypeRepository.findAll().size();
        // Create the TemplateType
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(templateType);
        restTemplateTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TemplateType testTemplateType = templateTypeList.get(templateTypeList.size() - 1);
        assertThat(testTemplateType.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testTemplateType.getNameAn()).isEqualTo(DEFAULT_NAME_AN);
    }

    @Test
    @Transactional
    void createTemplateTypeWithExistingId() throws Exception {
        // Create the TemplateType with an existing ID
        templateType.setId(1L);
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(templateType);

        int databaseSizeBeforeCreate = templateTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTemplateTypes() throws Exception {
        // Initialize the database
        templateTypeRepository.saveAndFlush(templateType);

        // Get all the templateTypeList
        restTemplateTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(templateType.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameAn").value(hasItem(DEFAULT_NAME_AN)));
    }

    @Test
    @Transactional
    void getTemplateType() throws Exception {
        // Initialize the database
        templateTypeRepository.saveAndFlush(templateType);

        // Get the templateType
        restTemplateTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, templateType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(templateType.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameAn").value(DEFAULT_NAME_AN));
    }

    @Test
    @Transactional
    void getNonExistingTemplateType() throws Exception {
        // Get the templateType
        restTemplateTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplateType() throws Exception {
        // Initialize the database
        templateTypeRepository.saveAndFlush(templateType);

        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();

        // Update the templateType
        TemplateType updatedTemplateType = templateTypeRepository.findById(templateType.getId()).get();
        // Disconnect from session so that the updates on updatedTemplateType are not directly saved in db
        em.detach(updatedTemplateType);
        updatedTemplateType.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(updatedTemplateType);

        restTemplateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templateTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
        TemplateType testTemplateType = templateTypeList.get(templateTypeList.size() - 1);
        assertThat(testTemplateType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testTemplateType.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void putNonExistingTemplateType() throws Exception {
        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();
        templateType.setId(count.incrementAndGet());

        // Create the TemplateType
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(templateType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templateTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplateType() throws Exception {
        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();
        templateType.setId(count.incrementAndGet());

        // Create the TemplateType
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(templateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplateType() throws Exception {
        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();
        templateType.setId(count.incrementAndGet());

        // Create the TemplateType
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(templateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplateTypeWithPatch() throws Exception {
        // Initialize the database
        templateTypeRepository.saveAndFlush(templateType);

        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();

        // Update the templateType using partial update
        TemplateType partialUpdatedTemplateType = new TemplateType();
        partialUpdatedTemplateType.setId(templateType.getId());

        partialUpdatedTemplateType.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);

        restTemplateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateType))
            )
            .andExpect(status().isOk());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
        TemplateType testTemplateType = templateTypeList.get(templateTypeList.size() - 1);
        assertThat(testTemplateType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testTemplateType.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void fullUpdateTemplateTypeWithPatch() throws Exception {
        // Initialize the database
        templateTypeRepository.saveAndFlush(templateType);

        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();

        // Update the templateType using partial update
        TemplateType partialUpdatedTemplateType = new TemplateType();
        partialUpdatedTemplateType.setId(templateType.getId());

        partialUpdatedTemplateType.nameAr(UPDATED_NAME_AR).nameAn(UPDATED_NAME_AN);

        restTemplateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplateType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplateType))
            )
            .andExpect(status().isOk());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
        TemplateType testTemplateType = templateTypeList.get(templateTypeList.size() - 1);
        assertThat(testTemplateType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testTemplateType.getNameAn()).isEqualTo(UPDATED_NAME_AN);
    }

    @Test
    @Transactional
    void patchNonExistingTemplateType() throws Exception {
        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();
        templateType.setId(count.incrementAndGet());

        // Create the TemplateType
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(templateType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, templateTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplateType() throws Exception {
        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();
        templateType.setId(count.incrementAndGet());

        // Create the TemplateType
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(templateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplateType() throws Exception {
        int databaseSizeBeforeUpdate = templateTypeRepository.findAll().size();
        templateType.setId(count.incrementAndGet());

        // Create the TemplateType
        TemplateTypeDTO templateTypeDTO = templateTypeMapper.toDto(templateType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TemplateType in the database
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplateType() throws Exception {
        // Initialize the database
        templateTypeRepository.saveAndFlush(templateType);

        int databaseSizeBeforeDelete = templateTypeRepository.findAll().size();

        // Delete the templateType
        restTemplateTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, templateType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TemplateType> templateTypeList = templateTypeRepository.findAll();
        assertThat(templateTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
