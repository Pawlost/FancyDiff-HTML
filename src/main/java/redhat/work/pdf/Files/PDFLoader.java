package redhat.work.pdf.Files;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFLoader {

    public ArrayList<String> loadFile(String fileName) throws IOException {
        ArrayList<String> loadedText = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String s;
        while ((s = br.readLine()) != null) {
            loadedText.add(s);
        }
        return (ArrayList<String>) loadedText.clone();
    }

    public Document loadOriginalPDF(String folderPath) {
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
            System.out.println("HTML file does not exist or there are no files inside");
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Integer, Document> loadTemporaryPDFS(String folderPath) {
        HashMap<Integer, Document> clearPdfs = new HashMap<>();
        ArrayList<File> rawPdfs = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        System.out.println("Loading file from location: "+folderPath);
        try {
            File folder = new File(folderPath);
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file != null && !file.isDirectory()) {
                    rawPdfs.add(file);
                }
            }

            for(File file: rawPdfs) {
                try {
                    for (String s : loadFile(file.getPath())) {
                        builder.append(s);
                    }
                    Matcher m = Pattern.compile("[0-9]+").matcher(file.getName());
                    if( m.find()) {
                        int pageNumber = Integer.parseInt(m.group());
                        Document document = Jsoup.parse(builder.toString());
                        clearPdfs.put(pageNumber, document);
                        builder.setLength(0);
                    }
                } catch (IOException e) {
                    System.out.println("PDF file didnt load");
                    e.printStackTrace();
                }
            }
        }catch (NullPointerException e){
            System.out.println("HTML file does not exist or there are no files inside");
            e.printStackTrace();
        }
        System.out.println("Files pushed to compare\n");
        return (HashMap<Integer, Document>) clearPdfs.clone();
    }
}