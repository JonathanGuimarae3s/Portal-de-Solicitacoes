<script setup lang="ts">
import DataTable, {type DataTablePageEvent, type DataTableSortEvent} from "primevue/datatable"
import type {LazyParams} from "@/types/interfaces.ts";
import {formatarStatusSolicitacao} from "@/shared/utils/mappers/StatusUtils.ts";
import {formatarPrioridade} from "@/shared/utils/mappers/PrioridadeSolicitacaoUtils.ts";
import type {SolicitacaoResumo} from "@/modules/dashboard/interfaces/dashboard.ts";
import Column from "primevue/column"
import Tag from "primevue/tag";
import Skeleton from "primevue/skeleton";
import {computed} from "vue";
import {PrioridadeSolicitacao, StatusSolicitacao} from "@/enums/enums.ts";

function getSeverityPrioridade(prioridade: string): string {

  switch (prioridade) {
    case "MEDIA":
      return "warn"
    case "ALTA":
      return "danger"
    default:
      return "info"
  }
}

function getSeverityStatus(status: string): string {

  switch (status) {
    case "ABERTA":
      return "primary";
    case "EM_APROVACAO":
      return "warn"
    case "APROVADA":
      return "info"
    case "REJEITADA":
      return "danger"
    case "CONCLUIDA":
      return "success"
    default:
      return "info"
  }
}


interface iProps {
  lazyParams: LazyParams;
  paginar: (event: DataTablePageEvent) => void;
  ordenar: (event: DataTableSortEvent) => void;
  totalRegistros: number;
  solicitacoes: SolicitacaoResumo[] | [];
  isLoading: boolean

}

const props = defineProps<iProps>();

const linhasTabela = computed<SolicitacaoResumo[]>(() => {
  if (!props.isLoading) {
    return props.solicitacoes;
  }

  return Array.from({length: 5}, (_, index) => ({
    id: -(index + 1),
    titulo: "",
    status: StatusSolicitacao.ABERTA,
    prioridade: PrioridadeSolicitacao.BAIXA,
  }));
});

</script>

<template>


  <DataTable
      :value="linhasTabela"
      lazy
      paginator
      dataKey="id"
      class="w-full"
      paginatorPosition="top"
      :first="lazyParams.first"
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

    <Column field="titulo" header="Título" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="70%"/>
        <span v-else>{{ slotProps.data.titulo }}</span>
      </template>
    </Column>

    <Column field="status" header="Status" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="7rem" borderRadius="1rem"/>
        <template v-else>
          <Tag rounded :severity="getSeverityStatus(slotProps.data.status)">
            {{ formatarStatusSolicitacao(slotProps.data.status) }}
          </Tag>
        </template>
      </template>
    </Column>

    <Column field="prioridade" header="Prioridade" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="6rem" borderRadius="1rem"/>
        <template v-else>
          <Tag rounded :severity="getSeverityPrioridade(slotProps.data.prioridade)">
            {{ formatarPrioridade(slotProps.data.prioridade) }}
          </Tag>
        </template>
      </template>
    </Column>


  </DataTable>

</template>
