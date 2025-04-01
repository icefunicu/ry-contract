package com.ruoyi.web.service.impl;


import java.util.Date;
import java.util.List;

import com.ruoyi.web.domain.Contract;
import com.ruoyi.web.domain.ContractApproval;
import com.ruoyi.web.mapper.ContractApprovalMapper;
import com.ruoyi.web.mapper.ContractMapper;
import com.ruoyi.web.service.IContractService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


/**
 * 合同Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class ContractServiceImpl implements IContractService
{
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractApprovalMapper contractApprovalMapper;

    /**
     * 查询合同
     *
     * @param id 合同主键
     * @return 合同
     */
    @Override
    public Contract selectContractById(Long id)
    {
        return contractMapper.selectContractById(id);
    }

    /**
     * 查询合同列表
     *
     * @param contract 合同
     * @return 合同
     */
    @Override
    public List<Contract> selectContractList(Contract contract)
    {
        return contractMapper.selectContractList(contract);
    }

    /**
     * 新增合同
     *
     * @param contract 合同
     * @return 结果
     */
    @Override
    public int insertContract(Contract contract)
    {
        return contractMapper.insertContract(contract);
    }

    /**
     * 修改合同
     *
     * @param contract 合同
     * @return 结果
     */
    @Override
    public int updateContract(Contract contract)
    {
        return contractMapper.updateContract(contract);
    }

    /**
     * 批量删除合同
     *
     * @param ids 需要删除的合同主键
     * @return 结果
     */
    @Override
    public int deleteContractByIds(Long[] ids)
    {
        return contractMapper.deleteContractByIds(ids);
    }

    /**
     * 删除合同信息
     *
     * @param id 合同主键
     * @return 结果
     */
    @Override
    public int deleteContractById(Long id)
    {
        return contractMapper.deleteContractById(id);
    }

    @Override
    public List<Contract> selectContractByPaOrPb(Long userId) {
        return contractMapper.selectContractByPaOrPb(userId);
    }
    @Override
    public List<Contract> selectContractByTitle(String title) {
        return contractMapper.selectContractByTitle(title);
    }

}
