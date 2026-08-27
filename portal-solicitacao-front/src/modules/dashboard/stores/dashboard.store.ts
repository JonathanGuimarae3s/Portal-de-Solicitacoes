import {defineStore} from "pinia";
import {ref} from "vue";
import {buscarIndicadoresDashboard, buscarSolicitacoesResumo,} from "../services/dashboard.service";

import type {IndicadoresDashboard, SolicitacaoResumo,} from "@/modules/dashboard/interfaces/dashboard.types";
import type {Consulta, PageResponse} from "@/interfaces/interfaces.ts";

export const useDashboardStore = defineStore("dashboard", () => {
    const isLoading = ref(false);

    const erroIndicadores = ref<string | null>(null);
    const indicadores = ref<IndicadoresDashboard | null>(null);


    const solicitacoes = ref<PageResponse<SolicitacaoResumo> | null>(null);

    async function carregarDashboard() {
        try {
            isLoading.value = true;

            erroIndicadores.value = null;

            indicadores.value = await buscarIndicadoresDashboard()

        } catch {
            erroIndicadores.value = "Não foi possível carregar o dashboard.";
            indicadores.value = null;
            solicitacoes.value = null;
        } finally {
            isLoading.value = false;
        }
    }

    const erroTabela = ref<string | null>(null);

    async function carregarSolicitacoes(consulta: Consulta) {
        try {

            erroTabela.value = null;
            solicitacoes.value = await buscarSolicitacoesResumo(consulta);
        } catch {
            erroTabela.value = "Não foi possível carregar as solicitações.";
            solicitacoes.value = null;

            throw new Error(erroTabela.value);
        }
    }

    return {
        isLoading,
        erroIndicadores,
        erroTabela,
        indicadores,
        solicitacoes,
        carregarDashboard,
        carregarSolicitacoes
    };
});
