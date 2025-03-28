package com.ruoyi.web.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.ContractTemplatesMapper;
import com.ruoyi.web.domain.ContractTemplates;
import com.ruoyi.web.service.IContractTemplatesService;

/**
 * 合同模板Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@Service
public class ContractTemplatesServiceImpl implements IContractTemplatesService 
{
    @Autowired
    private ContractTemplatesMapper contractTemplatesMapper;

    /**
     * 查询合同模板
     * 
     * @param id 合同模板主键
     * @return 合同模板
     */
    @Override
    public ContractTemplates selectContractTemplatesById(Long id)
    {
        return contractTemplatesMapper.selectContractTemplatesById(id);
    }

    /**
     * 查询合同模板列表
     * 
     * @param contractTemplates 合同模板
     * @return 合同模板
     */
    @Override
    public List<ContractTemplates> selectContractTemplatesList(ContractTemplates contractTemplates)
    {
        return contractTemplatesMapper.selectContractTemplatesList(contractTemplates);
    }

    /**
     * 新增合同模板
     * 
     * @param contractTemplates 合同模板
     * @return 结果
     */
    @Override
    public int insertContractTemplates(ContractTemplates contractTemplates)
    {
        return contractTemplatesMapper.insertContractTemplates(contractTemplates);
    }

    /**
     * 修改合同模板
     * 
     * @param contractTemplates 合同模板
     * @return 结果
     */
    @Override
    public int updateContractTemplates(ContractTemplates contractTemplates)
    {
        return contractTemplatesMapper.updateContractTemplates(contractTemplates);
    }

    /**
     * 批量删除合同模板
     * 
     * @param ids 需要删除的合同模板主键
     * @return 结果
     */
    @Override
    public int deleteContractTemplatesByIds(Long[] ids)
    {
        return contractTemplatesMapper.deleteContractTemplatesByIds(ids);
    }

    /**
     * 删除合同模板信息
     * 
     * @param id 合同模板主键
     * @return 结果
     */
    @Override
    public int deleteContractTemplatesById(Long id)
    {
        return contractTemplatesMapper.deleteContractTemplatesById(id);
    }
}
