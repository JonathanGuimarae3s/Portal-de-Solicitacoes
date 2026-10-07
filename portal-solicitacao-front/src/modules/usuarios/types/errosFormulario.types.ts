import type {UsuarioRequest} from "@/modules/usuarios/types/usuario.types.ts";

export type ErrosFormulario = Partial<
    Record<keyof UsuarioRequest, Array<{ message: string }>>
>;