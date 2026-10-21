import {defineStore} from "pinia";
import {ref} from "vue";
import type {Consulta, PageResponse} from "@/types/interfaces.ts";
import type {TipoSolicitacao, TipoSolicitacaoRequest} from "@/modules/tipos/types/tipos.ts";
import * as tiposService from "@/modules/tipos/services/tiposService.ts";

export const useTiposStore = defineStore("tipos", () => {
    const tipos = ref<PageResponse<TipoSolicitacao> | null>(null);
    const isLoading = ref(false);
    const erro = ref<string | null>(null);

    async function executarComLoading<T>(acao: () => Promise<T>): Promise<T> {
        isLoading.value = true;
        erro.value = null;

        try {
            return await acao();
        } finally {
            isLoading.value = false;
        }
    }

    async function carregarTipos(consulta: Consulta): Promise<void> {
        try {
            tipos.value = await executarComLoading(
                () => tiposService.buscarTipos(consulta)
            );
        } catch {
            tipos.value = null;
            erro.value = "Não foi possível carregar os tipos.";
        }
    }

    function buscarTipoPorId(id: number): Promise<TipoSolicitacao> {
        return executarComLoading(
            () => tiposService.buscarPorId(id)
        );
    }

    function criarTipo(request: TipoSolicitacaoRequest): Promise<void> {
        return executarComLoading(async () => {
            await tiposService.salvar(request);
        });
    }

    function atualizarTipo(id: number, request: TipoSolicitacaoRequest): Promise<void> {
        return executarComLoading(async () => {
            await tiposService.atualizar(id, request);
        });
    }

    function excluirTipo(id: number): Promise<void> {
        return executarComLoading(async () => {
            await tiposService.deletarTipo(id);
        });
    }

    return {
        tipos,
        isLoading,
        erro,
        carregarTipos,
        buscarTipoPorId,
        criarTipo,
        atualizarTipo,
        excluirTipo,
    };
});
