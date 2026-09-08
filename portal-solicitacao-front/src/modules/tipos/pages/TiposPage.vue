<script setup lang="ts">
import Titulo from "@/shared/components/Titulo.vue";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import {type TipoSolitacaoRequest, useTiposStores} from "@/modules/tipos/stores/tipos.stores.ts";
import {ref} from "vue";
import TabelaTipos from "@/modules/tipos/components/TabelaTipos.vue";
import Button from "primevue/button";
import axios from "axios";
import type {ErrorResponse} from "@/interfaces/interfaces.ts";
import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";
import DialogCadastroTipo from "@/modules/tipos/pages/DialogCadastroTipo.vue";

const {toastError, toastSuccess} = useCustomToast();

const tiposStore = useTiposStores()

const tabelaTipoRef = ref(null)


const visible = ref(false);

const preparaDialog = () => {

  visible.value = true;
}

const fecharDialog = () => {
  visible.value = false;
}


const tipoSolicitacao = ref<TipoSolitacaoRequest>({
  nome: ""
})

const salvar = async () => {

  try {

    if (tipoSolicitacao.value.nome == "") {
      toastError("Nome do Tipo da solicitacao não pode ser vazia!");
    }

    await tiposStore.salvarTipo(tipoSolicitacao.value)

    toastSuccess("Criado com sucesso");
    visible.value = false;
    await tabelaTipoRef.value?.loadLazyData()
  } catch (e) {
    if (axios.isAxiosError<ErrorResponse>(e)) {
      let erro =
          e.response?.data.userMessage ??
          "Não foi possível conectar ao servidor.";

      toastError(erro)
    }
  }
}

</script>

<template>
  <container-page>
    <template #conteudo>


      <div class="flex items-center justify-between">
        <Titulo
            text="Tipos de solicitação"
            subtext="Cadastre categorias usadas nas solicitações."
        />
        <Button label="Novo Tipo" @click="preparaDialog()"/>
      </div>

      <TabelaTipos ref="tabelaTipoRef"/>


      <DialogCadastroTipo :fechar-dialog="fecharDialog" :salvar="salvar" :tipoSolicitacao="tipoSolicitacao"
                    :visible="visible"/>


    </template>
  </container-page>
</template>
