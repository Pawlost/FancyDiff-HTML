package redhat.work.pdf.Core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import redhat.work.pdf.Files.PDFCreater;
import redhat.work.pdf.Files.PDFDownloader;
import redhat.work.pdf.Files.PDFLoader;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class NormalConvert {

    public static final String TEMPORARY_PATH = "temp/";
    public static final String DOC_WRAPPER_ID = "div.doc-wrapper";
    public static final String DOC_CHAPTER_ID = "section.chapter";
    private HashMap<String, String> information = new HashMap<>();

    public NormalConvert() {}

    public NormalConvert(String[] args) {
        try {
            System.out.println("\nStarting program \n");
            if(checkArguments(args)){

                if (information.get("file") != null && information.get("r_id") != null && information.get("bulk") != null
                        && information.get("path") != null && information.get("p_bulk") != null) {

                    PDFDownloader pdfDownloader = new PDFDownloader(information.get("file"), information.get("path"),
                            information.get("bulk"), information.get("r_id"));

                    download(information.get("bulk"), information.get("p_bulk"), pdfDownloader);

                    HashMap<Integer, Document> oldPDFChapters = load(pdfDownloader.getTemporaryPathOld());
                    HashMap<Integer, Document> newPDFChapters = load(pdfDownloader.getTemporaryPathNew());

                    HashMap<String , HashMap<Integer, Document>>  comparedSources =
                            hardCompare((HashMap<Integer, Document>) oldPDFChapters.clone(), (HashMap<Integer,
                            Document>)newPDFChapters.clone(),  pdfDownloader.getTemporaryPath()+"/diffChapters/");

                    if (checkChapters(comparedSources)){
                            softCompare(comparedSources.get("old"), comparedSources.get("new"));
                    }

                    createDiff(pdfDownloader.getTemporaryPath()+"/diffChapters/");
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

    public boolean checkArguments(String[] args){
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

    public void download(String bulk, String previousBulk, PDFDownloader pdfDownloader){
        pdfDownloader.saveNewPDF(bulk);
        pdfDownloader.saveOldPDF(previousBulk);
    }

    public HashMap<Integer, Document> load(String tempFolder) {
        PDFLoader pdfLoader = new PDFLoader();
        return (HashMap<Integer, Document>) pdfLoader.loadTemporaryPDFS(tempFolder).clone();
    }

    public HashMap<String, HashMap<Integer, Document>> hardCompare(HashMap<Integer, Document> oldPDFChapters,
                                                             HashMap<Integer, Document> newPDFChapters,
                             String tempFolder) throws IOException {
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
                pdfCreater.writeFile(tempFolder + "chapter" + i + ".html", newPDFChapters.get(i).html());
                oldPDFChapters.remove(i);
                newPDFChapters.remove(i);
            }
        }
        HashMap<String, HashMap<Integer, Document>> returnHashMap = new HashMap<>();

        returnHashMap.put("old", oldPDFChapters);
        returnHashMap.put("new", newPDFChapters);

        System.out.println("Hard compare done");
        System.out.println("Chapters remain to compare: " + oldPDFChapters.size() + " old and " + newPDFChapters.size()
                + " new \n");
        return (HashMap<String, HashMap<Integer, Document>>) returnHashMap.clone();
    }

    public void softCompare( HashMap<Integer, Document> oldPDFChapters,  HashMap<Integer, Document> newPDFChapters) {
        System.out.print(oldPDFChapters.get(1).text());

        for(int i=1; i<oldPDFChapters.size(); i++){
            Document document = Jsoup.parse(oldPDFChapters.get(i).html());
            for (Element div:document.select("div")) {

            }
        }
    }

    public void createDiff(String tempPath){
        System.out.println("Creating diff from temporary files");
    }


    public boolean checkChapters(HashMap<String , HashMap<Integer, Document>>  comparedSources){
        System.out.println("Checking chapters");
        return false;
    }
}
