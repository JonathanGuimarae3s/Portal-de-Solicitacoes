import type {Consulta, PageResponse} from "@/types/interfaces.ts";

import api from "@/core/http/api.ts";
import type {TipoSolicitacao, TipoSolicitacaoRequest} from "@/modules/tipos/types/tipos.ts";

const ENDPOINT = "/tipos";

export async function buscarTipos(consulta: Consulta = {
    pagina: 0,
    tamanho: 10,
    ordenarPor: "id",
    direcao: "asc",
}) {


    const response = await api.get<PageResponse<TipoSolicitacao>>(
        ENDPOINT,
        {
            params: {
                page: consulta.pagina,
                size: consulta.tamanho,
                sort: `${consulta.ordenarPor},${consulta.direcao}`,
            }
        }
    );

    return response.data;
}

export async function buscarPorId(id: number) {

    const response = await api.get(
        `${ENDPOINT}/${id}`,
    )

    return response.data;


}

export async function deletarTipo(id: number) {

    const response = await api.delete(
        `${ENDPOINT}/${id}`,
    )

    return response.data;


}


export async function atualizar(id: number, tipoSolicitacaoRequest: TipoSolicitacaoRequest) {


    const response = await api.put<TipoSolicitacaoRequest>(
        `${ENDPOINT}/${id}`, tipoSolicitacaoRequest
    )

    return response.data;


}

export async function salvar(tipoSolicitacaoRequest: TipoSolicitacaoRequest) {


    const response = await api.post<TipoSolicitacaoRequest>(
        ENDPOINT, tipoSolicitacaoRequest
    )

    return response.data;


}