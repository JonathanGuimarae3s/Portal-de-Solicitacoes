<script setup lang="ts">

import Loading from '@/shared/components/Loading.vue';
import {Button, InputText, Panel, Password} from 'primevue';
import {ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import type {UserLogin} from '@/modules/auth/interfaces/UserLogin';
import {useAuthStore} from '../stores/auth';

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();

const userLogin = ref<UserLogin>({
  email: "",
  password: ""
});

let emailInvalido = ref(false);
let senhaInvalida = ref(false);
let acessoInvalido = ref(false);

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

    acessoInvalido.value = true;
    alert("Erro ao fazer login: " + error);
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
          <InputText id="email" v-model="userLogin.email" fluid :invalid="emailInvalido || acessoInvalido"/>
          <p v-if="emailInvalido" class="text-red-500 text-sm">Email é obrigatório</p>
        </div>

        <div>
          <label for="password">Senha</label>
          <Password v-model="userLogin.password" inputId="password" :feedback="false" toggleMask fluid
                    :invalid="senhaInvalida || acessoInvalido"/>
          <p v-if="senhaInvalida" class="text-red-500 text-sm">Senha é obrigatória</p>
        </div>

        <p v-if="acessoInvalido">Credencias inválidas.</p>

      </div>

      <Button label="Login" class="mt-3" @click="handleLogin" fluid/>

    </Panel>

  </div>

</template>
