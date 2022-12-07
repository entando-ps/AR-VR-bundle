package it.iedx.login.domain.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.iedx.login.domain.LeaseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LeaseStatusConverter implements AttributeConverter<LeaseStatus, Integer>  {

    private final Logger log = LoggerFactory.getLogger(LeaseStatusConverter.class);

    @Override
    public Integer convertToDatabaseColumn(LeaseStatus leaseStatus) {
        ObjectMapper mapper = new ObjectMapper();
        int value = -1;
        try {
            String json = mapper.writeValueAsString(leaseStatus);
            value = Integer.parseInt(json);
        } catch (Throwable e) {
            log.error("Error converting LeaseStatus before database store", e);
        }
        return value;
    }

    @Override
    public LeaseStatus convertToEntityAttribute(Integer value) {
        LeaseStatus status = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            status =  objectMapper.readValue(value.toString(), LeaseStatus.class);
        } catch (Throwable t) {
            log.error("Error converting entity coming from database into LeaseStatus object", t);
        }
        return status;
    }
}
