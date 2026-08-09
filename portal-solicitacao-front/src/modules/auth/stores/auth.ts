import { defineStore } from "pinia";
import { computed, ref } from "vue";
import { settaAuthToken } from "@/core/http/api";
import { login } from "../services/auth.service";
import type { SessaoUsuario } from "@/modules/auth/interfaces/SessaoUsuario.ts";

export const useAuthStore = defineStore("auth", () => {
    const isLoading = ref(false);

    const userSession = ref<SessaoUsuario | null>(null);

    const isAuthenticated = computed(() => !!userSession.value);

    async function autenticar(email: string, password: string) {
        try {
            isLoading.value = true;

            const session = await login({ email, password });
            userSession.value = session;
            settaAuthToken(session.token);

        } finally {
            isLoading.value = false;
        }
    }

    async function logout() {
        try {
            isLoading.value = true;
            userSession.value = null;
            settaAuthToken(null);
        } finally {
            isLoading.value = false;
        }
    }

    return {isLoading, userSession, isAuthenticated,  autenticar, logout};
});
