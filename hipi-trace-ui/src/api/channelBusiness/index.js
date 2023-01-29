import request from '@/utils/request'

// 查询渠道商列表
export function listChannelBusiness(query) {
  return request({

    url: '/code/distributorManage/page',
    method: 'get',
    params: query
  })
}

// 查询树列表
export function treeList(query) {
  return request({
    url: '/code/distributorCatalogue/selectTreeList',
    method: 'get',
    params: query
  })
}

// 新增渠道商
export function addChannelBusiness(data) {
  return request({
    url: '/code/distributorManage/save',
    method: 'post',
    data: data
  })
}

// 修改渠道商
export function updateChannelBusiness(data) {
  return request({
    url: '/code/distributorManage/update',
    method: 'put',
    data: data
  })
}

// 删除渠道商
export function delChannelBusiness(deptId) {
  return request({
    url: '/code/distributorManage/deleteById/' + deptId,
    method: 'delete'
  })
}

//根据id查详情
export function getById(id) {
  return request({
    url: '/code/distributorManage/getById/' + id,
    method: 'get'
  })
}

