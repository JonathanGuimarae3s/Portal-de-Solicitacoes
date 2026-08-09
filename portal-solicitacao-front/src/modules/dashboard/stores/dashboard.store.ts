import { defineStore } from "pinia";
import { ref } from "vue";
import {
    buscarIndicadoresDashboard,
    buscarSolicitacoesResumo,
} from "../services/dashboard.service";
import type {
    IndicadoresDashboard,
    PageResponse,
    SolicitacaoResumo,
} from "@/modules/dashboard/interfaces/dashboard.types";

export const useDashboardStore = defineStore("dashboard", () => {
    const isLoading = ref(false);
    const erro = ref<string | null>(null);
    const indicadores = ref<IndicadoresDashboard | null>(null);
    const solicitacoes = ref<PageResponse<SolicitacaoResumo> | null>(null);

    async function carregarDashboard() {
        try {
            isLoading.value = true;
            erro.value = null;

            const [novosIndicadores, novasSolicitacoes] = await Promise.all([
                buscarIndicadoresDashboard(),
                buscarSolicitacoesResumo(),
            ]);

            indicadores.value = novosIndicadores;
            solicitacoes.value = novasSolicitacoes;

            console.log(solicitacoes.value);
        } catch {
            erro.value = "Não foi possível carregar o dashboard.";
            indicadores.value = null;
            solicitacoes.value = null;
        } finally {
            isLoading.value = false;
        }
    }

    return {
        isLoading,
        erro,
        indicadores,
        solicitacoes,
        carregarDashboard,
    };
});
