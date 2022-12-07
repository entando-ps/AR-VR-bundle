package it.iedx.login.domain.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.iedx.login.domain.ExperienceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ExperienceStatusConverter implements AttributeConverter<ExperienceStatus, String> {

    private final Logger log = LoggerFactory.getLogger(ExperienceStatusConverter.class);

    @Override
    public String convertToDatabaseColumn(ExperienceStatus profile) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(profile);
        } catch (JsonProcessingException e) {
            log.error("Error converting ExperienceStatus before database store", e);
        }
        return json;
    }

    @Override
    public ExperienceStatus convertToEntityAttribute(String json) {
        ExperienceStatus tmp = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            tmp = objectMapper.readValue(json, ExperienceStatus.class);
        } catch (JsonProcessingException t) {
            log.error("Error converting entity coming from database into ExperienceStatus object", t);
        }
        return tmp;
    }
}
