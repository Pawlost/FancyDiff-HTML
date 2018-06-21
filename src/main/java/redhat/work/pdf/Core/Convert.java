package redhat.work.pdf.Core;

import redhat.work.pdf.Core.Objects.PDFChapter;
import redhat.work.pdf.Files.PDFDownloader;
import redhat.work.pdf.Files.PDFLoader;

import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.HashMap;

public class Convert {

    public static final String TEMPORARY_PATH = "temp/";
    public static final String DOC_WRAPPER_ID = "div.doc-wrapper";
    public static final String DOC_CHAPTER_ID = "section.chapter";
    private static HashMap<String, String> information = new HashMap<>();

    public static void main(String[] args) {
        try {
            System.out.println("Starting programm");
            if(setArguments(args)){
                String originPath = information.get("path");
                String tempPath = download(originPath);
                load(tempPath);
            }
        } catch (NullPointerException e) {
            System.out.println("Write correct parameters or type --help to see arguments");
            e.printStackTrace();
        }
    }

    private static boolean setArguments(String[] args){

        System.out.println("Checking Arguments");
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

    private static String download(String originPath){
        if (information.get("file") != null && information.get("r_id") != null && information.get("bulk") != null
                && information.get("path") != null && information.get("p_bulk") != null) {

            System.out.println("Starting download");
            PDFDownloader pdfDownloader = new PDFDownloader(information.get("file"), information.get("path"),
                    information.get("bulk"), information.get("r_id"));

            pdfDownloader.saveNewPDF(originPath, information.get("bulk"));
            pdfDownloader.saveOldPDF(originPath, information.get("p_bulk"));

            return pdfDownloader.getAbsolutePath();
        } else {
            System.out.println("Write all neccesary arguments or type --help to see correct order");
            return null;
        }
    }

    private static void load(String tempPath) {
        PDFLoader pdfLoader = new PDFLoader();
    }

    private static void compare(ArrayList<PDFChapter> chaptersToCompare) {
    }

    private static void save(String originPath) {

    }
}
