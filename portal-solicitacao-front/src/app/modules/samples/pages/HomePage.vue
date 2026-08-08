<script setup lang="ts">
import Titulo from '@/app/shared/components/Titulo.vue';
import Button from 'primevue/button';
import PanelDashboard from "@/app/shared/components/PanelDashboard.vue";
import {useDashboardResumoStore} from "@/app/modules/samples/stores/dashboardResumo.ts";
import Loading from "@/app/shared/components/Loading.vue";
import {storeToRefs} from "pinia";
import {onMounted} from "vue";
import Message from "primevue/message";
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';

const dashboardStore = useDashboardResumoStore()

const {cardDados, solicitacaoResumidas, erro} = storeToRefs(dashboardStore);


onMounted(async () => {
  await dashboardStore.populaDashboard();
});


</script>

<template>


  <div v-if="erro">

    <Message severity="error" :closable="false">{{ erro }}</Message>

  </div>

  <div v-else class=" flex flex-col gap-9 justify-between align-items-center ">

    <div class=" flex  justify-between items-center  ">

      <Titulo text="Dashboard" subtext="Resumo geral das Solicitações internas."/>
      <Button label="Nova solicitação"/>

    </div>

    <Loading v-if="dashboardStore.isLoading"/>

    <div v-else class=" flex justify-between items-center  gap-4">

      <PanelDashboard titulo="Total" descricao="Solicitações cadastradas" :quantidade="cardDados?.total"/>

      <PanelDashboard titulo="Em aprovação" descricao="Aguardando gestor"
                      :quantidade="cardDados?.emAprovacao"/>

      <PanelDashboard titulo="Aprovadas" descricao="Prontas para concluir"
                      :quantidade="cardDados?.aprovadas"/>

      <PanelDashboard titulo="Usuários ativos" descricao="Ambiente dev"
                      :quantidade="cardDados?.usuariosAtivos"/>

    </div>

    <Loading v-if="dashboardStore.isLoading"/>

    <div v-else class=" flex justify-between items-center  gap-4">

      <DataTable :value="solicitacaoResumidas?.dados" :rows="8" class="w-full"
                 :total-records="solicitacaoResumidas?.totalDados"
                 :paginator="true">
        <Column field="id" header="Código"></Column>
        <Column field="titulo" header="Título"></Column>
        <Column field="status" header="Status"></Column>
      </DataTable>

    </div>


  </div>


</template>