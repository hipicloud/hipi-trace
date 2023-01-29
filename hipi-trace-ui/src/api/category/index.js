import request from '@/utils/request'

// 查询品类列表
export function listCategory(query) {
  return request({
    url: '/code/categoryManage/page',
    method: 'get',
    params: query
  })
}

// 所有品类列表
export function selectList(query) {
  return request({
    url: '/code/categoryManage/selectList',
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
    url: '/code/categoryManage/save',
    method: 'post',
    data: data
  })
}

// 修改品类
export function updateCategory(data) {
  return request({
    url: '/code/categoryManage/update',
    method: 'put',
    data: data
  })
}

// 删除品类
export function delCategory(Id) {
  return request({
    url: '/code/categoryManage/deleteById/' + Id,
    method: 'delete'
  })
}

//根据id查详情
export function getById(id) {
  return request({
    url: '/code/categoryManage/getById/' + id,
    method: 'get'
  })
}

