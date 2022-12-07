package it.iedx.login.domain.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.iedx.login.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocationConverter implements AttributeConverter<Location, String> {

    private final Logger log = LoggerFactory.getLogger(LocationConverter.class);

    @Override
    public String convertToDatabaseColumn(Location location) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(location);
        } catch (JsonProcessingException e) {
            log.error("Error converting Location before database store", e);
        }
        return json;
    }

    @Override
    public Location convertToEntityAttribute(String json) {
        Location tmp = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            tmp = objectMapper.readValue(json, Location.class);
        } catch (JsonProcessingException t) {
            log.error("Error converting entity coming from database into Location object", t);
        }
        return tmp;
    }

}
