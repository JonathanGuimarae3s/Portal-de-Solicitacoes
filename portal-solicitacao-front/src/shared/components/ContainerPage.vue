<script setup lang="ts">
import {Toast} from "primevue";
</script>
<template>
  <Toast/>

  <div class="flex flex-col gap-6 pb-4 justify-content-center ">
    <slot name="conteudo"/>
  </div>
</template>

