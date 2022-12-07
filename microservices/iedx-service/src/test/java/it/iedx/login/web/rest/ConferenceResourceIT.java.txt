package it.iedx.login.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.iedx.login.IntegrationTest;
import it.iedx.login.domain.Conference;
import it.iedx.login.repository.ConferenceRepository;
import it.iedx.login.service.criteria.ConferenceCriteria;
import it.iedx.login.service.dto.ConferenceDTO;
import it.iedx.login.service.mapper.ConferenceMapper;
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
 * Integration tests for the {@link ConferenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConferenceResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceMapper conferenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConferenceMockMvc;

    private Conference conference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conference createEntity(EntityManager em) {
        Conference conference = new Conference().nome(DEFAULT_NOME);
        return conference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conference createUpdatedEntity(EntityManager em) {
        Conference conference = new Conference().nome(UPDATED_NOME);
        return conference;
    }

    @BeforeEach
    public void initTest() {
        conference = createEntity(em);
    }

    @Test
    @Transactional
    void createConference() throws Exception {
        int databaseSizeBeforeCreate = conferenceRepository.findAll().size();
        // Create the Conference
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(conference);
        restConferenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeCreate + 1);
        Conference testConference = conferenceList.get(conferenceList.size() - 1);
        assertThat(testConference.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createConferenceWithExistingId() throws Exception {
        // Create the Conference with an existing ID
        conference.setId(1L);
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(conference);

        int databaseSizeBeforeCreate = conferenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConferenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConferences() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conference.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getConference() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get the conference
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, conference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conference.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getConferencesByIdFiltering() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        Long id = conference.getId();

        defaultConferenceShouldBeFound("id.equals=" + id);
        defaultConferenceShouldNotBeFound("id.notEquals=" + id);

        defaultConferenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConferenceShouldNotBeFound("id.greaterThan=" + id);

        defaultConferenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConferenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConferencesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where nome equals to DEFAULT_NOME
        defaultConferenceShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the conferenceList where nome equals to UPDATED_NOME
        defaultConferenceShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllConferencesByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where nome not equals to DEFAULT_NOME
        defaultConferenceShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the conferenceList where nome not equals to UPDATED_NOME
        defaultConferenceShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllConferencesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultConferenceShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the conferenceList where nome equals to UPDATED_NOME
        defaultConferenceShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllConferencesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where nome is not null
        defaultConferenceShouldBeFound("nome.specified=true");

        // Get all the conferenceList where nome is null
        defaultConferenceShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllConferencesByNomeContainsSomething() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where nome contains DEFAULT_NOME
        defaultConferenceShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the conferenceList where nome contains UPDATED_NOME
        defaultConferenceShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllConferencesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        // Get all the conferenceList where nome does not contain DEFAULT_NOME
        defaultConferenceShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the conferenceList where nome does not contain UPDATED_NOME
        defaultConferenceShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConferenceShouldBeFound(String filter) throws Exception {
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conference.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));

        // Check, that the count call also returns 1
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConferenceShouldNotBeFound(String filter) throws Exception {
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConference() throws Exception {
        // Get the conference
        restConferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConference() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();

        // Update the conference
        Conference updatedConference = conferenceRepository.findById(conference.getId()).get();
        // Disconnect from session so that the updates on updatedConference are not directly saved in db
        em.detach(updatedConference);
        updatedConference.nome(UPDATED_NOME);
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(updatedConference);

        restConferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conferenceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
        Conference testConference = conferenceList.get(conferenceList.size() - 1);
        assertThat(testConference.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void putNonExistingConference() throws Exception {
        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();
        conference.setId(count.incrementAndGet());

        // Create the Conference
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(conference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conferenceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConference() throws Exception {
        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();
        conference.setId(count.incrementAndGet());

        // Create the Conference
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(conference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConference() throws Exception {
        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();
        conference.setId(count.incrementAndGet());

        // Create the Conference
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(conference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConferenceWithPatch() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();

        // Update the conference using partial update
        Conference partialUpdatedConference = new Conference();
        partialUpdatedConference.setId(conference.getId());

        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConference))
            )
            .andExpect(status().isOk());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
        Conference testConference = conferenceList.get(conferenceList.size() - 1);
        assertThat(testConference.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void fullUpdateConferenceWithPatch() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();

        // Update the conference using partial update
        Conference partialUpdatedConference = new Conference();
        partialUpdatedConference.setId(conference.getId());

        partialUpdatedConference.nome(UPDATED_NOME);

        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConference))
            )
            .andExpect(status().isOk());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
        Conference testConference = conferenceList.get(conferenceList.size() - 1);
        assertThat(testConference.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingConference() throws Exception {
        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();
        conference.setId(count.incrementAndGet());

        // Create the Conference
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(conference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conferenceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConference() throws Exception {
        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();
        conference.setId(count.incrementAndGet());

        // Create the Conference
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(conference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConference() throws Exception {
        int databaseSizeBeforeUpdate = conferenceRepository.findAll().size();
        conference.setId(count.incrementAndGet());

        // Create the Conference
        ConferenceDTO conferenceDTO = conferenceMapper.toDto(conference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConferenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conferenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conference in the database
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConference() throws Exception {
        // Initialize the database
        conferenceRepository.saveAndFlush(conference);

        int databaseSizeBeforeDelete = conferenceRepository.findAll().size();

        // Delete the conference
        restConferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, conference.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conference> conferenceList = conferenceRepository.findAll();
        assertThat(conferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
