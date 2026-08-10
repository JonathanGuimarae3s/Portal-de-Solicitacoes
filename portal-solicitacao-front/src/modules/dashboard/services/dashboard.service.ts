import api from "@/core/http/api";
import type {
    ConsultaSolicitacoes,
    IndicadoresDashboard,
    PageResponse,
    SolicitacaoResumo,
} from "@/modules/dashboard/interfaces/dashboard.types";

export async function buscarIndicadoresDashboard(): Promise<IndicadoresDashboard> {
    const response = await api.get<IndicadoresDashboard>(
        "/dashboard/indicadores",
    );

    return response.data;
}

export async function buscarSolicitacoesResumo(
    consulta: ConsultaSolicitacoes = {
        pagina: 0,
        tamanho: 10,
        ordenarPor: "id",
    }
): Promise<PageResponse<SolicitacaoResumo>> {

    const response = await api.get<PageResponse<SolicitacaoResumo>>(
        "/dashboard/solicitacoes",
        {
            params: {
                page: consulta.pagina,
                size: consulta.tamanho,
                sort: `${consulta.ordenarPor},desc`,
            },
        },
    );

    return response.data;
}
