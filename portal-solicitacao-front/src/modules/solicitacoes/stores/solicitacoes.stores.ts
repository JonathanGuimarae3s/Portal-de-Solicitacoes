import {defineStore} from "pinia";
import {ref} from "vue";
import type {Consulta, ConsultaTabelaSolicitacao, PageResponse} from "@/types/interfaces.ts";

import * as solicitacaoService from "@/modules/solicitacoes/services/solicitacaoService.ts"
import type {Solicitacao, SolicitacaoRequest} from "@/modules/solicitacoes/types/solicitacao.ts";

export const useSolicitacaoStore = defineStore("solicitacao", () => {
    const solicitacoes = ref<PageResponse<Solicitacao> | null>(null);
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

    async function carregarSolicitacoes(consulta: ConsultaTabelaSolicitacao): Promise<void> {
        try {
            solicitacoes.value = await executarComLoading(
                () => solicitacaoService.buscarSolicitacoes(consulta)
            )
            ;
        } catch {
            solicitacoes.value = null;
            erro.value = "Não foi possível carregar as solicitações.";
        }
    }

    function buscarSolicitacaoPorId(id: number): Promise<Solicitacao> {
        return executarComLoading(
            () => solicitacaoService.buscarPorId(id)
        );
    }

    function criarSolicitacao(request: SolicitacaoRequest): Promise<void> {
        return executarComLoading(async () => {
            await solicitacaoService.salvar(request);
        });
    }

    function atualizarSolicitacao(id: number, request: SolicitacaoRequest): Promise<void> {
        return executarComLoading(async () => {
            await solicitacaoService.atualizar(id, request);
        });
    }


    return {
        solicitacoes,
        isLoading,
        erro,
        carregarSolicitacoes,
        buscarSolicitacaoPorId,
        criarSolicitacao,
        atualizarSolicitacao,
    };
});
