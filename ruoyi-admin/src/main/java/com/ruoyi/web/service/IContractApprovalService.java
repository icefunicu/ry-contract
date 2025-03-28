package com.ruoyi.web.service;

import java.util.List;
import com.ruoyi.web.domain.ContractApproval;

/**
 * 合同流程Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IContractApprovalService 
{
    /**
     * 查询合同流程
     * 
     * @param id 合同流程主键
     * @return 合同流程
     */
    public ContractApproval selectContractApprovalById(Long id);

    /**
     * 查询合同流程列表
     * 
     * @param contractApproval 合同流程
     * @return 合同流程集合
     */
    public List<ContractApproval> selectContractApprovalList(ContractApproval contractApproval);

    /**
     * 新增合同流程
     * 
     * @param contractApproval 合同流程
     * @return 结果
     */
    public int insertContractApproval(ContractApproval contractApproval);

    /**
     * 修改合同流程
     * 
     * @param contractApproval 合同流程
     * @return 结果
     */
    public int updateContractApproval(ContractApproval contractApproval);

    /**
     * 批量删除合同流程
     * 
     * @param ids 需要删除的合同流程主键集合
     * @return 结果
     */
    public int deleteContractApprovalByIds(Long[] ids);

    /**
     * 删除合同流程信息
     * 
     * @param id 合同流程主键
     * @return 结果
     */
    public int deleteContractApprovalById(Long id);
}
