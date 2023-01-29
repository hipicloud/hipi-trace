import request from '@/utils/request'

// 查询溯源节点列表
export function listTraceability(query) {
  return request({
    url: '/code/traceabilityManage/page',
    method: 'get',
    params: query
  })
}

// 查询树列表
export function treeList(query) {
  return request({
    url: '/code/traceabilityCatalogue/selectTreeList',
    method: 'get',
    params: query
  })
}

// 新增溯源节点
export function addTraceability(data) {
  return request({
    url: '/code/traceabilityManage/save',
    method: 'post',
    data: data
  })
}

// 修改溯源节点
export function updateTraceability(data) {
  return request({
    url: '/code/traceabilityManage/update',
    method: 'put',
    data: data
  })
}

// 删除溯源节点
export function delTraceability(deptId) {
  return request({
    url: '/code/traceabilityManage/deleteById/' + deptId,
    method: 'delete'
  })
}


//根据id查详情
export function getById(id) {
  return request({
    url: '/code/traceabilityManage/getById/' + id,
    method: 'get'
  })
}

