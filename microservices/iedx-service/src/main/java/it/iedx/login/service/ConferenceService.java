package it.iedx.login.service;

import it.iedx.login.service.dto.ConferenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link it.iedx.login.domain.Conference}.
 */
public interface ConferenceService {
    /**
     * Save a conference.
     *
     * @param conferenceDTO the entity to save.
     * @return the persisted entity.
     */
    ConferenceDTO save(ConferenceDTO conferenceDTO);

    /**
     * Partially updates a conference.
     *
     * @param conferenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConferenceDTO> partialUpdate(ConferenceDTO conferenceDTO);

    /**
     * Get all the conferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConferenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" conference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConferenceDTO> findOne(Long id);

    /**
     * Delete the "id" conference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
