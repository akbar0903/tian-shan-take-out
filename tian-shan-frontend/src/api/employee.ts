import type { LoginParams } from '@/data-types'
import request from '@/utils/request'

interface LoginResponse {
  id: number
  username: string
  name: string
  token: string
}

// 员工登录
export const loginAPI = (data: LoginParams):Promise<LoginResponse>=> {
  return request.post('/admin/employee/login', data)
}

