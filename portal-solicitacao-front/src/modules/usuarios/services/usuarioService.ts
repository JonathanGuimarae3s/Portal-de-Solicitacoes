import type {Consulta, PageResponse} from "@/interfaces/interfaces.ts";

import api from "@/core/http/api.ts";
import type {Usuario, UsuarioRequest} from "@/modules/usuarios/types/usuario.types.ts";

const ENDPOINT = "/usuarios";

export async function buscarUsuarios(consulta: Consulta = {
    pagina: 0,
    tamanho: 10,
    ordenarPor: "id",
    direcao: "asc",
}) {


    const response = await api.get<PageResponse<Usuario>>(
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

export async function ativarInativarUsuario(id: number, ativo: boolean) {

    const usuarioStatusRequest = {"ativo": !ativo};

    const response = await api.patch(
        `${ENDPOINT}/${id}/status`, usuarioStatusRequest
    )

    return response.data;


}


export async function atualizar(id: number, UsuarioRequest: UsuarioRequest) {


    const response = await api.put<UsuarioRequest>(
        `${ENDPOINT}/${id}`, UsuarioRequest
    )

    return response.data;


}

export async function salvar(UsuarioRequest: UsuarioRequest) {

    const response = await api.post<UsuarioRequest>(
        ENDPOINT, UsuarioRequest
    )

    return response.data;


}