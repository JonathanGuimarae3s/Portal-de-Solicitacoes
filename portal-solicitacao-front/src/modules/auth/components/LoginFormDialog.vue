<script setup lang="ts">
import Button from "primevue/button"
import {computed} from 'vue';
import {Form, type FormSubmitEvent} from "@primevue/forms";
import CampoInput from "@/shared/components/CampoInput.vue";
import type {UserLogin} from "@/modules/auth/types/UserLogin.ts";
import type {ErrosFormularioLogin} from "@/modules/auth/types/errosFormularioLogin.ts";
import Message from "primevue/message";


interface iProps {
  auttenticar: (userLogin: UserLogin) => Promise<void>,
  usuarioLogin: UserLogin,
  errorLogin: boolean
  menssagemLogin: string
}

const props = defineProps<iProps>();


const initialValues = computed<UserLogin>(() => ({
      email: props.usuarioLogin.email,
      password: props.usuarioLogin.password,
    })
);

const resolver = ({values}: Record<string, any>) => {
  const errors: ErrosFormularioLogin = {};

  if (!values.email) {
    errors.email = [{message: 'Email é obrigatório!'}];
  }

  if (!values.password) {
    errors.password = [{message: 'Senha é obrigatória!'}];
  }

  return {
    values,
    errors
  };
};

const enviar = (event: FormSubmitEvent) => {


  if (!event.valid) {
    return;
  }

  props.auttenticar(
      event.values as UserLogin
  );
};

</script>

<template>
  <Form v-slot="$form" :initialValues :resolver @submit="enviar">

    <CampoInput label="Email" type="email" campo="email" :message="$form.email?.error?.message ?? ''"
                :invalid="$form.email?.invalid ?? false"/>

    <CampoInput label="Senha" type="password" campo="password" :message="$form.password?.error?.message ?? ''"
                :invalid="$form.password?.invalid ?? false"/>

    <Message v-if="errorLogin" severity="error" :closable="false">
      {{ menssagemLogin }}
    </Message>

    <Button label="Login" class="mt-3" outlined severity="success" type="submit" fluid/>

  </Form>


</template>