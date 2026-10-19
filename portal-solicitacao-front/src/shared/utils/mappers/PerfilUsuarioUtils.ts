import {PerfilUsuario} from "@/enums/enums.ts";

const PerfilUsuarioDescricao: Record<PerfilUsuario, string> = {
    [PerfilUsuario.ADMIN]: "Administrador",
    [PerfilUsuario.GESTOR]: "Gestor",
    [PerfilUsuario.USUARIO]: "Usuário",

};

export function formatarPerfilUsuario(setor: PerfilUsuario): string {
    return PerfilUsuarioDescricao[setor] || 'Perfil de usuário desconhecido';
}