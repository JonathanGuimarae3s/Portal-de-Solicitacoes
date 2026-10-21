<script setup lang="ts">

import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";
import {storeToRefs} from "pinia";
import {onMounted, ref} from "vue";
import type {LazyParams} from "@/types/interfaces.ts";
import type {DataTablePageEvent, DataTableSortEvent} from "primevue/datatable";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import {Button} from "primevue";
import Titulo from "@/shared/components/Titulo.vue";
import {useAuthStore} from "@/modules/auth/stores/auth.ts";
import {useSolicitacaoStore} from "@/modules/solicitacoes/stores/solicitacoes.stores.ts";
import TabelaSolicitacoes from "@/modules/solicitacoes/components/TabelaSolicitacoes.vue";
import {obterMensagemErro} from "@/shared/utils/store/mensagem.store.ts";
import {PrioridadeSolicitacao, StatusSolicitacao} from "@/enums/enums.ts";
import SolicitacaoFormDialog from "@/modules/solicitacoes/components/SolicitacaoFormDialog.vue";
import {formatarPrioridade} from "@/shared/utils/mappers/PrioridadeSolicitacaoUtils.ts";
import {formatarStatusSolicitacao} from "@/shared/utils/mappers/StatusUtils.ts";
import Message from "primevue/message";
import SolicitacaoFiltroSelect from "@/modules/solicitacoes/components/SolicitacaoFiltroSelect.vue";
import {useSolicitacaoForm} from "@/modules/solicitacoes/composables/useSolicitacaoForm.ts";
import {useRoute} from "vue-router";

const authStore = useAuthStore();
const {userSession, isAdmin, isGestor, isUsuario} = storeToRefs(authStore);

const usuarioAutenticado = userSession.value?.usuarioAutenticado;

const {toastError, toastSuccess} = useCustomToast();
const {
  preparaDialog,
  fecharDialog,
  salvar,
  abrirEdicao,
  idSelecionado,
  visible,
  modo,
  solicitacao,
  minhaSolicitacao,
  prioridadeList,
  statusList,
  usuarioList,
  tipoList,
} = useSolicitacaoForm({
  isGestor: isGestor.value,
  isAdmin: isAdmin.value,
  isUsuario: isUsuario.value,
  usuarioAutenticado: usuarioAutenticado!,
  mostrarMensagemErro: toastError,
  mostrarMensagemSucesso: toastSuccess,
  recarregarSolicitacoes: loadLazyData,
});

const solicitacaoStore = useSolicitacaoStore()


const {solicitacoes, erro, isLoading} = storeToRefs(solicitacaoStore);


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
  try {
    const {
      first,
      rows,
      sortField,
      sortOrder,
    } = lazyParams.value;

    await solicitacaoStore.carregarSolicitacoes({
      pagina: Math.floor(first / rows),
      tamanho: rows,
      ordenarPor: sortField,
      direcao: sortOrder === 1 ? "asc" : "desc",
      status: statusPesquisaSelecionada.value ?? undefined,
      prioridade: prioridadePesquisaSelecionada.value ?? undefined,
    });
  } catch (e) {
    let erro = obterMensagemErro(e)

    toastError(erro)
  }
}

//filtro
const statusPesquisaList = Object.values(StatusSolicitacao)
const prioridadePesquisaList = Object.values(PrioridadeSolicitacao)
const statusPesquisaSelecionada = ref<StatusSolicitacao | null>(null)
const prioridadePesquisaSelecionada = ref<PrioridadeSolicitacao | null>(null)


async function filtrarTabela() {
  try {
    lazyParams.value = {
      first: 0,
      rows: 10,
      sortField: "id",
      sortOrder: -1,
    }

    await loadLazyData()
  } catch (e) {
    let erro = obterMensagemErro(e)

    toastError(erro)
  }

}

const route = useRoute();
if (route.query.novaSolicitacao) {
  preparaDialog("cadastro")
}


onMounted(() => {
  loadLazyData()
})

</script>

<template>
  <container-page>
    <template #conteudo>

      <div class="flex items-center justify-between mb-4">
        <Titulo
            text="Solicitações"
            subtext="Consulte, filtre e acompanhe o fluxo de aprovação."
        />
        <Button label="Nova Solicitação" @click="preparaDialog('cadastro')"/>
      </div>


      <Message v-if="erro" severity="error" :closable="false">
        {{ erro }}
      </Message>


      <div v-else>

        <div class="grid grid-cols-2 gap-4 mb-4">

          <div>
            <SolicitacaoFiltroSelect :options="statusPesquisaList" :filtrar-tabela="filtrarTabela"
                                     :option-label="formatarStatusSolicitacao"
                                     v-model="statusPesquisaSelecionada"
                                     id="status"
                                     label="Status"/>
          </div>

          <div>
            <SolicitacaoFiltroSelect :options="prioridadePesquisaList" :filtrar-tabela="filtrarTabela"
                                     :option-label="formatarPrioridade"
                                     v-model="prioridadePesquisaSelecionada"
                                     id="prioridade"
                                     label="Prioridade"/>
          </div>

        </div>


        <TabelaSolicitacoes
            :isLoading="isLoading"
            :solicitacoes="solicitacoes?.dados?? []"
            :totalRegistros="solicitacoes?.totalDados??0"
            :lazyParams="lazyParams"
            :paginar="onPage"
            :ordenar="onSort"
            :editar="abrirEdicao"
        />

      </div>


      <SolicitacaoFormDialog :idSelecionado="idSelecionado"
                             :isAdmin="isAdmin" :isGestor="isGestor" :visible="visible" :modo="modo"
                             :solicitacao="solicitacao" :minhaSolicitacao="minhaSolicitacao"
                             :prioridadeList="prioridadeList" :statusList="statusList" :usuarioList="usuarioList"
                             :tipoList="tipoList"
                             :fechar-dialog="fecharDialog" :salvar="salvar"
      />

    </template>
  </container-page>


</template>
