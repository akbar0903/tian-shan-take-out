import { useUserStore } from '@/stores'
import axios from 'axios'

const userStore = useUserStore()

const request = axios.create({
  baseURL: import.meta.env.VITE_BASE_API,
  timeout: 60000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 每次请求之前携带token
    const token = userStore.userInfo.token
    if (token) {
      config.headers.Token = token
    }
    return config
  },

  (err) => {
    return Promise.reject(err)
  },
)

// 3.响应拦截器
request.interceptors.response.use(
  (response) => {
    const { code, msg, data } = response.data
    if (code === 1) {
      return data 
    } else {
      return Promise.reject(new Error(msg || '请求失败')) 
    }
  },

  (error) => {
    // 监控401状态码
    if (error.response?.status === 401) {
      userStore.removeToken()
      window.location.href = '/login'
    }
    return Promise.reject(new Error(error.response?.data?.msg || '服务器端错误'))
  }
)

export { request }