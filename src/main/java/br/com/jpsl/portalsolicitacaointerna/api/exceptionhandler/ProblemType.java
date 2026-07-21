package br.com.jpsl.portalsolicitacaointerna.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
    RECURSO_NAO_ENCONTRADO("recurso-nao-encontrado", "Recurso não encontrado"),
    ENTIDADE_EM_USO("entidade-em-uso", "Entidade em uso"),
    CORPO_MAL_FORMATADO("corpo-mal-formatado", "Corpo da requisição mal formatado"),
    PARAMETRO_INVALIDO("parametro-invalido", "Parâmetro inválido"),
    ERRO_DE_SISTEMA("erro-de-sistema", "Erro de sistema"),
    DADOS_INVALIDOS("dados-invalidos", "Dados inválidos"),
    NEGOCIO("negocio", "Violação de regra de negócio"),
    NAO_AUTORIZADO("nao-autorizado", "Usuário não autorizado "),;

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.title = title;
        this.uri = "https://freecsfood.com.br/problems/" + path;
    }
}
