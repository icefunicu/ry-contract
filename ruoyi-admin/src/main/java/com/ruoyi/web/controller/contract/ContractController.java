package com.ruoyi.web.controller.contract;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.Contract;
import com.ruoyi.web.domain.ContractApproval;
import com.ruoyi.web.domain.ContractNotify;
import com.ruoyi.web.domain.ContractNotifyVo;
import com.ruoyi.web.service.IContractApprovalService;
import com.ruoyi.web.service.IContractNotifyService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
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
    @Autowired
    private IContractNotifyService contractNotifyService;
    @Autowired
    private IContractApprovalService contractApprovalService;
    /**
     * 查询合同列表
     */

    @GetMapping("/list")
    public TableDataInfo list(Contract contract)
    {
        startPage();
        // 判断用户身份，如果是管理员返回所有数据，如果是普通用户返回自己的数据
        if (getUserId()==1) {
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
        List<Contract> list = contractService.selectContractByPaOrPb(getUserId());
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
    /*
    * 根据合同标题查询合同
    * */
    @PostMapping("/search")
    public TableDataInfo search(Contract contract){
        startPage();
        List<Contract> list = contractService.selectContractByTitle(contract.getTitle());
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

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ContractNotify contractNotify = new ContractNotify();
        contractNotify.setContractId(id);
        List<ContractNotify> contractNotifies = contractNotifyService.selectContractNotifyList(contractNotify);
        Contract contract = contractService.selectContractById(id);
        int userId = contract.getCreatedBy();
        int partyA = contract.getPartyA();
        int partyB = contract.getPartyB();
        contract.setCreatedByName(userService.selectUserById((long) userId).getNickName());
        contract.setPartyAName(userService.selectUserById((long)partyA).getNickName());
        contract.setPartyBName(userService.selectUserById((long)partyB).getNickName());
        ContractNotifyVo contractNotifyVo = new ContractNotifyVo();
        contractNotifyVo.setContract(contract);
        contractNotifyVo.setNotifyInfoList(contractNotifies);
        return success(contractNotifyVo);
    }

    /**
     * 新增合同
     */

    @Log(title = "合同", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Contract contract, HttpServletRequest request)
    {
        contract.setCreatedBy(Math.toIntExact(getUserId()));

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
     *  开始签署合同
     * */
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody Contract contract){
        contract.setStatus("待查看");
        return success(contractService.updateContract(contract));
    }
    /**
     * 删除合同
     */

    @Log(title = "合同", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contractService.deleteContractByIds(ids));
    }

    /**
     *  根据 contractId 查找合同并转换为 PDF 返回
     */
    @GetMapping("/{contractId}/word2pdf")
    public ResponseEntity<InputStreamResource> getContractPdf(@PathVariable String contractId) throws Exception {
        Contract contract = contractService.selectContractById(Long.parseLong(contractId));

        String docxPath = fileLocation + contract.getFilePath();

        File docxFile = new File(docxPath);
        if (!docxFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 临时 PDF 文件路径
        File pdfFile = File.createTempFile(fileLocation + contractId, ".pdf");

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
    *
    *  提交意见，更新合同状态为待修改
    * */
    @PostMapping("/submitOpinion")
    public AjaxResult submitOpinion(@RequestBody Contract contract){
        Long contractId = (long) contract.getId();
        String opinion = contract.getOpinion();
        contract = contractService.selectContractById(contractId);
        if(contract.getCreatedBy() == getUserId()){
            ContractNotify contractNotify = new ContractNotify();
            contractNotify.setContractId(contractId);
            contractNotify.setContent(opinion);
            contractNotify.setNotifyStatus("未处理");
            contractNotify.setUserId(getUserId());
            contractNotify.setUserName(userService.selectUserById(getUserId()).getNickName());
            contractNotifyService.insertContractNotify(contractNotify);
            contractService.updateContract(contract);
            return success();
        }
        contract.setStatus("待修改");
        ContractNotify contractNotify = new ContractNotify();
        contractNotify.setContractId(contractId);
        contractNotify.setContent(opinion);
        contractNotify.setNotifyStatus("未处理");
        contractNotify.setUserId(getUserId());
        contractNotify.setUserName(userService.selectUserById(getUserId()).getNickName());
        contractNotifyService.insertContractNotify(contractNotify);
        contractService.updateContract(contract);
        return success();
    }


    /**
     *  通过合同，更新合同状态为法务审核
     * */
    @PostMapping("/pass")
    public AjaxResult pass(@RequestBody Contract contract) throws IOException, InterruptedException {

        contract.setStatus("法务审核");
        ContractApproval contractApproval = new ContractApproval();
        contractApproval.setContractId((long) contract.getId());
        contractApproval.setComment("法务审核");
        contractApproval.setApproverId(104L);
        contractApproval.setStatus("法务审核");
        contractApproval.setApprovedTime(new Date());

        contractApprovalService.insertContractApproval(contractApproval);
        contract.setFilePath(generatePdfPath(contract.getId()));
        return success(contractService.updateContract(contract));
    }

    private String generatePdfPath(int contractId) throws IOException, InterruptedException {
        Contract contract = contractService.selectContractById((long) contractId);
        if (contract == null) {
            return null;
        }
        // 2. 生成 PDF
        String htmlFilePath = fileLocation + "/contract_" + contractId + ".html";
        String pdfFilePath = fileLocation + "/contract_" + contractId + ".pdf";

        saveHtmlToFile(contract.getContent(), htmlFilePath);
        generatePdfFromHtml(htmlFilePath, pdfFilePath);

        return "http://localhost:8080/profile/" + "contract_" + contractId + ".pdf";
    }

    /**
     * 上传接口,用来接收前端传来的二进制文件
     * */
    @PostMapping("/uploadfile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，文件为空";
        }

        try {
            // 获取文件路径
            String filePath = Paths.get(file.getOriginalFilename()).toString();

            // 保存文件
            file.transferTo(new File(filePath));

            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return "文件上传失败";
        }
    }

    @Value("${ruoyi.profile}")
    private String fileLocation;

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
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource1 = classLoader.getResource("");
            String classpath = resource1.getPath();
            if (classpath.startsWith("/")) {
                classpath = classpath.substring(1);
            }
            String htmlFilePath =  fileLocation+ "/contract_" + contractId + ".html";
            String pdfFilePath =  fileLocation+ "/contract_" + contractId + ".pdf";
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
                "\n<p>电子印章盖章处：</p ><p><br data-mce-bogus=\"1\"></p ><p><br data-mce-bogus=\"1\"></p ><p><br data-mce-bogus=\"1\"></p ><p><br data-mce-bogus=\"1\"></p ><p>个人手写签名处：</p ></body></body>\n</html>";
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
