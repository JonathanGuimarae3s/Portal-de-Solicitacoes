<script setup lang="ts">
import Column from "primevue/column"
import DataTable, {type DataTablePageEvent, type DataTableSortEvent} from "primevue/datatable"
import {onMounted, ref} from "vue";
import {storeToRefs} from "pinia";
import Message from "primevue/message";
import Loading from "@/shared/components/Loading.vue";
import {useTiposStores} from "@/modules/tipos/stores/tipos.stores.ts";
import type {ErrorResponse, LazyParams} from "@/interfaces/interfaces.ts";
import Button from "primevue/button";
import ButtonGroup from "primevue/buttongroup";

import IconComponent from "@/modules/tipos/components/IconComponent.vue";
import {ConfirmDialog, useConfirm, useToast} from "primevue";
import axios from "axios";
import Toast from "primevue/toast";

const tiposStores = useTiposStores();
const {tipos, erro} = storeToRefs(tiposStores);


const lazyParams = ref<LazyParams>({
  first: 0,
  rows: 10,
  sortField: "id",
  sortOrder: -1,
});

const isLoading = ref(false);

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
  try {
    isLoading.value = true;
    const {
      first,
      rows,
      sortField,
      sortOrder,
    } = lazyParams.value;


    await tiposStores.carregaTipos({
      pagina: Math.floor(first / rows),
      tamanho: rows,
      ordenarPor: sortField,
      direcao: sortOrder === 1 ? "asc" : "desc",
    });


  } finally {
    isLoading.value = false;
  }
}


onMounted(() => {
  loadLazyData();
});


const confirm = useConfirm();
const toast = useToast();

const confirm1 = () => {
  confirm.require({
    message: 'Are you sure you want to proceed?',
    header: 'Confirmation',
    icon: 'pi pi-exclamation-triangle',
    rejectClass: 'p-button-secondary p-button-outlined',
    rejectLabel: 'Cancel',
    acceptLabel: 'Save',
    accept: () => {
      toast.add({severity: 'info', summary: 'Confirmed', detail: 'You have accepted', life: 3000});
    },
    reject: () => {
      toast.add({severity: 'error', summary: 'Rejected', detail: 'You have rejected', life: 3000});
    }
  });
};

const apagar = (id: number) => {
  confirm.require({
    message: 'Você quer deletar esse registro?',
    header: 'Atencão!',
    icon: 'pi pi-info-circle',
    rejectLabel: 'Cancelar',
    acceptLabel: 'Deletar',
    rejectClass: 'p-button-secondary p-button-outlined',
    acceptClass: 'p-button-danger',
    accept: async () => {


      try {
        isLoading.value = true

        await tiposStores.apagarTipos(id)

        toast.add({severity: 'success', summary: 'Concluído', detail: 'Registro deletado!', life: 5000});
      } catch (error) {

        let erroDeletar: string | undefined = "Não foi possível deletar o registro. Tente mais tarde.";
        if (axios.isAxiosError<ErrorResponse>(error)) {
          erroDeletar = error.response?.data.detail
        }


        toast.add({severity: 'error', summary: 'Error', detail: erroDeletar, life: 5000});


      } finally {
        await loadLazyData()
      }
    },
  });
};

</script>

<template>
  <Toast/>
  <ConfirmDialog></ConfirmDialog>

  <Loading v-if="isLoading"/>

  <Message v-else-if="erro" severity="error" :closable="false">
    {{ erro }}
  </Message>

  <DataTable v-else
             :value="tipos?.dados ?? []"
             lazy
             paginator
             dataKey="id"
             paginatorPosition="top"
             class="w-full" :first="lazyParams.first"
             :rows="lazyParams.rows"
             :rowsPerPageOptions="[5, 10, 20]"
             :sortField="lazyParams.sortField"
             :sortOrder="lazyParams.sortOrder"
             :totalRecords="tipos?.totalDados ?? 0"
             :loading="isLoading"
             @page="onPage"
             @sort="onSort"
  >

    <Column field="id" header="Código" sortable/>
    <Column field="nome" header="Nome" sortable/>
    <Column>
      <template #body="slotProps">

        <ButtonGroup>

          <Button severity="info" @click="confirm1()">
            <IconComponent icon="pi pi-pencil" :estilo="{fontWeight: 'bold'}"/>
          </Button>

          <Button severity="danger" @click="apagar(slotProps.data.id)">
            <IconComponent icon="pi pi-trash" :estilo="{fontWeight: 'bold'}"/>
          </Button>

        </ButtonGroup>

      </template>
    </Column>
  </DataTable>


</template>
