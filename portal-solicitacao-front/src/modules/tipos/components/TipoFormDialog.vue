<script setup lang="ts">
import Button from "primevue/button"
import Dialog from "primevue/dialog"
import InputText from "primevue/inputtext"
import type {TipoSolicitacaoRequest} from "@/modules/tipos/types/tipos.types.ts";


interface iProps {
  salvar: (modo: "cadastro" | "edicao") => void,
  fecharDialog: () => void;
  modo: "cadastro" | "edicao";
  tipoSolicitacao: TipoSolicitacaoRequest,
  visible: boolean;
  loading?: boolean;
}

defineProps<iProps>();
</script>

<template>
  <div class="card flex justify-content-center">
    <Dialog :visible="visible" modal :style="{ width: '50rem' }">

      <template #header>
        <div class="inline-flex align-items-center justify-content-center gap-2">
          <span v-if="modo == 'cadastro'"
                class="font-bold white-space-nowrap">Novo Tipo de solicitação</span>

          <span v-if="modo == 'edicao'"
                class="font-bold white-space-nowrap">Editando Registro  </span>
        </div>
      </template>
      <div class="mb-3">

        <label for="tipo" class="font-semibold w-6rem">Nome do tipo</label>
        <InputText id="tipo" class="flex-auto" autocomplete="off" fluid v-model="tipoSolicitacao.nome"/>

      </div>
      <template #footer>
        <Button label="Cancelar" text severity="secondary" @click="fecharDialog"/>
        <Button label="Salvar" outlined severity="success" @click="salvar(modo)" autofocus/>
      </template>
    </Dialog>
  </div>
</template>