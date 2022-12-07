package it.iedx.login.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {

    private Long accessCodeId;
    private List<ExperienceSummaryDTO> experiences;

    public Long getAccessCodeId() {
        return accessCodeId;
    }

    public void setAccessCodeId(Long accessCodeId) {
        this.accessCodeId = accessCodeId;
    }

    public List<ExperienceSummaryDTO> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceSummaryDTO> experiences) {
        this.experiences = experiences;
    }
}
