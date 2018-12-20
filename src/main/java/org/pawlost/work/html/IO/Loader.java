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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pawlost.work.html.elements.WholeElement;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Loader {

    public ArrayList<String> loadFile(String fileName) throws IOException {
        ArrayList<String> loadedText = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String s;
        while ((s = br.readLine()) != null) {
            loadedText.add(s);
        }
        return (ArrayList<String>) loadedText.clone();
    }

    public Document loadOriginalChapters(String folderPath) {
        System.out.println("Loading file from location: "+folderPath);
        try {
            File folder = new File(folderPath);
            File pdf = null;
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file != null && !file.isDirectory()) {
                    pdf = file;
                }
            }

            if (pdf != null) {
                try {
                    StringBuilder builder = new StringBuilder();
                    for (String s : loadFile(pdf.getPath())) {
                        builder.append(s);
                    }
                    System.out.println("File loaded and saved \n");
                    return Jsoup.parse(builder.toString());
                } catch (IOException e) {
                    System.out.println("PDF file didnt load");
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException e){
            System.out.println("HTML file does not exist or there are no IO inside");
            e.printStackTrace();
        }
        return null;
    }

    public WholeElement loadTemporaryChapters(String folderPath) {
        WholeElement element = new WholeElement();


        StringBuilder builder = new StringBuilder();
        System.out.println("Loading file from location: " + folderPath);
        try {
            File folder = new File(folderPath);
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file != null && !file.isDirectory()) {
                    for (String s : loadFile(file.getPath())) {
                        builder.append(s);
                    }
                    Matcher m = Pattern.compile("[0-9]+").matcher(file.getName());
                    if (m.find()) {
                        int pageNumber = Integer.parseInt(m.group());
                        Document document = Jsoup.parse(builder.toString());
                        clearHtmls.put(pageNumber, document);
                        builder.setLength(0);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("HTML file does not exist or there are no IO inside");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("PDF file didnt load");
            e.printStackTrace();
        }
        System.out.println("IO pushed to compare\n");
        return (HashMap<Integer, Document>) clearHtmls.clone();
    }

    public HashMap<Integer, String> showDiffChapterNames(String folderPath){
        HashMap<Integer, String> chapterNames = new HashMap<>();
        System.out.println("Loading names from location: "+folderPath);
        try {
            File folder = new File(folderPath);
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file != null && !file.isDirectory()) {

                    Matcher m = Pattern.compile("[0-9]+").matcher(file.getName());

                    if( m.find()) {
                        int pageNumber = Integer.parseInt(m.group());
                        chapterNames.put(pageNumber, file.getName().replace(pageNumber+".html", ""));
                    }
                }
            }
        }catch (NullPointerException e){
            System.out.println("HTML file does not exist or there are no IO inside");
            e.printStackTrace();
        }
        System.out.println("Names pushed to compare\n");
        return (HashMap<Integer, String>) chapterNames.clone();
    }
}