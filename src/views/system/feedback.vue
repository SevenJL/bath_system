<template>
  <div class="feedback-list-container">
    <!-- 搜索区域 -->
    <TableSearch
        :query="query"
        :options="searchOpt"
        :search="handleSearch"
        :reset-form="resetForm"/>
    <!-- 反馈列表表格 -->
    <div class="container">
      <TableCustom
          :columns="columns"
          :tableData="paginatedFeedbackList"
          :total="paginatedFeedbackList.length"
          :viewFunc="handleView"
          :delFunc="handleDelete"
          :changePage="changePage"
          :currentPage="page.index"
      >
        <template #status="{ row }">
          <el-tag type="success" v-if="row.status">启用</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
        <template #actions="{ rows }">
          <el-button type="danger" size="small" @click="handleDelete(rows)">删除</el-button>
        </template>
      </TableCustom>
    </div>

    <!-- 查看详情对话框 -->
    <el-dialog title="查看详情" v-model="viewDialogVisible" width="500px" destroy-on-close>
      <TableDetail :data="viewData">
        <template #status="{ rows }">
          <el-tag type="success" v-if="rows.status">启用</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
      </TableDetail>
      <span slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, onMounted, watch} from 'vue';
import {ElMessage} from 'element-plus';
import {getAllFeedback, deleteFeedback} from '@/api'
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import TableSearch from '@/components/table-search.vue';
import {FormOptionList} from '@/types/form-option';
import {Feedback} from "@/types/feedback";

const filteredFeedbackList = ref<Feedback[]>([]);
const paginatedFeedbackList = ref<Feedback[]>([]);


// 查询相关
const query = reactive({
  userId: '',
});

const searchOpt = ref<FormOptionList[]>([
  {type: 'input', label: '用户ID：', prop: 'userId'},
]);

// 获取所有反馈数据
const fetchFeedbackList = async () => {
  try {
    const res = await getAllFeedback({
      page: page.index,
      size: page.size,
    });
    if (res.data.code === 200) {
      // 对类型进行过滤处理
      paginatedFeedbackList.value = res.data.data.data;
      page.total = res.data.data.pageTotal;

    } else {
      ElMessage.error(res.data.message || '获取反馈列表失败');
    }
  } catch (error) {
    console.error('获取反馈列表错误:', error);
    ElMessage.error('获取反馈列表失败，请稍后再试。');
  }
};

// 搜索功能
const handleSearch = () => {
  if (query.userId === '') {
    fetchFeedbackList();
    return;
  }
  paginatedFeedbackList.value = paginatedFeedbackList.value.filter((item: Feedback) => {
    return item.userId.toString() === query.userId.toString();
  });
};

// 重置搜索条件
const resetForm = () => {
  query.userId = '';
  fetchFeedbackList();
};

// 表格列定义
const columns = ref([
  {type: 'index', label: '序号', width: 55, align: 'center'},
  {prop: 'feedbackId', label: '反馈ID', width: 100, align: 'center'},
  {prop: 'userId', label: '用户ID', width: 100, align: 'center'},
  {prop: 'feedbackType', label: '反馈类型', width: 120, align: 'center'},
  {prop: 'feedbackText', label: '反馈内容'},
  {prop: 'createdTime', label: '提交时间', width: 180, align: 'center'},
  {prop: 'actions', label: '操作', width: 150, align: 'center',slotName: 'actions'},
]);

// 分页相关
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
});

// 更新分页总数
onMounted(() => {
  fetchFeedbackList();
});

// 监听反馈列表变化，更新分页总数
watch(filteredFeedbackList, (newVal) => {
  page.total = newVal.length;
  page.index = 1; // 重置到第一页
});

// 改变页码
const changePage = (val: number) => {
  page.index = val;
};

// 查看详情相关
const viewDialogVisible = ref(false);
const viewData = ref<Feedback>({
  feedbackId: 0,
  userId: 0,
  feedbackText: '',
  createdTime: '',
  feedbackType: ''
});

const handleView = (row: Feedback) => {
  viewData.value = {...row};
  viewDialogVisible.value = true;
};

// 删除反馈相关
const handleDelete = async (row: Feedback) => {
  try {
    const res = await deleteFeedback(row.feedbackId);
    if (res.data.code !== 200) {
      ElMessage.error('删除反馈失败');
    }
    ElMessage.success('删除反馈成功');
    // 刷新列表
    await fetchFeedbackList();
  } catch (error) {
    console.error('删除反馈错误:', error);
    ElMessage.error('删除反馈失败，请稍后再试。');
  }
};
</script>

<style scoped>
.feedback-list-container {
  padding: 20px;
  background-color: #f5f5f5;
}

.container {
  margin-top: 20px;
}
</style>
