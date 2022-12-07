package it.iedx.login.service;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.LeaseStatus;
import it.iedx.login.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AccessCode}.
 */
public interface AccesscodeService {

    /**
     * Save a accesscode.
     *
     * @param accesscodeDTO the entity to save.
     * @return the persisted entity.
     */
    AccessCode save(AccessCode accesscodeDTO);

    /**
     * Update the checked field and return the current status of the access code
     * @param id the ID of the desired accesscode
     * @return the current status of the accesscode
     */
    LeaseStatus keepAlive(long id);

    /**
     * Get all the accesscodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccessCode> findAll(Pageable pageable);


    /**
     * Get the "id" accesscode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccessCode> findOne(Long id);

    /**
     * Delete the "id" accesscode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Find a profile associated to the given access code.
     * Only the records with state PENDING or EXTERNAL will be considered.
     * @return return the profile associated with the given access code in PENDING or EXTERNAL state
     */
    Profile getProfileByAccessCode(String accessCode);

    /**
     * Find a record.
     * Only those records in status PENDING or EXTERNAL will be considered.
     * @param accessCode desired access code
     * @return return the access code in PENDING or EXTERNAL state
     */
    Optional<AccessCode> loadPendingByAccessCode(String accessCode);

    /**
     * Find the PENDING devices given the UID.
     * Only the records with state PENDING will be considered.
     * @param id the ID associated to a device
     * @return return the desired access code
     */
    Optional<AccessCode> findPendingByDevice(Long id);

    /**
     * Update the state of teh given access code
     * @param id the id of the access code to update
     * @param status the new state
     * @return the new status
     */
    LeaseStatus updateStatus(Long id, LeaseStatus status);
}
