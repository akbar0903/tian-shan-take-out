//导入仓库api
import { defineStore } from 'pinia'
import { ref } from 'vue'

interface UserInfo {
  id: number
  username: string
  name: string
}

//创建小仓库
export const useUserStore = defineStore(
  'userInfo',
  () => {
    // state
    const token = ref<string | null>(null)
    const userInfo = ref<UserInfo | null>(null)

    // actions
    const setToken = (newToken: string) => {
      token.value = newToken
    }

    const setUserInfo = (info: UserInfo) => {
      userInfo.value = info
    }

    const logout = () => {
      token.value = null
      userInfo.value = null
    }

    return {
      token,
      userInfo,
      setToken,
      setUserInfo,
      logout,
    }
  },
  {
    // enable persistence for this store (works with pinia-plugin-persistedstate)
    persist: true,
  },
)
