package com.ruoyi.web.controller.contract;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.web.domain.*;
import com.ruoyi.web.service.IContractService;
import com.ruoyi.web.service.impl.EntSealClipService;
import io.swagger.annotations.ApiOperation;
import org.junit.jupiter.api.Test;
import org.resrun.sdk.service.EntSealGenerateService;
import org.resrun.sdk.service.SDKService;
import org.resrun.sdk.utils.Base64;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
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
public class ContractSignerController extends BaseController {
    @Autowired
    private IContractSignerService contractSignerService;
    @Autowired
    private IContractService contractService;

    /**
     * 查询合同签署列表
     */

    @GetMapping("/list")
    public TableDataInfo list(ContractSigner contractSigner) {

        startPage();
        List<ContractSigner> list = contractSignerService.selectContractSignerList(contractSigner);
        for(ContractSigner cs : list){
            Long ContractId = cs.getContractId();
            Contract contract = contractService.selectContractById(ContractId);
            cs.setContract(contract);
        }
        return getDataTable(list);
    }

    /**
     * 导出合同签署列表
     */

    @Log(title = "合同签署", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContractSigner contractSigner) {
        List<ContractSigner> list = contractSignerService.selectContractSignerList(contractSigner);
        ExcelUtil<ContractSigner> util = new ExcelUtil<ContractSigner>(ContractSigner.class);
        util.exportExcel(response, list, "合同签署数据");
    }

    /**
     * 获取合同签署详细信息
     */

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(contractSignerService.selectContractSignerById(id));
    }

    /**
     * 新增合同签署
     */

    @Log(title = "合同签署", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractSigner contractSigner) {
        return toAjax(contractSignerService.insertContractSigner(contractSigner));
    }

    /**
     * 修改合同签署
     */

    @Log(title = "合同签署", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractSigner contractSigner) {
        return toAjax(contractSignerService.updateContractSigner(contractSigner));
    }


    /**
     * 删除合同签署
     */

    @Log(title = "合同签署", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(contractSignerService.deleteContractSignerByIds(ids));
    }
}
