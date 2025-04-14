package com.ruoyi.web.controller.contract;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.Contract;
import com.ruoyi.web.domain.ContractSigner;
import com.ruoyi.web.service.IContractService;
import com.ruoyi.web.service.IContractSignerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.domain.ContractApproval;
import com.ruoyi.web.service.IContractApprovalService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同签署流程Controller
 *
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/contract/approval")
public class ContractApprovalController extends BaseController
{
    @Autowired
    private IContractApprovalService contractApprovalService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IContractService contractService;
    @Autowired
    private IContractSignerService contractSignerService;
    /**
     * 查询合同签署流程列表
     */

    @GetMapping("/list")
    public TableDataInfo list(ContractApproval contractApproval)
    {
        startPage();
        List<ContractApproval> list = contractApprovalService.selectContractApprovalList(contractApproval);
        for (ContractApproval approval : list) {
            Long approverId = approval.getApproverId();
            Long contractId = approval.getContractId();
            approval.setApproverName(userService.selectUserById(approverId).getUserName());
            approval.setContractTitle(contractService.selectContractById(contractId).getTitle());
            approval.setContract(contractService.selectContractById(contractId));
        }
        return getDataTable(list);
    }

    /**
     * 导出合同签署流程列表
     */

    @Log(title = "合同签署流程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContractApproval contractApproval)
    {
        List<ContractApproval> list = contractApprovalService.selectContractApprovalList(contractApproval);
        ExcelUtil<ContractApproval> util = new ExcelUtil<ContractApproval>(ContractApproval.class);
        util.exportExcel(response, list, "合同签署流程数据");
    }

    /**
     * 获取合同签署流程详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ContractApproval contractApproval = contractApprovalService.selectContractApprovalById(id);
        contractApproval.setContract(contractService.selectContractById(contractApproval.getContractId()));
        return success(contractApproval);
    }

    /**
     * 新增合同签署流程
     */

    @Log(title = "合同签署流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractApproval contractApproval)
    {

        return toAjax(contractApprovalService.insertContractApproval(contractApproval));
    }

    /**
     * 修改合同签署流程
     */

    @Log(title = "合同签署流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractApproval contractApproval)
    {
        return toAjax(contractApprovalService.updateContractApproval(contractApproval));
    }

    /**
     * 法务审核状态的流程记录展示list
     * */
    @GetMapping("/legal")
    public TableDataInfo legalList(ContractApproval contractApproval)
    {
        startPage();
        List<ContractApproval> list = contractApprovalService.selectLegalList(contractApproval);
        for (ContractApproval approval : list) {
            Long approverId = approval.getApproverId();
            Long contractId = approval.getContractId();
            approval.setApproverName(userService.selectUserById(approverId).getUserName());
            approval.setContractTitle(contractService.selectContractById(contractId).getTitle());
            approval.setContract(contractService.selectContractById(contractId));
        }
        return getDataTable(list);
    }

    /**
     * 通过法务审核，合同状态变更为待审核
     * */
    @PutMapping("/legal/approve")
    public AjaxResult legalApprove(@RequestBody ContractApproval contractApproval) {
        ContractApproval approval = contractApprovalService.selectContractApprovalById(contractApproval.getId());
        approval.setStatus("待审核");
        Long contractId = approval.getContractId();
        Contract contract = contractService.selectContractById(contractId);
        contract.setStatus("待审核");
        contractService.updateContract(contract);
        return toAjax(contractApprovalService.updateContractApproval(approval));
    }

    /**
     * 法务审核驳回
     * */
    @PutMapping("/legal/reject")
    public AjaxResult legalReject(@RequestBody ContractApproval contractApproval) {
        ContractApproval approval = contractApprovalService.selectContractApprovalById(contractApproval.getId());
        approval.setStatus("已驳回");
        Long contractId = approval.getContractId();
        Contract contract = contractService.selectContractById(contractId);
        contract.setStatus("已驳回");
        contractService.updateContract(contract);
        return toAjax(contractApprovalService.updateContractApproval(approval));
    }


    /**
     *   通过审核
     * */
    @PostMapping ("/approve")
    public AjaxResult approve(@RequestBody ContractApproval contractApproval) {
        ContractApproval approval = contractApprovalService.selectContractApprovalById(contractApproval.getId());
        approval.setStatus("已通过");
        Long contractId = approval.getContractId();
        Contract contract = contractService.selectContractById(contractId);
        contract.setStatus("待签字");
        contractService.updateContract(contract);
        // 向签署表中插入数据
        ContractSigner contractSignerJ = new ContractSigner();
        contractSignerJ.setContractId(contractId);
        contractSignerJ.setUserId((long) contract.getPartyA());
        contractSignerJ.setSigned(0);

        ContractSigner contractSignerY = new ContractSigner();
        contractSignerY.setContractId(contractId);
        contractSignerY.setUserId((long) contract.getPartyB());
        contractSignerY.setSigned(0);
        contractSignerService.insertContractSigner(contractSignerJ);
        contractSignerService.insertContractSigner(contractSignerY);
        return toAjax(contractApprovalService.updateContractApproval(approval));
    }

    /**
     * 驳回审核
     * */
    @PutMapping("/reject")
    public AjaxResult reject(@RequestBody ContractApproval contractApproval) {
        ContractApproval approval = contractApprovalService.selectContractApprovalById(contractApproval.getId());
        approval.setStatus("已驳回");
        Long contractId = approval.getContractId();
        Contract contract = contractService.selectContractById(contractId);
        contract.setStatus("待修改");
        contractService.updateContract(contract);
        return toAjax(contractApprovalService.updateContractApproval(approval));
    }

    /**
     * 删除合同签署流程
     */

    @Log(title = "合同签署流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contractApprovalService.deleteContractApprovalByIds(ids));
    }
}
