<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import { getEmpGenderDataApi, getEmpJobDataApi } from '@/api/report';

const genderChartRef = ref(null)
const jobChartRef = ref(null)
let genderChart = null
let jobChart = null

// 饼图通用配置 - 悬停放大的 emphasise 效果
const pieOption = {
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c}人 ({d}%)'
  },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    center: ['50%', '55%'],
    avoidLabelOverlap: true,
    itemStyle: {
      borderRadius: 6,
      borderColor: '#fff',
      borderWidth: 2
    },
    label: {
      show: true,
      formatter: '{b}\n{d}%'
    },
    emphasis: {
      scale: true,
      label: {
        show: true,
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    data: []
  }]
}

const COLORS = ['#409EFF', '#F56C6C', '#67C23A', '#E6A23C', '#909399', '#B37FEB', '#00CED1']

onMounted(() => {
  loadData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  genderChart?.dispose()
  jobChart?.dispose()
})

const handleResize = () => {
  genderChart?.resize()
  jobChart?.resize()
}

const loadData = async () => {
  const genderResult = await getEmpGenderDataApi()
  if (genderResult.code) {
    const data = genderResult.data.map((item, i) => ({
      ...item,
      itemStyle: { color: COLORS[i % COLORS.length] }
    }))
    genderChart = echarts.init(genderChartRef.value)
    genderChart.setOption({
      ...pieOption,
      title: { text: '员工性别分布', left: 'center' },
      series: [{ ...pieOption.series[0], data }]
    })
  }

  const jobResult = await getEmpJobDataApi()
  if (jobResult.code) {
    const data = jobResult.data.jobList.map((name, i) => ({
      name,
      value: jobResult.data.dataList[i],
      itemStyle: { color: COLORS[i % COLORS.length] }
    }))
    jobChart = echarts.init(jobChartRef.value)
    jobChart.setOption({
      ...pieOption,
      title: { text: '员工职位分布', left: 'center' },
      series: [{ ...pieOption.series[0], data }]
    })
  }
}
</script>

<template>
  <h1>员工信息统计</h1>
  <el-row :gutter="30">
    <el-col :span="12">
      <el-card shadow="hover">
        <div ref="genderChartRef" style="height: 400px;"></div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card shadow="hover">
        <div ref="jobChartRef" style="height: 400px;"></div>
      </el-card>
    </el-col>
  </el-row>
</template>
