<script lang="ts" setup>

import MenuItem from "./MenuItem.vue";
import {useAuthStore} from "@/modules/auth/stores/auth.ts";
import {storeToRefs} from "pinia";

const authStore = useAuthStore();
const {isAdmin, isGestor} = storeToRefs(authStore)


const isAdminOrGestor = isAdmin.value || isGestor.value


</script>

<template>

  <nav class="flex flex-col">
    <MenuItem label="Dashboard" to="/"/>
    <MenuItem label="Solicitações" to="/solicitacoes"/>
    <MenuItem v-if="isAdmin" label="Tipos" to="/tipos"/>
    <MenuItem v-if="isAdminOrGestor" label="Usuários" to="/usuarios"/>
  </nav>

</template>
