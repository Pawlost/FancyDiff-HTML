package redhat.work.pdf.Files;

import com.itextpdf.html2pdf.HtmlConverter;

import java.io.*;

public class PDFCreater {

    public void writeFile(String filePath, String text) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
        bw.write(text);
        bw.newLine();
        bw.flush();
    }

    public void createPDF(String filePath, String html) throws IOException {
        OutputStream file = new FileOutputStream(new File(filePath));
        HtmlConverter.convertToPdf(html, file);
        file.close();
    }
}