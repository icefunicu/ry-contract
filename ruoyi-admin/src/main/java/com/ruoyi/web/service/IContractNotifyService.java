package com.ruoyi.web.service;

import java.util.List;
import com.ruoyi.web.domain.ContractNotify;

/**
 * 留言Service接口
 * 
 * @author ruoyi
 * @date 2025-03-30
 */
public interface IContractNotifyService 
{
    /**
     * 查询留言
     * 
     * @param id 留言主键
     * @return 留言
     */
    public ContractNotify selectContractNotifyById(Long id);

    /**
     * 查询留言列表
     * 
     * @param contractNotify 留言
     * @return 留言集合
     */
    public List<ContractNotify> selectContractNotifyList(ContractNotify contractNotify);

    /**
     * 新增留言
     * 
     * @param contractNotify 留言
     * @return 结果
     */
    public int insertContractNotify(ContractNotify contractNotify);

    /**
     * 修改留言
     * 
     * @param contractNotify 留言
     * @return 结果
     */
    public int updateContractNotify(ContractNotify contractNotify);

    /**
     * 批量删除留言
     * 
     * @param ids 需要删除的留言主键集合
     * @return 结果
     */
    public int deleteContractNotifyByIds(Long[] ids);

    /**
     * 删除留言信息
     * 
     * @param id 留言主键
     * @return 结果
     */
    public int deleteContractNotifyById(Long id);
}
