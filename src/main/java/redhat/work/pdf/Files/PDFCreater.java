package redhat.work.pdf.Files;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class PDFCreater {

    private String filePath;

    public PDFCreater (String fileName){
        filePath = "/Converted/" + fileName +".pdf";
    }

    public void writeFileString(String[] text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            for (String subtext : text) {
                bw.write(subtext);
                bw.newLine();
                bw.flush();
                System.out.println("Do souboru bylo připsáno");
            }
        } catch (Exception e) {
            System.err.println("Do souboru se nepovedlo zapsat.");
        }
    }
}
