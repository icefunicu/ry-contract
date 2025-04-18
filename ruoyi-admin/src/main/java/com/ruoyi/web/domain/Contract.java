package com.ruoyi.web.domain;

import java.util.Date;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 合同对象 contract
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@Data
public class Contract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private int id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String title;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private int createdBy;

    private String createdByName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String filePath;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date createdTime;

    private int partyA;

    private int partyB;

    private String partyAName;
    private String partyBName;
    private String opinion;
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("createdBy", getCreatedBy())
            .append("status", getStatus())
            .append("filePath", getFilePath())
            .append("createdTime", getCreatedTime())
            .append("partyA", getPartyA())
            .append("partyB", getPartyB())
            .append("partyAName", getPartyAName())
            .append("partyBName", getPartyBName())
            .append("createdByName", getCreatedByName())
            .toString();
    }
}
