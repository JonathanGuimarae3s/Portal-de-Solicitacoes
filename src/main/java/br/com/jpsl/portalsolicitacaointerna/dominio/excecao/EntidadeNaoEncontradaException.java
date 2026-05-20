package br.com.jpsl.portalsolicitacaointerna.dominio.excecao;


public class EntidadeNaoEncontradaException extends NegocioException {

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
