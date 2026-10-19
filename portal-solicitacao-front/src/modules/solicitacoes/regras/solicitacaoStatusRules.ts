import {StatusSolicitacao} from "@/enums/enums.ts";


export function carregaStatusDeAcordoComStatusAnterior(statusSalvo: StatusSolicitacao, isUsuario: boolean) {
    if (isUsuario) {
        return [statusSalvo]
    }

    switch (statusSalvo) {

        case StatusSolicitacao.EM_APROVACAO :
            return [StatusSolicitacao.APROVADA, StatusSolicitacao.REJEITADA, statusSalvo]

        case StatusSolicitacao.APROVADA :
            return [StatusSolicitacao.CONCLUIDA, statusSalvo]

        default:
        case StatusSolicitacao.ABERTA :
            return [StatusSolicitacao.EM_APROVACAO, statusSalvo]

    }

}