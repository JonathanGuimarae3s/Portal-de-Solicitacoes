import {type PerfilUsuario, Setor} from "@/enums/enums.ts";

export interface UsuarioAutenticado {
    id: number;
    nome: string;
    email: string;
    perfil: PerfilUsuario;
    setor: Setor;
}