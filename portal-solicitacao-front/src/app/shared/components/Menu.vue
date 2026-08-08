<script lang="ts" setup>

import MenuItem from "./MenuItem.vue";

</script>

<template>

    <nav class="flex flex-col">
        <MenuItem label="Home" to="/" />
        <MenuItem label="Solicitacoes" to="/counter" />
         <MenuItem label="Usuarios" to="/counterPinia" />
    </nav>

</template>