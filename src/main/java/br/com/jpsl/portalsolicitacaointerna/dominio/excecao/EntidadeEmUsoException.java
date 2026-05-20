package br.com.jpsl.portalsolicitacaointerna.dominio.excecao;


public class EntidadeEmUsoException extends NegocioException {

    public EntidadeEmUsoException(String mensagem) {
        super(mensagem);
    }
}
