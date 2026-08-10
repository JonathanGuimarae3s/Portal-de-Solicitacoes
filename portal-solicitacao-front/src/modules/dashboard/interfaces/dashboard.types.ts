import {PrioridadeSolicitacao, type StatusSolicitacao} from "@/modules/enums/enums.ts";

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


export interface PageResponse<T> {
    dados: T[];
    pagina: number;
    tamanho: number;
    totalDados: number;
    totalPaginas: number;
}


export interface ConsultaSolicitacoes {
    pagina: number;
    tamanho: number;
    ordenarPor: string;
}
