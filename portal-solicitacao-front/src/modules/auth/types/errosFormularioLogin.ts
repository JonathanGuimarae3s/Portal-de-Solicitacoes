import type {UserLogin} from "@/modules/auth/types/UserLogin.ts";

export type ErrosFormularioLogin = Partial<
    Record<keyof UserLogin, Array<{ message: string }>>
>;