import type { PerfilUsuario } from "@/enums/enums";

declare module "vue-router" {
    interface RouteMeta {
        requiresAuth?: boolean;
        perfisPermitidos?: PerfilUsuario[];
    }
}

export {};