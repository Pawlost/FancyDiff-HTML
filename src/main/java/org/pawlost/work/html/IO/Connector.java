/*  This file is part of FancyDiff-HTML.

    FancyDiff-HTML is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FancyDiff-HTML is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FancyDiff-HTML.  If not, see <https://www.gnu.org/licenses/>.
    */
package org.pawlost.work.html.IO;

import org.apache.commons.io.FileUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pawlost.work.html.core.NormalConvert;
import org.pawlost.work.html.elements.WholeElement;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

//Downloads document from internet
public class Connector {

    private final static String oldChapter = "/oldChapters/";
    private final static String newChapter = "/newChapters/";

    private Loader Loader;
    private String tempPath;
    private String file;
    private String r_id;
    private String originPath;
    private String chapterTitle;

    public Connector(String file, String originPath, String tempID, String r_id) {
        Loader = new Loader();
        tempPath = NormalConvert.TEMPORARY_PATH + tempID;
        this.originPath = originPath;
        this.file = originPath + "/" + file;
        this.r_id = r_id;
    }

    public WholeElement getElement(String previousBulk){
        Document webDocument = downloadHTML();
        Document bulkDocument = Loader.loadOldChapters(originPath + previousBulk);
        if (webDocument != null && bulkDocument != null){
            return new WholeElement(webDocument, bulkDocument);
        }else{
            System.out.println("Documents not found");
        }
        return null;
    }

    public void newHTMLFile(File path, String html) throws IOException{
        createFile(path.getAbsolutePath() + "/" + chapterTitle + "-<" + r_id + ">.html", html);
        System.out.println("File saved in " + path.getAbsolutePath() + "/" + chapterTitle + ".html \n");
        path = new File(path.getAbsolutePath() + "/.oldone/");
        path.mkdirs();
        createFile(path.getAbsolutePath() + "/.oldone-" + chapterTitle + "-<" + r_id + ">.html", html);
    }

    private Document downloadHTML() {
        Document rawHTML = null;
        try {
            ArrayList<String> links = Loader.loadFile(file);
            for (String s : links) {

                try {
                    System.out.println("Downloading file from " + s);
                    rawHTML = Jsoup.connect(s).get();
                    System.out.println("File downloaded" + "\n");

                } catch (IllegalArgumentException e) {
                    System.out.println("This is wrong URL " + s);
                }
            }
        } catch (IOException e) {
            System.out.println("Wrong file or unfunctional internet");
            e.printStackTrace();
        }
        return rawHTML.clone();
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

    public static void createFile(String filePath, String text) throws IOException {
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
