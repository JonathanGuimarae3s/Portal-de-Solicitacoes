import axios from "axios";
import type {ErrorResponse} from "@/types/interfaces.ts";

export function obterMensagemErro(error: unknown): string {
    if (axios.isAxiosError<ErrorResponse>(error)) {

        return error.response?.data.userMessage ?? "Não foi possível conectar ao servidor.";

    }

    return "Não foi possível conectar ao servidor.";
}