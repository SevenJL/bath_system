<template>
  <div class="home-container">
    <!-- 顶部欢迎标题 -->
    <div class="welcome-header">
      <h1>欢迎来到学生管理系统</h1>
      <p>管理学生信息更加简单高效！</p>
    </div>

    <!-- 快捷功能卡片 -->
    <div class="shortcut-cards">
      <el-card
          class="shortcut-card"
          shadow="hover"
          @click="$router.push('/students')"
      >
        <el-icon><User /></el-icon>
        <div class="card-content">
          <h3>学生信息</h3>
          <p>查看和管理学生的详细信息</p>
        </div>
      </el-card>

      <el-card
          class="shortcut-card"
          shadow="hover"
          @click="$router.push('/add-student')"
      >
        <el-icon><Plus /></el-icon>
        <div class="card-content">
          <h3>新增学生</h3>
          <p>快速添加新的学生信息</p>
        </div>
      </el-card>

      <el-card
          class="shortcut-card"
          shadow="hover"
          @click="$router.push('/class-management')"
      >
        <el-icon><Collection /></el-icon>
        <div class="card-content">
          <h3>班级管理</h3>
          <p>管理班级和年级信息</p>
        </div>
      </el-card>

      <el-card
          class="shortcut-card"
          shadow="hover"
          @click="$router.push('/settings')"
      >
        <el-icon><Setting /></el-icon>
        <div class="card-content">
          <h3>系统设置</h3>
          <p>配置系统相关参数</p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import { User, Plus, Collection, Setting } from "@element-plus/icons-vue";

const router = useRouter();
</script>

<style scoped>
.home-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: #f5f5f5;
}

.welcome-header {
  text-align: center;
  margin-bottom: 40px;
}

.welcome-header h1 {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.welcome-header p {
  font-size: 16px;
  color: #666;
}

.shortcut-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  width: 100%;
  max-width: 800px;
}

.shortcut-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.shortcut-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
}

.shortcut-card .card-content h3 {
  margin-top: 10px;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.shortcut-card .card-content p {
  margin-top: 5px;
  font-size: 14px;
  color: #666;
}

.el-icon {
  font-size: 40px;
  color: #409eff;
}
</style>
