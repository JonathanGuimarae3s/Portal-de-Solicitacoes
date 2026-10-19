<script setup lang="ts">
import {useAuthStore} from '@/modules/auth/stores/auth';
import Header from '../components/Header.vue';
import Loading from '../components/Loading.vue';
import Menu from '../components/Menu.vue';

const authStore = useAuthStore();
</script>

<template>

  <Loading v-if="authStore.isLoading"/>

  <div v-else class=" flex flex-col min-h-screen " >
    <Header/>

    <div class="flex-1 flex">
      <aside class=" w-64 bg-zinc-900 border-r border-zinc-800 p-4">
        <Menu/>
      </aside>
      <main class="pt-5 px-6  w-full">
        <router-view/>
      </main>
    </div>
  </div>

</template>
