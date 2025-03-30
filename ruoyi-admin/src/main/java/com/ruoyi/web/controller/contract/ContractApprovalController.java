package com.ruoyi.web.controller.contract;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 查询合同签署流程列表
     */
    @PreAuthorize("@ss.hasPermi('contract:approval:list')")
    @GetMapping("/list")
    public TableDataInfo list(ContractApproval contractApproval)
    {
        startPage();
        List<ContractApproval> list = contractApprovalService.selectContractApprovalList(contractApproval);
        return getDataTable(list);
    }

    /**
     * 导出合同签署流程列表
     */
    @PreAuthorize("@ss.hasPermi('contract:approval:export')")
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
    @PreAuthorize("@ss.hasPermi('contract:approval:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(contractApprovalService.selectContractApprovalById(id));
    }

    /**
     * 新增合同签署流程
     */
    @PreAuthorize("@ss.hasPermi('contract:approval:add')")
    @Log(title = "合同签署流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractApproval contractApproval)
    {

        return toAjax(contractApprovalService.insertContractApproval(contractApproval));
    }

    /**
     * 修改合同签署流程
     */
    @PreAuthorize("@ss.hasPermi('contract:approval:edit')")
    @Log(title = "合同签署流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractApproval contractApproval)
    {
        return toAjax(contractApprovalService.updateContractApproval(contractApproval));
    }

    /**
     * 删除合同签署流程
     */
    @PreAuthorize("@ss.hasPermi('contract:approval:remove')")
    @Log(title = "合同签署流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contractApprovalService.deleteContractApprovalByIds(ids));
    }
}
