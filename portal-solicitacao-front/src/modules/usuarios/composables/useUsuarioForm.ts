import {ref} from "vue";
import {PerfilUsuario, Setor} from "@/enums/enums.ts";
import {obterMensagemErro} from "@/shared/utils/store/mensagem.store.ts";
import type {UsuarioRequest} from "@/modules/usuarios/types/usuario.ts";
import {useUsuarioStore} from "@/modules/usuarios/stores/usuarios.stores.ts";
import type {UseUsuarioFormOptions} from "@/modules/usuarios/types/usuarioFormOptions.ts";

export const useUsuarioForm = ({
                                   isAdmin,
                                   setorUsuarioAutenticado,
                                   mostrarMensagemErro,
                                   mostrarMensagemSucesso,
                                   recarregarUsuarios,
                               }: UseUsuarioFormOptions) => {
    const usuarioStore = useUsuarioStore();

    const visible = ref(false);
    const modo = ref<"cadastro" | "edicao">("cadastro");
    const idSelecionado = ref<number | null>(null);
    const usuario = ref<UsuarioRequest>({
        nome: "",
        email: "",
        setor: setorUsuarioAutenticado,
        senha: "",
        perfil: PerfilUsuario.USUARIO,
    })

    const listaPerfisUsuario = ref<PerfilUsuario[]>([]);
    const listaSetores = ref<Setor[]>([]);

    const preparaDialog = (modoDialog: "cadastro" | "edicao") => {
        modo.value = modoDialog
        visible.value = true;

        if (isAdmin) {
            listaPerfisUsuario.value = Object.values(PerfilUsuario);
            listaSetores.value = Object.values(Setor);
        } else {
            listaPerfisUsuario.value = [PerfilUsuario.USUARIO];
            listaSetores.value = [setorUsuarioAutenticado];
        }

    }

    const fecharDialog = () => {
        visible.value = false;
        usuario.value = {
            nome: "",
            email: "",
            setor: setorUsuarioAutenticado,
            senha: "",
            perfil: PerfilUsuario.USUARIO,
        }

        idSelecionado.value = null
    }

    const salvar = async (modo: "cadastro" | "edicao", dados: UsuarioRequest) => {
        try {
            if (modo === "cadastro") {
                await usuarioStore.criarUsuario(dados)
                mostrarMensagemSucesso("Criado com sucesso");
            } else {
                if (idSelecionado.value === null) {
                    mostrarMensagemErro("Registro não selecionado.")
                    return
                }

                await usuarioStore.atualizarUsuario(idSelecionado.value, dados)
                mostrarMensagemSucesso("Atualizado com sucesso");
            }

            fecharDialog()
            await recarregarUsuarios()
        } catch (e) {
            const erro = obterMensagemErro(e)
            mostrarMensagemErro(erro)
        }
    }

    const abrirEdicao = async (id: number) => {
        try {
            const usuarioEdicao = await usuarioStore.buscarUsuarioPorId(id)

            usuario.value = {
                nome: usuarioEdicao.nome,
                email: usuarioEdicao.email,
                setor: Setor[usuarioEdicao.setor as keyof typeof Setor],
                senha: "",
                perfil: PerfilUsuario[usuarioEdicao.perfil as keyof typeof PerfilUsuario],
            }

            idSelecionado.value = id
            preparaDialog("edicao")
        } catch (e) {
            const erro = obterMensagemErro(e)
            mostrarMensagemErro(erro)
        }
    }

    return {
        preparaDialog,
        fecharDialog,
        abrirEdicao,
        salvar,
        idSelecionado,
        visible,
        modo,
        usuario,
        listaPerfisUsuario,
        listaSetores,
    };
};
