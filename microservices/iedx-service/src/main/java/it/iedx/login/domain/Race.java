package it.iedx.login.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public enum Race {
    EAST_ASIAN("EAST_ASIAN"),
    SOUTHEAST_ASIAN("SOUTHEAST_ASIAN"),
    INDIAN("INDIAN"),
    BLACK("BLACK"),
    WHITE("WHITE"),
    MIDDLE_EASTERN("MIDDLE_EASTERN"),
    LATINO_ISPANIC("LATINO_ISPANIC");

    private String text;

    Race(String text) {
        this.text = text;
    }

    public static Race codeOf(String gender) {
        if (StringUtils.isNotBlank(gender)) {
            if (gender.equalsIgnoreCase("EAST_ASIAN")) {
                return Race.EAST_ASIAN;
            } else if (gender.equalsIgnoreCase("SOUTHEAST_ASIAN")) {
                return Race.SOUTHEAST_ASIAN;
            }  else if (gender.equalsIgnoreCase("INDIAN")) {
                return Race.INDIAN;
            }  else if (gender.equalsIgnoreCase("BLACK")) {
                return Race.BLACK;
            }  else if (gender.equalsIgnoreCase("WHITE")) {
                return Race.WHITE;
            }  else if (gender.equalsIgnoreCase("MIDDLE_EASTERN")) {
                return Race.MIDDLE_EASTERN;
            } else {
                return Race.LATINO_ISPANIC;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
