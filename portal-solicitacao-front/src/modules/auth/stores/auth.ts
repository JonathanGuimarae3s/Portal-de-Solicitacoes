import {defineStore} from "pinia";
import {computed, ref} from "vue";
import {settaAuthToken} from "@/core/http/api";
import {login} from "../services/auth.service";
import type {SessaoUsuario} from "@/modules/auth/types/SessaoUsuario.ts";
import {PerfilUsuario} from "@/enums/enums.ts";
import type {UserLogin} from "@/modules/auth/types/UserLogin.ts";

export const useAuthStore = defineStore("auth", () => {
    const isLoading = ref(false);

    const userSession = ref<SessaoUsuario | null>(null);

    const isAuthenticated = computed(() => !!userSession.value);

    const perfil = computed(
        () => userSession.value?.usuarioAutenticado.perfil
    );

    const isAdmin = computed(
        () => perfil.value === PerfilUsuario.ADMIN
    );

    const isGestor = computed(
        () => perfil.value === PerfilUsuario.GESTOR
    );

    const isUsuario = computed(
        () => perfil.value === PerfilUsuario.USUARIO
    );


    async function autenticar(userLogin: UserLogin) {

        try {
            isLoading.value = true;

            const session = await login(userLogin);
            userSession.value = session;

            calculaExpiracao(userSession.value);

            sessionStorage.setItem("sessaoUsuario", JSON.stringify(userSession.value))

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
            sessionStorage.removeItem("sessaoUsuario")
        } finally {
            isLoading.value = false;
        }
    }


    function restaurarSessao() {

        try {
            const sessaoUsuario: string | null = sessionStorage.getItem("sessaoUsuario")

            if (sessaoUsuario != null) {
                const user = JSON.parse(sessaoUsuario);

                if (user.token != null && user?.usuarioAutenticado != null && Date.now() < user.expiresInMillis) {
                    userSession.value = user
                    settaAuthToken(userSession.value!.token);
                    return
                }

                limparVariaveisSessao()


                return;

            }

        } catch {
            limparVariaveisSessao();
        }
    }

    function limparVariaveisSessao() {
        userSession.value = null;
        settaAuthToken(null);
        sessionStorage.removeItem("sessaoUsuario")
    }

    function calculaExpiracao(user: SessaoUsuario) {
        const expireat = Date.now() + user.expiresInMillis
        user.expiresInMillis = expireat;
    }

    return {
        isLoading,
        userSession,
        isAuthenticated,
        perfil,
        isAdmin,
        isGestor,
        isUsuario,

        autenticar,
        logout,
        restaurarSessao,
        limparVariaveisSessao
    };
});
