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
import com.ruoyi.web.domain.ContractSigner;
import com.ruoyi.web.service.IContractSignerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同签署Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/contract/signer")
public class ContractSignerController extends BaseController
{
    @Autowired
    private IContractSignerService contractSignerService;

    /**
     * 查询合同签署列表
     */
    @PreAuthorize("@ss.hasPermi('contract:signer:list')")
    @GetMapping("/list")
    public TableDataInfo list(ContractSigner contractSigner)
    {
        startPage();
        List<ContractSigner> list = contractSignerService.selectContractSignerList(contractSigner);
        return getDataTable(list);
    }

    /**
     * 导出合同签署列表
     */
    @PreAuthorize("@ss.hasPermi('contract:signer:export')")
    @Log(title = "合同签署", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContractSigner contractSigner)
    {
        List<ContractSigner> list = contractSignerService.selectContractSignerList(contractSigner);
        ExcelUtil<ContractSigner> util = new ExcelUtil<ContractSigner>(ContractSigner.class);
        util.exportExcel(response, list, "合同签署数据");
    }

    /**
     * 获取合同签署详细信息
     */
    @PreAuthorize("@ss.hasPermi('contract:signer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(contractSignerService.selectContractSignerById(id));
    }

    /**
     * 新增合同签署
     */
    @PreAuthorize("@ss.hasPermi('contract:signer:add')")
    @Log(title = "合同签署", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractSigner contractSigner)
    {
        return toAjax(contractSignerService.insertContractSigner(contractSigner));
    }

    /**
     * 修改合同签署
     */
    @PreAuthorize("@ss.hasPermi('contract:signer:edit')")
    @Log(title = "合同签署", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractSigner contractSigner)
    {
        return toAjax(contractSignerService.updateContractSigner(contractSigner));
    }

    /**
     * 删除合同签署
     */
    @PreAuthorize("@ss.hasPermi('contract:signer:remove')")
    @Log(title = "合同签署", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contractSignerService.deleteContractSignerByIds(ids));
    }
}
