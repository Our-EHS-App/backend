package com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.IntegrationTest;
import com.domain.Form;
import com.repository.FormRepository;
import com.service.dto.FormDTO;
import com.service.mapper.FormMapper;
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
 * Integration tests for the {@link FormResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormMapper formMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormMockMvc;

    private Form form;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Form createEntity(EntityManager em) {
        Form form = new Form().nameAr(DEFAULT_NAME_AR).nameEn(DEFAULT_NAME_EN);
        return form;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Form createUpdatedEntity(EntityManager em) {
        Form form = new Form().nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);
        return form;
    }

    @BeforeEach
    public void initTest() {
        form = createEntity(em);
    }

    @Test
    @Transactional
    void createForm() throws Exception {
        int databaseSizeBeforeCreate = formRepository.findAll().size();
        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);
        restFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formDTO)))
            .andExpect(status().isCreated());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeCreate + 1);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testForm.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void createFormWithExistingId() throws Exception {
        // Create the Form with an existing ID
        form.setId(1L);
        FormDTO formDTO = formMapper.toDto(form);

        int databaseSizeBeforeCreate = formRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllForms() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the formList
        restFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(form.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)));
    }

    @Test
    @Transactional
    void getForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get the form
        restFormMockMvc
            .perform(get(ENTITY_API_URL_ID, form.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(form.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN));
    }

    @Test
    @Transactional
    void getNonExistingForm() throws Exception {
        // Get the form
        restFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form
        Form updatedForm = formRepository.findById(form.getId()).get();
        // Disconnect from session so that the updates on updatedForm are not directly saved in db
        em.detach(updatedForm);
        updatedForm.nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);
        FormDTO formDTO = formMapper.toDto(updatedForm);

        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testForm.getNameEn()).isEqualTo(UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void putNonExistingForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormWithPatch() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form using partial update
        Form partialUpdatedForm = new Form();
        partialUpdatedForm.setId(form.getId());

        partialUpdatedForm.nameAr(UPDATED_NAME_AR);

        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForm))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testForm.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
    }

    @Test
    @Transactional
    void fullUpdateFormWithPatch() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form using partial update
        Form partialUpdatedForm = new Form();
        partialUpdatedForm.setId(form.getId());

        partialUpdatedForm.nameAr(UPDATED_NAME_AR).nameEn(UPDATED_NAME_EN);

        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForm))
            )
            .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
        Form testForm = formList.get(formList.size() - 1);
        assertThat(testForm.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testForm.getNameEn()).isEqualTo(UPDATED_NAME_EN);
    }

    @Test
    @Transactional
    void patchNonExistingForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamForm() throws Exception {
        int databaseSizeBeforeUpdate = formRepository.findAll().size();
        form.setId(count.incrementAndGet());

        // Create the Form
        FormDTO formDTO = formMapper.toDto(form);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Form in the database
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        int databaseSizeBeforeDelete = formRepository.findAll().size();

        // Delete the form
        restFormMockMvc
            .perform(delete(ENTITY_API_URL_ID, form.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Form> formList = formRepository.findAll();
        assertThat(formList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
