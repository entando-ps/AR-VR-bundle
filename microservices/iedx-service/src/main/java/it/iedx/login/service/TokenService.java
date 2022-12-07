package it.iedx.login.service;

import it.iedx.login.domain.Token;

import java.util.Optional;

public interface TokenService {

    /**
     * Get current token for authentication
     * @return the first token found in the table
     */
    Optional<Token> getCurrent();
}
