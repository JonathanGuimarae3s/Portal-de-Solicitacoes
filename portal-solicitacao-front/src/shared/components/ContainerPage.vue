<script setup lang="ts">
import {Toast} from "primevue";
</script>
<template>
  <Toast/>

  <div class="flex flex-col  gap-5 ">
    <slot name="conteudo"/>
  </div>
</template>

