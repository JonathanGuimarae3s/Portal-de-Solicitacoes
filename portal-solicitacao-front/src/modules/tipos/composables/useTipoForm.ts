import {ref} from "vue";
import {obterMensagemErro} from "@/shared/utils/store/mensagem.store.ts";
import type {TipoSolicitacaoRequest} from "@/modules/tipos/types/tipos.ts";
import {useConfirm} from "primevue";
import {useTiposStore} from "@/modules/tipos/stores/tipos.stores.ts";
import type {UseTipoFormOptions} from "@/modules/tipos/types/tipoFormOptions.ts";


export const useTipoForm = ({
                                mostrarMensagemErro,
                                mostrarMensagemSucesso,
                                recarregarTipos,
                            }: UseTipoFormOptions) => {
    const tiposStore = useTiposStore();

    const visible = ref(false);
    const modo = ref<"cadastro" | "edicao">("cadastro");
    const idSelecionado = ref<number | null>(null);
    const tipoSolicitacao = ref<TipoSolicitacaoRequest>({
        nome: ""
    })

    const preparaDialog = (modoDialog: "cadastro" | "edicao") => {
        modo.value = modoDialog
        visible.value = true;
    }

    const fecharDialog = () => {
        visible.value = false;
        tipoSolicitacao.value.nome = ""
        idSelecionado.value = null
    }

    const salvar = async (modo: "cadastro" | "edicao") => {

        try {

            const nome = tipoSolicitacao.value.nome.trim();
            if (!nome) {
                mostrarMensagemErro("Nome do tipo da solicitação não pode ser vazio.");
                return
            }

            const request: TipoSolicitacaoRequest = {nome}

            if (modo === "cadastro") {
                await tiposStore.criarTipo(request)

                mostrarMensagemSucesso("Criado com sucesso");
            } else {
                if (idSelecionado.value === null) {
                    mostrarMensagemErro("Registro não selecionado.")
                    return
                }

                await tiposStore.atualizarTipo(idSelecionado.value, request)
                mostrarMensagemSucesso("Atualizado com sucesso");

            }

            fecharDialog()

            await recarregarTipos()

        } catch (e) {
            let erro = obterMensagemErro(e)

            mostrarMensagemErro(erro)
        }
    }


//EDITARR

    const abrirEdicao = async (id: number) => {

        if (id === null) {
            mostrarMensagemErro("Registro não capturado")
            return
        }


        try {
            tipoSolicitacao.value = await tiposStore.buscarTipoPorId(id)
            idSelecionado.value = id
            preparaDialog("edicao")

        } catch (e) {
            let erro = obterMensagemErro(e)

            mostrarMensagemErro(erro)
        }
    }


//EXCLUIR


    const confirm = useConfirm();


    const confirmarExclusao = (id: number) => {
        confirm.require({
            message: 'Você quer deletar esse registro?',
            header: 'Atencão!',
            icon: 'pi pi-info-circle',
            rejectLabel: 'Cancelar',
            acceptLabel: 'Deletar',
            rejectClass: 'p-button-secondary p-button-outlined',
            acceptClass: 'p-button-danger',
            accept: async () => {


                try {
                    await tiposStore.excluirTipo(id)

                    mostrarMensagemSucesso('Registro deletado!')

                    await recarregarTipos()
                } catch (error) {

                    let erroDeletar: string | undefined = "Não foi possível deletar o registro. Tente mais tarde.";
                    if (error != null) {
                        erroDeletar = obterMensagemErro(error)
                    }

                    mostrarMensagemErro(erroDeletar)
                }
            },
        });
    };


    return {
        visible,
        modo,
        tipoSolicitacao,
        preparaDialog,
        fecharDialog,
        salvar,
        abrirEdicao,
        confirmarExclusao,
    };
};
