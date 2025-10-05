<script lang="ts" setup>
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'
import AppMain from './AppMain.vue'

</script>

<template>
  <div class="app-container">
    <!-- 顶部头部 -->
    <Header />

    <!-- 下方内容区 -->
    <div class="content-container">
      <!-- 左侧边栏 -->
      <Sidebar />

      <!-- 右侧主内容区 -->
      <AppMain />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;

  .content-container {
    flex-grow: 1;
    display: flex;
     /* 禁止此区域自身滚动，让滚动发生在子元素内部 */
    overflow: hidden;
  }
}
</style>