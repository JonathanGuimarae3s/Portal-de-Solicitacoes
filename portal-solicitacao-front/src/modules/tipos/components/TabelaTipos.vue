<script setup lang="ts">
import Column from "primevue/column"
import DataTable, {type DataTablePageEvent, type DataTableSortEvent} from "primevue/datatable"
import Button from "primevue/button";
import ButtonGroup from "primevue/buttongroup";

import IconComponent from "@/shared/components/IconComponent.vue";
import type {LazyParams} from "@/interfaces/interfaces.ts";
import type {TipoSolicitacao} from "@/modules/tipos/types/tipos.types.ts";


interface iProps {
  paginar: (event: DataTablePageEvent) => void;
  ordenar: (event: DataTableSortEvent) => void;
  editar: (id: number) => void;
  excluir: (id: number) => void;
  tipos: TipoSolicitacao[] | [],
  totalRegistros: number;
  lazyParams: LazyParams;
}

defineProps<iProps>();

</script>

<template>


  <DataTable
      :value="tipos"
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
  >

    <Column field="id" header="Código" sortable/>
    <Column field="nome" header="Nome" sortable/>
    <Column>
      <template #body="slotProps">

        <ButtonGroup>

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
