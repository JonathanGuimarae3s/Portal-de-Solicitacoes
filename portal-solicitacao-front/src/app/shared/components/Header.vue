<script setup lang="ts">
import {useAuthStore} from '@/app/modules/auth/stores/auth';
import {useRouter} from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const usuarioAutenticado = authStore.userSession?.usuarioAutenticado

async function handleLogout() {
  await authStore.logout();
  router.push({name: "Auth"})

}


</script>

<template>
  <header class="flex justify-between items-center px-6 h-16 border-b border-zinc-800">
    <h1 class="text-xl font-black">Portal solicitacoes</h1>

    <div class="flex gap-3 items-center">

      <div class="flex flex-col items-end">
                <span class="text-small font-bold text-zinc-400">
                    {{ usuarioAutenticado?.nome }}
                </span>

                <span class="text-xs text-zinc-500">
                    {{ usuarioAutenticado?.email }}
                </span>
      </div>

      <button class="btn btn-danger" @click="handleLogout"> Sair</button>
    </div>

  </header>
</template>