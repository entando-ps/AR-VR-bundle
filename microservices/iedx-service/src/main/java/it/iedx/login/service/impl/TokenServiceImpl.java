package it.iedx.login.service.impl;

import it.iedx.login.domain.Token;
import it.iedx.login.repository.TokenRepository;
import it.iedx.login.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Optional<Token> getCurrent() {
        return tokenRepository.getToken();
    }
}
