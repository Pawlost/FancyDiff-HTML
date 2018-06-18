package redhat.work.pdf.Files;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import redhat.work.pdf.Core.Objects.FilePDF;

import java.io.IOException;

import java.util.ArrayList;

public class PDFDownloader {
    private PDFLoader pdfLoader;
    private ArrayList<FilePDF> pdfs;
    public PDFDownloader(String file, String absolutePath) {
        if (absolutePath != null) {
            pdfLoader = new PDFLoader(file, absolutePath);
        }else{
            pdfLoader = new PDFLoader(file);
        }

        download();
    }

    private void download() {
        try {
            ArrayList<String> links = pdfLoader.loadFileString();
            for (String s : links) {
                try {
                    System.out.println("Downloading file from " + s);
                    FilePDF pdf = new FilePDF(Jsoup.connect(s).get());
                    pdfs.add(pdf);
                    System.out.println("File downloaded" + "\n");
                } catch (IllegalArgumentException e) {
                    System.out.println("This is wrong URL " + s);
                }
            }
        } catch (IOException e) {
            System.out.println("Wrong file");
            e.printStackTrace();
        }
    }

    private ArrayList<FilePDF> getPdfs(){
        return (ArrayList<FilePDF>) pdfs.clone();
    }
}
