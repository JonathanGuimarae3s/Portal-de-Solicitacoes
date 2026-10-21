<script setup lang="ts" xmlns="http://www.w3.org/1999/html">
import Column from "primevue/column"
import DataTable, {type DataTablePageEvent, type DataTableSortEvent} from "primevue/datatable"
import Button from "primevue/button";

import IconComponent from "@/shared/components/IconComponent.vue";
import type {LazyParams} from "@/types/interfaces.ts";
import type {Solicitacao} from "@/modules/solicitacoes/types/solicitacao.ts";
import {formatarStatusString} from "@/shared/utils/mappers/StatusUtils.ts";
import {formatarPrioridadeString} from "@/shared/utils/mappers/PrioridadeSolicitacaoUtils.ts";
import {formatarLocalDateTimeString} from "@/shared/utils/dateUtils.ts";
import Tag from "primevue/tag";
import {StatusSolicitacao} from "@/enums/enums.ts";
import Skeleton from "primevue/skeleton";
import {computed} from "vue";


interface iProps {
  paginar: (event: DataTablePageEvent) => void;
  ordenar: (event: DataTableSortEvent) => void;
  editar: (id: number) => void;
  solicitacoes: Solicitacao[] | [],
  totalRegistros: number;
  lazyParams: LazyParams;
  isLoading: boolean;
}

const props = defineProps<iProps>();

/*https://primevue.dev/datatable/#loading-skeleton*/
const linhasTabela = computed(() => {
  if (!props.isLoading) {
    return props.solicitacoes;
  }

  return Array.from({length: 5}, (_, index) => ({id: -(index + 1)}));
});

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

    <Column field="titulo" header="Titulo" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="70%"/>
        <span v-else>{{ slotProps.data.titulo }}</span>
      </template>
    </Column>

    <Column field="usuario.nome" header="Solicitante" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="70%"/>
        <span v-else>{{ slotProps.data.usuario.nome }}</span>
      </template>
    </Column>

    <Column field="tipo.nome" header="Tipo" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="60%"/>
        <span v-else>{{ slotProps.data.tipo.nome }}</span>
      </template>
    </Column>

    <Column field="prioridade" header="Prioridade" sortable>

      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="6rem" borderRadius="1rem"/>
        <template v-else>
          <Tag rounded :severity="retornaSeverityPrioridade(slotProps.data.prioridade)">
            <p class="font-bold">
              {{ formatarPrioridadeString(slotProps.data.prioridade) }}
            </p>
          </Tag>
        </template>
      </template>

    </Column>

    <Column header="Status" field="status" filter sortable>

      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="7rem" borderRadius="1rem"/>
        <template v-else>
          <Tag rounded :severity="retornaSeverityStatus(slotProps.data.status)">
            <p class="font-bold">
              {{ formatarStatusString(slotProps.data.status) }}
            </p>
          </Tag>
        </template>
      </template>
    </Column>

    <Column header="Data da Solicitacão" field="dataCriacao" sortable>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="9rem"/>
        <span v-else>{{ formatarLocalDateTimeString(slotProps.data.dataCriacao) }}</span>
      </template>
    </Column>

    <Column>
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="3rem" height="2.5rem"/>
        <template v-else>
          <Button severity="info" @click="editar(slotProps.data.id)"
                  v-if="slotProps.data.status !== StatusSolicitacao.CONCLUIDA && slotProps.data.status !== StatusSolicitacao.REJEITADA">
            <IconComponent icon="pi pi-pencil" :estilo="{fontWeight: 'bold'}"/>
          </Button>
        </template>


      </template>
    </Column>

  </DataTable>


</template>
