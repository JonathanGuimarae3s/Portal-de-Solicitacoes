<script setup lang="ts">
import Button from "primevue/button"
import Dialog from "primevue/dialog"
import InputText from "primevue/inputtext"
import type {TipoSolitacaoRequest} from "@/modules/tipos/stores/tipos.stores.ts";


interface iProps {
  salvar: () => void,
  fecharDialog: () => void;
  tipoSolicitacao: TipoSolitacaoRequest,
  visible: boolean,
}

defineProps<iProps>();
</script>

<template>
  <div class="card flex justify-content-center">
    <Dialog :visible="visible" modal :style="{ width: '50rem' }">

      <template #header>
        <div class="inline-flex align-items-center justify-content-center gap-2">
          <span
              class="font-bold white-space-nowrap">Novo Tipo de solicitação</span>
        </div>
      </template>
      <div class="mb-3">

        <label for="tipo" class="font-semibold w-6rem">Nome do tipo</label>
        <InputText id="tipo" class="flex-auto" autocomplete="off" fluid v-model="tipoSolicitacao.nome"/>

      </div>
      <template #footer>
        <Button label="Cancelar" text severity="secondary" @click="fecharDialog" autofocus/>
        <Button label="Salvar" outlined severity="success" @click="salvar" autofocus/>
      </template>
    </Dialog>
  </div>
</template>