<script setup lang="ts">
import Button from "primevue/button";
import Titulo from "@/shared/components/Titulo.vue";
import TabelaDashboard from "@/modules/dashboard/components/TabelaDashboard.vue";
import IndicadoresDashboard from "@/modules/dashboard/components/IndicadoresDashboard.vue";
import ContainerPage from "@/shared/components/ContainerPage.vue";
</script>

<template>

  <ContainerPage>

    <template #conteudo>

      <div class="flex items-center justify-between">
        <Titulo text="Dashboard" subtext="Resumo geral das solicitações internas."/>
        <Button label="Nova solicitação"/>
      </div>

      <IndicadoresDashboard/>

      <TabelaDashboard/>

    </template>

  </ContainerPage>

</template>
