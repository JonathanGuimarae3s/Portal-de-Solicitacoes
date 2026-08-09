<script lang="ts" setup>

import MenuItem from "./MenuItem.vue";

</script>

<template>

    <nav class="flex flex-col">
        <MenuItem label="Dashboard" to="/" />
        <MenuItem label="Solicitações" to="/solicitacoes" />
        <MenuItem label="Usuários" to="/usuarios" />
    </nav>

</template>
