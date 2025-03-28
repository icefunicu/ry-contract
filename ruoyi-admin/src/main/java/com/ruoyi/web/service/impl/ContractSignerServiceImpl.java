package com.ruoyi.web.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.ContractSignerMapper;
import com.ruoyi.web.domain.ContractSigner;
import com.ruoyi.web.service.IContractSignerService;

/**
 * 合同签署Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class ContractSignerServiceImpl implements IContractSignerService 
{
    @Autowired
    private ContractSignerMapper contractSignerMapper;

    /**
     * 查询合同签署
     * 
     * @param id 合同签署主键
     * @return 合同签署
     */
    @Override
    public ContractSigner selectContractSignerById(Long id)
    {
        return contractSignerMapper.selectContractSignerById(id);
    }

    /**
     * 查询合同签署列表
     * 
     * @param contractSigner 合同签署
     * @return 合同签署
     */
    @Override
    public List<ContractSigner> selectContractSignerList(ContractSigner contractSigner)
    {
        return contractSignerMapper.selectContractSignerList(contractSigner);
    }

    /**
     * 新增合同签署
     * 
     * @param contractSigner 合同签署
     * @return 结果
     */
    @Override
    public int insertContractSigner(ContractSigner contractSigner)
    {
        return contractSignerMapper.insertContractSigner(contractSigner);
    }

    /**
     * 修改合同签署
     * 
     * @param contractSigner 合同签署
     * @return 结果
     */
    @Override
    public int updateContractSigner(ContractSigner contractSigner)
    {
        return contractSignerMapper.updateContractSigner(contractSigner);
    }

    /**
     * 批量删除合同签署
     * 
     * @param ids 需要删除的合同签署主键
     * @return 结果
     */
    @Override
    public int deleteContractSignerByIds(Long[] ids)
    {
        return contractSignerMapper.deleteContractSignerByIds(ids);
    }

    /**
     * 删除合同签署信息
     * 
     * @param id 合同签署主键
     * @return 结果
     */
    @Override
    public int deleteContractSignerById(Long id)
    {
        return contractSignerMapper.deleteContractSignerById(id);
    }
}
