package it.iedx.login.domain.converters;

import it.iedx.login.domain.EventType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EventTypeConverter  implements AttributeConverter<EventType, String> {

    private final Logger log = LoggerFactory.getLogger(EventTypeConverter.class);

    @Override
    public String convertToDatabaseColumn(EventType eventType) {
        String type = null;

        try {
            if (eventType != null) {
                type = eventType.getCode();
            }
        } catch (Throwable t) {
            log.error("Error converting EventType before database store", t);
        }
        return type;
    }

    @Override
    public EventType convertToEntityAttribute(String text) {
        EventType type = null;

        try {
            if (StringUtils.isNotBlank(text)) {
                type = EventType.codeOf(text);
            }
        } catch (Throwable t) {
            log.error("Error converting entity coming from database into EventType object", t);
        }
        return type;
    }
}
