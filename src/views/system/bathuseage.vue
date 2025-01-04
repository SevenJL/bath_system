<template>
  <div>
    <el-card>
      <div class="chart-header">
        <h3>澡堂使用情况统计</h3>
        <el-button type="primary" @click="fetchData">刷新数据</el-button>
      </div>
      <!-- 引入 EchartBar 图表组件 -->
      <echart-bar :chart-data="chartData"/>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import EchartBar from '@/components/echartbar.vue';
import {getChartInfo} from "@/api";

// 模拟从后端获取数据
const fetchData = async () => {
  try {
    // 获取后端数据
    const res = await getChartInfo();
    const rawData = res.data;
    console.log(rawData)

    if (rawData.code !== 200) {
      ElMessage.error(res.data.message)
      return;
    }

    // 后端数据中的 usage 数据
    const usageData = rawData.data;

    // 转换为图表需要的格式
    const hours = ['6:00-9:00', '9:00-12:00', '12:00-15:00', '15:00-18:00', '18:00-21:00', '21:00-6:00'];
    const usage = [
      usageData['6:00-9:00'] || 0,
      usageData['9:00-12:00'] || 0,
      usageData['12:00-15:00'] || 0,
      usageData['15:00-18:00'] || 0,
      usageData['18:00-21:00'] || 0,
      usageData['21:00-6:00'] || 0,
    ];

    // 计算总和
    const total = usage.reduce((prev, curr) => prev + curr, 0);

    // 更新图表数据
    chartData.value = {
      hours,
      usage,
      totalUsers: total,
    };

    ElMessage.success('数据刷新成功');
  } catch (error) {
    ElMessage.error(error.message || '数据刷新失败');
  }
};

// 图表数据
const chartData = ref({
  hours: [],
  usage: [],
  totalUsers: 0,
});

// 初始化加载数据
fetchData();
</script>

<style scoped>
.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
