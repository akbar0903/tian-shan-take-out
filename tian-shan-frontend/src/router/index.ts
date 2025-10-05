import { createWebHistory, createRouter } from 'vue-router'
import Layout from '@/layout/index.vue'

const routes = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '天山外卖', hidden: true, noNeedAuth: true },
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404.vue'),
    meta: { title: '404', hidden: true, noNeedAuth: true },
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        name: 'Dashboard',
      },
      {
        path: 'employee',
        component: () => import('@/views/employee/index.vue'),
        name: 'Employee',
      },
      {
        path: 'dish',
        component: () => import('@/views/dish/index.vue'),
        name: 'Dish',
      },
      {
        path: 'category',
        component: () => import('@/views/category/index.vue'),
        name: 'Category',
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
