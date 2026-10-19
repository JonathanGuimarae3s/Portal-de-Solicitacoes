<script setup lang="ts" xmlns="http://www.w3.org/1999/html">
import Column from "primevue/column"
import DataTable, {type DataTablePageEvent, type DataTableSortEvent} from "primevue/datatable"
import Button from "primevue/button";

import IconComponent from "@/shared/components/IconComponent.vue";
import type {LazyParams} from "@/interfaces/interfaces.ts";
import type {Solicitacao} from "@/modules/solicitacoes/types/solicitacao.types.ts";
import {formatarStatusString} from "@/shared/utils/mappers/StatusUtils.ts";
import {formatarPrioridadeString} from "@/shared/utils/mappers/PrioridadeSolicitacaoUtils.ts";
import {formatarLocalDateTimeString} from "@/shared/utils/dateUtils.ts";
import Tag from "primevue/tag";
import {StatusSolicitacao} from "@/enums/enums.ts";


interface iProps {
  paginar: (event: DataTablePageEvent) => void;
  ordenar: (event: DataTableSortEvent) => void;
  editar: (id: number) => void;
  solicitacoes: Solicitacao[] | [],
  totalRegistros: number;
  lazyParams: LazyParams;
}

defineProps<iProps>();

function retornaSeverityPrioridade(prioridade: string): string {

  switch (prioridade) {
    case "ALTA":
      return "danger"

    case "MEDIA":
      return "warn"

    default:
    case "BAIXA":
      return "info"
  }


}

function retornaSeverityStatus(status: string): string {

  switch (status) {
    case "EM_APROVACAO":
      return "warn"

    case "APROVADA":
      return "contrast"

    case "REJEITADA":
      return "danger"

    case "CONCLUIDA":
      return "success"

    default:
    case "ABERTA":
      return "info"
  }

}


</script>

<template>


  <DataTable
      :value="solicitacoes"
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

    <Column field="id" header="Código" sortable/>
    <Column field="titulo" header="Titulo" sortable/>
    <Column field="usuario.nome" header="Solicitante" sortable/>
    <Column field="tipo.nome" header="Tipo" sortable/>

    <Column field="prioridade" header="Prioridade" sortable>

      <template #body="slotProps">

        <Tag rounded :severity="retornaSeverityPrioridade(slotProps.data.prioridade)">
          <p class="font-bold">
            {{ formatarPrioridadeString(slotProps.data.prioridade) }}
          </p>
        </Tag>

      </template>

    </Column>

    <Column header="Status" field="status" filter sortable>

      <template #body="slotProps">

        <Tag rounded :severity="retornaSeverityStatus(slotProps.data.status)">
          <p class="font-bold">
            {{ formatarStatusString(slotProps.data.status) }}
          </p>
        </Tag>

      </template>
    </Column>

    <Column header="Data da Solicitacão" field="dataCriacao" sortable>
      <template #body="slotProps">
        {{ formatarLocalDateTimeString(slotProps.data.dataCriacao) }}
      </template>
    </Column>

    <Column>
      <template #body="slotProps">


        <Button severity="info" @click="editar(slotProps.data.id)"
                v-if=" slotProps.data.status !== StatusSolicitacao.CONCLUIDA &&  slotProps.data.status !== StatusSolicitacao.REJEITADA
">
          <IconComponent icon="pi pi-pencil" :estilo="{fontWeight: 'bold'}"/>
        </Button>


      </template>
    </Column>

  </DataTable>


</template>
