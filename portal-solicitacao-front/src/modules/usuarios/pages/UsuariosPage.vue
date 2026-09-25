<script setup lang="ts">

import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";
import {storeToRefs} from "pinia";
import {useUsuarioStore} from "@/modules/usuarios/stores/usuarios.stores.ts";
import {onMounted, ref} from "vue";
import type {ErrorResponse, LazyParams} from "@/interfaces/interfaces.ts";
import type {DataTablePageEvent, DataTableSortEvent} from "primevue/datatable";
import axios from "axios";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import TabelaUsuarios from "@/modules/usuarios/components/TabelaUsuarios.vue";
import Loading from "@/shared/components/Loading.vue";
import {Button, ConfirmDialog, useConfirm} from "primevue";
import Titulo from "@/shared/components/Titulo.vue";
import UsuarioFormDialog from "@/modules/usuarios/components/UsuarioFormDialog.vue";
import {obterMensagemErro} from "@/utils/mensagem.store.ts";
import type {UsuarioRequest} from "@/modules/usuarios/types/usuario.types.ts";
import {PerfilUsuario, Setor} from "@/enums/enums.ts";

const {toastError, toastSuccess} = useCustomToast();

const usuarioStore = useUsuarioStore()


const {usuarios, erro, isLoading} = storeToRefs(usuarioStore);


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

  await usuarioStore.carregarUsuarios({
    pagina: Math.floor(first / rows),
    tamanho: rows,
    ordenarPor: sortField,
    direcao: sortOrder === 1 ? "asc" : "desc",
  });
}


//salvar

//cadastroo


const visible = ref(false);
const modo = ref<"cadastro" | "edicao">("cadastro");
const idSelecionado = ref<number | null>(null);
const usuario = ref<UsuarioRequest>({

  nome: "",
  email: "",
  setor: Setor.ATENDIMENTO,
  senha: "",
  perfil: PerfilUsuario.USUARIO,

})

const preparaDialog = (modoDialog: "cadastro" | "edicao") => {

  modo.value = modoDialog
  visible.value = true;
}

const fecharDialog = () => {
  visible.value = false;
  usuario.value = {
    nome: "",
    email: "",
    setor: Setor.ATENDIMENTO,
    senha: "",
    perfil: PerfilUsuario.USUARIO,

  }

  idSelecionado.value = null
}


//salvar

const salvar = async (modo: "cadastro" | "edicao") => {

  try {

    if (modo === "cadastro") {
      await usuarioStore.criarUsuario(usuario.value)

      toastSuccess("Criado com sucesso");
    } else {
      if (idSelecionado.value === null) {
        toastError("Registro não selecionado.")
        return
      }

      await usuarioStore.atualizarUsuario(idSelecionado.value, usuario.value)

      toastSuccess("Atualizado com sucesso");


    }

    fecharDialog()

    await loadLazyData()

  } catch (e) {
    let erro = obterMensagemErro(e)

    toastError(erro)
  }
}

// editar


const abrirEdicao = async (id: number) => {

  if (id === null) {
    toastError("Registro não capturado")
    return
  }


  try {
    const usuarioEdicao = await usuarioStore.buscarUsuarioPorId(id)

    usuario.value = {
      nome: usuarioEdicao.nome,
      email: usuarioEdicao.email,
      setor: Setor[usuarioEdicao.setor as keyof typeof Setor],
      senha: "",
      perfil: PerfilUsuario[usuarioEdicao.perfil as keyof typeof PerfilUsuario],
    }



    idSelecionado.value = id
    preparaDialog("edicao")

  } catch (e) {
    let erro = obterMensagemErro(e)

    toastError(erro)
  }
}


//ativar /INATIVAR


const confirm = useConfirm();


const inativarAtivar = (id: number, nome: String, ativo: boolean) => {
  const message = ativo ? `Inativar o usuário: ${nome} ?` : `Ativar o usuário: ${nome} ?`
  const acceptLabel = ativo ? `Inativar` : `Ativar`
  const acceptClass = ativo ? 'p-button-danger' : 'p-button-success'

  confirm.require({
    message: message,
    header: 'Atencão!',
    icon: 'pi pi-info-circle',
    rejectLabel: 'Cancelar',
    acceptLabel: acceptLabel,
    rejectClass: 'p-button-secondary p-button-outlined',
    acceptClass: acceptClass,
    accept: async () => {


      try {
        await usuarioStore.ativarInativarUsuario(id, ativo)

        const messageSucces = ativo ? "Usuário inativado com sucesso!" : "Usuário ativado com sucesso!"


        toastSuccess(messageSucces)
        await loadLazyData()

      } catch (error) {
        let erroDeletar: string | undefined = ativo ? "Não foi possível inativar o registro. Tente mais tarde." : "Não foi possível ativar o registro. Tente mais tarde.";
        if (axios.isAxiosError<ErrorResponse>(error)) {
          erroDeletar = error.response?.data.detail
        }

        toastError(erroDeletar)


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
            text="Usuários"
            subtext="Gerencie perfis, setores e status de acesso."
        />
        <Button label="Novo Usuário" @click="preparaDialog('cadastro')"/>
      </div>


      <Loading v-if="isLoading"/>

      <Message v-else-if="erro" severity="error" :closable="false">
        {{ erro }}
      </Message>

      <TabelaUsuarios v-else
                      :tipos="usuarios?.dados?? []"
                      :totalRegistros="usuarios?.totalDados??0"
                      :lazyParams="lazyParams"
                      :paginar="onPage"
                      :ordenar="onSort"
                      :editar="abrirEdicao"
                      :inativarAtivar="inativarAtivar"/>


      <UsuarioFormDialog :fechar-dialog="fecharDialog" :salvar="salvar" :usuario="usuario"
                         :visible="visible" :modo="modo"/>


      <ConfirmDialog/>


    </template>
  </container-page>
</template>
