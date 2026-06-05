import request from "@/utils/request";

// 学员分页查询
export const queryPageApi = (name, degree, clazzId, page, pageSize) =>
  request.get(`/students?name=${name || ''}&degree=${degree || ''}&clazzId=${clazzId || ''}&page=${page}&pageSize=${pageSize}`)

// 根据ID查询
export const queryByIdApi = (id) => request.get(`/students/${id}`)

// 新增学员
export const addApi = (student) => request.post('/students', student)

// 修改学员
export const updateApi = (student) => request.put('/students', student)

// 批量删除学员（路径参数传ids列表）
export const deleteByIdApi = (ids) => request.delete(`/students/${ids}`)

// 违纪扣分处理
export const violationHandleApi = (id, score) => request.put(`/students/violation/${id}/${score}`)
