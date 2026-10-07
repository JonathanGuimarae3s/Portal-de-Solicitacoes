<script setup lang="ts">
import Button from "primevue/button"
import Dialog from "primevue/dialog"
import type {UsuarioRequest} from "@/modules/usuarios/types/usuario.types.ts";
import {PerfilUsuario, Setor} from "@/enums/enums.ts";
import {Select} from "primevue";
import {computed} from 'vue';
import {Form, type FormSubmitEvent} from "@primevue/forms";
import CampoForm from "@/shared/components/CampoForm.vue";

import type {ErrosFormulario} from "@/modules/usuarios/types/errosFormulario.types.ts";
import {formatarPerfilUsuario} from "@/shared/utils/mappers/PerfilUsuarioUtils.ts";
import {formatarSetor} from "@/shared/utils/mappers/SetorUtils.ts";


interface iProps {
  salvar: (modo: "cadastro" | "edicao", usuario: UsuarioRequest) => void | Promise<void>,
  fecharDialog: () => void;
  modo: "cadastro" | "edicao";
  usuario: UsuarioRequest,
  visible: boolean;
  loading?: boolean;
  perfisUsuario: PerfilUsuario[];
  setores: Setor[];
  isAdmin: boolean;
}

const props = defineProps<iProps>();


const initialValues = computed<UsuarioRequest>(() => ({
  nome: props.usuario.nome,
  email: props.usuario.email,
  senha: props.usuario.senha,
  perfil: props.usuario.perfil,
  setor: props.usuario.setor
}));

const resolver = ({values}: Record<string, any>) => {
  const errors: ErrosFormulario = {};

  if (!values.nome) {
    errors.nome = [{message: 'Nome é obrigatório!'}];
  }

  if (!values.email) {
    errors.email = [{message: 'Email é obrigatório!'}];
  }

  if (!values.senha && props.modo === "cadastro") {
    errors.senha = [{message: 'Senha é obrigatório!'}];
  }

  if (!values.perfil) {
    errors.perfil = [{message: 'Perfil é obrigatório!'}];
  }

  if (!values.setor) {
    errors.setor = [{message: 'Setor é obrigatório!'}];
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

  props.salvar(
      props.modo,
      event.values as UsuarioRequest
  );
};
</script>

<template>
  <div class="card flex justify-content-center">
    <Dialog :visible="visible" modal :style="{ width: '50rem' }" :closable="false">


      <template #header>
        <div class="inline-flex align-items-center justify-content-center gap-2">
          <span v-if="modo == 'cadastro'"
                class="font-bold white-space-nowrap">Novo Usuário</span>

          <span v-if="modo == 'edicao'"
                class="font-bold white-space-nowrap">Editando Registro</span>
        </div>
      </template>

      <Form v-slot="$form" :initialValues :resolver @submit="enviar">

        <CampoForm label="Nome" type="text" campo="nome" :message="$form.nome?.error?.message ?? ''"
                   :invalid="$form.nome?.invalid ?? false"/>


        <CampoForm label="E-mail" type="email" campo="email" :message="$form.email?.error?.message ?? ''"
                   :invalid="$form.email?.invalid ?? false"/>

        <CampoForm label="Perfil" type="" campo="perfil" :message="$form.perfil?.error?.message ?? ''"
                   :invalid="$form.perfil?.invalid ?? false">

          <template #input>

            <Select name="perfil" :options="perfisUsuario" :optionLabel="formatarPerfilUsuario"
                    placeholder="Selecione o perfil" :disabled="!isAdmin"
                    fluid
            />

          </template>

        </CampoForm>


        <CampoForm label="Setor" type="" campo="setor" :message="$form.setor?.error?.message ?? ''"
                   :invalid="$form.setor?.invalid ?? false">
          <template #input>

            <Select name="setor" :options="setores" :optionLabel="formatarSetor"
                    :invalid="$form.setor?.invalid"
                    placeholder="Selecione o setor" :disabled="!isAdmin"
                    fluid
            />

          </template>

        </CampoForm>


        <CampoForm label="Senha" type="password" campo="senha" :message="$form.senha?.error?.message ?? ''"
                   :invalid="$form.senha?.invalid ?? false"/>


        <div class="  flex align-items-center justify-content-center gap-2  ">

          <Button label="Cancelar" outlined severity="secondary" @click="fecharDialog" fluid/>
          <Button label="Salvar" outlined severity="success" type="submit" autofocus fluid/>

        </div>


      </Form>


    </Dialog>
  </div>


</template>