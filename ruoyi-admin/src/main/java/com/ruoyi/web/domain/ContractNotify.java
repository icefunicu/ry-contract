package com.ruoyi.web.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 留言对象 contract_notify
 * 
 * @author ruoyi
 * @date 2025-03-30
 */
public class ContractNotify extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 留言ID */
    private Long id;

    /** 合同ID */
    @Excel(name = "合同ID")
    private Long contractId;

    /** 留言人ID */
    @Excel(name = "留言人ID")
    private Long userId;

    /** 留言人姓名 */
    @Excel(name = "留言人姓名")
    private String userName;

    /** 留言内容 */
    @Excel(name = "留言内容")
    private String content;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 留言状态 */
    @Excel(name = "留言状态")
    private String notifyStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setContractId(Long contractId) 
    {
        this.contractId = contractId;
    }

    public Long getContractId() 
    {
        return contractId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setNotifyStatus(String notifyStatus) 
    {
        this.notifyStatus = notifyStatus;
    }

    public String getNotifyStatus() 
    {
        return notifyStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("contractId", getContractId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("content", getContent())
            .append("createdAt", getCreatedAt())
            .append("notifyStatus", getNotifyStatus())
            .toString();
    }
}
