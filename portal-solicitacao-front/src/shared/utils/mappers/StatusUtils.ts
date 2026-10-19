import {StatusSolicitacao} from "@/enums/enums.ts";

const StatusSolicitacaoDescricao: Record<StatusSolicitacao, string> = {
    [StatusSolicitacao.ABERTA]: "Aberta",
    [StatusSolicitacao.EM_APROVACAO]: "Em aprovação",
    [StatusSolicitacao.APROVADA]: "Aprovada",
    [StatusSolicitacao.REJEITADA]: "Rejeitada",
    [StatusSolicitacao.CONCLUIDA]: "Concluído",


};

export function formatarStatusSolicitacao(statusSolicitacao: StatusSolicitacao): string {
    return StatusSolicitacaoDescricao[statusSolicitacao] || 'Status de solicitação desconhecida';
}


export function formatarStatusString(status: string): string {

    const statusEnum = StatusSolicitacao [status as keyof typeof StatusSolicitacao]

    return formatarStatusSolicitacao(statusEnum)
}

export function formatarStatusEnum(prioridade: string): StatusSolicitacao {

    return StatusSolicitacao [prioridade as keyof typeof StatusSolicitacao]


}