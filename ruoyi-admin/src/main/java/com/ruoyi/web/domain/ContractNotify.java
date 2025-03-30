package com.ruoyi.web.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
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
@Data
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



}
