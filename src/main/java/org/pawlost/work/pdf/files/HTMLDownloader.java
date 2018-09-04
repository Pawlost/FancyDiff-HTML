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

package org.pawlost.work.pdf.files;

import org.apache.commons.io.FileUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pawlost.work.pdf.core.NormalConvert;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

public class HTMLDownloader {

    private final static String oldChapter = "/oldChapters/";
    private final static String newChapter = "/newChapters/";

    private HTMLLoader HTMLLoader;
    private String tempPath;
    private String file;
    private String r_id;
    private String originPath;
    private String chapterTitle;

    public HTMLDownloader(String file, String originPath, String tempID, String r_id) {
        HTMLLoader = new HTMLLoader();
        tempPath = NormalConvert.TEMPORARY_PATH + tempID;
        this.originPath = originPath;
        this.file = originPath + "/" + file;
        this.r_id = r_id;
    }

    public void newTemp(String bulk) throws IOException {
        Document rawHtml = downloadHTML();
        if (rawHtml != null) {
            Elements html = rawHtml.select(NormalConvert.DOC_WRAPPER_ID);

            chapterTitle = rawHtml.title().replaceAll(" ", "_");

            File tempFiles = new File(tempPath + newChapter);
            File path = new File(originPath + "/" + bulk);

            tempFiles.mkdirs();
            path.mkdirs();
            tempChapters(tempFiles.getAbsolutePath(), html);
            newHTMLFile(path, html.html());

        } else {
            System.out.println("Wrong file downloads");
        }
    }

    public void oldTemp (String previousBulk) {
        Document oldPDF = HTMLLoader.loadOriginalChapters(originPath + previousBulk + "/.htms/");
        File tempFiles = new File(tempPath + oldChapter);
        tempFiles.mkdirs();
        tempChapters(tempFiles.getAbsolutePath(), oldPDF.getAllElements());
    }

    public void newHTMLFile(File path, String html) throws IOException{
        createFile(path.getAbsolutePath() + "/" + chapterTitle + "-<" + r_id + ">.html", html);
        System.out.println("File saved in " + path.getAbsolutePath() + "/" + chapterTitle + ".html \n");
        path = new File(path.getAbsolutePath() + "/.oldone/");
        path.mkdirs();
        createFile(path.getAbsolutePath() + "/.oldone-" + chapterTitle + "-<" + r_id + ">.html", html);
    }

    private Document downloadHTML() {
        Document rawPdf = null;
        try {
            ArrayList<String> links = HTMLLoader.loadFile(file);
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
        return rawPdf.clone();
    }

    public void tempChapters(String absolutePath, Elements rawPdf) {
        Elements pdf= rawPdf.select(NormalConvert.DOC_CHAPTER_ID);
        for (int i = 0; i < pdf.size(); i++) {
            try {
                String file = "/" + "chapter" + (i + 1) + ".html";
                createFile(absolutePath + file, pdf.get(i).html());
                System.out.println("File splited and temporary saved in " +
                        absolutePath + file + "\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createFile(String filePath, String text) throws IOException {
        File file = new File(filePath);
        FileUtils.writeStringToFile(file, text + "\n", "UTF-8");
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

    public String getChapterTitle() {
        return chapterTitle;
    }
}
