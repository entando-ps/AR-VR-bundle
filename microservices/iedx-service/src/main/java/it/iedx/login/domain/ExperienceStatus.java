package it.iedx.login.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExperienceStatus {
    DRAFT,
    PUBLISHED;

    @JsonValue
    public int toValue() {
        return ordinal();
    }

}
