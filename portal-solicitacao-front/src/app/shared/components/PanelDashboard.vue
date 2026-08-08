<script setup lang="ts">
import Panel from 'primevue/panel'

interface PanelDashboardProps {
  titulo: string;
  quantidade?: number;
  descricao: string;
}

defineProps<PanelDashboardProps>();
</script>

<template>
  <Panel class="w-1/4">

    <template #header>
      {{ titulo }}
    </template>

    <p class="font-bold text-4xl"> {{ quantidade }}</p>

    <p class=" mt-3 text-zinc-400">
      {{ descricao }}
    </p>

  </Panel>
</template>