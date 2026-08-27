export interface PageResponse<T> {
    dados: T[];
    pagina: number;
    tamanho: number;
    totalDados: number;
    totalPaginas: number;
}

export interface Consulta {
    pagina: number;
    tamanho: number;
    ordenarPor: string;
    direcao: "asc" | "desc";

}

export interface ErrorResponse {
    detail: string
    status: number;
    timestamp: string;
    title: string;
    type: string;
}

type SortOrder = 1 | -1;

export interface LazyParams {
    first: number;
    rows: number;
    sortField: string;
    sortOrder: SortOrder;
}
