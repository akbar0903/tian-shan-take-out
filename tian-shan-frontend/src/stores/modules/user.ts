//导入仓库api
import { defineStore } from 'pinia'
import { ref } from 'vue'

interface UserState {
  id: number
  username: string
  name: string
  token: string
}

//创建小仓库
export const useUserStore = defineStore('userInfo', () => {

  // 初始化数据
  const userInfo = ref<UserState>({
    id: 0,
    name: '',
    username: '',
    token: ''
  })

  const setToken = (token: string) => {
    userInfo.value.token = token
  }

  const removeToken = () => {
    userInfo.value.token = ''
  }



  //一定要返回一个对象，返回的对象中的属性和方法可以提供给组件使用
  return {
    userInfo,
    setToken,
    removeToken
  }
})
