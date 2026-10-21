<script setup lang="ts">
import Button from "primevue/button";
import Titulo from "@/shared/components/Titulo.vue";
import TabelaDashboard from "@/modules/dashboard/components/TabelaDashboard.vue";
import IndicadoresDashboard from "@/modules/dashboard/components/IndicadoresDashboard.vue";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import {onMounted, ref} from "vue";
import {useDashboardStore} from "@/modules/dashboard/stores/dashboard.store.ts";
import {storeToRefs} from "pinia";
import {useAuthStore} from "@/modules/auth/stores/auth.ts";
import {useRoute} from "vue-router";
import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";
import router from "@/router";
import type {DataTablePageEvent, DataTableSortEvent} from "primevue/datatable";
import Message from "primevue/message";
import Loading from "@/shared/components/Loading.vue";


const {toastError, toastSuccess} = useCustomToast();


const dashboardStore = useDashboardStore();
const {
  indicadores,
  erroIndicadores,
  isLoadingIndicadores,
  isLoadingTabela,
  solicitacoes,
  erroTabela
} = storeToRefs(dashboardStore);


const authStore = useAuthStore();
const {isAdmin} = authStore;


const route = useRoute();
const acessoNegado = route.query.acessoNegado;
if (acessoNegado) {
  toastError("Vocë não possui autorização de acesso!")
}

async function redirecionarSolicitacaoPage() {
  await router.push("/solicitacoes?novaSolicitacao=true");
}

type SortOrder = 1 | -1;

interface LazyParams {
  first: number;
  rows: number;
  sortField: string;
  sortOrder: SortOrder;
}

const lazyParams = ref<LazyParams>({
  first: 0,
  rows: 10,
  sortField: "id",
  sortOrder: -1,
});


async function onPage(event: DataTablePageEvent) {
  lazyParams.value.first = event.first;
  lazyParams.value.rows = event.rows;

  await loadLazyData();
}

async function onSort(event: DataTableSortEvent) {
  lazyParams.value.first = 0;

  lazyParams.value.sortField =
      typeof event.sortField === "string"
          ? event.sortField
          : "id";

  lazyParams.value.sortOrder =
      event.sortOrder === 1 ? 1 : -1;

  await loadLazyData();
}

async function loadLazyData() {

  const {
    first,
    rows,
    sortField,
    sortOrder,
  } = lazyParams.value;


  await dashboardStore.carregarSolicitacoes({
    pagina: Math.floor(first / rows),
    tamanho: rows,
    ordenarPor: sortField,
    direcao: sortOrder === 1 ? "asc" : "desc",

  });
}


onMounted(
    () => {
      loadLazyData()
      dashboardStore.carregarIndicadores()
    }
)
</script>

<template>

  <ContainerPage>

    <template #conteudo>

      <div class="flex items-center justify-between">
        <Titulo text="Dashboard" subtext="Resumo geral das solicitações internas."/>
        <Button label="Nova Solicitação" @click="redirecionarSolicitacaoPage"/>
      </div>


      <Loading v-if="isLoadingIndicadores"/>

      <Message v-else-if="erroIndicadores" severity="error" :closable="false">
        {{ erroIndicadores }}
      </Message>

      <IndicadoresDashboard v-else :indicadores="indicadores"
                            :isAdmin="isAdmin"/>


      <Message v-if="erroTabela" severity="error" :closable="false">
        {{ erroTabela }}
      </Message>

      <TabelaDashboard v-else
                       :isLoading="isLoadingTabela"
                       :paginar="onPage"
                       :ordenar="onSort"
                       :solicitacoes="solicitacoes?.dados?? []"
                       :totalRegistros="solicitacoes?.totalDados??0"
                       :lazyParams="lazyParams"/>

    </template>

  </ContainerPage>

</template>
