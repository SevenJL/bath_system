<template>
    <div>
        <TableSearch :query="query" :options="searchOpt" :search="handleSearch" />
        <div class="container">

            <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                :delFunc="handleDelete" :page-change="changePage" :editFunc="handleEdit">
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
        <el-dialog title="权限管理" v-model="visible2" width="500px" destroy-on-close>
            <RolePermission :permiss-options="permissOptions" />
        </el-dialog>
    </div>
</template>

<script setup lang="ts" name="system-role">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Role } from '@/types/role';
import { fetchRoleData } from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import RolePermission from './role-permission.vue'
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import TableEdit from "@/components/table-edit.vue";
import TableSearch from "@/components/table-search.vue";

// 查询相关
const query = reactive({
    name: '',
});
const searchOpt = ref<FormOptionList[]>([
    { type: 'input', label: '老师姓名：', prop: 'name' }
])
const handleSearch = () => {
    changePage(1);
};

// 表格相关
let columns = ref([
    { type: 'index', label: '序号', width: 55, align: 'center' },
    { prop: 'name', label: '老师姓名' },
    { prop: 'key', label: '老师标识' },
    { prop: 'phone', label: '手机号' },
    { prop: 'identityCard', label: '身份证号码' },
    { prop: 'status', label: '状态' },
    { prop: 'permissions', label: '权限管理' },
    { prop: 'operator', label: '操作', width: 250 },
])

// 表格数据相关
const page = reactive({
    index: 1,
    size: 10,
    total: 0,
})

// 获取表格数据
const tableData = ref<Role[]>([]);
const getData = async () => {
    const res = await fetchRoleData()
    tableData.value = res.data.list;
    page.total = res.data.pageTotal;
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
        { type: 'input', label: '老师姓名', prop: 'name', required: true },
        { type: 'input', label: '老师标识', prop: 'key', required: true },
        { type: 'input', label: '手机号', prop: 'phone', required: true },
        { type: 'input', label: '身份证号码', prop: 'identityCard', required: true },
        { type: 'switch', label: '状态', prop: 'status', required: false, activeText: '启用', inactiveText: '禁用' },
    ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref({});
const handleEdit = (row: Role) => {
    rowData.value = { ...row };
    isEdit.value = true;
    visible.value = true;
};

// 更新数据
const updateData = () => {
    closeDialog();
    getData();
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
const handleView = (row: Role) => {
    viewData.value.row = { ...row }
    viewData.value.list = [
        {
            prop: 'name',
            label: '老师姓名',
        },
        {
            prop: 'key',
            label: '老师标识',
        },
        {
            prop: 'phone',
            label: '手机号',
        },
        {
            prop: 'identityCard',
            label: '身份证号码',
        },
        {
            prop: 'status',
            label: '状态',
        },
    ]
    visible1.value = true;
};

// 删除相关
const handleDelete = (row: Role) => {
    ElMessage.success('删除成功');
}


// 权限管理弹窗相关
const visible2 = ref(false);
const permissOptions = ref({})
const handlePermission = (row: Role) => {
    visible2.value = true;
    permissOptions.value = {
        id: row.id,
        permiss: row.permiss
    };
}
</script>

<style scoped></style>