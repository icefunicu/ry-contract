import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listContract(query) {
  return request({
    url: '/system/contract/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getContract(id) {
  return request({
    url: '/system/contract/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addContract(data) {
  return request({
    url: '/system/contract',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateContract(data) {
  return request({
    url: '/system/contract',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delContract(id) {
  return request({
    url: '/system/contract/' + id,
    method: 'delete'
  })
}
