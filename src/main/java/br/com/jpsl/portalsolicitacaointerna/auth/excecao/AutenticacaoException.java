package br.com.jpsl.portalsolicitacaointerna.auth.excecao;

import com.auth0.jwt.exceptions.JWTCreationException;

public class AutenticacaoException extends RuntimeException {
    public AutenticacaoException(String message, JWTCreationException exception) {
        super(message, exception);
    }

    public AutenticacaoException(String message) {
        super(message);
    }
}
