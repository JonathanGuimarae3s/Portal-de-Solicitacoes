import {type PerfilUsuario, Setor} from "@/enums/enums.ts";

export interface UsuarioAutenticado {
    nome: string;
    email: string;
    perfil: PerfilUsuario;
    setor: Setor;
}