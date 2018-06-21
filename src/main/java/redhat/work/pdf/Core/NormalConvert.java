package redhat.work.pdf.Core;

import redhat.work.pdf.Files.PDFDownloader;
import redhat.work.pdf.Files.PDFLoader;

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
                }
            } else {
            System.out.println("Write all neccesary arguments or type --help to see correct order");
        }
        } catch (NullPointerException e) {
            System.out.println("Write correct parameters or type --help to see arguments");
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
                            "--path /path/ --previous_bulk name>, optional parameters [-- html_id --download_only]");
                    return false;
            }
        }
        return true;
    }

    public void download(String bulk, String previousBulk, PDFDownloader pdfDownloader){
        pdfDownloader.saveNewPDF(bulk);
        pdfDownloader.saveOldPDF(previousBulk);
    }

    public void load() {
        PDFLoader pdfLoader = new PDFLoader();
    }

    public void hardCompare() {
    }

    public void softCompare() {
    }

    public void saveDiffParts(String originPath) {

    }

    public void createDiff(){

    }

}
