export interface IndicadoresDashboard {
    total: number;
    emAprovacao: number;
    aprovadas: number;
    usuariosAtivos: number;
}

export interface SolicitacaoResumo {
    id: number;
    titulo: string;
    status: StatusSolicitacao;
    prioridade: PrioridadeSolicitacao;
}

export enum StatusSolicitacao {
    ABERTA = "Aberta",
    EM_APROVACAO = "Em aprovacao",
    APROVADA = "Aprovada",
    REJEITADA = "Rejeitada",
    CONCLUIDA = "Concluído"

}

export enum PrioridadeSolicitacao {

    BAIXA = "Baixa",
    MEDIA = "Média",
    ALTA = "Alta"
}

export interface PageResponse<T> {
    dados: T[];
    pagina: number;
    tamanho: number;
    totalDados: number;
    totalPaginas: number;
}
