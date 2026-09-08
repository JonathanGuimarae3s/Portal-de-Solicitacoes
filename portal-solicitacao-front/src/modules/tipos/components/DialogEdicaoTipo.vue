<script setup lang="ts">
import Button from "primevue/button"
import Dialog from "primevue/dialog"
import InputText from "primevue/inputtext"
import type {TipoSolitacao} from "@/modules/tipos/stores/tipos.stores.ts";


interface iProps {
  atualizarRegistro: () => void,
  fecharDialog: () => void;
  tipoSolicitacaoEditada: TipoSolitacao,
  visible: boolean,
}

defineProps<iProps>();
</script>

<template>
  <div class="card flex justify-content-center">
    <Dialog :visible="visible" modal :style="{ width: '25rem' }">

      <template #header>
        <div class="inline-flex align-items-center justify-content-center gap-2">
          <span
              class="font-bold white-space-nowrap">Editando Tipo de solicitação: {{
              tipoSolicitacaoEditada.nome
            }}</span>
        </div>
      </template>
      <div class="mb-3">
        <label for="username" class="font-semibold w-6rem">Nome do tipo</label>
        <InputText id="username" class="flex-auto" autocomplete="off" fluid v-model="tipoSolicitacaoEditada.nome"/>

      </div>
      <template #footer>
        <Button label="Cancelar" text severity="secondary" @click="fecharDialog()" autofocus/>
        <Button label="Salvar" outlined severity="success" @click="atualizarRegistro()" autofocus/>
      </template>
    </Dialog>
  </div>
</template>