package br.com.jpsl.portalsolicitacaointerna.dominio.excecao;

public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }
}
