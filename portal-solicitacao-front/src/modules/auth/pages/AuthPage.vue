<script setup lang="ts">

import Loading from '@/shared/components/Loading.vue';
import {Button, InputText, Panel, Password} from 'primevue';
import {ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import type {UserLogin} from '@/modules/auth/interfaces/UserLogin';
import {useAuthStore} from '../stores/auth';
import Message from "primevue/message";
import axios from "axios";
import type {ErrorResponse} from "@/interfaces/interfaces.ts";


const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();

const userLogin = ref<UserLogin>({
  email: "",
  password: ""
});

let emailInvalido = ref(false);
let senhaInvalida = ref(false);
let errorLogin = ref(false);

let menssagemLogin = ref("")

async function handleLogin() {

  if (!userLogin.value.email || !userLogin.value.password) {
    emailInvalido.value = true;
    senhaInvalida.value = true;
    return;
  }

  try {
    await authStore.autenticar(userLogin.value.email, userLogin.value.password);
    await redirect();
  } catch (error) {

    errorLogin.value = true;


    if (axios.isAxiosError<ErrorResponse>(error)) {
      menssagemLogin.value =
          error.response?.data.detail ??
          "Não foi possível conectar ao servidor.";
    }


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

        <div>
          <label for="email">Email</label>
          <InputText id="email" v-model="userLogin.email" fluid :invalid="emailInvalido || errorLogin"/>
          <p v-if="emailInvalido" class="text-red-500 text-sm">Email é obrigatório</p>
        </div>

        <div>
          <label for="password">Senha</label>
          <Password v-model="userLogin.password" inputId="password" :feedback="false" toggleMask fluid
                    :invalid="senhaInvalida || errorLogin"/>
          <p v-if="senhaInvalida" class="text-red-500 text-sm">Senha é obrigatória</p>
        </div>

        <Message v-if="errorLogin" severity="error" :closable="false">
          {{ menssagemLogin }}
        </Message>

      </div>

      <Button label="Login" class="mt-3" @click="handleLogin" fluid/>

    </Panel>

  </div>

</template>
