package redhat.work.pdf.Files;

import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.commons.io.FileUtils;
import java.io.*;

public class PDFCreater {

    public void writeFile(String filePath, String text) throws IOException {
        FileUtils.writeStringToFile(new File(filePath), text, "UTF-8");
    }

    public void createPDF(String filePath, String html) throws IOException {
        OutputStream file = new FileOutputStream(new File(filePath));
        HtmlConverter.convertToPdf(html, file);
        file.close();
    }
}