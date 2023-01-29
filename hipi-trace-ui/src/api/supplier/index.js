import request from '@/utils/request'

// 查询供应商列表
export function listSupplier(query) {
  return request({
    url: '/code/supplierManage/page',
    method: 'get',
    params: query
  })
}

// 查询树列表
export function treeList(query) {
  return request({
    url: '/code/supplierCatalogue/selectTreeList',
    method: 'get',
    params: query
  })
}

// 新增供应商
export function addSupplier(data) {
  return request({
    url: '/code/supplierManage/save',
    method: 'post',
    data: data
  })
}

// 修改供应商
export function updateSupplier(data) {
  return request({
    url: '/code/supplierManage/update',
    method: 'put',
    data: data
  })
}

// 删除供应商
export function delSupplier(deptId) {
  return request({
    url: '/code/supplierManage/deleteById/' + deptId,
    method: 'delete'
  })
}

//根据id查详情
export function getById(id) {
  return request({
    url: '/code/supplierManage/getById/' + id,
    method: 'get'
  })
}

