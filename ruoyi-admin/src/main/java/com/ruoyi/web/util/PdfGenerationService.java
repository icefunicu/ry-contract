package com.ruoyi.web.util;

import com.ruoyi.web.domain.Contract;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PdfGenerationService {

    public byte[] generateContractPdf(Contract contract) throws IOException, DocumentException, com.lowagie.text.DocumentException {
        // 生成 HTML 模板
        String htmlTemplate = generateHtmlTemplate(contract);

        // 创建 ITextRenderer 实例
        ITextRenderer renderer = new ITextRenderer();

        // 从 resources 中加载字体文件
        String fontPath = "fonts/SimSun.ttf"; // 字体文件路径
        InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontPath);
        if (fontStream == null) {
            throw new IOException("字体文件未找到: " + fontPath);
        }

        // 使用 BaseFont 加载字体文件
        FontFactory.getFont("SimSun", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        // 设置字体
        renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        // 使用 Flying Saucer 渲染 HTML
        renderer.setDocumentFromString(htmlTemplate);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.layout();
        renderer.createPDF(outputStream);

        // 返回生成的 PDF 字节流
        return outputStream.toByteArray();
    }

    private String generateHtmlTemplate(Contract contract) {
        return "<html>" +
                "<head><style>" +
                "body {font-family: SimSun, sans-serif; font-size: 12px; line-height: 1.6;}" +  // 使用 SimSun 字体
                "h1 {color: #0073e6;}" +
                "p {margin: 10px 0;}" +
                ".contract-details {margin-top: 20px;}" +
                "strong {font-weight: bold;}" +
                "</style></head>" +
                "<body>" +
                "<h1>" + contract.getTitle() + "</h1>" +
                "<p><strong>创建人：</strong>" + contract.getCreatedBy() + "</p>" +
                "<p><strong>合同状态：</strong>" + contract.getStatus() + "</p>" +
                "<p><strong>甲方：</strong>" + contract.getPartyA() + "</p>" +
                "<p><strong>乙方：</strong>" + contract.getPartyB() + "</p>" +
                "<div class='contract-details'>" + contract.getContent() + "</div>" +
                "</body>" +
                "</html>";
    }
}