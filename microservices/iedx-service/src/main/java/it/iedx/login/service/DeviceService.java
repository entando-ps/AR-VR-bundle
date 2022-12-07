package it.iedx.login.service;

import it.iedx.login.domain.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DeviceService {

    /**
     * Save a device.
     *
     * @param accesscodeDTO the entity to save.
     * @return the persisted entity.
     */
    Device save(Device accesscodeDTO);

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Device> findAll(Pageable pageable);


    List<Device> findAll();

    /**
     * Get the "id" device.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Device> findOne(Long id);

    /**
     * Delete the "id" device.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Find the first device available for assignation
     * @return
     */
    Optional<Device> findAvailable();

    /**
     * Return the first device available for use
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    Optional<Device> getAvailableDevices();

    /**
     * Return a device by its unique ID
     * @param uid
     * @return
     */
    Optional<Device> getDeviceByUID(String uid);
}
