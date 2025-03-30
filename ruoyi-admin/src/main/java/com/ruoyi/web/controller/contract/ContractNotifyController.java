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
import com.ruoyi.web.domain.ContractNotify;
import com.ruoyi.web.service.IContractNotifyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 留言Controller
 *
 * @author ruoyi
 * @date 2025-03-30
 */
@RestController
@RequestMapping("/notify")
public class ContractNotifyController extends BaseController
{
    @Autowired
    private IContractNotifyService contractNotifyService;

    /**
     * 查询留言列表
     */

    @GetMapping("/list")
    public TableDataInfo list(ContractNotify contractNotify)
    {
        startPage();
        List<ContractNotify> list = contractNotifyService.selectContractNotifyList(contractNotify);
        return getDataTable(list);
    }

    /**
     * 导出留言列表
     */

    @Log(title = "留言", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContractNotify contractNotify)
    {
        List<ContractNotify> list = contractNotifyService.selectContractNotifyList(contractNotify);
        ExcelUtil<ContractNotify> util = new ExcelUtil<ContractNotify>(ContractNotify.class);
        util.exportExcel(response, list, "留言数据");
    }

    /**
     * 获取留言详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(contractNotifyService.selectContractNotifyById(id));
    }

    /**
     * 新增留言
     */

    @Log(title = "留言", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractNotify contractNotify)
    {
        return toAjax(contractNotifyService.insertContractNotify(contractNotify));
    }

    /**
     * 修改留言
     */

    @Log(title = "留言", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractNotify contractNotify)
    {
        return toAjax(contractNotifyService.updateContractNotify(contractNotify));
    }

    /**
     * 删除留言
     */

    @Log(title = "留言", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contractNotifyService.deleteContractNotifyByIds(ids));
    }
}
