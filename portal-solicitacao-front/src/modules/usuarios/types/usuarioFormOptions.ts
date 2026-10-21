import type {Setor} from "@/enums/enums.ts";

export interface UseUsuarioFormOptions {
    isAdmin: boolean;
    setorUsuarioAutenticado: Setor;
    mostrarMensagemErro: (mensagem: string) => void;
    mostrarMensagemSucesso: (mensagem: string) => void;
    recarregarUsuarios: () => Promise<void>;
}
