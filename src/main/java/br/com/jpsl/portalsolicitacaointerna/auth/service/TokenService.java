package br.com.jpsl.portalsolicitacaointerna.auth.service;

import br.com.jpsl.portalsolicitacaointerna.auth.excecao.AutenticacaoException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("2")
    private Long expirationHours;


    public String gerarToken(Usuario usuario) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("api-solicitacao-interna")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(retornaExpiracao())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new AutenticacaoException("Error ao gerar o token de acesso.", exception);
        }
    }

    public String validaToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("api-solicitacao-interna")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public Long getExpiresInMillis() {
        return getExpirationDuration().toMillis();
    }

    private Instant retornaExpiracao() {
        return Instant.now().plus(getExpirationDuration());
    }

    private Duration getExpirationDuration() {
        return Duration.ofHours(expirationHours);
    }

}
