package redhat.work.pdf.Core;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import redhat.work.pdf.Files.PDFCreater;
import redhat.work.pdf.Files.PDFDownloader;
import redhat.work.pdf.Files.PDFLoader;

import java.io.File;
import java.io.IOException;

import java.util.*;

public class NormalConvert {

    public static final String TEMPORARY_PATH = "temp/";
    public static final String DOC_WRAPPER_ID = "div.doc-wrapper";
    public static final String DOC_CHAPTER_ID = "section.chapter";
    private HashMap<String, String> information = new HashMap<>();

    public NormalConvert() {
    }

    public NormalConvert(String[] args) {
        try {
            System.out.println("\nStarting program \n");
            if (checkArguments(args)) {

                if (information.get("file") != null && information.get("r_id") != null && information.get("bulk") != null
                        && information.get("path") != null && information.get("p_bulk") != null) {

                    PDFDownloader pdfDownloader = new PDFDownloader(information.get("file"), information.get("path"),
                            information.get("bulk"), information.get("r_id"));

                    download(information.get("bulk"), information.get("p_bulk"), pdfDownloader);

                    String originDiffPath = information.get("path") + information.get("bulk") + "/comparison/";
                    File originDiffFile = new File(originDiffPath);
                    originDiffFile.mkdirs();

                    HashMap<Integer, Document> oldPDFChapters = load(pdfDownloader.getTemporaryPathOld());
                    HashMap<Integer, Document> newPDFChapters = load(pdfDownloader.getTemporaryPathNew());

                    String tempPath = pdfDownloader.getTemporaryPath() + "/diffChapters/";

                    HashMap<String, HashMap<Integer, Document>> comparedSources =
                            hardCompare((HashMap<Integer, Document>) oldPDFChapters.clone(), (HashMap<Integer,
                                    Document>) newPDFChapters.clone(), tempPath);

                    comparedSources =  normalCompare(comparedSources, tempPath);

                    if (comparedSources.get("old").size() != 0 && comparedSources.get("new").size() != 0) {
                        softCompare(comparedSources);
                    }

                    createDiff(tempPath, originDiffFile.getAbsolutePath() + "/" + pdfDownloader.getNewPDFTitle()
                            + "-DIFF.pdf");

                    deleteFile(pdfDownloader.getTemporaryPath());
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

                case "--help":
                    System.out.println("Place paramaters in correct order <--file name.txt --revision_id id --bulk name " +
                            "--path /path/ --previous_bulk name>, optional parameters [-- wrapper_id id, --download_only, --test_mode]");
                    return false;
            }
        }
        return true;
    }

    public void download(String bulk, String previousBulk, PDFDownloader pdfDownloader) {
        pdfDownloader.saveNewPDF(bulk);
        pdfDownloader.saveOldPDF(previousBulk);
    }

    public HashMap<Integer, Document> load(String tempFolder) {
        PDFLoader pdfLoader = new PDFLoader();
        return (HashMap<Integer, Document>) pdfLoader.loadTemporaryChapters(tempFolder).clone();
    }

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
                PDFCreater pdfCreater = new PDFCreater();
                pdfCreater.writeFile(tempFolder + "same" + i + ".html", newPDFChapters.get(i).html());
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

    public void softCompare(HashMap<String ,HashMap<Integer, Document>> comparedSources) {
        System.out.println("Starting soft compare");

        HashMap<Integer, Document> oldPDFChapters = comparedSources.get("old");
        HashMap<Integer, Document> newPDFChapters = comparedSources.get("old");

        for (int i = 1; i < oldPDFChapters.size(); i++) {
            Document document = Jsoup.parse(oldPDFChapters.get(i).html());
            for (Element div : document.select("div")) {

            }
        }
        System.out.println("Soft compare done");
        System.out.println("Chapters remain to compare: " + oldPDFChapters.size() + " old chapter and " + newPDFChapters.size()
                + " new chapter \n");
    }

    public void createDiff(String tempPath, String originPath) {
        System.out.println("Creating diff from temporary files");
        PDFLoader pdfLoader = new PDFLoader();
        PDFCreater pdfCreater = new PDFCreater();

        HashMap<Integer, Document> diffChapter = pdfLoader.loadTemporaryChapters(tempPath);
        HashMap<Integer, String> diffChapterName = pdfLoader.showDiffChapterNames(tempPath);
        Document document = null;

        for (int i : diffChapter.keySet()) {
            Document doc = diffChapter.get(i);
            String name = diffChapterName.get(i);
            if (name.equals("removed") || name.equals("new")) {
                if (document != null) {
                    document = Jsoup.parse( document.html() + editDiffChapter(doc, name).html());
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
            pdfCreater.createPDF(originPath, Objects.requireNonNull(document).html());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Diff completed");
    }

    private Document editDiffChapter(Document diffChapter, String type) {
        switch (type) {
            case "removed":
                diffChapter.prependElement("<font color='red'>");
                diffChapter.prependElement("<del>");
                break;
            case "new":
                diffChapter.prependElement("<font color='green'>");
                break;
        }
        return diffChapter.clone();
    }


    public HashMap<String, HashMap<Integer, Document>> normalCompare(HashMap<String, HashMap<Integer, Document>>
                                                                             comparedSources, String tempPath) {
        System.out.println("Normal compare");
        PDFCreater pdfCreater = new PDFCreater();
        HashMap<Integer, Document> oldPDFChapters = comparedSources.get("old");
        HashMap<Integer, Document> newPDFChapters = comparedSources.get("new");
        if (oldPDFChapters.size() < newPDFChapters.size()) {
            ArrayList<Integer> keys = (ArrayList<Integer>) new ArrayList(newPDFChapters.keySet());
            for (int i : keys) {
                if(i > oldPDFChapters.size()) {
                    try {
                        pdfCreater.writeFile(tempPath + "new" + i + ".html", newPDFChapters.get(i).html());
                        newPDFChapters.remove(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            ArrayList<Integer> keys = (ArrayList<Integer>) new ArrayList(oldPDFChapters.keySet());
            for (int i : keys) {
                if(i > newPDFChapters.size()) {
                    try {
                        pdfCreater.writeFile(tempPath + "removed" + i + ".html", oldPDFChapters.get(i).html());
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

    public void deleteFile(String tempPath){
        System.out.println("Deleting temp files");
        File tempFiles =  new File(tempPath);
        try {
            FileUtils.deleteDirectory(tempFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
