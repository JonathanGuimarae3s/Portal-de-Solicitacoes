<script setup lang="ts">

import Loading from '@/shared/components/Loading.vue';
import {Panel} from 'primevue';
import {ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import type {UserLogin} from '@/modules/auth/types/UserLogin';
import {useAuthStore} from '../stores/auth';
import {obterMensagemErro} from "@/shared/utils/store/mensagem.store.ts";
import LoginFormDialog from "@/modules/auth/components/LoginFormDialog.vue";


const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();

const userLoginRequest = ref<UserLogin>({
  email: "",
  password: ""
});

let emailInvalido = ref(false);
let senhaInvalida = ref(false);
let errorLogin = ref(false);

let menssagemLogin = ref("")

const handleLogin = async (userLogin: UserLogin) => {
  errorLogin.value = false;
  menssagemLogin.value = "";

  if (!userLogin.email || !userLogin.password) {
    emailInvalido.value = true;
    senhaInvalida.value = true;

    userLoginRequest.value.email = "";
    userLoginRequest.value.password = "";

    return;
  }

  try {
    await authStore.autenticar(userLogin);
    await redirect();
  } catch (e) {
    errorLogin.value = true;
    menssagemLogin.value = obterMensagemErro(e)
  }
}


async function redirect() {
  const query = route.query.redirect as string;
  const redirect = typeof query === "string" && query.startsWith("/") ? query : "/";
  await router.replace(redirect)
}


</script>


<template>


  <Loading v-if="authStore.isLoading"/>

  <div v-else class="flex flex-col  gap-4 justify-center items-center min-h-screen">

    <Panel header="Login">

      <div class="flex flex-col  gap-4 ">


        <LoginFormDialog :auttenticar="handleLogin" :usuarioLogin="userLoginRequest" :errorLogin="errorLogin"
                         :menssagemLogin="menssagemLogin"/>

      </div>


    </Panel>

  </div>

</template>
