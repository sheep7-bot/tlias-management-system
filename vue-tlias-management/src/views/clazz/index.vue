<script setup>
import { ref, onMounted } from 'vue';
import { queryPageApi, addApi, queryByIdApi, updateApi, deleteByIdApi } from '@/api/clazz';
import { queryAllApi as queryAllEmpApi } from '@/api/emp';
import { ElMessage, ElMessageBox } from 'element-plus'

//钩子函数
onMounted(() => {
  search();
  queryMasters();
})

//查询
const searchName = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const clazzList = ref([])

const search = async () => {
  const result = await queryPageApi(searchName.value, '', '', currentPage.value, pageSize.value)
  if (result.code) {
    clazzList.value = result.data.rows
    total.value = result.data.total
  }
}

//学科选项
const subjects = ref([
  { name: 'Java', value: 1 },
  { name: 'Python', value: 2 },
  { name: '前端', value: 3 }
])

//班主任列表
const masters = ref([])
const queryMasters = async () => {
  const result = await queryAllEmpApi()
  if (result.code) {
    masters.value = result.data
  }
}

//Dialog对话框
const dialogFormVisible = ref(false)
const formTitle = ref('')
const clazz = ref({ name: '', room: '', beginDate: '', endDate: '', masterId: '', subject: '' })

//新增
const addClazz = () => {
  dialogFormVisible.value = true
  formTitle.value = '新增班级'
  clazz.value = { name: '', room: '', beginDate: '', endDate: '', masterId: '', subject: '' }
  if (clazzFormRef.value) clazzFormRef.value.resetFields()
}

//保存
const save = async () => {
  if (!clazzFormRef.value) return
  clazzFormRef.value.validate(async (valid) => {
    if (valid) {
      let result
      if (clazz.value.id) {
        result = await updateApi(clazz.value)
      } else {
        result = await addApi(clazz.value)
      }
      if (result.code) {
        ElMessage.success('操作成功')
        dialogFormVisible.value = false
        search()
      } else {
        ElMessage.error(result.msg)
      }
    } else {
      ElMessage.error('表单校验不通过')
    }
  })
}

//表单校验
const rules = ref({
  name: [{ required: true, message: '班级名称是必填项', trigger: 'blur' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }],
  beginDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
})
const clazzFormRef = ref()

//编辑
const edit = async (id) => {
  formTitle.value = '修改班级'
  if (clazzFormRef.value) clazzFormRef.value.resetFields()
  const result = await queryByIdApi(id)
  if (result.code) {
    dialogFormVisible.value = true
    clazz.value = result.data
  }
}

//删除
const delById = async (id) => {
  ElMessageBox.confirm('您确认删除该班级吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    const result = await deleteByIdApi(id)
    if (result.code) {
      ElMessage.success('删除成功')
      search()
    } else {
      ElMessage.error(result.msg)
    }
  }).catch(() => {
    ElMessage.info('您已取消删除')
  })
}

//分页
const handleSizeChange = () => search()
const handleCurrentChange = () => search()

const getSubjectName = (val) => {
  const s = subjects.value.find(item => item.value === val)
  return s ? s.name : '未知'
}
</script>

<template>
  <h1>班级管理</h1>

  <div class="container">
    <el-form :inline="true">
      <el-form-item label="班级名称">
        <el-input v-model="searchName" placeholder="请输入班级名称" clearable @keyup.enter="search" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="searchName = ''; search()">清空</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="container">
    <el-button type="primary" @click="addClazz"><el-icon><Plus /></el-icon> 新增班级</el-button>
  </div>

  <div class="container">
    <el-table :data="clazzList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="80" align="center" />
      <el-table-column prop="name" label="班级名称" width="200" />
      <el-table-column prop="room" label="教室" width="100" align="center" />
      <el-table-column label="学科" width="100" align="center">
        <template #default="scope">{{ getSubjectName(scope.row.subject) }}</template>
      </el-table-column>
      <el-table-column prop="masterName" label="班主任" width="120" align="center" />
      <el-table-column prop="beginDate" label="开始日期" width="130" align="center" />
      <el-table-column prop="endDate" label="结束日期" width="130" align="center" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === '已结课' ? 'info' : 'success'">
            {{ scope.row.status || '在读中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" @click="edit(scope.row.id)"><el-icon><EditPen /></el-icon> 编辑</el-button>
          <el-button type="danger" size="small" @click="delById(scope.row.id)"><el-icon><Delete /></el-icon> 删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20, 50]"
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>

  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="600">
    <el-form :model="clazz" :rules="rules" ref="clazzFormRef" label-width="100px">
      <el-form-item label="班级名称" prop="name">
        <el-input v-model="clazz.name" placeholder="请输入班级名称" />
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="教室" prop="room">
            <el-input v-model="clazz.room" placeholder="如 A101" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学科" prop="subject">
            <el-select v-model="clazz.subject" placeholder="请选择学科" style="width: 100%">
              <el-option v-for="s in subjects" :key="s.value" :label="s.name" :value="s.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期" prop="beginDate">
            <el-date-picker v-model="clazz.beginDate" type="date" style="width: 100%" placeholder="选择开始日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker v-model="clazz.endDate" type="date" style="width: 100%" placeholder="选择结束日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="班主任">
        <el-select v-model="clazz.masterId" placeholder="请选择班主任" style="width: 100%" clearable>
          <el-option v-for="m in masters" :key="m.id" :label="m.name" :value="m.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.container {
  margin: 15px 0px;
}
</style>
