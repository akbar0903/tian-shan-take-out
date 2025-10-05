<script lang="ts" setup>
import { ref } from 'vue'
import { Lock, User } from 'lucide-vue-next'
import { loginAPI } from '@/api/employee'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores'

const router = useRouter()
const userStore = useUserStore()

const imageList = [
  'src/assets/login/swiper/1.webp',
  'src/assets/login/swiper/2.webp',
  'src/assets/login/swiper/3.webp',
  'src/assets/login/swiper/4.webp',
  'src/assets/login/swiper/5.webp',
  'src/assets/login/swiper/6.webp',
  'src/assets/login/swiper/7.webp',
  'src/assets/login/swiper/8.webp',
  'src/assets/login/swiper/9.webp',
  'src/assets/login/swiper/10.webp',
  'src/assets/login/swiper/11.webp',
  'src/assets/login/swiper/12.webp',
  'src/assets/login/swiper/13.webp',
  'src/assets/login/swiper/14.webp',
  'src/assets/login/swiper/15.webp',
  'src/assets/login/swiper/16.webp',
]

const loginForm = ref({
  username: '',
  password: '',
})

const onSubmit = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.error('用户名和密码不能为空')
    return
  }
  try {
    const response = await loginAPI(loginForm.value)
    userStore.setToken(response.token)
    userStore.setUserInfo(response)
    router.push('/')
    ElMessage.success('登录成功')
  } catch (error) {
    console.error(error)
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="left-container">
        <el-carousel
          class="el-carousel"
          :autoplay="true"
          :interval="6000"
          :pause-on-hover="false"
          indicator-position="none"
          arrow="never"
        >
          <el-carousel-item v-for="item in imageList" :key="item" style="height: 474px">
            <img :src="item" class="image" />
          </el-carousel-item>
        </el-carousel>
      </div>

      <div class="right-container">
        <div class="title-container">
          <img src="/src/assets/logo.png" />
          <div>
            <h1>天山外卖</h1>
            <h1>TIAN SHAN TAKE OUT</h1>
          </div>
        </div>
        <el-form :model="loginForm" class="login-form">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username">
              <template #prefix>
                <user :size="16" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" type="password" show-password>
              <template #prefix>
                <Lock :size="16" />
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="onSubmit" class="primary-btn">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: $primary-text-color;
  overflow: auto;
  .login-box {
    width: 1000px;
    height: 474px;
    border-radius: $radius-md;
    display: flex;
    flex-shrink: 0;
    overflow: hidden;
    .left-container {
      width: 60%;
      height: 100%;
      .el-carousel {
        height: 100%;
        .image {
          height: 100%;
          width: auto;
          object-fit: cover;
        }
      }
    }

    .right-container {
      width: 40%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      gap: $space-md;
      background-color: $white-color;
      .title-container {
        width: 60%;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: $space-xs;
        img {
          width: 64px;
          border-radius: $radius-full;
        }
        h1 {
          color: #f5a524;
          font-size: $text-lg;
          text-align: center;
        }
      }

      .login-form {
        width: 60%;
        .primary-btn {
          width: 100%;
        }
      }
    }
  }
}
</style>
