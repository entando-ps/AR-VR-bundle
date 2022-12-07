package it.iedx.login.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetType {
    IMAGE,
    VIDEO,
    HOTSPOT,
    SUBTITLE,
    DETAILS_PANEL;

    @JsonValue
    public int toValue() {
        return ordinal();
    }

}
