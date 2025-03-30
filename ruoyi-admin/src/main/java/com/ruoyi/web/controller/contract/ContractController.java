package com.ruoyi.web.controller.contract;


import javax.servlet.http.HttpServletResponse;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.Contract;
import com.ruoyi.web.service.IContractService;
import com.ruoyi.web.util.PdfGenerationService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.ruoyi.common.core.page.TableDataInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 合同Controller
 * 
 * @author ruoyi
 * @date 2025-03-28
 */
@RestController
@RequestMapping("contract")
public class ContractController extends BaseController
{
    @Autowired
    private IContractService contractService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private PdfGenerationService pdfGenerationService;
    /**
     * 查询合同列表
     */
    @PreAuthorize("@ss.hasPermi('contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(Contract contract)
    {
        startPage();
        List<Contract> list = contractService.selectContractList(contract);
        for(Contract c:list){
            int userId = c.getCreatedBy();
            int partyA = c.getPartyA();
            int partyB = c.getPartyB();
            c.setCreatedByName(userService.selectUserById((long) userId).getNickName());
            c.setPartyAName(userService.selectUserById((long)partyA).getNickName());
            c.setPartyBName(userService.selectUserById((long)partyB).getNickName());
        }
        return getDataTable(list);
    }

    /**
     * 导出合同列表
     */
    @PreAuthorize("@ss.hasPermi('contract:export')")
    @Log(title = "合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Contract contract)
    {
        List<Contract> list = contractService.selectContractList(contract);
        ExcelUtil<Contract> util = new ExcelUtil<Contract>(Contract.class);
        util.exportExcel(response, list, "合同数据");
    }

    /**
     * 获取合同详细信息
     */
    @PreAuthorize("@ss.hasPermi('contract:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(contractService.selectContractById(id));
    }

    /**
     * 新增合同
     */
    @PreAuthorize("@ss.hasPermi('contract:add')")
    @Log(title = "合同", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Contract contract)
    {
        return toAjax(contractService.insertContract(contract));
    }

    /**
     * 修改合同
     */
    @Log(title = "合同", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Contract contract)
    {
        return toAjax(contractService.updateContract(contract));
    }

    /**
     * 删除合同
     */
    @PreAuthorize("@ss.hasPermi('contract:remove')")
    @Log(title = "合同", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contractService.deleteContractByIds(ids));
    }

    @Value("${ruoyi.profile}")
    private String contractPath;

    /**
     *  根据 contractId 查找合同并转换为 PDF 返回
     */
    @GetMapping("/{contractId}/word2pdf")
    public ResponseEntity<InputStreamResource> getContractPdf(@PathVariable String contractId) throws Exception {
        Contract contract = contractService.selectContractById(Long.parseLong(contractId));

        String docxPath = contractPath + contract.getFilePath();

        File docxFile = new File(docxPath);
        if (!docxFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 临时 PDF 文件路径
        File pdfFile = File.createTempFile(contractPath + contractId, ".pdf");

        // 执行转换
        convertWordToPdf(docxPath, pdfFile.getAbsolutePath());

        // 读取 PDF 并返回
        InputStream inputStream = new FileInputStream(pdfFile);
        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + contractId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getContractPdf(@PathVariable Long id) {
        try {
            // 从数据库中获取合同对象
            Contract contract = contractService.selectContractById(id);

            // 生成 PDF 文件
            byte[] pdfBytes = pdfGenerationService.generateContractPdf(contract);

            // 设置响应头，告诉浏览器这是一个 PDF 文件
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=contract_" + id + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 调用wkhtmltopdf生成pdf
     */
    @GetMapping("/{contractId}/html2pdf")
    public ResponseEntity<Resource> generateContractPdf(@PathVariable long contractId) {
        try {
            // 1. 查找合同对象
            Contract contract = contractService.selectContractById(contractId);
            if (contract == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // 2. 生成 PDF
            String htmlFilePath = contractPath + "/contract_" + contractId + ".html";
            String pdfFilePath = contractPath + "/contract_" + contractId + ".pdf";
            saveHtmlToFile(contract.getContent(), htmlFilePath);
            generatePdfFromHtml(htmlFilePath, pdfFilePath);

            // 3. 读取 PDF 并返回给前端
            File file = new File(pdfFilePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // 生成 HTML 文件
    private void saveHtmlToFile(String htmlContent, String filePath) throws IOException {
        String htmlWrapper = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>合同</title>\n</head>\n<body>\n"
                + htmlContent +
                "\n</body>\n</html>";
        Files.write(Paths.get(filePath), htmlWrapper.getBytes());
    }
    // 调用 wkhtmltopdf 生成 PDF
    private void generatePdfFromHtml(String htmlFilePath, String pdfFilePath) throws IOException, InterruptedException {
//        ProcessBuilder builder = new ProcessBuilder(
//                "D:\\language_pakge\\wkhtmltopdf\\bin\\wkhtmltopdf.exe", "--enable-local-file-access", htmlFilePath, pdfFilePath
//        );
        ProcessBuilder builder = new ProcessBuilder(
                "wkhtmltopdf.exe", "--enable-local-file-access", htmlFilePath, pdfFilePath
        );

        builder.redirectErrorStream(true); // 合并标准输出和错误输出
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 读取输出
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {


            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[wkhtmltopdf] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("wkhtmltopdf 执行失败，退出码：" + exitCode);
        }
    }
    /**
     *  Word 转换为 PDF 方法
     */
    private void convertWordToPdf(String wordPath, String pdfPath) throws Exception {
        FileInputStream fis = new FileInputStream(wordPath);
        XWPFDocument document = new XWPFDocument(fis);

        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document pdfDoc = new Document(pdf);

        // 读取 Word 内容并写入 PDF
        document.getParagraphs().forEach(paragraph -> pdfDoc.add(new Paragraph(paragraph.getText())));

        // 关闭流
        pdfDoc.close();
        pdf.close();
        document.close();
        fis.close();
    }
}
