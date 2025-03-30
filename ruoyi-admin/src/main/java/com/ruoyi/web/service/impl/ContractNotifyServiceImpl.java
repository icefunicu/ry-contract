package com.ruoyi.web.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.ContractNotifyMapper;
import com.ruoyi.web.domain.ContractNotify;
import com.ruoyi.web.service.IContractNotifyService;

/**
 * 留言Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-03-30
 */
@Service
public class ContractNotifyServiceImpl implements IContractNotifyService 
{
    @Autowired
    private ContractNotifyMapper contractNotifyMapper;

    /**
     * 查询留言
     * 
     * @param id 留言主键
     * @return 留言
     */
    @Override
    public ContractNotify selectContractNotifyById(Long id)
    {
        return contractNotifyMapper.selectContractNotifyById(id);
    }

    /**
     * 查询留言列表
     * 
     * @param contractNotify 留言
     * @return 留言
     */
    @Override
    public List<ContractNotify> selectContractNotifyList(ContractNotify contractNotify)
    {
        return contractNotifyMapper.selectContractNotifyList(contractNotify);
    }

    /**
     * 新增留言
     * 
     * @param contractNotify 留言
     * @return 结果
     */
    @Override
    public int insertContractNotify(ContractNotify contractNotify)
    {
        return contractNotifyMapper.insertContractNotify(contractNotify);
    }

    /**
     * 修改留言
     * 
     * @param contractNotify 留言
     * @return 结果
     */
    @Override
    public int updateContractNotify(ContractNotify contractNotify)
    {
        return contractNotifyMapper.updateContractNotify(contractNotify);
    }

    /**
     * 批量删除留言
     * 
     * @param ids 需要删除的留言主键
     * @return 结果
     */
    @Override
    public int deleteContractNotifyByIds(Long[] ids)
    {
        return contractNotifyMapper.deleteContractNotifyByIds(ids);
    }

    /**
     * 删除留言信息
     * 
     * @param id 留言主键
     * @return 结果
     */
    @Override
    public int deleteContractNotifyById(Long id)
    {
        return contractNotifyMapper.deleteContractNotifyById(id);
    }
}
