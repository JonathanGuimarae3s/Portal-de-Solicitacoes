import api from "@/core/http/api";
import type {
    IndicadoresDashboard,
    PageResponse,
    SolicitacaoResumo,
} from "@/modules/dashboard/interfaces/dashboard.types";

export async function buscarIndicadoresDashboard(): Promise<IndicadoresDashboard> {
    const response = await api.get<IndicadoresDashboard>(
        "/solicitacoes/contabilizaSolicitacoes",
    );

    return response.data;
}

export async function buscarSolicitacoesResumo(): Promise<PageResponse<SolicitacaoResumo>> {
    const response = await api.get<PageResponse<SolicitacaoResumo>>(
        "/solicitacoes/resumo",
        {
            params: {
                sort: "id,desc",
            },
        },
    );

    return response.data;
}
