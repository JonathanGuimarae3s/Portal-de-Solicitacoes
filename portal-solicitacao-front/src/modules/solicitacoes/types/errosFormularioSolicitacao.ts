import type {Solicitacao, SolicitacaoRequest} from "@/modules/solicitacoes/types/solicitacao.ts";

export type ErrosFormulario = Partial<
    Record<keyof SolicitacaoRequest, Array<{ message: string }>>
>;