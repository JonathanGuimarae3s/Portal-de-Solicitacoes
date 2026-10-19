<script setup lang="ts">

import Message from "primevue/message";
import InputText from "primevue/inputtext";


interface iProps {
  label: string;
  type?: string;
  campo: string;
  message: string;
  invalid?: boolean | undefined;
  disabled?: boolean
}

defineProps<iProps>();
</script>

<template>


  <div class="mb-3">
    <label :for="campo" class="font-semibold w-full ">{{ label }} </label>
    <slot name="input">

      <InputText :id="campo" :name="campo" :type="type" :disabled="disabled"  class="mt-2" fluid/>

    </slot>


    <Message v-if="invalid" severity="error" size="small" class="mt-2">
      {{ message }}
    </Message>
  </div>


</template>

<style scoped>

</style>