package it.iedx.login.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.iedx.login.domain.ExperienceStatus;
import it.iedx.login.domain.Profile;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperienceDTO {

    private String name;
    private String description;
    private Long id;
    private List<Asset> assets;
    private List<Profile> profiles;
    private ExperienceStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public ExperienceStatus getStatus() {
        return status;
    }

    public void setStatus(ExperienceStatus status) {
        this.status = status;
    }
}
