<script lang="ts" setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/modules/user'
import { useRouter } from 'vue-router'
import { ChevronDown, Power, AlarmClockCheck, UserRound } from 'lucide-vue-next'

const userStore = useUserStore()
const router = useRouter()

const userInfo = computed(() => userStore.userInfo)
const username = computed(() => userInfo.value?.username || '用户')
const avatar = computed(() => userInfo.value?.name?.charAt(0).toUpperCase() || 'U')

const handleLogout = () => {
  router.push('/login')
}

const handleProfile = () => {
  router.push('/profile')
}

const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      handleProfile()
      break
    case 'logout':
      handleLogout()
      break
  }
}
</script>

<template>
  <header class="header-container">
    <!-- 左侧Logo区域 -->
    <div class="logo-container">
      <el-avatar src="src/assets/logo.png" class="logo" />
      <el-text class="title" type="primary" tag="b">天山外卖</el-text>
    </div>

    <!-- 右侧功能区 -->
    <div class="right-container">
      <!-- 设置营业状态 -->
      <div class="status">
        <el-icon><AlarmClockCheck /></el-icon>
        <el-text class="status-text">营业状态设置</el-text>
      </div>

      <!-- 用户信息下拉菜单 -->
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-info-container">
          <el-avatar
            size="small"
            :style="{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }"
          >
            {{ avatar }}
          </el-avatar>
          <el-text class="username" tag="span">{{ username }}</el-text>
          <el-icon>
            <ChevronDown />
          </el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><UserRound /></el-icon>
              <span>个人中心</span>
            </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              <el-icon><Power /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </header>
</template>

<style lang="scss" scoped>
.header-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 $space-lg;
  background-color: $primary-color;

  .logo-container {
    display: flex;
    align-items: center;

    .logo {
      width: 42px;
      height: 42px;
      border: 2px solid $white-color;
    }

    .title {
      margin-left: $space-sm;
      font-size: $text-xl;
      font-weight: bold;
      color: $primary-text-color;
    }
  }

  .right-container {
    display: flex;
    align-items: center;
    gap: $space-md;

    .status {
      height: 60px;
      display: flex;
      align-items: center;
      gap: $space-sm;
      padding: 0 $space-md;
      cursor: pointer;

      &:hover {
        background-color: $secondary-color;
      }
      .el-icon {
        font-size: $text-lg;
        color: $primary-text-color;
      }
      .status-text {
        font-size: $text-base;
        color: $primary-text-color;
        font-weight: 500;
      }
    }

    .user-info-container {
      background-color: $secondary-color;
      display: flex;
      align-items: center;
      gap: $space-sm;
      padding: $space-sm $space-md;
      cursor: pointer;
      border-radius: $radius-md;

      .username {
        font-size: $text-sm;
        font-weight: 500;
        color: $primary-text-color;
        max-width: 100px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}
</style>
