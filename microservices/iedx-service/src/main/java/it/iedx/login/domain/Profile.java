package it.iedx.login.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile implements Serializable {

    private final Logger log = LoggerFactory.getLogger(Profile.class);

    private Gender gender;
    private Age age;
    private Sentiment sentiment;
    private Race race;

    public Profile() { }

    public Profile(Gender gender, Age age, Race race, Sentiment sentiment) {
        this.age = age;
        this.gender = gender;
        this.race = race;
        this.sentiment = sentiment;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public String toString() {
        return "[AGE: " + this.getAge() + ", GENDER: " + this.getGender() + " RACE: " + race + "  SENTIMENT: " + sentiment + "]";
    }
}
