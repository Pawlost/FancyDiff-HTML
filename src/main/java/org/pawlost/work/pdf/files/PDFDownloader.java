/*    This file is part of PDFConverter.

    PDFConverter is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PDFConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with PDFConverter.  If not, see <https://www.gnu.org/licenses/>.
    */

package pawlost.work.pdf.Files;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pawlost.work.pdf.Core.NormalConvert;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

public class PDFDownloader {

    private final static String oldChapter = "/oldChapters/";
    private final static String newChapter = "/newChapters/";

    private PDFLoader pdfLoader;
    private String tempPath;
    private String file;
    private String r_id;
    private String originPath;
    private String newPDFTitle;
    private PDFCreater pdfCreater;

    public PDFDownloader(String file, String originPath, String tempID, String r_id) {

        pdfLoader = new PDFLoader();
        tempPath = NormalConvert.TEMPORARY_PATH + tempID;
        pdfCreater = new PDFCreater();
        this.originPath = originPath;
        this.file = originPath + "/" + file;
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
            System.out.println("Wrong file or unfunctional internet");
            e.printStackTrace();
        }
        return rawPdf;
    }

    public void saveNewPDF(String bulk) {
        Document rawPdf = download();

        if (rawPdf != null) {
            Elements pdf = rawPdf.select(NormalConvert.DOC_WRAPPER_ID);

            newPDFTitle = rawPdf.title().replaceAll(" ", "_");

            File tempFiles = new File(tempPath + newChapter);
            File path = new File(originPath + "/" + bulk);

            tempFiles.mkdirs();
            path.mkdirs();

            saveChapters(tempFiles.getAbsolutePath(), pdf);
            try {
                pdfCreater.createPDF(path.getAbsolutePath() + "/" + newPDFTitle + "-<" + r_id + ">.pdf", pdf.html());
                System.out.println("File saved in " + path.getAbsolutePath() + "/" + newPDFTitle + ".pdf \n");
                path = new File(path.getAbsolutePath() + "/.htms/");
                path.mkdirs();
                pdfCreater.writeFile(path.getAbsolutePath() + "/.htms-" + newPDFTitle + "-<" + r_id + ">.html", pdf.html());

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("Wrong file downloads");
        }
    }

    public void saveOldPDF(String previousBulk) {
        Document oldPDF = pdfLoader.loadOriginalChapters(originPath + previousBulk + "/.htms/");
        File tempFiles = new File(tempPath + oldChapter);
        tempFiles.mkdirs();
        saveChapters(tempFiles.getAbsolutePath(), oldPDF.getAllElements());

    }

    private void saveChapters(String absolutePath, Elements rawPdf) {
        Elements pdf= rawPdf.select(NormalConvert.DOC_CHAPTER_ID);
        for (int i = 0; i < pdf.size(); i++) {
            try {
                String file = "/" + "chapter" + (i + 1) + ".html";
                pdfCreater.writeFile(absolutePath + file, pdf.get(i).html());
                System.out.println("File splited and temporary saved in " +
                        absolutePath + file + "\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTemporaryPathOld() {
        return tempPath + oldChapter;
    }

    public String getTemporaryPathNew() {
        return tempPath + newChapter;
    }

    public String getTemporaryPath() {
        return tempPath;
    }

    public String getNewPDFTitle() {
        return newPDFTitle;
    }
}
