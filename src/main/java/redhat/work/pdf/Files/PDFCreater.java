package redhat.work.pdf.Files;

import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.commons.io.FileUtils;
import java.io.*;

public class PDFCreater {

    public void writeFile(String filePath, String text) throws IOException {
        File file = new File(filePath);
        FileUtils.writeStringToFile(file, text + "\n", "UTF-8");
    }

    public void createPDF(String filePath, String html) throws IOException {
        OutputStream file = new FileOutputStream(new File(filePath));
        HtmlConverter.convertToPdf(html, file);
        file.close();
    }
}