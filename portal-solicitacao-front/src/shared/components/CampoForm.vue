<script setup lang="ts">

import Message from "primevue/message";
import InputText from "primevue/inputtext";


interface iProps {
  label: string;
  type: string;
  campo: string;
  message: string;
  invalid: boolean | undefined;
}

defineProps<iProps>();

</script>

<template>


  <div class="mb-3">
    <label :for="campo" class="font-semibold w-6rem">{{ label }} </label>
    <slot name="input">

      <InputText :id="campo" :name="campo" :type="type" fluid/>

    </slot>


    <Message v-if="invalid" severity="error" size="small"  class="mt-2"  >
      {{ message }}
    </Message>
  </div>


</template>

<style scoped>

</style>