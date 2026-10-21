import {defineStore} from "pinia";
import {ref} from "vue";
import {buscarIndicadoresDashboard, buscarSolicitacoesResumo,} from "../services/dashboard.service";

import type {IndicadoresDashboard, SolicitacaoResumo,} from "@/modules/dashboard/interfaces/dashboard.ts";
import type {Consulta, PageResponse} from "@/types/interfaces.ts";

export const useDashboardStore = defineStore("dashboard", () => {


    const isLoadingIndicadores = ref<boolean>(false);
    const erroIndicadores = ref<string>("");
    const indicadores = ref<IndicadoresDashboard | null>(null);


    const solicitacoes = ref<PageResponse<SolicitacaoResumo> | null>(null);
    const isLoadingTabela = ref<boolean>(false);
    const erroTabela = ref<string | null>(null);

    async function carregarIndicadores() {
        try {

            isLoadingIndicadores.value = true;
            erroIndicadores.value = "";
            indicadores.value = await buscarIndicadoresDashboard()

        } catch {

            erroIndicadores.value = "Não foi possível carregar os indicadores.";
            indicadores.value = null;

        } finally {
            isLoadingIndicadores.value = false;
        }
    }


    async function carregarSolicitacoes(consulta: Consulta) {
        try {

            isLoadingTabela.value = true
            erroTabela.value = null;
            solicitacoes.value = await buscarSolicitacoesResumo(consulta);

        } catch {

            erroTabela.value = "Não foi possível carregar as solicitações.";
            solicitacoes.value = null;
            isLoadingTabela.value = false

        } finally {

            isLoadingTabela.value = false;
        }
    }

    return {
        isLoadingTabela,
        isLoadingIndicadores,
        erroIndicadores,
        erroTabela,
        indicadores,
        solicitacoes,
        carregarIndicadores,
        carregarSolicitacoes
    };
});
