export interface UseTipoFormOptions {
    mostrarMensagemErro: (mensagem: string) => void;
    mostrarMensagemSucesso: (mensagem: string) => void;
    recarregarTipos: () => Promise<void>;
}