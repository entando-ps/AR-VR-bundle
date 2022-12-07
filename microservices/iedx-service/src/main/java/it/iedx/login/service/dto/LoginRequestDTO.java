package it.iedx.login.service.dto;

public class LoginRequestDTO {

    // può essere indifferentemente l' access code oppure il device UID
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
