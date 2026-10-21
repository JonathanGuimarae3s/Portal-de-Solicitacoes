<script setup lang="ts">
import DashboardCard from "./DashboardCard.vue"
import Loading from "@/shared/components/Loading.vue"
import Message from "primevue/message"
import type {IndicadoresDashboard} from "@/modules/dashboard/interfaces/dashboard.ts";

interface IndicadoresDashboardProps {
  indicadores: IndicadoresDashboard | null;
  isAdmin: boolean;

}

defineProps<IndicadoresDashboardProps>();

</script>

<template>

  <div class="flex flex-wrap gap-4" >
    <DashboardCard titulo="Total" descricao="Solicitações cadastradas" :quantidade="indicadores?.total"/>
    <DashboardCard titulo="Em aprovação" descricao="Aguardando gestor" :quantidade="indicadores?.emAprovacao"/>
    <DashboardCard titulo="Aprovadas" descricao="Prontas para concluir" :quantidade="indicadores?.aprovadas"/>
    <DashboardCard titulo="Usuários ativos" descricao="Ambiente dev" :quantidade="indicadores?.usuariosAtivos"
                   v-show="isAdmin"/>
  </div>
</template>