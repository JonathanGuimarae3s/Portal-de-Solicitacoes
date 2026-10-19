<script setup lang="ts">
import {Panel, Toast} from "primevue";
</script>
<template>
  <Toast/>

  <Panel class="flex flex-col items-center justify-content-center ">
    <slot name="conteudo"/>
  </Panel>
</template>

