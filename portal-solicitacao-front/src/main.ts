import {createApp} from "vue";
import {createPinia} from "pinia";
import PrimeVue from "primevue/config";
import Aura from "@primevue/themes/aura";
import "primeicons/primeicons.css";
import "./assets/styles/app.css";
import App from "./App.vue";
import router from "./router";
import {useAuthStore} from "@/modules/auth/stores/auth.ts";
import {configurarInterceptor} from "@/core/http/api.ts";


import ConfirmationService from 'primevue/confirmationservice';
import ToastService from 'primevue/toastservice';


const app = createApp(App);

//pinia
const pinia = createPinia();

app.use(pinia);

// axios
const authStore = useAuthStore(pinia);
authStore.restaurarSessao();

configurarInterceptor(() => {
    authStore.limparVariaveisSessao();

    if (router.currentRoute.value.name !== "Login") {
        router.replace({name: "Login"});
    }
});

//service
app.use(ConfirmationService);
app.use(ToastService);

//router
app.use(router);

//Prime
app.use(PrimeVue, {
    theme: {
        preset: Aura,
    },
});

app.mount("#app");

