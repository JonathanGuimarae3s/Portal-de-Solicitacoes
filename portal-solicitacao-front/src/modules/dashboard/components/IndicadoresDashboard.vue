<script setup lang="ts">
import DashboardCard from "./DashboardCard.vue"
import Loading from "@/shared/components/Loading.vue"
import Message from "primevue/message"
import {useDashboardStore} from "@/modules/dashboard/stores/dashboard.store.js";
import {storeToRefs} from "pinia";
import {onMounted} from "vue";

const dashboardStore = useDashboardStore();
const {indicadores, erroIndicadores, isLoading,} = storeToRefs(dashboardStore);

onMounted(() => {
  dashboardStore.carregarDashboard()
});
</script>

<template>
  <Loading v-if="isLoading"/>
  <Message v-if="erroIndicadores" severity="error" :closable="false">
    {{ erroIndicadores }}
  </Message>
  <div class="flex flex-wrap gap-4" v-else>
    <DashboardCard titulo="Total" descricao="Solicitações cadastradas" :quantidade="indicadores?.total"/>
    <DashboardCard titulo="Em aprovação" descricao="Aguardando gestor" :quantidade="indicadores?.emAprovacao"/>
    <DashboardCard titulo="Aprovadas" descricao="Prontas para concluir" :quantidade="indicadores?.aprovadas"/>
    <DashboardCard titulo="Usuários ativos" descricao="Ambiente dev" :quantidade="indicadores?.usuariosAtivos"/>
  </div>
</template>