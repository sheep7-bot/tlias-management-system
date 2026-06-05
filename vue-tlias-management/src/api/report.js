import request from "@/utils/request";

// 员工性别统计
export const getEmpGenderDataApi = () => request.get('/report/empGenderData')

// 员工职位统计
export const getEmpJobDataApi = () => request.get('/report/empJobData')

// 学员学历统计
export const getStudentDegreeDataApi = () => request.get('/report/studentDegreeData')

// 班级人数统计
export const getStudentCountDataApi = () => request.get('/report/studentCountData')
