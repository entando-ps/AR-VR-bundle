package it.iedx.login.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public enum Sentiment {
    ANGRY("ANGRY"),
    DISGUST("DISGUST"),
    FEAR("FEAR"),
    HAPPY("HAPPY"),
    SAD("SAD"),
    SURPRISE("SURPRISE"),
    NEUTRAL("NEUTRAL");

    private String text;

    Sentiment(String text) {
        this.text = text;
    }

    public static Sentiment codeOf(String gender) {
        if (StringUtils.isNotBlank(gender)) {
            if (gender.equalsIgnoreCase("ANGRY")) {
                return Sentiment.ANGRY;
            } else if (gender.equalsIgnoreCase("DISGUST")) {
                return Sentiment.DISGUST;
            } else if (gender.equalsIgnoreCase("FEAR")) {
                return Sentiment.FEAR;
            } else if (gender.equalsIgnoreCase("HAPPY")) {
                return Sentiment.HAPPY;
            } else if (gender.equalsIgnoreCase("SAD")) {
                return Sentiment.SAD;
            } else if (gender.equalsIgnoreCase("SURPRISE")) {
                return Sentiment.SURPRISE;
            } else {
                return Sentiment.NEUTRAL;
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
