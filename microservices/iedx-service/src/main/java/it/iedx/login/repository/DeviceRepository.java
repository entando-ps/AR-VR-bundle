package it.iedx.login.repository;

import it.iedx.login.domain.Device;
import it.iedx.login.domain.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {

    List<Device> statusIs(DeviceStatus status);

    @Query(
        value = "SELECT D.id, D.deviceid, D.status, D.note, D.name, D.updated, D.added  FROM device D WHERE D.status = 1 AND D.id NOT IN (SELECT DISTINCT(A.device) FROM accesscode A WHERE A.status != 2 AND A.device IS NOT NULL) LIMIT 1",
        nativeQuery = true
    )
    Optional<Device> getAvailableDevices();

    Optional<Device> deviceIdIs(String id);

}
