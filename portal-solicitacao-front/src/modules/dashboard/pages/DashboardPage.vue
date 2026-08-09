<script setup lang="ts">
import {onMounted} from "vue";
import {storeToRefs} from "pinia";
import Button from "primevue/button";
import Column from "primevue/column";
import DataTable from "primevue/datatable";
import Message from "primevue/message";
import DashboardCard from "../components/DashboardCard.vue";
import {useDashboardStore} from "../stores/dashboard.store";
import Loading from "@/shared/components/Loading.vue";
import Titulo from "@/shared/components/Titulo.vue";
import {PrioridadeSolicitacao, StatusSolicitacao} from "@/modules/dashboard/interfaces/dashboard.types.ts";

const dashboardStore = useDashboardStore();
const {indicadores, solicitacoes, erro, isLoading} = storeToRefs(dashboardStore);

onMounted(() => dashboardStore.carregarDashboard());
</script>

<template>
  <Loading v-if="isLoading"/>

  <Message v-else-if="erro" severity="error" :closable="false">
    {{ erro }}
  </Message>

  <div v-else class="flex flex-col gap-9">
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

    <DataTable :value="solicitacoes?.dados"
               :total-records="solicitacoes?.totalDados"
               :paginator="true"
               :rows="8" class="w-full"
               paginatorPosition="top"
    >
      <Column field="id" header="Código"/>
      <Column field="titulo" header="Título"/>
      <Column field="status" header="Status">
        <template #body="slotProps">
            <span>
                {{ StatusSolicitacao[slotProps.data.status as keyof typeof StatusSolicitacao] }}
            </span>
        </template>
      </Column>
      <Column field="prioridade" header="Prioridade">
        <template #body="slotProps">
            <span>
                {{ PrioridadeSolicitacao[slotProps.data.prioridade as keyof typeof PrioridadeSolicitacao] }}
            </span>
        </template>
      </Column>
    </DataTable>
  </div>
</template>
