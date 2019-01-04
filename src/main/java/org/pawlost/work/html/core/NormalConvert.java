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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.pawlost.work.html.IO.Loader;
import org.pawlost.work.html.IO.Connector;
import org.pawlost.work.html.compare.HardCompare;
import org.pawlost.work.html.compare.SoftCompare;
import org.pawlost.work.html.elements.WholeElement;

import java.io.File;
import java.io.IOException;

import java.util.*;

public class NormalConvert {

    public static final String TEMPORARY_PATH = "temp/";
    public static final String DOC_WRAPPER_ID = "div.doc-wrapper";
    public static final String DOC_CHAPTER_ID = "section.chapter";

    private HashMap<String, String> information = new HashMap<>();
    private Connector Connector;
    private WholeElement element;
    private boolean canDestroy = true;

    public NormalConvert(String[] args) {
        try {
            System.out.println("\nStarting program \n");
            if (checkArguments(args)) {

                if (information.get("file") != null && information.get("r_id") != null && information.get("bulk") != null
                        && information.get("path") != null && information.get("p_bulk") != null) {

                    Connector = new Connector(information.get("file"), information.get("path"),
                            information.get("bulk"), information.get("r_id"));

                    element = Connector.getElement(information.get("p_bulk"));


                    String originDiffPath = information.get("path") + information.get("bulk") + "/comparison/";
                    File originDiffFile = new File(originDiffPath);
                    originDiffFile.mkdirs();

                    String tempDiffPath = Connector.getTemporaryPath() + "/diffChapters/";

                    HardCompare hardCompare = new HardCompare(element);
                    element = hardCompare.start();

                    if (element.getNewChapters().size() != 0 && element.getOldChapters().size() != 0) {
                        SoftCompare softCompare = new SoftCompare(element);
                        element = softCompare.start();
                    }

                    createDiff(tempDiffPath, originDiffFile.getAbsolutePath() + "/" + Connector.getChapterTitle()
                            + "-DIFF.html");

                    deleteTempFile(NormalConvert.TEMPORARY_PATH);
                }
            } else {
                System.out.println("Write all neccesary arguments or type --help to see correct order");
            }
        } catch (NullPointerException e) {
            System.out.println("Write correct parameters or type --help to see arguments");
            e.printStackTrace();
        }
    }

    public boolean checkArguments(String[] args) {
        System.out.println("Checking arguments \n");
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {

                case "--file":
                    information.put("file", args[i + 1]);
                    break;

                case "--revision_id":
                    information.put("r_id", args[i + 1]);
                    break;

                case "--bulk":
                    information.put("bulk", args[i + 1]);
                    break;

                case "--path":
                    information.put("path", args[i + 1]);
                    break;

                case "--previous_bulk":
                    information.put("p_bulk", args[i + 1]);
                    break;

                case "--tempFiles":
                    canDestroy = false;
                    break;

                case "--help":
                    System.out.println("Place paramaters in correct order <--file name.txt --revision_id id --bulk name " +
                            "--path /path/ --previous_bulk name>, optional parameters [-- wrapper_id id, --download_only, " +
                            "--test_mode, --tempFiles]");
                    return false;
            }
        }
        return true;
    }

    //end of compare system, diff creation
    public void createDiff(String tempPath, String originPath) {
        System.out.println("Creating diff from temporary IO");
        Loader Loader = new Loader();

       // HashMap<Integer, Document> diffChapter = Loader.loadTemporaryChapters(tempPath);
      //  HashMap<Integer, String> diffChapterName = Loader.showDiffChapterNames(tempPath);
        Document document = null;

        for (int i : diffChapter.keySet()) {
            Document doc = diffChapter.get(i);
            String name = diffChapterName.get(i);
            if (name.equals("removed") || name.equals("created")) {
                if (document != null) {
                    document = Jsoup.parse(document.html() + editDiffText(doc, name).html());
                } else {
                    document = Jsoup.parse(editDiffText(doc, name).html());
                }
            } else {
                if (document != null) {
                    document = Jsoup.parse(document.html() + doc.html());
                } else {
                    document = Jsoup.parse(doc.html());
                }
            }
        }
        try {
            Connector.createFile(originPath, Objects.requireNonNull(document).html());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Diff completed");
    }

    //creates colored changes
    private Document editDiffText(Document diffChapter, String type) {
        switch (type) {
            case "removed":
                for (Element edited : diffChapter.select("body")) {
                    String html = edited.html();
                    edited.remove();
                    diffChapter.append("<font color='red'><del>" + html + "</del></font>");
                }
                break;

            case "created":
                for (Element edited : diffChapter.select("body")) {
                    String html = edited.html();
                    edited.remove();
                    diffChapter.append("<font color='green'>" + html + "</font>");
                }
                break;

            case "tag":
                for (Element edited : diffChapter.select("body")) {
                    String html = edited.html();
                    edited.remove();
                    diffChapter.append("<font color='orange'>" + html + "</font>");
                }
                break;
        }
        return diffChapter.clone();
    }

    public void deleteTempFile(String tempPath) {
        if (canDestroy) {
            System.out.println("Deleting temp Chapters");
            File tempFiles = new File(tempPath);
            try {
                FileUtils.deleteDirectory(tempFiles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}