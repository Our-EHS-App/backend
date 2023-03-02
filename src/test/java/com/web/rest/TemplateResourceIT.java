package com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.IntegrationTest;
import com.domain.Template;
import com.repository.TemplateRepository;
import com.service.TemplateService;
import com.service.dto.TemplateDTO;
import com.service.mapper.TemplateMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TemplateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TemplateResourceIT {

    private static final String DEFAULT_TITLE_AR = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_AR = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE_EN = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_EN = "BBBBBBBBBB";

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemplateRepository templateRepository;

    @Mock
    private TemplateRepository templateRepositoryMock;

    @Autowired
    private TemplateMapper templateMapper;

    @Mock
    private TemplateService templateServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplateMockMvc;

    private Template template;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Template createEntity(EntityManager em) {
        Template template = new Template().titleAr(DEFAULT_TITLE_AR).titleEn(DEFAULT_TITLE_EN).duration(DEFAULT_DURATION);
        return template;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Template createUpdatedEntity(EntityManager em) {
        Template template = new Template().titleAr(UPDATED_TITLE_AR).titleEn(UPDATED_TITLE_EN).duration(UPDATED_DURATION);
        return template;
    }

    @BeforeEach
    public void initTest() {
        template = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplate() throws Exception {
        int databaseSizeBeforeCreate = templateRepository.findAll().size();
        // Create the Template
        TemplateDTO templateDTO = templateMapper.toDto(template);
        restTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateDTO)))
            .andExpect(status().isCreated());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeCreate + 1);
        Template testTemplate = templateList.get(templateList.size() - 1);
        assertThat(testTemplate.getTitleAr()).isEqualTo(DEFAULT_TITLE_AR);
        assertThat(testTemplate.getTitleEn()).isEqualTo(DEFAULT_TITLE_EN);
        assertThat(testTemplate.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void createTemplateWithExistingId() throws Exception {
        // Create the Template with an existing ID
        template.setId(1L);
        TemplateDTO templateDTO = templateMapper.toDto(template);

        int databaseSizeBeforeCreate = templateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTemplates() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(template.getId().intValue())))
            .andExpect(jsonPath("$.[*].titleAr").value(hasItem(DEFAULT_TITLE_AR)))
            .andExpect(jsonPath("$.[*].titleEn").value(hasItem(DEFAULT_TITLE_EN)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(templateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(templateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(templateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(templateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get the template
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, template.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(template.getId().intValue()))
            .andExpect(jsonPath("$.titleAr").value(DEFAULT_TITLE_AR))
            .andExpect(jsonPath("$.titleEn").value(DEFAULT_TITLE_EN))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION));
    }

    @Test
    @Transactional
    void getNonExistingTemplate() throws Exception {
        // Get the template
        restTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        int databaseSizeBeforeUpdate = templateRepository.findAll().size();

        // Update the template
        Template updatedTemplate = templateRepository.findById(template.getId()).get();
        // Disconnect from session so that the updates on updatedTemplate are not directly saved in db
        em.detach(updatedTemplate);
        updatedTemplate.titleAr(UPDATED_TITLE_AR).titleEn(UPDATED_TITLE_EN).duration(UPDATED_DURATION);
        TemplateDTO templateDTO = templateMapper.toDto(updatedTemplate);

        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
        Template testTemplate = templateList.get(templateList.size() - 1);
        assertThat(testTemplate.getTitleAr()).isEqualTo(UPDATED_TITLE_AR);
        assertThat(testTemplate.getTitleEn()).isEqualTo(UPDATED_TITLE_EN);
        assertThat(testTemplate.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // Create the Template
        TemplateDTO templateDTO = templateMapper.toDto(template);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, templateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // Create the Template
        TemplateDTO templateDTO = templateMapper.toDto(template);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(templateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // Create the Template
        TemplateDTO templateDTO = templateMapper.toDto(template);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(templateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplateWithPatch() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        int databaseSizeBeforeUpdate = templateRepository.findAll().size();

        // Update the template using partial update
        Template partialUpdatedTemplate = new Template();
        partialUpdatedTemplate.setId(template.getId());

        partialUpdatedTemplate.titleEn(UPDATED_TITLE_EN).duration(UPDATED_DURATION);

        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplate))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
        Template testTemplate = templateList.get(templateList.size() - 1);
        assertThat(testTemplate.getTitleAr()).isEqualTo(DEFAULT_TITLE_AR);
        assertThat(testTemplate.getTitleEn()).isEqualTo(UPDATED_TITLE_EN);
        assertThat(testTemplate.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateTemplateWithPatch() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        int databaseSizeBeforeUpdate = templateRepository.findAll().size();

        // Update the template using partial update
        Template partialUpdatedTemplate = new Template();
        partialUpdatedTemplate.setId(template.getId());

        partialUpdatedTemplate.titleAr(UPDATED_TITLE_AR).titleEn(UPDATED_TITLE_EN).duration(UPDATED_DURATION);

        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemplate))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
        Template testTemplate = templateList.get(templateList.size() - 1);
        assertThat(testTemplate.getTitleAr()).isEqualTo(UPDATED_TITLE_AR);
        assertThat(testTemplate.getTitleEn()).isEqualTo(UPDATED_TITLE_EN);
        assertThat(testTemplate.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // Create the Template
        TemplateDTO templateDTO = templateMapper.toDto(template);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, templateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // Create the Template
        TemplateDTO templateDTO = templateMapper.toDto(template);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(templateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplate() throws Exception {
        int databaseSizeBeforeUpdate = templateRepository.findAll().size();
        template.setId(count.incrementAndGet());

        // Create the Template
        TemplateDTO templateDTO = templateMapper.toDto(template);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(templateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Template in the database
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        int databaseSizeBeforeDelete = templateRepository.findAll().size();

        // Delete the template
        restTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, template.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Template> templateList = templateRepository.findAll();
        assertThat(templateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
