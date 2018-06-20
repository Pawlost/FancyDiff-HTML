package redhat.work.pdf.Core;

import redhat.work.pdf.Core.Objects.FilePDF;
import redhat.work.pdf.Files.PDFDownloader;
import redhat.work.pdf.Files.PDFLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class Convert {

    public static final String TEMPORARY_PATH = "./temp/";
    public static final String DOC_WRAPPER_ID = "div.doc-wrapper";
    public static final String DOC_CHAPTER_ID = "section.chapter";
    private static HashMap<String, String> information = new HashMap<>();

    public static void main(String[] args) {
        try {
            if(setArguments(args)){
                String tempPath = download();
                load(tempPath);
            }
        } catch (NullPointerException e) {
            System.out.println("Write correct parameters or type --help to see arguments");
        }
    }

    private static boolean setArguments(String[] args){
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
                    information.put("pbulk", args[i + 1]);
                    break;

                case "--help":
                    System.out.println("Place paramaters in correct order <--file name.txt --revision_id id --bulk name " +
                            "--path /path/ --previous_bulk name>, optional parameters []");
                    return false;
            }
        }
        return true;
    }

    private static String download(){
        if (information.get("file") != null && information.get("r_id") != null && information.get("bulk") != null
                && information.get("path") != null && information.get("pbulk") != null) {
            PDFDownloader pdfDownloader = new PDFDownloader(information.get("file"), information.get("path"),
                    information.get("bulk"), information.get("r_id"));
            return pdfDownloader.savePDF();
        } else {
            System.out.println("Write all neccesary arguments or type help to see correct order");
            return null;
        }
    }

    private static ArrayList<FilePDF> load(String path) {
        PDFLoader pdfLoader = new PDFLoader();
        return new ArrayList<>();
    }

    private static void compare() {

    }

    private static void save() {

    }
}
