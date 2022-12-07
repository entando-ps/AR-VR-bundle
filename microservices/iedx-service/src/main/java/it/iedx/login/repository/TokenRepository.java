package it.iedx.login.repository;

import it.iedx.login.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String>, JpaSpecificationExecutor<Token> {

    @Query(
        value = "SELECT current FROM token LIMIT 1",
        nativeQuery = true
    )
    Optional<Token> getToken();

}
