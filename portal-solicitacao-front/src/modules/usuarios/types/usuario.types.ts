import {type PerfilUsuario, Setor} from "@/enums/enums.ts";

export interface Usuario {
    id: number
    nome: string
    email: string
    setor: string
    ativo: boolean
    perfil:string
}

export interface UsuarioRequest {
    nome: string,
    email: string,
    senha: string,
    perfil: PerfilUsuario,
    setor: Setor
}
