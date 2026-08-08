import axios from "axios";

axios.defaults.baseURL = import.meta.env.VITE_API_URL;

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
})

export function defineToken(token: string | null) {

    if (token) {
        console.log(token);

        api.defaults.headers.Authorization = `Bearer ${token}`
    } else {
        delete api.defaults.headers.Authorization
    }
}

export default api;
