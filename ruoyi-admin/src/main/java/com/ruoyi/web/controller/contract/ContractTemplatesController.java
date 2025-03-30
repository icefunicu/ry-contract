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
import com.ruoyi.web.domain.ContractTemplates;
import com.ruoyi.web.service.IContractTemplatesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 合同模板Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/contract/templates")
public class ContractTemplatesController extends BaseController
{
    @Autowired
    private IContractTemplatesService contractTemplatesService;

    /**
     * 查询合同模板列表
     */
    @PreAuthorize("@ss.hasPermi('contract:templates:list')")
    @GetMapping("/list")
    public TableDataInfo list(ContractTemplates contractTemplates)
    {
        startPage();
        List<ContractTemplates> list = contractTemplatesService.selectContractTemplatesList(contractTemplates);
        return getDataTable(list);
    }

    /**
     * 导出合同模板列表
     */
    @PreAuthorize("@ss.hasPermi('contract:templates:export')")
    @Log(title = "合同模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContractTemplates contractTemplates)
    {
        List<ContractTemplates> list = contractTemplatesService.selectContractTemplatesList(contractTemplates);
        ExcelUtil<ContractTemplates> util = new ExcelUtil<ContractTemplates>(ContractTemplates.class);
        util.exportExcel(response, list, "合同模板数据");
    }

    /**
     * 获取合同模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('contract:templates:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(contractTemplatesService.selectContractTemplatesById(id));
    }

    /**
     * 新增合同模板
     */
    @PreAuthorize("@ss.hasPermi('contract:templates:add')")
    @Log(title = "合同模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractTemplates contractTemplates)
    {
        return toAjax(contractTemplatesService.insertContractTemplates(contractTemplates));
    }

    /**
     * 修改合同模板
     */
    @PreAuthorize("@ss.hasPermi('contract:templates:edit')")
    @Log(title = "合同模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractTemplates contractTemplates)
    {
        return toAjax(contractTemplatesService.updateContractTemplates(contractTemplates));
    }

    /**
     * 删除合同模板
     */
    @PreAuthorize("@ss.hasPermi('contract:templates:remove')")
    @Log(title = "合同模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contractTemplatesService.deleteContractTemplatesByIds(ids));
    }

}
