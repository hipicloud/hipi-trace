import request from '@/utils/request'

// 查询批次列表
export function listBatch(query) {
  return request({
    url: '/code/traceBatch/page',
    method: 'get',
    params: query
  })
}

// 所有批次列表
export function selectList(query) {
  return request({
    url: '/code/categoryManage/selectList',
    method: 'get',
    params: query
  })
}

// 新增批次
export function addBatch(data) {
  return request({
    url: '/code/traceBatch/save',
    method: 'post',
    data: data
  })
}

// 修改批次
export function genCode(data) {
  return request({
    url: '/code/traceBatch/genCode',
    method: 'post',
    data: data
  })
}

// 更改状态
export function updateStatus(id) {
  return request({
    url: '/code/traceBatch/updateStatus/' + id,
    method: 'put'

  })
}

//根据id查详情
export function getById(id) {
  return request({
    url: '/code/traceBatch/getById/' + id,
    method: 'get'
  })
}

export function getTraceById(id) {
  return request({
    url: '/code/traceabilityManage/getById/' + id,
    method: 'get'
  })
}

//配置
export function configure(data) {
  return request({
    url: '/code/traceBatch/configure',
    method: 'post',
    data: data
  })
}

//下载
export function downloadCode(data) {
  return request({
    url: '/code/traceBatch/downloadCode/' + data,
    method: 'get',
    responseType: 'blob'
  })
}
  
  

