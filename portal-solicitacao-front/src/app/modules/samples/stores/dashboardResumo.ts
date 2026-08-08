import {defineStore} from "pinia";
import {ref} from "vue";
import api from "@/plugins/axios";


interface CardDados {
    total: number;
    emAprovacao: number;
    aprovadas: number;
    usuariosAtivos: number;


}

interface SolicitacaoResumida {

    dados: SolicitacaoResumo[];
    pagina: number;
    tamanho: number;
    totalDados: number;
    totalPaginas: number;
}

interface SolicitacaoResumo {
    id: number,
    titulo: string;
    status: string;
    dataCriacao: string
}

export const useDashboardResumoStore = defineStore("dashboardResumo", () => {
    const erro = ref<string | null>(null);
    const isLoading = ref(false);
    const cardDados = ref<CardDados | null>(null)
    const solicitacaoResumidas = ref<SolicitacaoResumida | null>(null)

    async function populaDashboard() {

        try {

            isLoading.value = true;

            await populaCardDashboard();
            await populaTabelaDashboard()

        } catch (error) {

            erro.value = "Não foi possivel carregar o dashboard";

        } finally {

            isLoading.value = false;

        }

    }

    async function populaCardDashboard() {
        try {

            erro.value = null;

            const response = await api.get(`/solicitacoes/contabilizaSolicitacoes`)

            cardDados.value = response.data;

        } catch (error) {

            console.error(error);

        }
    }

    async function populaTabelaDashboard() {
        try {
            erro.value = null;

            const response = await api.get(`/solicitacoes/resumo`)

            solicitacaoResumidas.value = response.data;

        } catch (error) {

            console.error(error);
            solicitacaoResumidas.value = null

        }
    }

    return {isLoading, cardDados, solicitacaoResumidas, erro, populaDashboard};


})