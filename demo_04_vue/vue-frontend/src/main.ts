import { createApp } from 'vue'
import App from './App.vue'
import './assets/main.css'
import Buffer from 'web3'
(window as any).global = window;
// @ts-ignore
window.Buffer = window.Buffer || Buffer;
createApp(App).mount('#app')
