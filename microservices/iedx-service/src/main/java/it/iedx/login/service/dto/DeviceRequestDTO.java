package it.iedx.login.service.dto;

import it.iedx.login.domain.Device;
import it.iedx.login.domain.DeviceStatus;
import org.springframework.beans.BeanUtils;

public class DeviceRequestDTO {

    public String deviceId;
    public String note;
    public String name;
    public DeviceStatus status;

    public Device toDevice() {
        Device dev = new Device();
        BeanUtils.copyProperties(this, dev);
        return dev;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }
}
