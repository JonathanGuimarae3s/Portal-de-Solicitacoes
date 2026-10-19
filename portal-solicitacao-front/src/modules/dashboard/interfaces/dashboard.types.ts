import {PrioridadeSolicitacao, type StatusSolicitacao} from "@/enums/enums.ts";

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





