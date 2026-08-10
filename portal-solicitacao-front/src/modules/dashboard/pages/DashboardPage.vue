<script setup lang="ts">
import {onMounted} from "vue";
import {storeToRefs} from "pinia";
import Button from "primevue/button";
import Message from "primevue/message";
import DashboardCard from "../components/DashboardCard.vue";
import {useDashboardStore} from "../stores/dashboard.store";
import Loading from "@/shared/components/Loading.vue";
import Titulo from "@/shared/components/Titulo.vue";
import TabelaDashboard from "@/modules/dashboard/pages/TabelaDashboard.vue";

const dashboardStore = useDashboardStore();
const {indicadores, erroIndicadores, isLoading,} = storeToRefs(dashboardStore);

onMounted(() => {
  dashboardStore.carregarDashboard()

});

</script>

<template>
  <Loading v-if="isLoading"/>

  <Message v-else-if="erroIndicadores" severity="error" :closable="false">
    {{ erroIndicadores }}
  </Message>

  <div v-else class="flex flex-col gap-6 pb-4">
    <div class="flex items-center justify-between">
      <Titulo text="Dashboard" subtext="Resumo geral das solicitações internas."/>
      <Button label="Nova solicitação"/>
    </div>

    <div class="flex flex-wrap gap-4">
      <DashboardCard titulo="Total" descricao="Solicitações cadastradas" :quantidade="indicadores?.total"/>
      <DashboardCard titulo="Em aprovação" descricao="Aguardando gestor" :quantidade="indicadores?.emAprovacao"/>
      <DashboardCard titulo="Aprovadas" descricao="Prontas para concluir" :quantidade="indicadores?.aprovadas"/>
      <DashboardCard titulo="Usuários ativos" descricao="Ambiente dev" :quantidade="indicadores?.usuariosAtivos"/>
    </div>

    <TabelaDashboard/>

  </div>
</template>
