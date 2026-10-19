<script setup lang="ts">
import Titulo from "@/shared/components/Titulo.vue";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import {useTiposStore} from "@/modules/tipos/stores/tipos.stores.ts";
import {onMounted, ref} from "vue";
import TabelaTipos from "@/modules/tipos/components/TabelaTipos.vue";
import Button from "primevue/button";
import axios from "axios";
import type {ErrorResponse, LazyParams} from "@/interfaces/interfaces.ts";
import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";
import TipoFormDialog from "@/modules/tipos/components/TipoFormDialog.vue";
import {storeToRefs} from "pinia";
import type {DataTablePageEvent, DataTableSortEvent} from "primevue/datatable";
import {ConfirmDialog, useConfirm} from "primevue";
import Message from "primevue/message";
import Loading from "@/shared/components/Loading.vue";
import type {TipoSolicitacaoRequest} from "@/modules/tipos/types/tipos.types.ts";
import Toast from "primevue/toast";
import {obterMensagemErro} from "@/shared/utils/store/mensagem.store.ts";


const {toastError, toastSuccess} = useCustomToast();

const tiposStore = useTiposStore()


const {tipos, erro, isLoading} = storeToRefs(tiposStore);

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
  const {
    first,
    rows,
    sortField,
    sortOrder,
  } = lazyParams.value;

  await tiposStore.carregarTipos({
    pagina: Math.floor(first / rows),
    tamanho: rows,
    ordenarPor: sortField,
    direcao: sortOrder === 1 ? "asc" : "desc",
  });
}

//SALVAR


const visible = ref(false);
const modo = ref<"cadastro" | "edicao">("cadastro");
const idSelecionado = ref<number | null>(null);
const tipoSolicitacao = ref<TipoSolicitacaoRequest>({
  nome: ""
})

const preparaDialog = (modoDialog: "cadastro" | "edicao") => {
  modo.value = modoDialog
  visible.value = true;
}

const fecharDialog = () => {
  visible.value = false;
  tipoSolicitacao.value.nome = ""
  idSelecionado.value = null
}

const salvar = async (modo: "cadastro" | "edicao") =>  {

  try {

    const nome = tipoSolicitacao.value.nome.trim();
    if (!nome) {
      toastError("Nome do Tipo da solicitacao não pode ser vazia!");
      return
    }

    const request: TipoSolicitacaoRequest = {nome}

    if (modo === "cadastro") {
      await tiposStore.criarTipo(request)

      toastSuccess("Criado com sucesso");
    } else {
      if (idSelecionado.value === null) {
        toastError("Registro não selecionado.")
        return
      }

      await tiposStore.atualizarTipo(idSelecionado.value, request)
      toastSuccess("Atualizado com sucesso");

    }

    fecharDialog()

    await loadLazyData()

  } catch (e) {
    let erro = obterMensagemErro(e)

    toastError(erro)
  }
}


//EDITARR


const abrirEdicao = async (id: number) => {

  if (id === null) {
    toastError("Registro não capturado")
    return
  }


  try {
    tipoSolicitacao.value = await tiposStore.buscarTipoPorId(id)
    idSelecionado.value = id
    preparaDialog("edicao")

  } catch (e) {
    let erro = obterMensagemErro(e)

    toastError(erro)
  }
}


//EXCLUIR


const confirm = useConfirm();


const confirmarExclusao = (id: number) => {
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
        await tiposStore.excluirTipo(id)

        toastSuccess('Registro deletado!')

      } catch (error) {

        let erroDeletar: string | undefined = "Não foi possível deletar o registro. Tente mais tarde.";
        if (axios.isAxiosError<ErrorResponse>(error)) {
          erroDeletar = error.response?.data.detail
        }

        toastError(erroDeletar)


      } finally {
        await loadLazyData()
      }
    },
  });
};

onMounted(() => {
  loadLazyData()
})
</script>

<template>
  <container-page>
    <template #conteudo>


      <div class="flex items-center justify-between">
        <Titulo
            text="Tipos de solicitação"
            subtext="Cadastre categorias usadas nas solicitações."
        />
        <Button label="Novo Tipo" @click="preparaDialog('cadastro')"/>
      </div>


      <Loading v-if="isLoading"/>

      <Message v-else-if="erro" severity="error" :closable="false">
        {{ erro }}
      </Message>

      <TabelaTipos v-else
                   :tipos="tipos?.dados?? []"
                   :totalRegistros="tipos?.totalDados??0"
                   :lazyParams="lazyParams"
                   :paginar="onPage"
                   :ordenar="onSort"
                   :editar="abrirEdicao"
                   :excluir="confirmarExclusao"
      />

      <TipoFormDialog :fechar-dialog="fecharDialog" :salvar="salvar" :tipoSolicitacao="tipoSolicitacao"
                      :visible="visible" :modo="modo"/>

      <Toast/>
      <ConfirmDialog/>

    </template>
  </container-page>
</template>
