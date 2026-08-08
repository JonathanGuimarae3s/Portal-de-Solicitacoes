import {defineStore} from "pinia";
import {computed, ref} from "vue";
import api, {defineToken} from "@/plugins/axios";
import type {UserSession} from "@/app/modules/auth/model/UserSession.ts";

export const useAuthStore = defineStore("auth", () => {
    const isLoading = ref(false);

    const userSession = ref<UserSession | null>(null);

    const isAuthenticated = computed(() => !!userSession.value);

    async function login(email: string, password: string) {
        try {
            isLoading.value = true;

            const data = {
                email: email,
                senha: password,
            };

            const response = await api.post(
                `/auth/login`,
                data,
            );

            userSession.value = response.data;
            defineToken(response.data.token);

        } finally {
            isLoading.value = false;
        }
    }

    async function logout() {
        try {
            isLoading.value = true;
            await new Promise((resolve) => setTimeout(resolve, 1000));
            userSession.value = null;
            defineToken(null);
        } finally {
            isLoading.value = false;
        }
    }

    return {isLoading, userSession, isAuthenticated, login, logout};
});
