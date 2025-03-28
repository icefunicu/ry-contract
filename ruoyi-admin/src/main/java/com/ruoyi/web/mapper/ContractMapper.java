package com.ruoyi.web.mapper;

import com.ruoyi.web.domain.Contract;

import java.util.List;


/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
public interface ContractMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public Contract selectContractById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param contract 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<Contract> selectContractList(Contract contract);

    /**
     * 新增【请填写功能名称】
     * 
     * @param contract 【请填写功能名称】
     * @return 结果
     */
    public int insertContract(Contract contract);

    /**
     * 修改【请填写功能名称】
     * 
     * @param contract 【请填写功能名称】
     * @return 结果
     */
    public int updateContract(Contract contract);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteContractById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteContractByIds(Long[] ids);
}
