import {ref} from "vue";
import {PrioridadeSolicitacao, StatusSolicitacao} from "@/enums/enums.ts";
import type {SolicitacaoRequest} from "@/modules/solicitacoes/types/solicitacao.types.ts";
import {obterMensagemErro} from "@/shared/utils/store/mensagem.store.ts";
import type {Usuario} from "@/modules/usuarios/types/usuario.types.ts";
import type {TipoSolicitacao} from "@/modules/tipos/types/tipos.types.ts";
import {storeToRefs} from "pinia";
import {useTiposStore} from "@/modules/tipos/stores/tipos.stores.ts";
import {useUsuarioStore} from "@/modules/usuarios/stores/usuarios.stores.ts";
import type {UsuarioAutenticado} from "@/modules/auth/interfaces/UsuarioAutenticado.ts";
import {useSolicitacaoStore} from "@/modules/solicitacoes/stores/solicitacoes.stores.ts";
import {formatarStatusEnum} from "@/shared/utils/mappers/StatusUtils.ts";
import {formatarPrioridadeEnum} from "@/shared/utils/mappers/PrioridadeSolicitacaoUtils.ts";
import {useCustomToast} from "@/shared/composables/toastCustomizados.ts";
import {carregaStatusDeAcordoComStatusAnterior} from "@/modules/solicitacoes/regras/solicitacaoStatusRules.ts";


export const useSolicitacaoForm = (isGestor: boolean,
                                   isAdmin: boolean, isUsuario: boolean, usuarioAutenticado: UsuarioAutenticado) => {
    const {toastError, toastSuccess} = useCustomToast();

    const solicitacaoStore = useSolicitacaoStore();

    let tipoList = ref<TipoSolicitacao[] | undefined>([])
    let usuarioList = ref<Usuario[] | undefined>([])
    const statusList = ref<StatusSolicitacao[]>([])

    const modo = ref<"cadastro" | "edicao">("cadastro")
    const visible = ref(false)


    const prioridadeList = Object.values(PrioridadeSolicitacao)

    const tiposStore = useTiposStore()
    const usuarioStore = useUsuarioStore()
    const {usuarios} = storeToRefs(usuarioStore);
    const {tipos} = storeToRefs(tiposStore);

    const solicitacaoInitial = {
        titulo: "",
        descricao: "",
        prioridade: PrioridadeSolicitacao.BAIXA,
        status: StatusSolicitacao.ABERTA,
        usuario: null,
        tipo: null
    }

    const solicitacao = ref<SolicitacaoRequest>(solicitacaoInitial);

    async function carregaLista() {

        if (isGestor || isAdmin) {
            await usuarioStore.carregarUsuarios({
                pagina: 0,
                tamanho: 10,
                ordenarPor: "id",
                direcao: "asc"
            })

            usuarioList.value = usuarios.value?.dados

        } else {
            const usuario = await usuarioStore.buscarUsuarioPorId(
                usuarioAutenticado!.id
            );

            solicitacao.value.usuario = usuario;
            usuarioList.value = [usuario];
        }

        await tiposStore.carregarTipos({
            pagina: 0,
            tamanho: 10,
            ordenarPor: "id",
            direcao: "asc"
        })

        tipoList.value = tipos.value?.dados

    }

    const preparaDialog = async (modoDialog: "cadastro" | "edicao") => {
        modo.value = modoDialog;

        if (modoDialog === "cadastro") {
            statusList.value = [StatusSolicitacao.ABERTA];
        }

        await carregaLista();
        visible.value = true;
    };

    const fecharDialog = () => {
        visible.value = false;
        solicitacao.value = solicitacaoInitial
        idSelecionado.value = 0

    }


//salvar

    const salvar = async (modo: "cadastro" | "edicao", dados: SolicitacaoRequest) => {

        try {

            if (modo === "cadastro") {

                await solicitacaoStore.criarSolicitacao(dados)

                toastSuccess("Solicitacao criada com sucesso!");

            } else {

                if (idSelecionado.value === null) {
                    toastError("Registro não selecionado.")
                    return
                }


                await solicitacaoStore.atualizarSolicitacao(idSelecionado.value, dados)

                toastSuccess("Solicitacao atualizada com sucesso!");

            }

            fecharDialog()

            await solicitacaoStore.carregarSolicitacoes({
                pagina: 0,
                tamanho: 10,
                ordenarPor: "id",
                direcao: "asc"
            })

        } catch (e) {
            let erro = obterMensagemErro(e)

            toastError(erro)
        }
    }

// editar

    const idSelecionado = ref(0)
    const minhaSolicitacao = ref(false)

    const abrirEdicao = async (id: number) => {

        if (id === null) {
            toastError("Registro não capturado")
            return
        }


        try {
            const solicitacaoCarregada = await solicitacaoStore.buscarSolicitacaoPorId(id)
            const usuario = await usuarioStore.buscarUsuarioPorId(solicitacaoCarregada.usuario.id)

            solicitacao.value = {
                titulo: solicitacaoCarregada.titulo,
                descricao: solicitacaoCarregada.descricao,
                prioridade: formatarPrioridadeEnum(solicitacaoCarregada.prioridade),
                status: formatarStatusEnum(solicitacaoCarregada.status),
                usuario: usuario,
                tipo: solicitacaoCarregada.tipo,
            }

            idSelecionado.value = id
            minhaSolicitacao.value = usuarioAutenticado!.email === usuario.email

            statusList.value = carregaStatusDeAcordoComStatusAnterior(solicitacao.value.status, isUsuario)

            await preparaDialog("edicao")

        } catch (e) {
            let erro = obterMensagemErro(e)

            toastError(erro)
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
        solicitacao,
        minhaSolicitacao,
        prioridadeList,
        statusList,
        usuarioList,
        tipoList,
    };
};