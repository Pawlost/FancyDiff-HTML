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
package org.pawlost.work.html.core;

import org.apache.commons.io.FileUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.pawlost.work.html.elements.WholeElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Objects;

//Downloads document from internet, works with files as IO
public class Connector {

    private String file;
    private String originPath;

    public Connector(String file, String originPath) {
        this.originPath = originPath;
        this.file = originPath + "/" + file;
    }

    public WholeElement getElement(String previousBulk) {
        Document webDocument = downloadHTML();
        Document bulkDocument = loadOldChapters(originPath + previousBulk);
        if (webDocument != null && bulkDocument != null) {
            return new WholeElement(webDocument, bulkDocument);
        } else {
            System.out.println("Documents not found");
        }
        return null;
    }

    private Document downloadHTML() {
        Document rawHTML = null;
        try {
            ArrayList<String> links = loadFile(file);
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

    public static void createFile(String filePath, String text) throws IOException {
        File file = new File(filePath);
        FileUtils.writeStringToFile(file, text + "\n", "UTF-8");
    }


    public ArrayList<String> loadFile(String fileName) throws IOException {
        ArrayList<String> loadedText = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String s;
        while ((s = br.readLine()) != null) {
            loadedText.add(s);
        }
        return (ArrayList<String>) loadedText.clone();
    }

    public Document loadOldChapters(String folderPath) {
        System.out.println("Loading file from location: " + folderPath);
        try {
            File folder = new File(folderPath);
            File html = null;
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file != null && !file.isDirectory()) {
                    html = file;
                }
            }

            if (html != null) {
                try {
                    StringBuilder builder = new StringBuilder();
                    for (String s : loadFile(html.getPath())) {
                        builder.append(s);
                    }
                    System.out.println("File loaded and saved \n");
                    return Jsoup.parse(builder.toString());
                } catch (IOException e) {
                    System.out.println("PDF file didnt load");
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e) {
            System.out.println("HTML file does not exist or there are no IO inside");
            e.printStackTrace();
        }
        return null;
    }
}