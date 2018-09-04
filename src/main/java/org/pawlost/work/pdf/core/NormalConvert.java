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

package org.pawlost.work.pdf.core;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.pawlost.work.pdf.files.HTMLLoader;
import org.pawlost.work.pdf.files.HTMLDownloader;

import java.io.File;
import java.io.IOException;

import java.util.*;

public class NormalConvert {

    public static final String TEMPORARY_PATH = "temp/";
    public static final String DOC_WRAPPER_ID = "div.doc-wrapper";
    public static final String DOC_CHAPTER_ID = "section.chapter";
    private HashMap<String, String> information = new HashMap<>();
    private HTMLDownloader HTMLDownloader;
    private boolean canDestroy = true;

    public NormalConvert() {
    }

    public NormalConvert(String[] args) {
        try {
            System.out.println("\nStarting program \n");
            if (checkArguments(args)) {

                if (information.get("file") != null && information.get("r_id") != null && information.get("bulk") != null
                        && information.get("path") != null && information.get("p_bulk") != null) {

                    HTMLDownloader = new HTMLDownloader(information.get("file"), information.get("path"),
                            information.get("bulk"), information.get("r_id"));

                    download(information.get("bulk"), information.get("p_bulk"));

                    String originDiffPath = information.get("path") + information.get("bulk") + "/comparison/";
                    File originDiffFile = new File(originDiffPath);
                    originDiffFile.mkdirs();

                    HashMap<Integer, Document> oldPDFChapters = load(HTMLDownloader.getTemporaryPathOld());
                    HashMap<Integer, Document> newPDFChapters = load(HTMLDownloader.getTemporaryPathNew());

                    String tempDiffPath = HTMLDownloader.getTemporaryPath() + "/diffChapters/";

                    HashMap<String, HashMap<Integer, Document>> comparedSources =
                            hardCompare((HashMap<Integer, Document>) oldPDFChapters.clone(), (HashMap<Integer,
                                    Document>) newPDFChapters.clone(), tempDiffPath);

                    comparedSources = normalCompare(comparedSources, tempDiffPath);

                    if (comparedSources.get("old").size() != 0 && comparedSources.get("new").size() != 0) {
                        softCompare(comparedSources, tempDiffPath);
                    }

                    createDiff(tempDiffPath, originDiffFile.getAbsolutePath() + "/" + HTMLDownloader.getChapterTitle()
                            + "-DIFF.html");

                    deleteFile(HTMLDownloader.getTemporaryPath());
                }
            } else {
                System.out.println("Write all neccesary arguments or type --help to see correct order");
            }
        } catch (NullPointerException e) {
            System.out.println("Write correct parameters or type --help to see arguments");
            e.printStackTrace();
        } catch (IOException e) {
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

    public void download(String bulk, String previousBulk) throws IOException {
        HTMLDownloader.newTemp(bulk);
        HTMLDownloader.oldTemp(previousBulk);
    }

    public HashMap<Integer, Document> load(String tempFolder) {
        HTMLLoader HTMLLoader = new HTMLLoader();
        return (HashMap<Integer, Document>) HTMLLoader.loadTemporaryChapters(tempFolder).clone();
    }

    //hard compare, compares whole pages
    public HashMap<String, HashMap<Integer, Document>> hardCompare(HashMap<Integer, Document> oldPDFChapters,
                                                                   HashMap<Integer, Document> newPDFChapters,
                                                                   String tempFolder) throws IOException {
        System.out.println("Starting Hard Compare");
        int size = (oldPDFChapters.size() <= newPDFChapters.size() ? oldPDFChapters.size() : newPDFChapters.size());
        File dir = new File(tempFolder);
        dir.mkdirs();
        for (int i = 1; i <= size; i++) {
            String htmlOldString = oldPDFChapters.get(i).html();
            String htmlNewString = newPDFChapters.get(i).html();

            htmlNewString = htmlNewString.replace(" ", "");
            htmlOldString = htmlOldString.replace(" ", "");

            if (htmlNewString.equals(htmlOldString)) {
                HTMLDownloader.createFile(tempFolder + "same" + i + ".html", newPDFChapters.get(i).html());
                oldPDFChapters.remove(i);
                newPDFChapters.remove(i);
            }
        }
        HashMap<String, HashMap<Integer, Document>> returnHashMap = new HashMap<>();

        returnHashMap.put("old", oldPDFChapters);
        returnHashMap.put("new", newPDFChapters);

        System.out.println("Hard compare done");
        System.out.println("Chapters remain to compare: " + oldPDFChapters.size() + " old chapter and " + newPDFChapters.size()
                + " new chapter \n");
        return (HashMap<String, HashMap<Integer, Document>>) returnHashMap.clone();
    }

    //argorithm itself, frontend of resources
    public void softCompare(HashMap<String, HashMap<Integer, Document>> comparedSources, String tempPath) {
       System.out.println("Starting soft compare");

        Document difference = null;
        HashMap<Integer, Document> oldPDFChapters = comparedSources.get("old");
        HashMap<Integer, Document> newPDFChapters = comparedSources.get("new");
        int size1 = (oldPDFChapters.size() <= newPDFChapters.size() ? newPDFChapters.size() : oldPDFChapters.size());
        System.out.println(size1);
        for (int i = 1; i <= size1; i++) {
            Document oldDiv = oldPDFChapters.get(i);
            Document newDiv = newPDFChapters.get(i);
            String[] tags = new String[]{"html", "div", "section", "code", "a", "em", "span", "strong", "ol"
                    , "li", "table", "tbody", "tr", "td", "dl", "dt", "dd", "ul"};
            try {
                for (String tag : tags) {
                    try {
                        for (Element elem : oldDiv.getElementsByTag(tag)) {
                            elem.parent().insertChildren(elem.siblingIndex(), elem.childNodes());
                            elem.remove();
                        }
                    } catch (NullPointerException ignore) {
                    }

                    try {
                        for (Element elem : newDiv.getElementsByTag(tag)) {
                            elem.parent().insertChildren(elem.siblingIndex(), elem.childNodes());
                            elem.remove();
                        }
                    } catch (NullPointerException ignore) {
                    }
                }
                try {
                    int oldSize = oldDiv.body().getAllElements().size();
                    int newSize = newDiv.body().getAllElements().size();
                    int size2 = (oldSize <= newSize ? newSize : oldSize);
                    for (int text = 1; text < size2; text++) {
                        try {
                            Element newDiv2 = newDiv.body().getAllElements().get(text);
                            Element oldDiv2 = oldDiv.body().getAllElements().get(text);

                            if (newDiv2.html().equals(oldDiv2.html())) {
                                if (difference != null) {
                                    difference = Jsoup.parse(difference.html() + "<p>\n</p>" + newDiv2.html()+ "<p>\n</p>");
                                } else {
                                    difference = Jsoup.parse(newDiv2.html());
                                }
                            } else {
                                Document removed = editDiffChapter(Jsoup.parse(oldDiv2.html()), "removed");
                                Document created = editDiffChapter(Jsoup.parse(newDiv2.html()), "created");
                                if (difference != null) {
                                    difference = Jsoup.parse(difference.html() + "<p>\n</p>" + removed.html() +
                                            "<p>\n</p>" + created.html() + "<p>\n</p>");
                                } else {
                                    difference = Jsoup.parse(removed.html() + "<p>\n</p>" + created.html() + "<p>\n</p>");
                                }
                            }
                        } catch (IndexOutOfBoundsException ignore) {
                            try {
                                Element newDiv2 = newDiv.body().getAllElements().get(text);
                                Document created = editDiffChapter(Jsoup.parse(newDiv2.html()), "created");
                                if (difference != null) {
                                    difference = Jsoup.parse(difference.html() + "<p>\n</p>" + created.html() + "<p>\n</p>");
                                } else {
                                    difference = Jsoup.parse(created.html() + "<p>\n</p>");
                                }
                            } catch (IndexOutOfBoundsException ignore2) {
                                Element oldDiv2 = oldDiv.body().getAllElements().get(text);
                                Document removed = editDiffChapter(Jsoup.parse(oldDiv2.html()), "removed");
                                if (difference != null) {
                                    difference = Jsoup.parse(difference.html() + "<p>\n</p>" + removed.html() + "<p>\n</p>");
                                } else {
                                    difference = Jsoup.parse(removed.html() + "<p>\n</p>");
                                }
                            }
                        }
                    }
                } catch (NullPointerException ignore) {
                    try {
                        oldDiv.getAllElements();
                        oldDiv = editDiffChapter(oldDiv, "removed");
                        difference = Jsoup.parse(oldDiv.html());
                    } catch (NullPointerException ignore1) {
                        if(newDiv != null) {
                            newDiv = editDiffChapter(newDiv, "created");
                            difference = Jsoup.parse(newDiv.html());
                        }
                    }
                }
                if(difference != null) {
                    HTMLDownloader.createFile(tempPath + "/difference" + i + ".html", difference.html());
                    difference = null;
                }

            } catch (IOException ignore) {
            }
        }
        System.out.println("Soft compare done");
    }

    //end of compare system, diff creation
    public void createDiff(String tempPath, String originPath) {
        System.out.println("Creating diff from temporary files");
        HTMLLoader HTMLLoader = new HTMLLoader();

        HashMap<Integer, Document> diffChapter = HTMLLoader.loadTemporaryChapters(tempPath);
        HashMap<Integer, String> diffChapterName = HTMLLoader.showDiffChapterNames(tempPath);
        Document document = null;

        for (int i : diffChapter.keySet()) {
            Document doc = diffChapter.get(i);
            String name = diffChapterName.get(i);
            if (name.equals("removed") || name.equals("created")) {
                if (document != null) {
                    document = Jsoup.parse(document.html() + editDiffChapter(doc, name).html());
                } else {
                    document = Jsoup.parse(editDiffChapter(doc, name).html());
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
            HTMLDownloader.createFile(originPath, Objects.requireNonNull(document).html());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Diff completed");
    }

    //creates colored changes
    private Document editDiffChapter(Document diffChapter, String type) {
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
        }
        return diffChapter.clone();
    }

    //finds added chapters and deletes them
    public HashMap<String, HashMap<Integer, Document>> normalCompare(HashMap<String, HashMap<Integer, Document>>
                                                                             comparedSources, String tempPath) {
        System.out.println("Normal compare");
        HashMap<Integer, Document> oldPDFChapters = comparedSources.get("old");
        HashMap<Integer, Document> newPDFChapters = comparedSources.get("new");
        if (oldPDFChapters.size() < newPDFChapters.size()) {
            ArrayList<Integer> keys = (ArrayList<Integer>) new ArrayList(newPDFChapters.keySet());
            for (int i : keys) {
                if (i > oldPDFChapters.size()) {
                    try {
                        HTMLDownloader.createFile(tempPath + "created" + i + ".html", newPDFChapters.get(i).html());
                        newPDFChapters.remove(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (oldPDFChapters.size() > newPDFChapters.size()) {
            ArrayList<Integer> keys = (ArrayList<Integer>) new ArrayList(oldPDFChapters.keySet());
            for (int i : keys) {
                if (i > newPDFChapters.size()) {
                    try {
                        HTMLDownloader.createFile(tempPath + "removed" + i + ".html", oldPDFChapters.get(i).html());
                        oldPDFChapters.remove(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Normal compare done");
        System.out.println("Chapters remain to compare: " + oldPDFChapters.size() + " old chapter and " + newPDFChapters.size()
                + " new chapter \n");
        comparedSources = new HashMap<>();
        comparedSources.put("new", newPDFChapters);
        comparedSources.put("old", oldPDFChapters);
        return comparedSources;
    }

    public void deleteFile(String tempPath) {
        if (canDestroy) {
            System.out.println("Deleting temp files");
            File tempFiles = new File(tempPath);
            try {
                FileUtils.deleteDirectory(tempFiles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setHTMLDownloader(org.pawlost.work.pdf.files.HTMLDownloader HTMLDownloader) {
        this.HTMLDownloader = HTMLDownloader;
    }
}