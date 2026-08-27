<script setup lang="ts">


interface IconProps {
  icon: string;
  estilo?: Record<string, string | number>;
}

defineProps<IconProps>();

</script>

<template>
  <i :class="icon" :style="estilo"></i>
</template>