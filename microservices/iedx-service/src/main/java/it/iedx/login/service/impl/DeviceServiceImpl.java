package it.iedx.login.service.impl;

import it.iedx.login.domain.Device;
import it.iedx.login.domain.DeviceStatus;
import it.iedx.login.repository.DeviceRepository;
import it.iedx.login.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device save(Device device) {
        log.debug("Request to save device : {}", device);
        if (null == device.getId()) {
            device.setAdded(ZonedDateTime.now());
        } else {
            device.setUpdated(ZonedDateTime.now());
        }
        return deviceRepository.save(device);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Device> findAll(Pageable pageable) {
        log.debug("Request to get all devices");
        return deviceRepository.findAll(pageable);
    }

    @Override
    public List<Device> findAll() {
        log.debug("Request to get all devices");
        return deviceRepository.findAll();
    }

    @Override
    public Optional<Device> findOne(Long id) {
        log.debug("Request to get device : {}", id);
        return deviceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete device : {}", id);
        try {
            deviceRepository.deleteById(id);
        } catch (Throwable t) {
            log.error("Error deleting device: " + t.getMessage());
        }
    }

    @Override
    public Optional<Device> findAvailable() {
        Optional<Device> available = Optional.ofNullable(null);

        List<Device> devices = deviceRepository.statusIs(DeviceStatus.AVAILABLE);
        if (null != devices) {
            available = devices.stream().findFirst();
        }
        return available;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Optional<Device> getAvailableDevices() {
        return deviceRepository.getAvailableDevices();
    }

    @Override
    public Optional<Device> getDeviceByUID(String uid) {
        return deviceRepository.deviceIdIs(uid);
    }

}
