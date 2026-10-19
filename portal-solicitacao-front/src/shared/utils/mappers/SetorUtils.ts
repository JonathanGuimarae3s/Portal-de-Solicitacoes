import {Setor} from "@/enums/enums.ts";

const SetorDescricao: Record<Setor, string> = {
    [Setor.ATENDIMENTO]: "Atendimento",
    [Setor.COMERCIAL]: "Comercial",
    [Setor.COMPRAS]: "Compras",
    [Setor.FINANCEIRO]: "Financeiro",
    [Setor.JURIDICO]: "Jurídico",
    [Setor.MARKETING]: "Marketing",
    [Setor.OPERACOES]: "Operações",
    [Setor.RH]: "Recursos Humanos",
    [Setor.TI]: "Tecnologia da Informação",
};

export function formatarSetor(setor: Setor): string {
    return SetorDescricao[setor] || 'Setor desconhecido';
}