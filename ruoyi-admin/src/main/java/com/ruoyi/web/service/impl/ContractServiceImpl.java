package com.ruoyi.web.service.impl;

import java.util.List;

import com.ruoyi.web.domain.Contract;
import com.ruoyi.web.mapper.ContractMapper;
import com.ruoyi.web.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class ContractServiceImpl implements IContractService
{
    @Autowired
    private ContractMapper contractMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public Contract selectContractById(Long id)
    {
        return contractMapper.selectContractById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param contract 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<Contract> selectContractList(Contract contract)
    {
        return contractMapper.selectContractList(contract);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param contract 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertContract(Contract contract)
    {
        return contractMapper.insertContract(contract);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param contract 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateContract(Contract contract)
    {
        return contractMapper.updateContract(contract);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteContractByIds(Long[] ids)
    {
        return contractMapper.deleteContractByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteContractById(Long id)
    {
        return contractMapper.deleteContractById(id);
    }
}
