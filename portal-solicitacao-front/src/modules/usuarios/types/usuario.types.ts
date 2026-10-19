import {type PerfilUsuario, Setor} from "@/enums/enums.ts";
import type {TipoSolicitacao} from "@/modules/tipos/types/tipos.types.ts";

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

export interface UsuarioResumido {
    "id": number,
    "nome": string,
    "email": string,
    "setor": TipoSolicitacao
}