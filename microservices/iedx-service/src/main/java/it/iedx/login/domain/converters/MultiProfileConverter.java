package it.iedx.login.domain.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.iedx.login.domain.MultiProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MultiProfileConverter implements AttributeConverter<MultiProfile, String> {

    private final Logger log = LoggerFactory.getLogger(MultiProfileConverter.class);

    @Override
    public String convertToDatabaseColumn(MultiProfile multiProfile) {
        String json = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(multiProfile);
        } catch (Throwable t) {
            log.error("Error converting multiprofile before database store", t);
        }
        return json;
    }

    @Override
    public MultiProfile convertToEntityAttribute(String s) {
        MultiProfile multiProfile = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            multiProfile = objectMapper.readValue(s, MultiProfile.class);
        } catch (Throwable t) {
            log.error("Error converting entity coming from database into MultiProfileConverter object", t);
        }
        return multiProfile;
    }

}
