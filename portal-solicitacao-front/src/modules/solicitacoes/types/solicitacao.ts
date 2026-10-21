import {PrioridadeSolicitacao, StatusSolicitacao} from "@/enums/enums.ts";
import type {Usuario, UsuarioResumido} from "@/modules/usuarios/types/usuario.ts";
import type {TipoSolicitacao} from "@/modules/tipos/types/tipos.ts";




export interface Solicitacao {
    id: number
    titulo: string
    descricao: string
    prioridade: PrioridadeSolicitacao
    status: StatusSolicitacao
    dataCriacao: string
    usuario: UsuarioResumido
    tipo: TipoSolicitacao
}

export interface SolicitacaoRequest {
    titulo: string
    descricao: string
    prioridade: string
    status: StatusSolicitacao
    usuario: Usuario | null
    tipo: TipoSolicitacao | null
}
