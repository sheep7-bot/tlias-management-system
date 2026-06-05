<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import { getStudentDegreeDataApi, getStudentCountDataApi } from '@/api/report';

const degreeChartRef = ref(null)
const classChartRef = ref(null)
let degreeChart = null
let classChart = null

// 饼图通用配置 - 悬停放大
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
  degreeChart?.dispose()
  classChart?.dispose()
})

const handleResize = () => {
  degreeChart?.resize()
  classChart?.resize()
}

const loadData = async () => {
  const degreeResult = await getStudentDegreeDataApi()
  if (degreeResult.code) {
    const data = degreeResult.data.map((item, i) => ({
      ...item,
      itemStyle: { color: COLORS[i % COLORS.length] }
    }))
    degreeChart = echarts.init(degreeChartRef.value)
    degreeChart.setOption({
      ...pieOption,
      title: { text: '学员学历分布', left: 'center' },
      series: [{ ...pieOption.series[0], data }]
    })
  }

  const classResult = await getStudentCountDataApi()
  if (classResult.code) {
    const data = classResult.data.clazzList.map((name, i) => ({
      name,
      value: classResult.data.dataList[i],
      itemStyle: { color: COLORS[i % COLORS.length] }
    }))
    classChart = echarts.init(classChartRef.value)
    classChart.setOption({
      ...pieOption,
      title: { text: '班级人数分布', left: 'center' },
      series: [{ ...pieOption.series[0], data }]
    })
  }
}
</script>

<template>
  <h1>学员信息统计</h1>
  <el-row :gutter="30">
    <el-col :span="12">
      <el-card shadow="hover">
        <div ref="degreeChartRef" style="height: 400px;"></div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card shadow="hover">
        <div ref="classChartRef" style="height: 400px;"></div>
      </el-card>
    </el-col>
  </el-row>
</template>
