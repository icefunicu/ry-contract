package com.ruoyi.web.mapper;

import java.util.List;
import com.ruoyi.web.domain.ContractTemplates;

/**
 * 合同模板Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface ContractTemplatesMapper 
{
    /**
     * 查询合同模板
     * 
     * @param id 合同模板主键
     * @return 合同模板
     */
    public ContractTemplates selectContractTemplatesById(Long id);

    /**
     * 查询合同模板列表
     * 
     * @param contractTemplates 合同模板
     * @return 合同模板集合
     */
    public List<ContractTemplates> selectContractTemplatesList(ContractTemplates contractTemplates);

    /**
     * 新增合同模板
     * 
     * @param contractTemplates 合同模板
     * @return 结果
     */
    public int insertContractTemplates(ContractTemplates contractTemplates);

    /**
     * 修改合同模板
     * 
     * @param contractTemplates 合同模板
     * @return 结果
     */
    public int updateContractTemplates(ContractTemplates contractTemplates);

    /**
     * 删除合同模板
     * 
     * @param id 合同模板主键
     * @return 结果
     */
    public int deleteContractTemplatesById(Long id);

    /**
     * 批量删除合同模板
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteContractTemplatesByIds(Long[] ids);
}
