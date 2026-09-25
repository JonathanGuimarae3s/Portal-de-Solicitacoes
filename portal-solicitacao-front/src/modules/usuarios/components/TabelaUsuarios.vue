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
  inativarAtivar: (id: number, nome: string, ativo: boolean) => void;
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
    <Column field="email" header="Email"/>
    <Column field="setor" header="Setor" sortable/>

    <Column header="Ativo?" sortable sortField="ativo">
      <template #body="slotProps">
        <Button v-if="slotProps.data.ativo" severity="success">
          <p class="font-bold">Sim</p>
        </Button>

        <Button v-else severity="danger">
          <p class="font-bold">Nào</p>
        </Button>
      </template>
    </Column>

    <Column>
      <template #body="slotProps">

        <ButtonGroup>

          <Button severity="info" @click="editar(slotProps.data.id)">
            <IconComponent icon="pi pi-pencil" :estilo="{fontWeight: 'bold'}"/>
          </Button>

          <Button v-if="slotProps.data.ativo" severity="danger"
                  @click="inativarAtivar(slotProps.data.id,slotProps.data.nome,slotProps.data.ativo)">
            <IconComponent icon="pi pi-ban" :estilo="{fontWeight: 'bold'}"/>
          </Button>

          <Button v-else severity="success"
                  @click="inativarAtivar(slotProps.data.id,slotProps.data.nome,slotProps.data.ativo)">
            <IconComponent icon="pi pi-check-circle" :estilo="{fontWeight: 'bold'}"/>
          </Button>

        </ButtonGroup>

      </template>
    </Column>
  </DataTable>


</template>
