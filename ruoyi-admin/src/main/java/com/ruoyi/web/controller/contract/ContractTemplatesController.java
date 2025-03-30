package com.ruoyi.web.controller.contract;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import fr.opensagres.poi.xwpf.converter.xhtml.Base64EmbedImgManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.domain.ContractTemplates;
import com.ruoyi.web.service.IContractTemplatesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;


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

    @PostMapping("/upload")
    public AjaxResult convertWordToHtml(@RequestParam("file") MultipartFile file) {
        try (InputStream input = file.getInputStream()) {
            XWPFDocument document = new XWPFDocument(input);
            XHTMLOptions options = XHTMLOptions.create()
                    .setImageManager(new Base64EmbedImgManager())  // 图片转Base64
                    .setFragment(true);  // 只转换内容，不包括头尾标签

            ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, htmlStream, options);

            String html = new String(htmlStream.toByteArray(), StandardCharsets.UTF_8);

            // 手动处理标题、居中和字体样式转换
            html = convertHeadingsToHtmlTags(html);  // 转换标题
            html = convertTextAlignmentToHtmlTags(html); // 处理居中样式
            html = convertFontStylesToHtmlTags(html); // 处理字体样式（粗体、斜体等）

            // 清洗 HTML，去除不必要的标签和错误的标签闭合
            html = cleanHtmlTags(html);

            System.out.println(html);  // 打印 HTML 内容查看

            // 保存到数据库
            ContractTemplates contractTemplates = new ContractTemplates();
            contractTemplates.setContent(html);
            contractTemplates.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));

            return contractTemplatesService.insertContractTemplates(contractTemplates) > 0 ? success("上传成功") : error("上传失败");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    private String convertHeadingsToHtmlTags(String html) {
        // 不需要转换为 h1, h2 等，保持为 <p><span> 格式
        // 如果需要对标题部分进行特别标记，可以保留 <p> 标签并应用样式
        html = html.replaceAll("(<p style=\"font-size:20pt;\">)(.*?)(</p>)", "<p><span style=\"font-size:20pt;\">$2</span></p>");
        html = html.replaceAll("(<p style=\"font-size:16pt;\">)(.*?)(</p>)", "<p><span style=\"font-size:16pt;\">$2</span></p>");
        html = html.replaceAll("(<p style=\"font-size:14pt;\">)(.*?)(</p>)", "<p><span style=\"font-size:14pt;\">$2</span></p>");
        html = html.replaceAll("(<p style=\"font-size:12pt;\">)(.*?)(</p>)", "<p><span style=\"font-size:12pt;\">$2</span></p>");
        return html;
    }

    private String convertTextAlignmentToHtmlTags(String html) {
        // 处理居中样式，只保留 <p> 和 <span>，去除多余的 <div> 标签
        html = html.replaceAll("<p style=\"text-align:center;\">", "<p><span style=\"text-align: center;\">");
        html = html.replaceAll("</p>", "</span></p>");
        return html;
    }

    private String convertFontStylesToHtmlTags(String html) {
        // 转换字体样式（如粗体、斜体等），保持为 <span> 标签
        html = html.replaceAll("<p style=\"font-weight:bold;\">", "<p><span style=\"font-weight: bold;\">");
        html = html.replaceAll("<p style=\"font-style:italic;\">", "<p><span style=\"font-style: italic;\">");
        html = html.replaceAll("<p style=\"text-decoration:underline;\">", "<p><span style=\"text-decoration: underline;\">");

        // 确保结束标签正确
        html = html.replaceAll("</p>", "</span></p>");

        return html;
    }

    private String cleanHtmlTags(String html) {
        // 去除多余的空标签
        html = html.replaceAll("<p><br/></p>", "");  // 清除没有实际内容的空 p 标签
        html = html.replaceAll("<div><br/></div>", "");  // 清除没有实际内容的空 div 标签
        html = html.replaceAll("<br/>", "");  // 删除所有无用的 <br> 标签

        // 修复标签嵌套问题，确保标签闭合正确
        html = html.replaceAll("<p><span[^>]*>", "<p><span>");  // 确保 span 标签没有多余属性
        html = html.replaceAll("<span[^>]*>", "<span>");  // 只保留 span 标签

        return html;
    }


}
