<script setup lang="ts">
import Titulo from '@/app/shared/components/Titulo.vue';
import { ref } from 'vue';

const count = ref(1000);


function incrementar() {
    count.value++;

}


function decrementar() {
    count.value--;

}

function resetar() {
    count.value = 0;

}
</script>


<template>
    <div class="flex flex-col gap-4">
        <Titulo text="Contador" subtext="exempod de contador vue 3" />

        <div>
            <span class="text-4xl font-black ">
                {{ count }}
            </span>

            <div class="flex gap-3">
                <button class="btn btn-primary" @click="incrementar()">+</button>
                <button class="btn btn-secondary" @click="decrementar()">-</button>
                <button class="btn btn-danger" @click="resetar()">Resetar</button>
            </div>
        </div>
    </div>
</template>
