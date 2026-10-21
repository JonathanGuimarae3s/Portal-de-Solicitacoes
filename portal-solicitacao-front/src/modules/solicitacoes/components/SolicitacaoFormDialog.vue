<script setup lang="ts">
import Button from "primevue/button"
import Dialog from "primevue/dialog"
import type {Usuario} from "@/modules/usuarios/types/usuario.ts";
import {computed} from 'vue';
import {Form, type FormSubmitEvent} from "@primevue/forms";
import CampoInput from "@/shared/components/CampoInput.vue";
import Textarea from "primevue/textarea";
import type {ErrosFormulario} from "@/modules/solicitacoes/types/errosFormularioSolicitacao.ts";
import type {SolicitacaoRequest} from "@/modules/solicitacoes/types/solicitacao.ts";
import type {TipoSolicitacao} from "@/modules/tipos/types/tipos.ts";
import {Select} from "primevue";
import {PrioridadeSolicitacao, StatusSolicitacao} from "@/enums/enums.ts";
import {formatarPrioridade} from "@/shared/utils/mappers/PrioridadeSolicitacaoUtils.ts";
import {formatarStatusSolicitacao} from "@/shared/utils/mappers/StatusUtils.ts";


interface iProps {
  salvar: (modo: "cadastro" | "edicao", solicitacaoRequest: SolicitacaoRequest) => void | Promise<void>,
  fecharDialog: () => void;
  modo: "cadastro" | "edicao";
  solicitacao: SolicitacaoRequest,
  usuarioList: Usuario[] | undefined;
  tipoList: TipoSolicitacao[] | undefined;
  prioridadeList: PrioridadeSolicitacao[];
  statusList: StatusSolicitacao[] | [];
  visible: boolean;
  isAdmin: boolean;
  isGestor: boolean
  minhaSolicitacao: boolean;
}

const props = defineProps<iProps>();


const initialValues = computed<SolicitacaoRequest>(() => ({
      titulo: props.solicitacao?.titulo,
      descricao: props.solicitacao?.descricao,
      prioridade: props.solicitacao?.prioridade,
      status: props.solicitacao?.status,
      usuario: props.solicitacao?.usuario,
      tipo: props.solicitacao?.tipo
    })
);

const resolver = ({values}: Record<string, any>) => {
  const errors: ErrosFormulario = {};

  if (!values.titulo) {
    errors.titulo = [{message: 'Título é obrigatório!'}];
  }

  if (!values.descricao) {
    errors.descricao = [{message: 'Descricão é obrigatória!'}];
  }

  if (!values.usuario) {
    errors.usuario = [{message: 'Usuário é obrigatório!'}];
  }

  if (!values.prioridade) {
    errors.prioridade = [{message: 'Prioridade é obrigatório!'}];
  }

  if (!values.status) {
    errors.status = [{message: 'Status é obrigatório!'}];
  }
  if (!values.tipo) {
    errors.tipo = [{message: 'Tipo é obrigatório!'}];
  }


  return {
    values,
    errors
  };
};

const enviar = (event: FormSubmitEvent) => {


  if (!event.valid) {
    return;
  }

  props.salvar(
      props.modo,
      event.values as SolicitacaoRequest
  );
};


const desabilitaCampo = () => {

  if (props.modo == "edicao") {

    return !props.isAdmin && !props.isGestor;
  }

  return false

}

const desabilitaEdicaoCampoTexto = () => {

  if ("cadastro" != props.modo && !props.minhaSolicitacao) {
    return true
  }

}


</script>

<template>
  <div class="card flex justify-content-center">
    <Dialog :visible="visible" modal :style="{ width: '50rem' }" :closable="false">


      <template #header>
        <div class="inline-flex align-items-center justify-content-center gap-2">
          <span v-if="modo == 'cadastro'"
                class="font-bold white-space-nowrap">Nova Solicitacao</span>

          <span v-if="modo == 'edicao'"
                class="font-bold white-space-nowrap">Atualizando solicitacao</span>
        </div>
      </template>

      <Form v-slot="$form" :initialValues :resolver @submit="enviar">

        <CampoInput label="Título" type="text" campo="titulo" :message="$form.titulo?.error?.message ?? ''"
                   :invalid="$form.titulo?.invalid ?? false" :disabled="desabilitaEdicaoCampoTexto()"/>

        <CampoInput label="Descricao" campo="descricao" :message="$form.descricao?.error?.message ?? ''"
                   :invalid="$form.descricao?.invalid ?? false">

          <template #input>

            <Textarea id="descricao" name="descricao" class="w-full mt-2" placeholder="Descreva a solicitacao"
                      :disabled="desabilitaEdicaoCampoTexto()"/>

          </template>

        </CampoInput>

        <CampoInput label="Prioridade" campo="prioridade" :message="$form.prioridade?.error?.message ?? ''"
                   :invalid="$form.prioridade?.invalid ?? false">

          <template #input>

            <Select class="mt-2" id="prioridade" name="prioridade" :options="prioridadeList"
                    :optionLabel="formatarPrioridade"
                    placeholder="Selecione a prioridade" :disabled="desabilitaCampo()"
                    fluid
            />

          </template>

        </CampoInput>


        <CampoInput label="Status" campo="status" :message="$form.status?.error?.message ?? ''"
                   v-if="modo=='edicao'"
                   :invalid="$form.status?.invalid ?? false">

          <template #input>

            <Select class="mt-2" id="status" name="status" :options="statusList"
                    :optionLabel="formatarStatusSolicitacao"
                    placeholder="Selecione o status" :disabled="desabilitaCampo()"
                    fluid
            />

          </template>

        </CampoInput>

        <CampoInput label="Solicitante" campo="usuario" :message="$form.usuario?.error?.message ?? ''"
                   :invalid="$form.usuario?.invalid ?? false">

          <template #input>

            <Select class="mt-2" id="usuario" name="usuario" :options="usuarioList" optionLabel="nome"
                    placeholder="Selecione o Solicitante" :disabled="modo=='edicao'"
                    fluid
            />

          </template>

        </CampoInput>

        <CampoInput
            label="Tipo de Solicitação"
            campo="tipo"
            :message="$form.tipo?.error?.message ?? ''"
            :invalid="$form.tipo?.invalid ?? false"
        >
          <template #input>
            <Select
                id="tipo"
                name="tipo"
                :options="tipoList"
                optionLabel="nome"
                placeholder="Selecione o tipo da solicitação"
                fluid
                class="mt-2"
                :disabled="desabilitaCampo()"
            />
          </template>
        </CampoInput>

        <div class="  flex align-items-center justify-content-center gap-2  ">

          <Button label="Cancelar" outlined severity="secondary" @click="fecharDialog" fluid/>
          <Button label="Salvar" outlined severity="success" type="submit" autofocus fluid/>

        </div>


      </Form>


    </Dialog>
  </div>


</template>