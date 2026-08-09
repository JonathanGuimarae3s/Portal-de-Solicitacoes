<script setup lang="ts">
import Panel from 'primevue/panel'

interface DashboardCardProps {
  titulo: string;
  quantidade?: number;
  descricao: string;
}

defineProps<DashboardCardProps>();
</script>

<template>
  <Panel class="min-w-56 flex-1">

    <template #header>
      {{ titulo }}
    </template>

    <p class="font-bold text-4xl"> {{ quantidade }}</p>

    <p class=" mt-3 text-zinc-400">
      {{ descricao }}
    </p>

  </Panel>
</template>
