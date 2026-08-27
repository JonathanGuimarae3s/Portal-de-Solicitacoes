import type {Consulta, PageResponse} from "@/interfaces/interfaces.ts";
import type {TipoSolitacao} from "@/modules/tipos/stores/tipos.stores.ts";
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

export async function deletarTipos(id: number) {

    const response = await api.delete(
        `/tipos/${id}`,
    ).catch((error) => {
        throw error;
    })

    return response.data;


}