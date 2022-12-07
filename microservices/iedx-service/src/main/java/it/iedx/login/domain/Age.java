package it.iedx.login.domain;

import org.apache.commons.lang3.StringUtils;

public enum Age {
    CHILD("CHILD", 1, 12),
    TEEN("TEEN", 13, 19),
    YOUNG_ADULT("YOUNG_ADULT",20,35),
    ADULT("ADULT", 36,64),
    ELDER("ELDER",65,101);

    private final String code;
    private int from;
    private int to;

    Age(String code, int from, int to) {
        this.from = from;
        this.to = to;
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static Age codeOf(String code) {
        if (StringUtils.isNotBlank(code)) {
            if (code.equalsIgnoreCase("CHILD")) {
                return Age.CHILD;
            } else if (code.equalsIgnoreCase("TEEN")) {
                return Age.TEEN;
            } else if (code.equalsIgnoreCase("YOUNG_ADULT")) {
                return Age.YOUNG_ADULT;
            } else if (code.equalsIgnoreCase("ADULT")) {
                return Age.ADULT;
            } else if (code.equalsIgnoreCase("ELDER")){
                return Age.ELDER;
            }
        }
        return null; //Does not match
    }

//    @JsonValue
    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
