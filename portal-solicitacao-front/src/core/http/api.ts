import axios from "axios";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    withCredentials: true,
});

export function settaAuthToken(token: string | null) {
    if (token) {
        api.defaults.headers.common.Authorization = `Bearer ${token}`;
    } else {
        delete api.defaults.headers.common.Authorization;
    }
}


export function configurarInterceptor(
    tratarNaoAutorizado: () => void
) {

    api.interceptors.response.use(
        response => response,
        error => {
            if (error.response?.status === 401 || error.response.status === 403) {
                tratarNaoAutorizado();
            }


            return Promise.reject(error);
        }
    );
}




export default api;
