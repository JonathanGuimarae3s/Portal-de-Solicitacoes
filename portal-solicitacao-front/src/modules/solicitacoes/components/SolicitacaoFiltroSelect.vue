<script setup lang="ts" generic="T extends string">

import InputGroup from "primevue/inputgroup";
import {Button, Select} from "primevue";
import FloatLabel from "primevue/floatlabel";
import IconComponent from "@/shared/components/IconComponent.vue";


interface Props {
  options: T[];
  filtrarTabela: () => void;
  optionLabel: (option: T) => string;
  label: string;
  id: string;
}

defineProps<Props>();

const valorSelecionado = defineModel<T | null>();
</script>

<template>
  <InputGroup>

    <FloatLabel variant="on">
      <Select :id="id" :options="options"
              :optionLabel="optionLabel"
              v-model="valorSelecionado"
              showClear
              fluid/>

      <label :for="id">{{ label }}</label>
    </FloatLabel>

    <Button @click="filtrarTabela">
      <IconComponent icon="pi pi-search"/>
    </Button>

  </InputGroup>
</template>

<style scoped>


</style>