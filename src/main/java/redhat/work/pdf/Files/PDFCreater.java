package redhat.work.pdf.Files;

import com.itextpdf.html2pdf.HtmlConverter;

import java.io.*;

public class PDFCreater {

    private String filePath;

    public PDFCreater (String fileName){
        filePath =  fileName;
    }

    public PDFCreater (String fileName, String path){
        filePath = path+ fileName;
    }

    public void writeFile(String text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
                bw.write(text);
                bw.newLine();
                bw.flush();
                System.out.println("Do souboru bylo připsáno");
        } catch (Exception e) {
            System.err.println("Do souboru se nepovedlo zapsat.");
        }
    }

    public void createPDF(String html) throws Exception{
        OutputStream file = new FileOutputStream(new File(filePath));
        HtmlConverter.convertToPdf(html, file);
        file.close();
    }
}
