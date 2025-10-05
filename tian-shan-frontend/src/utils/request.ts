import axios from 'axios'
import { useUserStore } from '@/stores'
import { ElMessage } from 'element-plus' 

const userStore = useUserStore()

// 定义后端返回的通用数据结构
export interface ApiResponse<T = any> {
  code: number
  msg: string
  data: T
}

// 创建 axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_BASE_API,
  timeout: 10000, 
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从 Pinia store 获取 token
    const token = userStore.token
    if (userStore.token) {
      config.headers.Token = token
    }
    return config
  },
  (error) => {
    console.error('Request Error:', error)
    return Promise.reject(error)
  },
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const { code, msg, data } = response.data

    if (code === 1) {
      // 请求成功，返回 data
      return data
    } else {
      // 请求失败，显示错误信息
      ElMessage.error(msg || '请求失败')
      return Promise.reject(new Error(msg || '请求失败'))
    }
  },
  (error) => {
    // 处理 HTTP 错误状态码
    if (error.response) {
      const { status } = error.response

      switch (status) {
        case 401:
          // 未授权，token 无效或过期
          ElMessage.error('登录已过期，请重新登录')
          userStore.logout() 
          // 跳转到登录页
          break
        case 403:
          ElMessage.error('拒绝访问')
          break
        case 404:
          ElMessage.error('请求地址不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(`请求失败，状态码: ${status}`)
      }
    } else if (error.request) {
      // 请求已发出，但没有收到响应
      ElMessage.error('网络错误，请检查您的网络连接')
    } else {
      // 在设置请求时触发了错误
      ElMessage.error('请求配置错误')
    }

    return Promise.reject(error)
  },
)

export default request
