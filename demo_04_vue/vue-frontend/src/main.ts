import { createApp } from 'vue'
import App from './App.vue'
import './assets/main.css'
import Buffer from 'web3'
import {createPinia} from "pinia";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

(window as any).global = window;
// @ts-ignore
window.Buffer = window.Buffer || Buffer;
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
createApp(App).use(pinia).mount('#app')
