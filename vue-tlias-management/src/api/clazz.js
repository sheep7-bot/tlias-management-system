import request from "@/utils/request";

// 班级分页查询
export const queryPageApi = (name, begin, end, page, pageSize) =>
  request.get(`/clazzs?name=${name || ''}&begin=${begin || ''}&end=${end || ''}&page=${page}&pageSize=${pageSize}`)

// 查询全部班级列表
export const queryAllApi = () => request.get('/clazzs/list')

// 根据ID查询
export const queryByIdApi = (id) => request.get(`/clazzs/${id}`)

// 新增班级
export const addApi = (clazz) => request.post('/clazzs', clazz)

// 修改班级
export const updateApi = (clazz) => request.put('/clazzs', clazz)

// 删除班级
export const deleteByIdApi = (id) => request.delete(`/clazzs/${id}`)
