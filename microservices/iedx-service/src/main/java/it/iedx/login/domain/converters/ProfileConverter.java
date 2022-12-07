package it.iedx.login.domain.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.iedx.login.domain.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProfileConverter implements AttributeConverter<Profile, String> {

    private final Logger log = LoggerFactory.getLogger(ProfileConverter.class);

    @Override
    public String convertToDatabaseColumn(Profile profile) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(profile);
        } catch (JsonProcessingException e) {
            log.error("Error converting profile before database store", e);
        }
        return json;
    }

    @Override
    public Profile convertToEntityAttribute(String json) {
        Profile tmp = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            tmp = objectMapper.readValue(json, Profile.class);
        } catch (JsonProcessingException t) {
            log.error("Error converting entity coming from database into profile object", t);
        }
        return tmp;
    }
}
