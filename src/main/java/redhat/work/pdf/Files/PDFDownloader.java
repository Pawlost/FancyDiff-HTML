package redhat.work.pdf.Files;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redhat.work.pdf.Core.Convert;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

public class PDFDownloader {

    private PDFLoader pdfLoader;
    private String file;
    private String absolutePath;
    private String bulk;
    private String r_id;

    public PDFDownloader(String file, String absolutePath, String bulk, String r_id) {
        pdfLoader = new PDFLoader();
        this.file = file;
        this.r_id = r_id;
        this.absolutePath = absolutePath;
        this.bulk = bulk;
        download();
    }

    private Document download() {
        Document rawPdf = null;
        try {
            ArrayList<String> links = pdfLoader.loadFile(file, absolutePath);
            for (String s : links) {
                try {
                    System.out.println("Downloading file from " + s);
                    rawPdf = Jsoup.connect(s).get();
                    System.out.println("File downloaded" + "\n");
                } catch (IllegalArgumentException e) {
                    System.out.println("This is wrong URL " + s);
                }
            }
        } catch (IOException e) {
            System.out.println("Wrong file");
            e.printStackTrace();
        }
        return rawPdf;
    }

    public String savePDF() {
        Document rawPdf = download();
        if (rawPdf != null) {
            Elements pdf = rawPdf.select(Convert.DOC_WRAPPER_ID);
            String title = rawPdf.title();
            title = title.replaceAll(" ", "_");
            File path = new File(absolutePath + bulk);
            File tempPath = new File(Convert.TEMPORARY_PATH + bulk);
            tempPath.mkdir();
            path.mkdir();
            PDFCreater pdfCreater = new PDFCreater();
            Elements splitPDF = pdf.select(Convert.DOC_CHAPTER_ID);
            try {
                pdfCreater.createPDF(path.getAbsolutePath() + "/" + title + "-<" + r_id + ">.pdf", pdf.html());
                for (int i = 0; i < splitPDF.size(); i++) {
                    Element e = splitPDF.get(i);
                    pdfCreater.createPDF(tempPath.getAbsolutePath() + "/" + e.id() + "-page" + i + ".pdf", e.html());
                    System.out.println("File splited and temporary saved in " +
                            tempPath.getAbsolutePath() + "/" + e.id() + "-page" + i + ".pdf");
                }
                System.out.println("File saved in " + path.getAbsolutePath() + "/" + path.getName() + title + ".pdf");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tempPath.getAbsolutePath();
        }else{
            System.out.println("Wrong file download");
            return null;
        }
    }
}
