package redhat.work.pdf.Files;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redhat.work.pdf.Core.Convert;
import redhat.work.pdf.Core.Objects.FilePDF;

import java.io.File;
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
                    savePDF(Jsoup.connect(s).get());
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

    private void savePDF(Document document) {
        Elements pdf = document.select("div.doc-wrapper");
        String title = document.title();
        title = title.replaceAll(" ", "_");
        File path = new File(Convert.TEMPORARY_PATH + title );
        path.mkdir();
        PDFCreater pdfCreater = new PDFCreater(path.getPath() + "/" + path.getName() + title +".pdf");
        try {
            pdfCreater.createPDF(pdf.html());
            System.out.println("File temporary saved in " + path.getAbsolutePath() + "/"  + path.getName()  + title +".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<FilePDF> getPdfs(){
        return (ArrayList<FilePDF>) pdfs.clone();
    }
}
