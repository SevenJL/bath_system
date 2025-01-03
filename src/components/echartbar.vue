<template>
  <div ref="chart" class="chart-container"></div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted, watch, ref } from 'vue';
import * as echarts from 'echarts';

// 接收父组件传递的数据
const{ chartData } = defineProps({
  chartData: {
    type: Object,
    required: true,
  },
});

const chart = ref(null);
let chartInstance = null;

// 初始化图表
const initChart = () => {
  if (chartInstance) {
    chartInstance.dispose();
  }
  chartInstance = echarts.init(chart.value);

  const option = {
    title: {
      text: `总使用人数: ${chartData.totalUsers}`,
      left: 'center',
    },
    tooltip: {
      trigger: 'axis',
      formatter: function (params) {
        return `${params[0].axisValue}<br/>使用人数: ${params[0].data}`;
      },
    },
    xAxis: {
      type: 'category',
      data: chartData.hours,
    },
    yAxis: {
      type: 'value',
      name: '人数',
    },
    series: [
      {
        data: chartData.usage,
        type: 'bar',
        name: '使用人数',
        itemStyle: {
          color: '#5470c6',
        },
      },
    ],
  };

  chartInstance.setOption(option);
};

// 监听数据变化，更新图表
watch(
    () => chartData,
    () => {
      if (chartInstance) {
        initChart();
      }
    },
    { deep: true }
);

// 生命周期
onMounted(() => {
  initChart();
});

// 销毁图表
onUnmounted(() => {
  if (chartInstance) {
    chartInstance.dispose();
  }
});
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 400px;
}
</style>
