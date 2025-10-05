import { createPinia } from 'pinia'
import piniaPluginPersistedState from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(piniaPluginPersistedState)

export default pinia

//统一用index.js导出
import { useUserStore } from './modules/user'
export { useUserStore }
