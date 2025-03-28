package com.ruoyi.web.service;

import java.util.List;
import com.ruoyi.web.domain.ContractSigner;

/**
 * 合同签署Service接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface IContractSignerService 
{
    /**
     * 查询合同签署
     * 
     * @param id 合同签署主键
     * @return 合同签署
     */
    public ContractSigner selectContractSignerById(Long id);

    /**
     * 查询合同签署列表
     * 
     * @param contractSigner 合同签署
     * @return 合同签署集合
     */
    public List<ContractSigner> selectContractSignerList(ContractSigner contractSigner);

    /**
     * 新增合同签署
     * 
     * @param contractSigner 合同签署
     * @return 结果
     */
    public int insertContractSigner(ContractSigner contractSigner);

    /**
     * 修改合同签署
     * 
     * @param contractSigner 合同签署
     * @return 结果
     */
    public int updateContractSigner(ContractSigner contractSigner);

    /**
     * 批量删除合同签署
     * 
     * @param ids 需要删除的合同签署主键集合
     * @return 结果
     */
    public int deleteContractSignerByIds(Long[] ids);

    /**
     * 删除合同签署信息
     * 
     * @param id 合同签署主键
     * @return 结果
     */
    public int deleteContractSignerById(Long id);
}
