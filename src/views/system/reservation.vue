<template>
  <div>
    <TableSearch :query="query" :options="searchOpt" :search="handleSearch" :reset-form="resetForm"/>
    <div class="container">
      <TableCustom
          :columns="columns"
          :tableData="tableData"
          :total="page.total"
          :delFunc="handleDelete"
          :changePage="changePage"
          :currentPage="page.index"
      >
        <template #toolbarBtn>
          <el-button type="warning" :icon="CirclePlusFilled" @click="flushData">刷新</el-button>
        </template>
        <template #status="{ rows }">
          <el-tag type="warning" v-if="rows.status === 'pending'">待确认</el-tag>
          <el-tag type="success" v-if="rows.status === 'confirmed'">已确认</el-tag>
          <el-tag type="danger" v-if="rows.status === 'cancelled'">已取消</el-tag>
        </template>
        <template #actions="{ rows }">
          <!-- 确认按钮：仅在状态为 "pending" 时显示 -->
          <el-button
              v-if="rows.status === 'pending'"
              type="primary"
              size="small"
              @click="confirmReservation(rows)"
          >
            确认
          </el-button>
          <!-- 删除按钮：仅在状态为 "pending" 时显示 -->
          <el-button
              v-if="rows.status === 'pending'"
              type="danger"
              size="small"
              @click="handleDelete(rows)"
          >
            删除
          </el-button>
        </template>
      </TableCustom>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive} from 'vue';
import {ElMessage} from 'element-plus';
import {getReservations, deleteReservation, confirmReservationMethod} from '@/api';
import {CirclePlusFilled} from '@element-plus/icons-vue';
import TableCustom from "@/components/table-custom.vue";
import TableSearch from "@/components/table-search.vue";

// 查询相关
const query = reactive({
  userId: '',
});
const searchOpt = ref([
  {type: 'input', label: '用户ID：', prop: 'userId'},
]);

const handleSearch = () => {
  if(query.userId === ''){
    getData();
    return;
  }
  if (!/^\d+$/.test(query.userId)) {
    ElMessage.warning('用户ID必须为数字');
    return;
  }
  // 对数据进行筛选
  tableData.value = tableData.value.filter((item) => {
    console.log(item);
    return query.userId.includes(item.userId);
  });
};

// 重置查询条件
const resetForm = () => {
  query.userId = '';
  getData();
};

// 表格相关
let columns = ref([
  {type: 'index', label: '序号', width: 55, align: 'center'},
  {prop: 'reservationId', label: '预约ID'},
  {prop: 'userId', label: '用户ID'},
  {prop: 'startTime', label: '开始时间'},
  {prop: 'endTime', label: '结束时间'},
  {prop: 'status', label: '状态', slotName: 'status'},
  {prop: 'actions', label: '操作', slotName: 'actions', width: 250},
]);

const page = reactive({
  index: 1,
  size: 10,
  total: 0,
});

// 表格数据相关
const tableData = ref([]);
const getData = async () => {
  const res = await getReservations({
    page: page.index,
    size: page.size,
  });
  // 格式化时间字段
  tableData.value = res.data.data.data.map((item) => {
    return {
      ...item,
      startTime: formatDateTime(item.startTime),
      endTime: formatDateTime(item.endTime),
    };
  });
  page.total = res.data.data.pageTotal;
};
getData();

// 刷新数据
const flushData = () => {
  getData();
  ElMessage.success("刷新成功")
}

// 分页
const changePage = (val: number) => {
  page.index = val;
  getData();
};

// 删除预约记录
const handleDelete = async (row: any) => {
  console.log(row)
  if (row.status === 'confirmed') {
    ElMessage.warning('已确认预约无法删除');
    return;
  }
  const res = await deleteReservation(row.reservationId);
  if (res.data.code !== 200) {
    ElMessage.error('删除失败');
    return;
  }
  ElMessage.success('删除成功');
  await getData();
};

// 确认预约
const confirmReservation = async (row: any) => {
  const res = await confirmReservationMethod(row.reservationId);
  if (res.data.code !== 200) {
    ElMessage.error('确认失败');
    return;
  }
  ElMessage.success('确认成功');
  await getData();
};

// 格式化时间
const formatDateTime = (datetime) => {
  const date = new Date(datetime);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

import {watch} from 'vue';

// 监听表格数据变化，根据状态排序
watch(tableData, (newVal) => {
      // 按照状态排序
      tableData.value = newVal.sort((a, b) => {
        const statusOrder = {pending: 0, confirmed: 1, cancelled: 2};
        return statusOrder[a.status] - statusOrder[b.status];
      });
    },
    {deep: true} // 深度监听，确保对数组内容的变化有效
);

</script>

<style scoped></style>
