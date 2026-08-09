import type { UsuarioAutenticado } from "./UsuarioAutenticado";

export interface SessaoUsuario {
    token: string;
    expiresInMillis: number;
    usuarioAutenticado: UsuarioAutenticado;

}
