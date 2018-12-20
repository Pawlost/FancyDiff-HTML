package org.pawlost.work.html.compare;

import org.jsoup.nodes.Document;
import org.pawlost.work.html.IO.Connector;

import java.io.File;
import java.util.HashMap;

//hard compare, compares whole pages
public class HardCompare implements Compare{
    public HardCompare() {

    }

    @Override
    public void start() {
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
                    Connector.createFile(tempFolder + "same" + i + ".html", newPDFChapters.get(i).html());
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
    }
}
