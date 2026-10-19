import {PrioridadeSolicitacao} from "@/enums/enums.ts";

const PrioridadeSolicitacaDescricao: Record<PrioridadeSolicitacao, string> = {
    [PrioridadeSolicitacao.BAIXA]: "Baixa",
    [PrioridadeSolicitacao.MEDIA]: "Média",
    [PrioridadeSolicitacao.ALTA]: "Alta",

};

export function formatarPrioridade(prioridade: PrioridadeSolicitacao): string {
    return PrioridadeSolicitacaDescricao[prioridade] || 'Prioridade desconhecida';
}

export function formatarPrioridadeString(prioridade: string): string {

    const prioridadeEnum = PrioridadeSolicitacao [prioridade as keyof typeof PrioridadeSolicitacao]

    return formatarPrioridade(prioridadeEnum)
}

export function formatarPrioridadeEnum(prioridade: string): PrioridadeSolicitacao {

    return PrioridadeSolicitacao [prioridade as keyof typeof PrioridadeSolicitacao]


}



