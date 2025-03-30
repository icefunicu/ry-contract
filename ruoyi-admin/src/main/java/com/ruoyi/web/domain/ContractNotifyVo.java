package com.ruoyi.web.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ContractNotifyVo {
    private long id;
    private String title;
    private String content;
    private Integer createdBy;
    private String createdByName;
    private String status;
    private String filePath;
    private Date createdTime;
    private Integer partyA;
    private Integer partyB;
    private String partyAName;
    private String partyBName;
    private List<ContractNotify> notifyInfoList;

    public void setContract(Contract contract) {
        this.id = contract.getId();
        this.title = contract.getTitle();
        this.content = contract.getContent();
        this.createdBy = contract.getCreatedBy();
        this.createdByName = contract.getCreatedByName();
        this.status = contract.getStatus();
        this.filePath = contract.getFilePath();
        this.createdTime = contract.getCreatedTime();
        this.partyA = contract.getPartyA();
        this.partyB = contract.getPartyB();
        this.partyAName = contract.getPartyAName();
        this.partyBName = contract.getPartyBName();
    }
}
