import { createApp } from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'

createApp(App).use(store).use(store).use(router).use(store).use(store).mount('#app')
