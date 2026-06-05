<script setup>
import { ref, onMounted } from 'vue';
import { queryPageApi, addApi, queryByIdApi, updateApi, deleteByIdApi, violationHandleApi } from '@/api/student';
import { queryAllApi as queryAllClazzApi } from '@/api/clazz';
import { ElMessage, ElMessageBox } from 'element-plus'

//钩子函数
onMounted(() => {
  search()
  queryClazzList()
})

//查询
const searchName = ref('')
const searchDegree = ref('')
const searchClazzId = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const studentList = ref([])

const search = async () => {
  const result = await queryPageApi(searchName.value, searchDegree.value, searchClazzId.value, currentPage.value, pageSize.value)
  if (result.code) {
    studentList.value = result.data.rows
    total.value = result.data.total
  }
}

//班级列表（搜索下拉用）
const clazzList = ref([])
const queryClazzList = async () => {
  const result = await queryAllClazzApi()
  if (result.code) {
    clazzList.value = result.data
  }
}

//学历选项
const degrees = ref([
  { name: '初中', value: 1 },
  { name: '高中', value: 2 },
  { name: '大专', value: 3 },
  { name: '本科', value: 4 },
  { name: '硕士', value: 5 },
  { name: '博士', value: 6 }
])

//Dialog对话框
const dialogFormVisible = ref(false)
const formTitle = ref('')
const student = ref({
  name: '', no: '', gender: '', phone: '', idCard: '',
  isCollege: '', address: '', degree: '', graduationDate: '', clazzId: ''
})

//新增
const addStudent = () => {
  dialogFormVisible.value = true
  formTitle.value = '新增学员'
  student.value = {
    name: '', no: '', gender: '', phone: '', idCard: '',
    isCollege: '', address: '', degree: '', graduationDate: '', clazzId: ''
  }
  if (studentFormRef.value) studentFormRef.value.resetFields()
}

//保存
const save = async () => {
  if (!studentFormRef.value) return
  studentFormRef.value.validate(async (valid) => {
    if (valid) {
      let result
      if (student.value.id) {
        result = await updateApi(student.value)
      } else {
        result = await addApi(student.value)
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
  name: [{ required: true, message: '学员姓名是必填项', trigger: 'blur' }],
  no: [{ required: true, message: '学号是必填项', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '手机号是必填项', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }
  ],
  clazzId: [{ required: true, message: '请选择班级', trigger: 'change' }]
})
const studentFormRef = ref()

//编辑
const edit = async (id) => {
  formTitle.value = '修改学员'
  if (studentFormRef.value) studentFormRef.value.resetFields()
  const result = await queryByIdApi(id)
  if (result.code) {
    dialogFormVisible.value = true
    student.value = result.data
  }
}

//删除
const delById = async (id) => {
  ElMessageBox.confirm('您确认删除该学员吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    const result = await deleteByIdApi([id])
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

//批量删除
const selectedIds = ref([])
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}
const delByIds = () => {
  if (!selectedIds.value.length) {
    ElMessage.info('请选择要删除的数据')
    return
  }
  ElMessageBox.confirm('您确认批量删除选中的学员吗?', '提示',
    { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    const result = await deleteByIdApi(selectedIds.value)
    if (result.code) {
      ElMessage.success('批量删除成功')
      search()
    } else {
      ElMessage.error(result.msg)
    }
  }).catch(() => {
    ElMessage.info('您已取消删除')
  })
}

//违纪扣分
const violationDialogVisible = ref(false)
const violationStudent = ref({ id: '', name: '' })
const violationScore = ref(0)

const openViolation = (row) => {
  violationStudent.value = { id: row.id, name: row.name }
  violationScore.value = 0
  violationDialogVisible.value = true
}
const submitViolation = async () => {
  if (!violationScore.value || violationScore.value <= 0) {
    ElMessage.warning('请输入扣分数值')
    return
  }
  const result = await violationHandleApi(violationStudent.value.id, violationScore.value)
  if (result.code) {
    ElMessage.success('违纪扣分处理成功')
    violationDialogVisible.value = false
    search()
  } else {
    ElMessage.error(result.msg)
  }
}

//分页
const handleSizeChange = () => search()
const handleCurrentChange = () => search()

const getDegreeName = (val) => {
  const d = degrees.value.find(item => item.value === val)
  return d ? d.name : '未知'
}
</script>

<template>
  <h1>学员管理</h1>

  <div class="container">
    <el-form :inline="true">
      <el-form-item label="姓名">
        <el-input v-model="searchName" placeholder="请输入学员姓名" clearable @keyup.enter="search" />
      </el-form-item>
      <el-form-item label="学历">
        <el-select v-model="searchDegree" placeholder="请选择" clearable style="width:120px">
          <el-option v-for="d in degrees" :key="d.value" :label="d.name" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="班级">
        <el-select v-model="searchClazzId" placeholder="请选择" clearable style="width:180px">
          <el-option v-for="c in clazzList" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button @click="searchName = ''; searchDegree = ''; searchClazzId = ''; search()">清空</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="container">
    <el-button type="primary" @click="addStudent"><el-icon><Plus /></el-icon> 新增学员</el-button>
    <el-button type="danger" @click="delByIds"><el-icon><Delete /></el-icon> 批量删除</el-button>
  </div>

  <div class="container">
    <el-table :data="studentList" border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column prop="name" label="姓名" width="100" align="center" />
      <el-table-column prop="no" label="学号" width="130" align="center" />
      <el-table-column label="性别" width="70" align="center">
        <template #default="scope">{{ scope.row.gender == 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="140" align="center" />
      <el-table-column label="学历" width="80" align="center">
        <template #default="scope">{{ getDegreeName(scope.row.degree) }}</template>
      </el-table-column>
      <el-table-column prop="clazzName" label="所属班级" width="180" />
      <el-table-column prop="violationCount" label="违纪次数" width="100" align="center" />
      <el-table-column prop="violationScore" label="违纪分数" width="100" align="center" />
      <el-table-column label="操作" align="center" width="280">
        <template #default="scope">
          <el-button type="primary" size="small" @click="edit(scope.row.id)"><el-icon><EditPen /></el-icon> 编辑</el-button>
          <el-button type="warning" size="small" @click="openViolation(scope.row)"><el-icon><WarningFilled /></el-icon> 违纪扣分</el-button>
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

  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="700">
    <el-form :model="student" :rules="rules" ref="studentFormRef" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="学员姓名" prop="name">
            <el-input v-model="student.name" placeholder="请输入姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学号" prop="no">
            <el-input v-model="student.no" placeholder="请输入学号" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select v-model="student.gender" placeholder="请选择" style="width:100%">
              <el-option label="男" :value="1" />
              <el-option label="女" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="student.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="身份证号">
            <el-input v-model="student.idCard" placeholder="请输入身份证号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学历">
            <el-select v-model="student.degree" placeholder="请选择" style="width:100%">
              <el-option v-for="d in degrees" :key="d.value" :label="d.name" :value="d.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="毕业院校">
            <el-select v-model="student.isCollege" placeholder="是否毕业" style="width:100%">
              <el-option label="是" :value="1" />
              <el-option label="否" :value="0" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="毕业日期">
            <el-date-picker v-model="student.graduationDate" type="date" style="width:100%" placeholder="选择日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="所在班级" prop="clazzId">
        <el-select v-model="student.clazzId" placeholder="请选择班级" style="width:100%">
          <el-option v-for="c in clazzList" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="student.address" placeholder="请输入地址" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="violationDialogVisible" title="违纪扣分处理" width="400">
    <el-form label-width="100px">
      <el-form-item label="学员姓名">
        <el-input :model-value="violationStudent.name" disabled />
      </el-form-item>
      <el-form-item label="扣分数值">
        <el-input-number v-model="violationScore" :min="1" :max="100" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="violationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitViolation">确认扣分</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
.container {
  margin: 15px 0px;
}
</style>
