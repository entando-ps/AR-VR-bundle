package it.iedx.login.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "\"event\"")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Event {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "\"experienceid\"")
    private Long experienceId;

    @Column(name = "\"accesscodeid\"")
    private Long accessCodeId; // FK

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "\"date\"")
    private ZonedDateTime date; // data di registrazione evento

    @Column(name = "\"event\"")
    private String eventName; // payload generico (in formato json?)

    @Column(name = "\"type\"")
    private EventType type;

    @Column(name = "\"scenarioelementid\"")
    private String scenarioElementId;

    @Column(name = "\"choiceid\"")
    private String choiceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }

    public Long getAccessCodeId() {
        return accessCodeId;
    }

    public void setAccessCodeId(Long accessCode) {
        this.accessCodeId = accessCode;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String event) {
        this.eventName = event;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getScenarioElementId() {
        return scenarioElementId;
    }

    public void setScenarioElementId(String menuid) {
        this.scenarioElementId = menuid;
    }

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public String toString() {
        return "[ id= " + id + " experienceId: " +
            experienceId + " accessCodeId: " + accessCodeId + " date: " + date + " event: " + eventName + " type: " + type + " menuId: " + scenarioElementId + " choiceId: " + choiceId+ " ]";
    }
}
