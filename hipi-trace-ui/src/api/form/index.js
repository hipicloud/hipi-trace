import request from '@/utils/request'

// 根据目录id查询动态字段
export function getCatalogueByDataId(query) {
  return request({
    url: '/code/catalogueForm/getCatalogueByDataId/' + query,
    method: 'get'
  })
}
