import type {ConsultaTabelaSolicitacao, PageResponse} from "@/types/interfaces.ts";

import api from "@/core/http/api.ts";
import type {Solicitacao, SolicitacaoRequest} from "@/modules/solicitacoes/types/solicitacao.ts";

const ENDPOINT = "/solicitacoes";

export async function buscarSolicitacoes(consulta: ConsultaTabelaSolicitacao = {
    pagina: 0,
    tamanho: 10,
    ordenarPor: "id",
    direcao: "asc",
}) {


    const response = await api.get<PageResponse<Solicitacao>>(
        ENDPOINT,
        {
            params: {
                page: consulta.pagina,
                size: consulta.tamanho,
                sort: `${consulta.ordenarPor},${consulta.direcao}`,
                prioridade: consulta.prioridade,
                status: consulta.status,
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


export async function atualizar(id: number, SolicitacaoRequest: SolicitacaoRequest) {


    const response = await api.put<SolicitacaoRequest>(
        `${ENDPOINT}/${id}`, SolicitacaoRequest
    )

    return response.data;


}

export async function salvar(SolicitacaoRequest: SolicitacaoRequest) {

    const response = await api.post<SolicitacaoRequest>(
        ENDPOINT, SolicitacaoRequest
    )

    return response.data;


}