import { createApp } from 'vue'
import App from './App.vue'
import store from './stores'
import router from './router'
import '@/styles/index.scss'

const app = createApp(App)

app.use(store)
app.use(router)

app.mount('#app')
