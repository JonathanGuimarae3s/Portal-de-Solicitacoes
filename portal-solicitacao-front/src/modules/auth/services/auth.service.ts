import api from "@/core/http/api";
import type { UserLogin } from "@/modules/auth/interfaces/UserLogin";
import type { SessaoUsuario } from "@/modules/auth/interfaces/SessaoUsuario.ts";

export async function login(credenciais: UserLogin): Promise<SessaoUsuario> {
    const response = await api.post<SessaoUsuario>("/auth/login", {
        email: credenciais.email,
        senha: credenciais.password,
    });

    return response.data;
}
