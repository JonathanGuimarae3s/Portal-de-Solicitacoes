import {defineStore} from "pinia";
import {ref} from "vue";
import type {Consulta, PageResponse} from "@/interfaces/interfaces.ts";
import {atualizar, buscarPorId, buscarTipos, deletarTipo, salvar} from "@/modules/tipos/services/tiposService.ts";


export interface TipoSolitacao {
    id: number;
    nome: string;

}

export interface TipoSolitacaoRequest {
    nome: string;
}

export const useTiposStores = defineStore("tipos", () => {

    const isLoading = ref(false);

    const erro = ref<string | null>(null);

    const tipos = ref<PageResponse<TipoSolitacao> | null>(null);

    async function carregaTipos(consulta: Consulta) {

        try {

            isLoading.value = true;

            erro.value = null;

            tipos.value = await buscarTipos(consulta)


        } catch {
            erro.value = "Não foi possível carregar o dashboard.";
            tipos.value = null;

        } finally {
            isLoading.value = false;
        }
    }

    async function buscarTipoPorId(id: number) {
        try {

            isLoading.value = true;

            erro.value = null;

            return await buscarPorId(id)

        } catch (error) {
            throw error;
        } finally {
            isLoading.value = false;
        }
    }

    async function apagarTipo(id: number) {
        try {

            isLoading.value = true;

            erro.value = null;

            await deletarTipo(id)

        } catch (error) {

            throw error;
        } finally {
            isLoading.value = false;
        }
    }

    async function atualizarTipo(id: number, request: TipoSolitacaoRequest) {
        try {

            isLoading.value = true;

            erro.value = null;

            await atualizar(id, request)

        } catch (error) {

            throw error;
        } finally {
            isLoading.value = false;
        }
    }

    async function salvarTipo(request: TipoSolitacaoRequest) {
        try {

            isLoading.value = true;

            erro.value = null;

            await salvar(request)

        } catch (error) {
            throw error;
        } finally {
            isLoading.value = false;
        }
    }

    return {tipos, isLoading, erro, carregaTipos, apagarTipos: apagarTipo, atualizarTipo, buscarTipoPorId, salvarTipo};
});