<template>
  <div>
    <TableSearch :query="query" :options="searchOpt" :search="handleSearch"/>
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
      <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData"/>
    </el-dialog>
    <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
      <TableDetail :data="viewData">
        <template #status="{ rows }">
          <el-tag type="success" v-if="rows.status">启用</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
      </TableDetail>
    </el-dialog>
    <el-dialog title="权限管理" v-model="visible2" width="500px" destroy-on-close>
      <RolePermission :permiss-options="permissOptions"/>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="system-teacher">
import {ref, reactive, watch} from 'vue';
import {ElMessage} from 'element-plus';
import {addTeacher, deleteTeacher, getTeacherDataByPage, updateTeacher} from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import RolePermission from './role-permission.vue'
import {CirclePlusFilled} from '@element-plus/icons-vue';
import {FormOption, FormOptionList} from '@/types/form-option';
import TableEdit from "@/components/table-edit.vue";
import TableSearch from "@/components/table-search.vue";
import {Teacher} from "@/types/teacher";
import {Student} from "@/types/student";

// 查询相关
const query = reactive({
  name: '',
});
const searchOpt = ref<FormOptionList[]>([
  {type: 'input', label: '老师姓名：', prop: 'name'}
])
const handleSearch = () => {
  // 对数据进行筛选
  tableData.value = tableData.value.filter((item: Teacher) => {
    return item.name.includes(query.name);
  });
};

// watch监听要查询的数据变化
watch(query, (value, oldValue, onCleanup) => {
  tableData.value = tableData.value.filter((item: Teacher) => {
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
  {prop: 'name', label: '姓名'},
  {prop: 'age', label: '年龄'},
  {prop: 'subject', label: '科目'},
  {prop: 'idNumber', label: '身份证号'},
  {prop: 'phone', label: '手机号'},
  {prop: 'operator', label: '操作', width: 250},
])

// 表格数据相关
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})

// 获取表格数据
const tableData = ref<Teacher[]>([]);
const getData = async () => {
  console.log("page:" + page.index, "size" + page.size)
  const res = await getTeacherDataByPage({
    page: page.index,
    size: page.size,
  })
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
    {type: 'input', label: '老师姓名', prop: 'name', required: true},
    {type: 'input', label: '年龄', prop: 'age', required: true},
    {type: 'input', label: '科目', prop: 'subject', required: true},
    {type: 'input', label: '身份证号码', prop: 'idNumber', required: true},
    {type: 'input', label: '手机号', prop: 'phone', required: true},
  ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref({});
const handleEdit = (row: Teacher) => {
  rowData.value = {...row};
  isEdit.value = true;
  visible.value = true;
};

// 更新数据
const updateData = async (data: Teacher) => {
  // 判断是新增还是编辑
  if (data.teacherId == null) {
    // 调用新增接口
    const res: any = await addTeacher(data);
    if (res.data.code !== 200) {
      ElMessage.error('新增失败');
      return;
    }
    ElMessage.success('新增成功');
    closeDialog();
    await getData();
    return;
  }
  // 调用更新接口
  const res: any = await updateTeacher(data);
  if (res.data.code !== 200) {
    ElMessage.error('更新失败');
    return;
  }
  ElMessage.success('更新成功');
  closeDialog();
  await getData();
};

// 关闭弹窗
const closeDialog = () => {
  visible.value = false;
  isEdit.value = false;
  rowData.value = {};
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
  row: {},
  list: [],
  column: 1
});

// 查看详情
const handleView = (row: Teacher) => {
  viewData.value.row = {...row}
  viewData.value.list = [
    {
      prop: 'name',
      label: '老师姓名',
    },
    {
      prop: 'phone',
      label: '手机号',
    },
    {
      prop: 'idNumber',
      label: '身份证号码',
    },
    {
      prop: 'subject',
      label: '科目',
    },
    {
      prop: 'age',
      label: '年龄',
    },
  ]
  visible1.value = true;
};

// 删除相关
const handleDelete = async (row: Teacher) => {
  // 调用删除接口
  const res: any = await deleteTeacher(row.teacherId);
  if (res.data.code !== 200) {
    ElMessage.error('删除失败');
    return;
  }
  ElMessage.success('删除成功');
  await getData();
}


// 权限管理弹窗相关
const visible2 = ref(false);
const permissOptions = ref({})
const handlePermission = (row: Teacher) => {
  visible2.value = true;
  permissOptions.value = {
    id: row.teacherId,
    permiss: row.permiss
  };
}
</script>

<style scoped></style>