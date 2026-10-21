<script setup lang="ts">
import Column from "primevue/column"
import DataTable, {type DataTablePageEvent, type DataTableSortEvent} from "primevue/datatable"
import Button from "primevue/button";
import ButtonGroup from "primevue/buttongroup";

import IconComponent from "@/shared/components/IconComponent.vue";
import type {LazyParams} from "@/types/interfaces.ts";
import type {TipoSolicitacao} from "@/modules/tipos/types/tipos.ts";
import Skeleton from "primevue/skeleton";
import {computed} from "vue";


interface iProps {
  paginar: (event: DataTablePageEvent) => void;
  ordenar: (event: DataTableSortEvent) => void;
  editar: (id: number) => void;
  excluir: (id: number) => void;
  tipos: TipoSolicitacao[] | [],
  totalRegistros: number;
  lazyParams: LazyParams;
  isLoading: boolean;
}

const props = defineProps<iProps>();

const linhasTabela = computed(() => {
  if (!props.isLoading) {
    return props.tipos;
  }

  return Array.from({length: 5}, (_, index) => ({id: -(index + 1)}));
});

</script>

<template>


  <DataTable
      :value="linhasTabela"
      lazy
      paginator
      dataKey="id"
      paginatorPosition="top"
      class="w-full" :first="lazyParams.first"
      :rows="lazyParams.rows"
      :rowsPerPageOptions="[5, 10, 20]"
      :sortField="lazyParams.sortField"
      :sortOrder="lazyParams.sortOrder"
      :totalRecords="totalRegistros"
      @page="paginar"
      @sort="ordenar"
      :stripedRows="true"
      :showGridlines="true"
  >

    <template #empty>
      <div class="text-center p-4">
        Nenhum registro foi encontrado.
      </div>
    </template>

    <Column field="id" header="Código" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="4rem"/>
        <span v-else>{{ slotProps.data.id }}</span>
      </template>
    </Column>

    <Column field="nome" header="Nome" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="70%"/>
        <span v-else>{{ slotProps.data.nome }}</span>
      </template>
    </Column>

    <Column>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="6rem" height="2.5rem"/>
        <ButtonGroup v-else>

          <Button severity="info" @click="editar(slotProps.data.id)">
            <IconComponent icon="pi pi-pencil" :estilo="{fontWeight: 'bold'}"/>
          </Button>

          <Button severity="danger" @click="excluir(slotProps.data.id)">
            <IconComponent icon="pi pi-trash" :estilo="{fontWeight: 'bold'}"/>
          </Button>

        </ButtonGroup>

      </template>
    </Column>
  </DataTable>


</template>
