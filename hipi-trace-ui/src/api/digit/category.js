import request from '@/utils/request'

// 查询品类列表
export function listCategory(query) {
  return request({
    url: '/code/categoryCatalogue/selectLastPage',
    method: 'get',
    params: query
  })
}

// 查询树列表
export function treeList(query) {
  return request({
    url: '/code/categoryCatalogue/selectTreeList',
    method: 'get',
    params: query
  })
}

// 新增品类
export function addCategory(data) {
  return request({
    url: '/code/categoryCatalogue/save',
    method: 'post',
    data: data
  })
}

// 修改品类
export function updateCategory(data) {
  return request({
    url: '/code/categoryCatalogue/update',
    method: 'put',
    data: data
  })
}

// 删除品类
export function delCategory(deptId) {
  return request({
    url: '/code/categoryCatalogue/deleteById/' + deptId,
    method: 'delete'
  })
}

//根据id查详情
export function getById(id) {
  return request({
    url: '/code/categoryCatalogue/getById/' + id,
    method: 'get'
  })
}

