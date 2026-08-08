<script setup lang="ts">

interface TituloProps {
    text: string;
    subtext?: string;
}
defineProps<TituloProps>();

</script>


<template>
    <div>

        <h1 class="text-lg">
            {{ text }}
        </h1>
        <h2 v-if="subtext" class="text-sm text-zinc-500">
            {{ subtext }}
        </h2>
    </div>
</template>