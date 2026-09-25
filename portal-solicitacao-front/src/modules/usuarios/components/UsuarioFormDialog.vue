<script setup lang="ts">
import Button from "primevue/button"
import Dialog from "primevue/dialog"
import InputText from "primevue/inputtext"
import type {UsuarioRequest} from "@/modules/usuarios/types/usuario.types.ts";
import {ref} from "vue";
import {PerfilUsuario, Setor} from "@/enums/enums.ts";
import {Select} from "primevue";

interface iProps {
  salvar: (modo: "cadastro" | "edicao") => void,
  fecharDialog: () => void;
  modo: "cadastro" | "edicao";
  usuario: UsuarioRequest,
  visible: boolean;
  loading?: boolean;
}

defineProps<iProps>();


const perfisUsuario = ref(Object.values(PerfilUsuario));
const setores = ref(Object.values(Setor));


</script>

<template>
  <div class="card flex justify-content-center">
    <Dialog :visible="visible" modal :style="{ width: '50rem' }">



      <template #header>
        <div class="inline-flex align-items-center justify-content-center gap-2">
          <span v-if="modo == 'cadastro'"
                class="font-bold white-space-nowrap">Novo Usuário</span>

          <span v-if="modo == 'edicao'"
                class="font-bold white-space-nowrap">Editando Registro  </span>
        </div>
      </template>

      <div class="mb-3">

        <label for="usuario" class="font-semibold w-6rem">Nome </label>
        <InputText id="usuario" class="flex-auto" autocomplete="off" fluid v-model="usuario.nome"
                   />

      </div>

      <div class="mb-3">

        <label for="email" class="font-semibold w-6rem">E-mail</label>
        <InputText type="email" id="email" class="flex-auto" autocomplete="off" fluid v-model="usuario.email"
                   />

      </div>

      <div class="mb-3">

        <label for="perfil" class="font-semibold w-6rem">Perfil</label>
        <Select v-model="usuario.perfil" :options="perfisUsuario" optionLabel="" placeholder="Selecione o perfil"
                fluid
              />

      </div>

      <div class="mb-3">

        <label for="setor" class="font-semibold w-6rem">Setor</label>
        <Select v-model="usuario.setor" :options="setores" optionLabel="" placeholder="Selecione o setor"
                fluid
                />



      </div>

      <div class="mb-3" >

        <label for="senha" class="font-semibold w-6rem">Senha</label>
        <InputText type="password" id="senha" class="flex-auto" autocomplete="off" fluid v-model="usuario.senha"
                   />

      </div>

      <template #footer>
        <Button label="Cancelar" text severity="secondary" @click="fecharDialog"/>
        <Button label="Salvar" outlined severity="success" @click="salvar(modo)" autofocus/>
      </template>
    </Dialog>
  </div>
</template>