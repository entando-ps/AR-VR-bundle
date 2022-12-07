package it.iedx.login.service.dto;

import it.iedx.login.domain.AccessCode;
import it.iedx.login.domain.LeaseStatus;

import java.io.Serializable;

/**
 * A DTO for the {@link AccessCode} entity.
 */
public class AccessCodeDTO implements Serializable {

    private Long id;

//    private String code;

//    private ZonedDateTime generated;

    private LeaseStatus status;

//    private ZonedDateTime used;

//    private String qrcode;

//    private Profile profile;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getCode() {
//        return code;
//    }

//    public void setCode(String code) {
//        this.code = code;
//    }

//    public ZonedDateTime getGenerated() {
//        return generated;
//    }

//    public void setGenerated(ZonedDateTime generated) {
//        this.generated = generated;
//    }

    public LeaseStatus getStatus() {
        return status;
    }

    public void setStatus(LeaseStatus status) {
        this.status = status;
    }

//    public ZonedDateTime getUsed() {
//        return used;
//    }

//    public void setUsed(ZonedDateTime used) {
//        this.used = used;
//    }

//    public String getQrcode() {
//        return qrcode;
//    }

//    public void setQrcode(String qrcode) {
//        this.qrcode = qrcode;
//    }

//    public Profile getProfile() {
//        return profile;
//    }

//    public void setProfile(Profile profile) {
//        this.profile = profile;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessCodeDTO)) {
            return false;
        }

        return id != null && id.equals(((AccessCodeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccessCodeDTO{" +
            "id=" + getId() +
//            ", code='" + getCode() + "'" +
//            ", generated='" + getGenerated() + "'" +
            ", status=" + getStatus() +
//            ", used='" + getUsed() + "'" +
//            ", qrcode='" + getQrcode() + "'" +
            "}";
    }
}
