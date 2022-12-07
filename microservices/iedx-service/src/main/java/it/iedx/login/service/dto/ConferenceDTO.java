package it.iedx.login.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.iedx.login.domain.Conference} entity.
 */
public class ConferenceDTO implements Serializable {

    private Long id;

    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConferenceDTO)) {
            return false;
        }

        ConferenceDTO conferenceDTO = (ConferenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, conferenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConferenceDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
