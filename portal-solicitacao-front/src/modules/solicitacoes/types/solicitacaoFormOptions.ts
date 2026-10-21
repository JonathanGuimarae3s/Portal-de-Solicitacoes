import type {UsuarioAutenticado} from "@/modules/auth/types/UsuarioAutenticado.ts";

export interface UseSolicitacaoFormOptions {
    isGestor: boolean;
    isAdmin: boolean;
    isUsuario: boolean;
    usuarioAutenticado: UsuarioAutenticado;
    mostrarMensagemErro: (mensagem: string) => void;
    mostrarMensagemSucesso: (mensagem: string) => void;
    recarregarSolicitacoes: () => Promise<void>;
}