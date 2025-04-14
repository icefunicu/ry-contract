package com.ruoyi.web.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.ContractApprovalMapper;
import com.ruoyi.web.domain.ContractApproval;
import com.ruoyi.web.service.IContractApprovalService;

/**
 * 合同流程Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class ContractApprovalServiceImpl implements IContractApprovalService
{
    @Autowired
    private ContractApprovalMapper contractApprovalMapper;

    /**
     * 查询合同流程
     *
     * @param id 合同流程主键
     * @return 合同流程
     */
    @Override
    public ContractApproval selectContractApprovalById(Long id)
    {
        return contractApprovalMapper.selectContractApprovalById(id);
    }

    /**
     * 查询合同流程列表
     *
     * @param contractApproval 合同流程
     * @return 合同流程
     */
    @Override
    public List<ContractApproval> selectContractApprovalList(ContractApproval contractApproval)
    {
        return contractApprovalMapper.selectContractApprovalList(contractApproval);
    }

    /**
     * 新增合同流程
     *
     * @param contractApproval 合同流程
     * @return 结果
     */
    @Override
    public int insertContractApproval(ContractApproval contractApproval)
    {
        return contractApprovalMapper.insertContractApproval(contractApproval);
    }

    /**
     * 修改合同流程
     *
     * @param contractApproval 合同流程
     * @return 结果
     */
    @Override
    public int updateContractApproval(ContractApproval contractApproval)
    {
        return contractApprovalMapper.updateContractApproval(contractApproval);
    }

    /**
     * 批量删除合同流程
     *
     * @param ids 需要删除的合同流程主键
     * @return 结果
     */
    @Override
    public int deleteContractApprovalByIds(Long[] ids)
    {
        return contractApprovalMapper.deleteContractApprovalByIds(ids);
    }

    /**
     * 删除合同流程信息
     *
     * @param id 合同流程主键
     * @return 结果
     */
    @Override
    public int deleteContractApprovalById(Long id)
    {
        return contractApprovalMapper.deleteContractApprovalById(id);
    }

    @Override
    public List<ContractApproval> selectLegalList(ContractApproval contractApproval) {
        return contractApprovalMapper.selectLegalList(contractApproval);
    }
}
