<script lang="ts" setup>
import { ref } from 'vue'
import { LayoutDashboard, UserRound, Salad, PanelLeftOpen, PanelLeftClose, Blocks } from 'lucide-vue-next'

const isCollapse = ref(false)

const menuItems = ref([
  {
    index: '/dashboard',
    title: '工作台',
    icon: LayoutDashboard,
    path: '/dashboard',
  },
  {
    index: '/dish',
    title: '菜品管理',
    icon: Salad,
    path: '/dish',
  },
  {
    index: '/category',
    title: '分类管理',
    icon: Blocks,
    path: '/category',
  },
  {
    index: '/employee',
    title: '员工管理',
    icon: UserRound,
    path: '/employee',
  },
])

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<template>
  <div class="sidebar-container" :style="{ width: isCollapse ? '64px' : '190px' }">
    <!-- 菜单区域 -->
    <el-menu
      router
      :default-active="$route.path"
      :collapse="false"
      class="sidebar-menu"
      :class="{ 'sidebar-menu--collapsed': isCollapse }"
      background-color="transparent"
      text-color="rgba(255, 255, 255, 0.8)"
      active-text-color="white"
    >
      <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.path">
        <el-icon>
          <component :is="item.icon" />
        </el-icon>
        <el-text v-show="!isCollapse" class="menu-text">{{ item.title }}</el-text>
      </el-menu-item>
    </el-menu>

    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="toggleCollapse">
      <el-icon :size="20">
        <PanelLeftOpen v-if="isCollapse" />
        <PanelLeftClose v-else />
      </el-icon>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.sidebar-container {
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  background-color: $sidebar-bg-color;
  /* 不要让sidebar滚动，应该让sidebar-menu滚动 */
  overflow: hidden;
  transition: width 0.3s ease;

  .sidebar-menu {
    flex-grow: 1;
    border: none;
    padding: $space-md 0;
    overflow-y: auto;
    overflow-x: hidden;

    :deep(.el-menu-item) {
      margin: $space-xs $space-md;
      border-radius: $radius-md;
      transition: all 0.3s ease;
      height: 48px;
      line-height: 48px;
      display: flex;
      align-items: center;

      &:hover {
        background: rgba(255, 255, 255, 0.1) !important;
      }

      &.is-active {
        background: rgba(255, 255, 255, 0.2) !important;
        color: white !important;
        font-weight: 500;
      }

      .el-icon {
        font-size: $text-lg;
        width: 18px;
        text-align: center;
        margin-right: $space-sm;
        transition: margin-right 0.3s ease;
        flex-shrink: 0;
      }

      .menu-text {
        transition: opacity 0.3s ease, transform 0.3s ease;
        transform: translateX(0);
        white-space: nowrap;
        color: white !important;
      }
    }

    // 折叠状态下隐藏文字并调整图标
    &.sidebar-menu--collapsed {
      :deep(.el-menu-item) {
        justify-content: center;
        padding: 0;
        margin: $space-xs $space-sm;

        .el-icon {
          margin-right: 0;
        }

        .menu-text {
          opacity: 0;
          transform: translateX(-10px);
          position: absolute;
          visibility: hidden;
        }
      }
    }
  }

  .collapse-btn {
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: rgba(255, 255, 255, 0.8);
    cursor: pointer;
    background: rgba(255, 255, 255, 0.05);
    transition: all 0.3s ease;
    border-top: 1px solid rgba(255, 255, 255, 0.1);

    &:hover {
      background: rgba(255, 255, 255, 0.1);
      color: white;
    }

    .el-icon {
      font-size: $text-base;
    }
  }
}
</style>
