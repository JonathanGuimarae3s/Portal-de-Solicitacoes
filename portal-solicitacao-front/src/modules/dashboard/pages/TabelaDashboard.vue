<script setup lang="ts">
import Column from "primevue/column"
import DataTable, {type DataTablePageEvent, type DataTableSortEvent} from "primevue/datatable"
import {onMounted, ref} from "vue";
import {PrioridadeSolicitacao, StatusSolicitacao} from "@/modules/enums/enums.ts"
import {useDashboardStore} from "@/modules/dashboard/stores/dashboard.store.ts";
import {storeToRefs} from "pinia";
import Message from "primevue/message";
import Loading from "@/shared/components/Loading.vue";
import Tag from 'primevue/tag';

const dashboardStore = useDashboardStore();
const {solicitacoes, erroTabela} = storeToRefs(dashboardStore);

type SortOrder = 1 | -1;

interface LazyParams {
  first: number;
  rows: number;
  sortField: string;
  sortOrder: SortOrder;
}

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
    } = lazyParams.value;


    await dashboardStore.carregarSolicitacoes({
      pagina: Math.floor(first / rows),
      tamanho: rows,
      ordenarPor: sortField,
    });


  } finally {
    isLoading.value = false;
  }
}

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


onMounted(() => {
  loadLazyData();
});

</script>

<template>

  <Loading v-if="isLoading"/>

  <Message v-else-if="erroTabela" severity="error" :closable="false">
    {{ erroTabela }}
  </Message>

  <DataTable v-else
             :value="solicitacoes?.dados ?? []"
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
             :totalRecords="solicitacoes?.totalDados ?? 0"
             :loading="isLoading"
             @page="onPage"
             @sort="onSort"
  >
    <Column field="id" header="Código" sortable/>
    <Column field="titulo" header="Título" sortable/>
    <Column field="status" header="Status" sortable>
      <template #body="slotProps">
            <span>
            </span>
        <Tag rounded :severity="getSeverityStatus(slotProps.data.status)">
          {{ StatusSolicitacao[slotProps.data.status as keyof typeof StatusSolicitacao] }}
        </Tag>
      </template>
    </Column>
    <Column field="prioridade" header="Prioridade" sortable>
      <template #body="slotProps">

        <Tag rounded :severity="getSeverityPrioridade(slotProps.data.prioridade)">
          {{ PrioridadeSolicitacao[slotProps.data.prioridade as keyof typeof PrioridadeSolicitacao] }}
        </Tag>

      </template>
    </Column>
  </DataTable>
</template>