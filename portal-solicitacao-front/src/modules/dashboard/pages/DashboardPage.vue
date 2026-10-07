<script setup lang="ts">
import Button from "primevue/button";
import Titulo from "@/shared/components/Titulo.vue";
import TabelaDashboard from "@/modules/dashboard/components/TabelaDashboard.vue";
import IndicadoresDashboard from "@/modules/dashboard/components/IndicadoresDashboard.vue";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import {onMounted} from "vue";
import {useDashboardStore} from "@/modules/dashboard/stores/dashboard.store.ts";
import {storeToRefs} from "pinia";
import {useAuthStore} from "@/modules/auth/stores/auth.ts";
import {useRoute} from "vue-router";
import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";

const {toastError, toastSuccess} = useCustomToast();


const dashboardStore = useDashboardStore();
const {indicadores, erroIndicadores, isLoading,} = storeToRefs(dashboardStore);


const authStore = useAuthStore();
const {isAdmin} = authStore;


const route = useRoute();


const acessoNegado = route.query.acessoNegado;

if (acessoNegado) {
  toastError("Vocë não possui autorização de acesso!")
}


onMounted(() => {
  dashboardStore.carregarDashboard()
});


</script>

<template>

  <ContainerPage>

    <template #conteudo>

      <div class="flex items-center justify-between">
        <Titulo text="Dashboard" subtext="Resumo geral das solicitações internas."/>
        <Button label="Nova solicitação"/>
      </div>

      <IndicadoresDashboard :indicadores="indicadores " :erroIndicadores="erroIndicadores" :isLoading="isLoading"
                            :isAdmin="isAdmin"/>

      <TabelaDashboard/>

    </template>

  </ContainerPage>

</template>
