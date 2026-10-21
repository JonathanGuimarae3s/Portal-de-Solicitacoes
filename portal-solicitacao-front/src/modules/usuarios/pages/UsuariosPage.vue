<script setup lang="ts">

import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";
import {storeToRefs} from "pinia";
import {useUsuarioStore} from "@/modules/usuarios/stores/usuarios.stores.ts";
import {onMounted, ref} from "vue";
import type {LazyParams} from "@/types/interfaces.ts";
import type {DataTablePageEvent, DataTableSortEvent} from "primevue/datatable";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import TabelaUsuarios from "@/modules/usuarios/components/TabelaUsuarios.vue";
import {Button, ConfirmDialog, useConfirm} from "primevue";
import Titulo from "@/shared/components/Titulo.vue";
import UsuarioFormDialog from "@/modules/usuarios/components/UsuarioFormDialog.vue";
import {useAuthStore} from "@/modules/auth/stores/auth.ts";
import {obterMensagemErro} from "@/shared/utils/store/mensagem.store.ts";
import {useUsuarioForm} from "@/modules/usuarios/composables/useUsuarioForm.ts";
import Message from "primevue/message"

const authStore = useAuthStore();
const {userSession, isAdmin, isGestor} = storeToRefs(authStore);

const usuarioAutenticado = userSession.value?.usuarioAutenticado;
const setorUsuarioAutenticado = ref(usuarioAutenticado!.setor)

const {toastError, toastSuccess} = useCustomToast();

const usuarioStore = useUsuarioStore()


const {usuarios, erro, isLoading,} = storeToRefs(usuarioStore);

const {
  preparaDialog,
  fecharDialog,
  abrirEdicao,
  salvar,
  visible,
  modo,
  usuario,
  listaPerfisUsuario,
  listaSetores,
} = useUsuarioForm({
  isAdmin: isAdmin.value,
  setorUsuarioAutenticado: usuarioAutenticado!.setor,
  mostrarMensagemErro: toastError,
  mostrarMensagemSucesso: toastSuccess,
  recarregarUsuarios: loadLazyData,
});

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


//ativar /INATIVAR


const confirm = useConfirm();


const inativarAtivar = (id: number, nome: String, ativo: boolean) => {
  const message = ativo ? `Inativar o usuário: ${nome} ?` : `Ativar o usuário: ${nome} ?`
  const acceptLabel = ativo ? `Inativar` : `Ativar`
  const acceptClass = ativo ? 'p-button-danger' : 'p-button-success'

  confirm.require({
    message: message,
    header: 'Atenção!',
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

        if (error != null) {
          erroDeletar = obterMensagemErro(error)
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
        <Button v-if="isAdmin || isGestor" label="Novo Usuário" @click="preparaDialog('cadastro')"/>
      </div>


      <Message v-if="erro" severity="error" :closable="false">
        {{ erro }}
      </Message>

      <TabelaUsuarios v-else
                      :isLoading="isLoading"
                      :usuarios="usuarios?.dados?? []"
                      :totalRegistros="usuarios?.totalDados??0"
                      :lazyParams="lazyParams"
                      :paginar="onPage"
                      :ordenar="onSort"
                      :editar="abrirEdicao"
                      :inativarAtivar="inativarAtivar"
                      :podeGerenciar="isAdmin"/>


      <UsuarioFormDialog :fechar-dialog="fecharDialog" :salvar="salvar" :usuario="usuario" :isAdmin="isAdmin"
                         :visible="visible" :modo="modo" :perfisUsuario="listaPerfisUsuario" :setores="listaSetores"/>


      <ConfirmDialog :closable="false"/>


    </template>
  </container-page>
</template>
