package com.ruoyi.web.util;

import com.itextpdf.text.DocumentException;
import com.lowagie.text.PageSize;
import com.ruoyi.web.domain.Contract;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class PdfGenerationService {

    public byte[] generateContractPdf(Contract contract) throws IOException, DocumentException, com.lowagie.text.DocumentException {
        // 生成 HTML 模板
        String htmlTemplate = generateHtmlTemplate(contract);

        // 创建 ITextRenderer 实例
        ITextRenderer renderer = new ITextRenderer();

        // 设置字体路径
        String fontPath = "fonts/SimSun.ttf"; // 根据实际路径调整
        URL fontUrl = getClass().getClassLoader().getResource(fontPath);
        if (fontUrl == null) {
            throw new IOException("字体文件未找到: " + fontPath);
        }
        String absoluteFontPath = fontUrl.getPath();

        // 设置字体
        renderer.getFontResolver().addFont(absoluteFontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


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
                "<head>" +
                "<meta charset='UTF-8'/>" +
                "<style>" +
                "body { font-family: 'SimSun'; }" +
                "h1, h2, h3, p { margin: 0; padding: 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                // 基本合同信息
                "<h1 style='text-align: center; font-size: 40px; font-family: SimSun;'>" +
                "<strong>" + contract.getTitle() + "</strong>" +
                "</h1>" +
                "<p><strong>创建人：</strong>" + contract.getCreatedBy() + "</p>" + "<br/>" +
                "<p><strong>合同状态：</strong>" + contract.getStatus() + "</p>" +"<br/>" +
                "<p><strong>甲方：</strong>" + contract.getPartyA() + "</p>" +"<br/>" +
                "<p><strong>乙方：</strong>" + contract.getPartyB() + "</p>" +"<br/>" +
                // 合同内容（原样嵌入）
                "<div>" +
                contract.getContent() +
                "</div>" +
                "</body>" +
                "</html>";
    }


}