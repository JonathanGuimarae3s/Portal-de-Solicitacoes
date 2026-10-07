import {
    createRouter,
    createWebHistory
} from "vue-router";

import { useAuthStore } from "@/modules/auth/stores/auth";
import { routes } from "./routes";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
});

router.beforeEach((to) => {
    const authStore = useAuthStore();

    // 1. Proteção por autenticação
    if (
        to.name !== "Login" &&
        to.meta.requiresAuth &&
        !authStore.isAuthenticated
    ) {
        return {
            name: "Login",
            query: { redirect: to.fullPath }
        };
    }

    // 2. Proteção por perfil
    const perfisPermitidos = to.meta.perfisPermitidos;
    const perfilUsuario = authStore.perfil;

    if (
        perfisPermitidos?.length &&
        (!perfilUsuario ||
            !perfisPermitidos.includes(perfilUsuario))
    ) {
        return {
            name: "Dashboard",
            query: { acessoNegado: "true" }
        };
    }

    // 3. Usuário autenticado não retorna ao login
    if (
        authStore.isAuthenticated &&
        to.name === "Login"
    ) {
        return { name: "Dashboard" };
    }


});

export default router;