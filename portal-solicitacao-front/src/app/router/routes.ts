import type { RouteRecordRaw } from "vue-router";
import AppLayout from "../shared/layouts/AppLayout.vue";
import HomePage from "../modules/samples/pages/HomePage.vue";
import ContadorPage from "../modules/samples/pages/ContadorPage.vue";
import AuthPage from "../modules/auth/pages/AuthPage.vue";
import ContadorPinia from "../modules/samples/pages/ContadorPinia.vue";

export const routes: RouteRecordRaw[] = [
  {
    name: "Auth",
    path: "/auth",
    component: AuthPage,
  },
  {
    path: "/",
    component: AppLayout,
    meta: { requiresAuth: true },
    children: [
      {
        name: "Home",
        path: "",
        component: HomePage,
      },
      {
        name: "Counter",
        path: "counter",
        component: ContadorPage,
      },
      {
        name: "CounterPinia",
        path: "counterPinia",
        component: ContadorPinia,
      },
    ],
  },
];
