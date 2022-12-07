package it.iedx.login.domain;

import org.apache.commons.lang3.StringUtils;

public enum EventType {
    EXPERIENCE_START("EXPERIENCE_START"),
    EXPERIENCE_END("EXPERIENCE_END"),
    HOTSPOT_SELECTION("HOTSPOT_SELECTION"),
    PAUSE("PAUSE"),
    RESUME("RESUME"),
    SKIP_FORWARD("SKIP_FORWARD"),
    SKIP_BACKWARD("SKIP_BACKWARD"),
    OPTIONSPOPUP_CHOICE("OPTIONSPOPUP_CHOICE"),
    RESTART_VIDEO("RESTART_VIDEO"),
    DETAILSPANEL_EXPAND("DETAILSPANEL_EXPAND"),
    DETAILSPANEL_MINIMIZE("DETAILSPANEL_MINIMIZE"),
    SCENARIO_SELECTION("SCENARIO_SELECTION"),
    START_SCENARIO("START_SCENARIO");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public String getCode() {
        return this.type;
    }

    public static EventType codeOf(String code) {
        if (StringUtils.isNotBlank(code)) {
            if (code.equalsIgnoreCase("EXPERIENCE_START")) {
                return EventType.EXPERIENCE_START;
            } else if (code.equalsIgnoreCase("EXPERIENCE_END")) {
                return EventType.EXPERIENCE_END;
            } else if (code.equalsIgnoreCase("HOTSPOT_SELECTION")) {
                return EventType.HOTSPOT_SELECTION;
            } else if (code.equalsIgnoreCase("PAUSE")) {
                return EventType.PAUSE;
            } else if (code.equalsIgnoreCase("RESUME")) {
                return EventType.RESUME;
            } else if (code.equalsIgnoreCase("SKIP_FORWARD")) {
                return EventType.SKIP_FORWARD;
            } else if (code.equalsIgnoreCase("SKIP_BACKWARD")) {
                return EventType.SKIP_BACKWARD;
            } else if (code.equalsIgnoreCase("OPTIONSPOPUP_CHOICE")) {
                return EventType.OPTIONSPOPUP_CHOICE;
            } else if (code.equalsIgnoreCase("RESTART_VIDEO")) {
                return EventType.RESTART_VIDEO;
            } else if (code.equalsIgnoreCase("DETAILSPANEL_EXPAND")) {
                return EventType.DETAILSPANEL_EXPAND;
            } else if (code.equalsIgnoreCase("DETAILSPANEL_MINIMIZE")) {
                return EventType.DETAILSPANEL_MINIMIZE;
            } else if (code.equalsIgnoreCase("SCENARIO_SELECTION")) {
                return EventType.SCENARIO_SELECTION;
            } else if (code.equalsIgnoreCase("START_SCENARIO")) {
                return EventType.START_SCENARIO;
            }else {
                return null;
            }
        }
        return null;
    }
}
