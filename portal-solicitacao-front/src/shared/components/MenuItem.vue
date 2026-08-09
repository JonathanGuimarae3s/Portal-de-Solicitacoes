<script setup lang="ts">

interface MenuItemProps {
    label: string;
    to: string;

}

defineProps<MenuItemProps>();


</script>

<template>

    <RouterLink :to="to" class="px-4 py-2 text-sm text-zinc-300 hover:text-white hover:bg-white/5 rounded-md">
        {{ label }}
    </RouterLink>

</template>