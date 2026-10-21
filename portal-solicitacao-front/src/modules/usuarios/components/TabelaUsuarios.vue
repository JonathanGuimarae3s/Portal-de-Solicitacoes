<script setup lang="ts">
import Column from "primevue/column"
import DataTable, {type DataTablePageEvent, type DataTableSortEvent} from "primevue/datatable"
import Button from "primevue/button";
import ButtonGroup from "primevue/buttongroup";
import Tag from "primevue/tag";

import IconComponent from "@/shared/components/IconComponent.vue";
import type {LazyParams} from "@/types/interfaces.ts";
import type {Usuario} from "@/modules/usuarios/types/usuario.ts";
import {formatarPerfilUsuario} from "@/shared/utils/mappers/PerfilUsuarioUtils.ts";
import {formatarSetor} from "@/shared/utils/mappers/SetorUtils.ts";
import Skeleton from "primevue/skeleton";
import {computed} from "vue";


interface iProps {
  paginar: (event: DataTablePageEvent) => void;
  ordenar: (event: DataTableSortEvent) => void;
  editar: (id: number) => void;
  inativarAtivar: (id: number, nome: string, ativo: boolean) => void;
  usuarios: Usuario[] | [],
  totalRegistros: number;
  lazyParams: LazyParams;
  podeGerenciar: boolean
  isLoading: boolean;
}

const props = defineProps<iProps>();

const linhasTabela = computed(() => {
  if (!props.isLoading) {
    return props.usuarios;
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

    <Column field="email" header="Email">
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="80%"/>
        <span v-else>{{ slotProps.data.email }}</span>
      </template>
    </Column>
    <Column header="Setor" field="setor" sortable>

      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="7rem"/>
        <span v-else>{{ formatarSetor(slotProps.data.setor) }}</span>
      </template>

    </Column>

    <Column header="Perfil" field="perfil" sortable>

      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="6rem"/>
        <span v-else>{{ formatarPerfilUsuario(slotProps.data.perfil) }}</span>
      </template>

    </Column>


    <Column header="Ativo?" field="ativo" sortable sortField="ativo">
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="4rem" borderRadius="1rem"/>
        <template v-else>
          <Tag rounded v-if="slotProps.data.ativo" severity="success">
            <p class="font-bold">Sim</p>
          </Tag>

          <Tag rounded v-else severity="danger">
            <p class="font-bold">Nào</p>
          </Tag>
        </template>
      </template>
    </Column>

    <Column v-if="podeGerenciar ">
      <template #body="slotProps">
        <Skeleton v-if="isLoading" width="6rem" height="2.5rem"/>
        <ButtonGroup v-else>

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
