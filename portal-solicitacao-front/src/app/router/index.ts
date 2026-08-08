import {createRouter, createWebHistory} from "vue-router";
import {routes} from "./routes.js";
import {useAuthStore} from "../modules/auth/stores/auth.ts";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: routes,
});

router.beforeEach(async (to) => {
    const authStore = useAuthStore();


    if (
        to.name !== "Auth" &&
        to.meta.requiresAuth &&
        !authStore.isAuthenticated
    ) {
        return {name: "Auth", query: {redirect: to.fullPath}};
    }

    if (authStore.isAuthenticated && to.name === "Auth") {
        const redirect = (to.query.redirect as string) ?? {name: "Home"};
        return redirect;
    }
});

export default router;
