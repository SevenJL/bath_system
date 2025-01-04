<template>
  <div>
    <TableSearch :query="query" :options="searchOpt" :search="handleSearch" :reset-form="resetForm"/>
    <div class="container">
      <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                   :delFunc="handleDelete" :changePage="changePage" :editFunc="handleEdit" :currentPage="page.index">
        <template #toolbarBtn>
          <el-button type="warning" :icon="CirclePlusFilled" @click="visible = true">新增</el-button>
        </template>
        <template #status="{ rows }">
          <el-tag type="success" v-if="rows.status">启用</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
        <template #permissions="{ rows }">
          <el-button type="primary" size="small" plain @click="handlePermission(rows)">管理</el-button>
        </template>
      </TableCustom>
    </div>
    <el-dialog :title="isEdit ? '编辑' : '新增'" v-model="visible" width="700px" destroy-on-close
               :close-on-click-modal="false" @close="closeDialog">
      <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData" />
    </el-dialog>
    <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
      <TableDetail :data="viewData">
        <template #status="{ rows }">
          <el-tag type="success" v-if="rows.status">启用</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
      </TableDetail>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="system-teacher">
import {ref, reactive, watch} from 'vue';
import {ElMessage} from 'element-plus';
import {addStudent, deleteStudent, getStudentDataByPage, updateStudent} from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import RolePermission from './role-permission.vue'
import {CirclePlusFilled} from '@element-plus/icons-vue';
import {FormOption, FormOptionList} from '@/types/form-option';
import TableSearch from "@/components/table-search.vue";
import TableEdit from "@/components/table-edit.vue";
import {Student} from "@/types/student";

// 查询相关
const query = reactive({
  name: '',
});
const searchOpt = ref<FormOptionList[]>([
  {type: 'input', label: '学生名称：', prop: 'name'}
])
const handleSearch = () => {
  // 对数据进行筛选
  tableData.value = tableData.value.filter((item: Student) => {
    return item.name.includes(query.name);
  });
};
// watch监听要查询的数据变化
watch(query, (value, oldValue, onCleanup) => {
  tableData.value = tableData.value.filter((item: Student) => {
    return item.name.includes(query.name);
  });
  if(query.name === ''){
    getData();
  }
});

// 重置查询条件
const resetForm = () => {
  query.name = '';
  getData();
}

// 表格相关
let columns = ref([
  {type: 'index', label: '序号', width: 55, align: 'center'},
  {prop: 'name', label: '学生姓名'},
  {prop: 'studentNumber', label: '学号'},
  {prop: 'grade', label: '年级'},
  {prop: 'studentClass', label: '班级'},
  {prop: 'operator', label: '操作', width: 250},
])

const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})

// 表格数据相关
const tableData = ref<Student[]>([]);
const getData = async () => {
  const res = await getStudentDataByPage({
    page: page.index,
    size: page.size,
  });
  tableData.value = res.data.data.data;
  page.total = res.data.data.pageTotal;
};
getData();
const changePage = (val: number) => {
  page.index = val;
  getData();
};

// 新增/编辑弹窗相关
const options = ref<FormOption>({
  labelWidth: '100px',
  span: 24,
  list: [
    {type: 'input', label: '学生姓名', prop: 'name', required: true},
    {type: 'input', label: '学号', prop: 'studentNumber', required: true},
    {type: 'input', label: '年级', prop: 'grade', required: true},
    {type: 'input', label: '班级', prop: 'studentClass', required: true},
  ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref<Student>(<Student>{});
const handleEdit = (row: Student) => {

  rowData.value = {...row};
  isEdit.value = true;
  visible.value = true;
};


// 更新数据
const updateData = async (data:Student) => {
  if (data.studentId == null) {
    const res = await addStudent(data)
    if (res.data.code !== 200) {
      ElMessage.error('新增失败');
      return;
    }
    ElMessage.success('新增成功');
    await getData();
  }
  // 调用更新接口
  const res = await updateStudent(data)
  if (res.data.code !== 200) {
    ElMessage.error('更新失败');
    return;
  }
  closeDialog();
  await getData();
};
// 关闭弹窗
const closeDialog = () => {
  console.log("关闭弹窗")
  console.log(rowData.value)
  visible.value = false;
  isEdit.value = false;
  rowData.value = <Student>{}
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
  row: {},
  list: [],
  column: 1
});
const handleView = (row: Student) => {
  console.log("查看详情")
  viewData.value.row = {...row}
  viewData.value.list = [
    {label: '学生姓名', value: row.name},
    {label: '学号', value: row.studentNumber},
    {label: '年级', value: row.grade},
    {label: '班级', value: row.studentClass},
  ]
  visible1.value = true;
};

// 删除相关
const handleDelete = async (row: Student) => {
  // 删除操作
  const res: any = await deleteStudent(Number(row.studentId))
  if (res.data.code !== 200) {
    ElMessage.error('删除失败');
    return;
  }
  // 刷新列表
  ElMessage.success('删除成功');
  // 刷新当前页
  window.location.reload();
}


// 权限管理弹窗相关
const visible2 = ref(false);
const permissOptions = ref({})
const handlePermission = (row: Student) => {
  visible2.value = true;
  permissOptions.value = {
    id: row.studentId,
    permiss: row.permiss
  };
}
</script>

<style scoped></style>
