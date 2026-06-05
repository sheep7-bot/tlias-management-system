<script setup>
import { ref, onMounted } from 'vue';
import { queryPageApi } from '@/api/log';

//钩子函数
onMounted(() => {
  search()
})

//查询
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const logList = ref([])

const search = async () => {
  const result = await queryPageApi(currentPage.value, pageSize.value)
  if (result.code) {
    logList.value = result.data.rows
    total.value = result.data.total
  }
}

//分页
const handleSizeChange = () => search()
const handleCurrentChange = () => search()

const getSimpleClassName = (className) => {
  if (!className) return ''
  const parts = className.split('.')
  return parts[parts.length - 1]
}
</script>

<template>
  <h1>日志信息统计</h1>

  <div class="container">
    <el-table :data="logList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column prop="operateEmpName" label="操作人" width="100" align="center" />
      <el-table-column prop="operateTime" label="操作时间" width="180" align="center" />
      <el-table-column label="类名" width="200">
        <template #default="scope">
          <span :title="scope.row.className">{{ getSimpleClassName(scope.row.className) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="methodName" label="方法名" width="120" align="center" />
      <el-table-column label="方法参数" min-width="200">
        <template #default="scope">
          <el-tooltip :content="scope.row.methodParams" placement="top" effect="dark">
            <span class="ellipsis-text">{{ scope.row.methodParams }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="返回值" min-width="200">
        <template #default="scope">
          <el-tooltip :content="scope.row.returnValue" placement="top" effect="dark">
            <span class="ellipsis-text">{{ scope.row.returnValue }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="costTime" label="耗时(ms)" width="100" align="center" />
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
</template>

<style scoped>
.container {
  margin: 15px 0px;
}
.ellipsis-text {
  display: inline-block;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
