import {useToast} from "primevue/usetoast";

export const useCustomToast = () => {
    const toast = useToast();

    const toastError = (detail: string | undefined) => {
        toast.add({
            severity: 'error',
            summary: 'Erro',
            detail: detail || 'Ocorreu um erro inesperado.',
            life: 5000
        });
    };

    const toastSuccess = (detail: string | undefined) => {
        toast.add({
            severity: 'success',
            summary: 'Sucesso',
            detail: detail || 'Ação realizada com sucesso!',
            life: 3000
        });
    };

    return {
        toastError,
        toastSuccess
    };
};
