<script setup lang="ts">
import Titulo from "@/shared/components/Titulo.vue";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import {useTiposStore} from "@/modules/tipos/stores/tipos.stores.ts";
import {onMounted, ref} from "vue";
import TabelaTipos from "@/modules/tipos/components/TabelaTipos.vue";
import Button from "primevue/button";
import type {LazyParams} from "@/types/interfaces.ts";
import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";
import TipoFormDialog from "@/modules/tipos/components/TipoFormDialog.vue";
import {storeToRefs} from "pinia";
import type {DataTablePageEvent, DataTableSortEvent} from "primevue/datatable";
import {ConfirmDialog} from "primevue";
import Message from "primevue/message";
import Toast from "primevue/toast";
import {useTipoForm} from "@/modules/tipos/composables/useTipoForm.ts";


const {toastError, toastSuccess} = useCustomToast();

const tiposStore = useTiposStore()


const {tipos, erro, isLoading} = storeToRefs(tiposStore);

const lazyParams = ref<LazyParams>({
  first: 0,
  rows: 10,
  sortField: "id",
  sortOrder: -1,
});


async function onPage(event: DataTablePageEvent) {
  lazyParams.value.first = event.first;
  lazyParams.value.rows = event.rows;

  await loadLazyData();
}

async function onSort(event: DataTableSortEvent) {
  lazyParams.value.first = 0;

  lazyParams.value.sortField =
      typeof event.sortField === "string"
          ? event.sortField
          : "id";

  lazyParams.value.sortOrder =
      event.sortOrder === 1 ? 1 : -1;

  await loadLazyData();
}

async function loadLazyData() {
  const {
    first,
    rows,
    sortField,
    sortOrder,
  } = lazyParams.value;

  await tiposStore.carregarTipos({
    pagina: Math.floor(first / rows),
    tamanho: rows,
    ordenarPor: sortField,
    direcao: sortOrder === 1 ? "asc" : "desc",
  });
}

const {
  visible,
  modo,
  tipoSolicitacao,
  preparaDialog,
  fecharDialog,
  salvar,
  abrirEdicao,
  confirmarExclusao,
} = useTipoForm({
  mostrarMensagemErro: toastError,
  mostrarMensagemSucesso: toastSuccess,
  recarregarTipos: loadLazyData,
});

onMounted(() => {
  loadLazyData()
})
</script>

<template>
  <container-page>
    <template #conteudo>


      <div class="flex items-center justify-between">
        <Titulo
            text="Tipos de solicitação"
            subtext="Cadastre categorias usadas nas solicitações."
        />
        <Button label="Novo Tipo" @click="preparaDialog('cadastro')"/>
      </div>


      <Message v-if="erro" severity="error" :closable="false">
        {{ erro }}
      </Message>

      <TabelaTipos v-else
                   :isLoading="isLoading"
                   :tipos="tipos?.dados?? []"
                   :totalRegistros="tipos?.totalDados??0"
                   :lazyParams="lazyParams"
                   :paginar="onPage"
                   :ordenar="onSort"
                   :editar="abrirEdicao"
                   :excluir="confirmarExclusao"
      />

      <TipoFormDialog :fechar-dialog="fecharDialog" :salvar="salvar" :tipoSolicitacao="tipoSolicitacao"
                      :visible="visible" :modo="modo"/>

      <Toast/>
      <ConfirmDialog/>

    </template>
  </container-page>
</template>
