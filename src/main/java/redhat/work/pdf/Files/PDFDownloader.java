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
    private String tempPath;
    private String file;
    private String r_id;
    private PDFCreater pdfCreater;

    public PDFDownloader(String file, String absolutePath, String tempID, String r_id) {

        pdfLoader = new PDFLoader();
        tempPath = Convert.TEMPORARY_PATH+"/"+ tempID;
        pdfCreater = new PDFCreater();

        this.file = absolutePath +"/"+ file;
        this.r_id = r_id;
    }

    private Document download() {
        Document rawPdf = null;
        try {
            ArrayList<String> links = pdfLoader.loadFile(file);
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
            System.out.println("Wrong file or unfunction internet");
            e.printStackTrace();
        }
        return rawPdf;
    }

    public void saveNewPDF(String originPath, String bulk) {
        Document rawPdf = download();

        if (rawPdf != null) {
            Elements pdf = rawPdf.select(Convert.DOC_WRAPPER_ID);
            Elements splitPdf = rawPdf.select(Convert.DOC_CHAPTER_ID);
            String title = rawPdf.title().replaceAll(" ", "_");

            File tempFiles = new File(tempPath+"/newChapter/");
            File path = new File(originPath +"/"+bulk);

            tempFiles.mkdirs();
            path.mkdirs();

            try {
                pdfCreater.createPDF(path.getAbsolutePath() + "/" + title + "-<" + r_id + ">.pdf", pdf.html());
                path = new File(path.getAbsolutePath() +"/HTML/");
                path.mkdirs();
                pdfCreater.writeFile(path.getAbsolutePath() + "/HTML-" + title + "-<" + r_id + ">.html", pdf.html());

                for (int i = 0; i < splitPdf.size(); i++) {
                    Element e = splitPdf.get(i);
                    pdfCreater.writeFile(tempFiles.getAbsolutePath() +"/" + "chapter" + (i + 1) + ".html", e.html());
                    System.out.println("File splited and temporary saved in " +
                            tempFiles.getAbsolutePath()  + "/" + "chapter" + (i +1) + ".pdf \n");
                }

                System.out.println("File saved in " + path.getAbsolutePath() + "/" + path.getName() + title + ".pdf +\n");

            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }else{
            System.out.println("Wrong file downloads");
        }
    }

    public void saveOldPDF(String originPath, String previousBulk) {
        Document oldPDF = pdfLoader.loadPDF(originPath + previousBulk+"/HTML/");
        File tempFiles = new File(tempPath+"/oldChapter/");
        tempFiles.mkdirs();

        for(int i=0; i < oldPDF.select(Convert.DOC_CHAPTER_ID).size(); i++) {
            Elements oldChapter = oldPDF.select(Convert.DOC_CHAPTER_ID);
            try {
                pdfCreater.writeFile(tempFiles.getAbsolutePath() + "/" + "chapter" + (i +1) + ".html", oldChapter.get(i).html());
                System.out.println("File splited and temporary saved in " +
                        tempFiles.getAbsolutePath() + "/" + "chapter" + (i +1) + ".html \n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAbsolutePath(){
        return tempPath;
    }
}
