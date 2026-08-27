import {createRouter, createWebHistory} from "vue-router";
import {useAuthStore} from "@/modules/auth/stores/auth";
import {routes} from "./routes";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
});

router.beforeEach((to) => {
    const authStore = useAuthStore();


    if (
        to.name !== "Login" &&
        to.meta.requiresAuth &&
        !authStore.isAuthenticated
    ) {
        return {name: "Login", query: {redirect: to.fullPath}};
    }

    if (authStore.isAuthenticated && to.name === "Login") {
        const redirect = to.query.redirect;
        return typeof redirect === "string" && redirect.startsWith("/")
            ? redirect
            : {name: "Dashboard"};
    }

});

export default router;
