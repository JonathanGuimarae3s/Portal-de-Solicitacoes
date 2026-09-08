import type {Consulta, PageResponse} from "@/interfaces/interfaces.ts";
import type {TipoSolitacao, TipoSolitacaoRequest} from "@/modules/tipos/stores/tipos.stores.ts";
import api from "@/core/http/api.ts";

export async function buscarTipos(consulta: Consulta = {
    pagina: 0,
    tamanho: 10,
    ordenarPor: "id",
    direcao: "asc",
}) {


    const response = await api.get<PageResponse<TipoSolitacao>>(
        "/tipos",
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
        `/tipos/${id}`,
    ).catch((error) => {
        throw error;
    })

    return response.data;


}

export async function deletarTipo(id: number) {

    const response = await api.delete(
        `/tipos/${id}`,
    ).catch((error) => {
        throw error;
    })

    return response.data;


}


export async function atualizar(id: number, tipoSolicitacaoRequest: TipoSolitacaoRequest) {


    const response = await api.put<TipoSolitacaoRequest>(
        `/tipos/${id}`, tipoSolicitacaoRequest
    ).catch((error) => {
        throw error;
    })

    return response.data;


}

export async function salvar(tipoSolicitacaoRequest: TipoSolitacaoRequest) {


    const response = await api.post<TipoSolitacaoRequest>(
        `/tipos`, tipoSolicitacaoRequest
    ).catch((error) => {
        throw error;
    })

    return response.data;


}