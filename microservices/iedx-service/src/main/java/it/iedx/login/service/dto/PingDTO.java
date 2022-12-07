package it.iedx.login.service.dto;

import it.iedx.login.domain.LeaseStatus;

import java.io.Serializable;

public class PingDTO implements Serializable {

    private Long id;
    private LeaseStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaseStatus getStatus() {
        return status;
    }

    public void setStatus(LeaseStatus status) {
        this.status = status;
    }
}
