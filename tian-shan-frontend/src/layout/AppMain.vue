<script lang="ts" setup>

</script>

<template>
  <div class="main-content">
    <!-- 路由视图 -->
    <router-view v-slot="{ Component }">
      <transition name="fade-transform" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
</template>

<style lang="scss" scoped>
.main-content {
  padding: $space-lg;
  flex-grow: 1;
  background-color: $main-bg-color;
  overflow-y: auto;
  overflow-x: hidden;
}

// 页面切换动画
:deep(.fade-transform-enter-active),
:deep(.fade-transform-leave-active) {
  transition: all 0.3s ease;
}

:deep(.fade-transform-enter-from) {
  opacity: 0;
  transform: translateX(-30px);
}

:deep(.fade-transform-leave-to) {
  opacity: 0;
  transform: translateX(30px);
}
</style>
