import type { RouteRecordRaw } from "vue-router";

export const routes: RouteRecordRaw[] = [
  {
    name: "Auth",
    path: "/auth",
    component: () => import("@/modules/auth/pages/AuthPage.vue"),
  },
  {
    path: "/",
    component: () => import("@/shared/layouts/AppLayout.vue"),
    meta: { requiresAuth: true },
    children: [
      {
        name: "Dashboard",
        path: "",
        component: () => import("@/modules/dashboard/pages/DashboardPage.vue"),
      },
      {
        name: "Solicitacoes",
        path: "solicitacoes",
        component: () => import("@/modules/solicitacoes/pages/SolicitacoesPage.vue"),
      },
      {
        name: "Usuarios",
        path: "usuarios",
        component: () => import("@/modules/usuarios/pages/UsuariosPage.vue"),
      },
    ],
  },
];
