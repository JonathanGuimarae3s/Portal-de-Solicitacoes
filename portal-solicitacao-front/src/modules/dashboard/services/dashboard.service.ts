import api from "@/core/http/api";
import type {
    IndicadoresDashboard,
    SolicitacaoResumo,
} from "@/modules/dashboard/interfaces/dashboard.types";
import type {Consulta, PageResponse} from "@/interfaces/interfaces.ts";

export async function buscarIndicadoresDashboard(): Promise<IndicadoresDashboard> {
    const response = await api.get<IndicadoresDashboard>(
        "/dashboard/indicadores",
    );

    return response.data;
}

export async function buscarSolicitacoesResumo(
    consulta: Consulta = {
        pagina: 0,
        tamanho: 10,
        ordenarPor: "id",
        direcao: "desc",
    }
): Promise<PageResponse<SolicitacaoResumo>> {

    const response = await api.get<PageResponse<SolicitacaoResumo>>(
        "/dashboard/solicitacoes",
        {
            params: {
                page: consulta.pagina,
                size: consulta.tamanho,
                sort: `${consulta.ordenarPor},${consulta.direcao}`,
            },
        },
    );

    return response.data;
}
