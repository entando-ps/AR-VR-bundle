package it.iedx.login.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * AccessCode entity
 */
@Entity
@Table(name = "\"accesscode\"")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccessCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "\"generated\"")
    private ZonedDateTime generated;

    @Column(name = "status")
    private LeaseStatus status;

    @Column(name = "checked")
    private ZonedDateTime checked;

    @Transient
    private String qrcode;

    @Column(name = "profile")
    private Profile profile;

    @Column(name = "\"device\"")
    private Long deviceId;

    @Transient // serve solo per passare un campo di testo al DTO successivo che non va persistito
    private String message;


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public AccessCode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ZonedDateTime getGenerated() {
        return generated;
    }

    public AccessCode generated(ZonedDateTime generated) {
        this.generated = generated;
        return this;
    }

    public void setGenerated(ZonedDateTime generated) {
        this.generated = generated;
    }

    public LeaseStatus getStatus() {
        return status;
    }

    public AccessCode status(LeaseStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(LeaseStatus status) {
        this.status = status;
    }

    public ZonedDateTime getChecked() {
        return checked;
    }

    public AccessCode used(ZonedDateTime used) {
        this.checked = used;
        return this;
    }

    public void setChecked(ZonedDateTime used) {
        this.checked = used;
    }

    public String getQrcode() {
        return qrcode;
    }

    public AccessCode qrcode(String qrcode) {
        this.qrcode = qrcode;
        return this;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public AccessCode profile(Profile profile) {
        this.profile = profile;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public AccessCode deviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessCode)) {
            return false;
        }
        return id != null && id.equals(((AccessCode) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccessCode{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", generated='" + getGenerated() + "'" +
            ", status=" + getStatus() +
            ", used='" + getChecked() + "'" +
            ", qrcode='" + getQrcode() + "'" +
            ", profile='" + getProfile() + "'" +
            "}";
    }
}
