package redhat.work.pdf.Files;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PDFDownloader {
    public PDFDownloader(String file){
        try {
            PDFLoader pdfLoader = new PDFLoader(file, "txt");
            ArrayList<String> links = pdfLoader.loadFileString();
            for (String s:links){
                URI uri = new URI(s);
                System.out.println(uri);
            }
        }catch (IOException e){
            System.out.println("Wrong file name");
        } catch (URISyntaxException e) {
            System.out.println("Wrong path name in <" + file + ">");
        }
    }
}
