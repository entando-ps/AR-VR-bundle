package it.iedx.login.domain;

import org.apache.commons.lang3.StringUtils;

public enum Gender {
    FEMALE("FEMALE"),
    MALE("MALE"),
    UNKNOWN("UNKNOWN");

    private String text;

    Gender(String text) {
        this.text = text;
    }

    public static Gender codeOf(String gender) {
        if (StringUtils.isNotBlank(gender)) {
            if (gender.equalsIgnoreCase("FEMALE")) {
                return Gender.FEMALE;
            } else if (gender.equalsIgnoreCase("MALE")) {
                return Gender.MALE;
            } else {
                return Gender.UNKNOWN;
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
