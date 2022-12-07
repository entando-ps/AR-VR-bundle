package it.iedx.login.domain.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.iedx.login.domain.DeviceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DeviceStatusConverter implements AttributeConverter<DeviceStatus, Integer> {

    private final Logger log = LoggerFactory.getLogger(DeviceStatusConverter.class);

    @Override
    public Integer convertToDatabaseColumn(DeviceStatus deviceStatus) {
        ObjectMapper mapper = new ObjectMapper();
        int value = -1;

        try {
            String json = mapper.writeValueAsString(deviceStatus);
            value = Integer.parseInt(json);
        } catch (Throwable e) {
            log.error("Error converting DeviceStatus before database store", e);
        }
        return value;
    }

    @Override
    public DeviceStatus convertToEntityAttribute(Integer value) {
        DeviceStatus status = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            status =  objectMapper.readValue(value.toString(), DeviceStatus.class);
        } catch (Throwable t) {
            log.error("Error converting entity coming from database into DeviceStatus object", t);
        }
        return status;
    }
}
