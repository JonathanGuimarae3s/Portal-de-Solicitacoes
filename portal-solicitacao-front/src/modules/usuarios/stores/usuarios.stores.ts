import {defineStore} from "pinia";
import {ref} from "vue";
import type {Consulta, PageResponse} from "@/interfaces/interfaces.ts";
import type {Usuario, UsuarioRequest} from "@/modules/usuarios/types/usuario.types.ts";
import * as usuarioService from "@/modules/usuarios/services/usuarioService.ts";

export const useUsuarioStore = defineStore("usuarios", () => {
    const usuarios = ref<PageResponse<Usuario> | null>(null);
    const isLoading = ref(false);
    const erro = ref<string | null>(null);

    async function executarComLoading<T>(acao: () => Promise<T>): Promise<T> {
        isLoading.value = true;
        erro.value = null;

        try {
            return await acao();
        } finally {
            isLoading.value = false;
        }
    }

    async function carregarUsuarios(consulta: Consulta): Promise<void> {
        try {
            usuarios.value = await executarComLoading(
                () => usuarioService.buscarUsuarios(consulta)
            );
        } catch {
            usuarios.value = null;
            erro.value = "Não foi possível carregar os usuários.";
        }
    }

    function buscarUsuarioPorId(id: number): Promise<Usuario> {
        return executarComLoading(
            () => usuarioService.buscarPorId(id)
        );
    }

    function criarUsuario(request: UsuarioRequest): Promise<void> {
        return executarComLoading(async () => {
            await usuarioService.salvar(request);
        });
    }

    function atualizarUsuario(id: number, request: UsuarioRequest): Promise<void> {
        return executarComLoading(async () => {
            await usuarioService.atualizar(id, request);
        });
    }

    function ativarInativarUsuario(id: number, ativo: boolean): Promise<void> {
        return executarComLoading(async () => {
            await usuarioService.ativarInativarUsuario(id, ativo);
        });
    }

    return {
        usuarios,
        isLoading,
        erro,
        carregarUsuarios,
        buscarUsuarioPorId,
        criarUsuario,
        atualizarUsuario,
        ativarInativarUsuario,
    };
});
