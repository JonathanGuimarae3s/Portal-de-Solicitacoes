import type {UsuarioAutenticado} from "@/app/modules/auth/model/UsuarioAutenticado.ts";

export interface UserSession {
    token: string;
    expiresInMillis: number;
    usuarioAutenticado: UsuarioAutenticado;

}