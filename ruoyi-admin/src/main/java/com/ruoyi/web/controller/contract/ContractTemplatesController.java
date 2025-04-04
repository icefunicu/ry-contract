package com.ruoyi.web.controller.contract;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import fr.opensagres.poi.xwpf.converter.xhtml.Base64EmbedImgManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.*;
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
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;


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

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(contractTemplatesService.selectContractTemplatesById(id));
    }

    /**
     * 新增合同模板
     */

    @Log(title = "合同模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractTemplates contractTemplates)
    {
        return toAjax(contractTemplatesService.insertContractTemplates(contractTemplates));
    }

    /**
     * 修改合同模板
     */

    @Log(title = "合同模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractTemplates contractTemplates)
    {
        return toAjax(contractTemplatesService.updateContractTemplates(contractTemplates));
    }

    /**
     * 删除合同模板
     */

    @Log(title = "合同模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contractTemplatesService.deleteContractTemplatesByIds(ids));
    }

    @PostMapping("/upload")
    public AjaxResult convertWordToHtml(@RequestParam("file") MultipartFile file) {
        try (InputStream input = file.getInputStream()) {
            String fileName = file.getOriginalFilename();
            String html;

            if (fileName != null && fileName.endsWith(".docx")) {
                // 处理 .docx 文件
                XWPFDocument document = new XWPFDocument(input);
                XHTMLOptions options = XHTMLOptions.create()
                        .setImageManager(new Base64EmbedImgManager()) // 图片转Base64
                        .setFragment(true);
                ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
                XHTMLConverter.getInstance().convert(document, htmlStream, options);
                html = new String(htmlStream.toByteArray(), StandardCharsets.UTF_8);
            } else if (fileName != null && fileName.endsWith(".doc")) {
                // 处理 .doc 文件
                HWPFDocument document = new HWPFDocument(input);
                WordToHtmlConverter converter = new WordToHtmlConverter(
                        DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
                );
                converter.processDocument(document);

                // 输出 HTML
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(
                        new DOMSource(converter.getDocument()), new StreamResult(outStream)
                );

                html = new String(outStream.toByteArray(), StandardCharsets.UTF_8);

                // 兜底方案：若 HTML 为空，尝试提取纯文本
                if (html.trim().isEmpty()) {
                    WordExtractor extractor = new WordExtractor(document);
                    html = "<p>" + extractor.getText().replace("\n", "<br>") + "</p>";
                }
            } else {
                return error("不支持的文件格式");
            }

            // 规范 HTML 内容
            html = cleanHtmlTags(html);

            // 保存到数据库
            ContractTemplates contractTemplates = new ContractTemplates();
            contractTemplates.setContent(html);
            contractTemplates.setName(fileName.substring(0, fileName.lastIndexOf(".")));

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
