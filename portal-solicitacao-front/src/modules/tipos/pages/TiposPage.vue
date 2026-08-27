<script setup lang="ts">
import Titulo from "@/shared/components/Titulo.vue";
import ContainerPage from "@/shared/components/ContainerPage.vue";
import {useTiposStores} from "@/modules/tipos/stores/tipos.stores.ts";
import {onMounted} from "vue";
import TabelaTipos from "@/modules/tipos/components/TabelaTipos.vue";


const tiposStore = useTiposStores()


onMounted(() => {
  tiposStore.carregaTipos({
    pagina: 0,
    tamanho: 10,
    ordenarPor: "id",
    direcao: "asc",
  })
})


</script>

<template>
  <container-page>
    <template #conteudo>

      <Titulo
          text="Tipos de solicitação"
          subtext="Cadastre categorias usadas nas solicitações."
      />


      <TabelaTipos/>


    </template>
  </container-page>
</template>
