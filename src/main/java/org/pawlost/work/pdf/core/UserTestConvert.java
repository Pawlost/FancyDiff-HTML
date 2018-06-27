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

import org.jsoup.nodes.Document;
import org.pawlost.work.pdf.files.PDFDownloader;

import java.util.HashMap;
import java.util.Scanner;

public class UserTestConvert {

    public UserTestConvert(String[] args) {

        Scanner sc = new Scanner(System.in);
        NormalConvert convert = new NormalConvert();

        HashMap<Integer, Document> oldPDFChapters = null;
        HashMap<Integer, Document> newPDFChapters = null;
        String tempPath = null;
        PDFDownloader pdfDownloader = null;

        System.out.println("Running test mode \n");

        do {
            System.out.println("Select one of following commands {\n 1) Continue to normal convert \n" +
                    " 2) Download and create temp folder \n 3) Only load temp files \n 4) Only Hard compare " +
                    "\n 5) Only Normal compare \n 6) Only Soft compare \n 7) Only Create diff \n 8) Delete temp folder \n " +
                    "9) Load file\n 10) Save divaded file");

            try {
                int decision = sc.nextInt();
                switch (decision) {

                    case 1:
                        System.out.println("Normal convert");
                        new NormalConvert(args);
                        break;

                    case 2:
                        System.out.println("Download and create temp folder");
                        System.out.println("Write name of file with links");
                        String file = sc.next();
                        System.out.println("Write path to files with pdfs");
                        pdfDownloader = new PDFDownloader(file, sc.next(), "test", "404");

                        System.out.println("Write name of new bulk");
                        String bulk = sc.next();
                        System.out.println("Write name of previous bulk");
                        convert.download(bulk, sc.next(), pdfDownloader);
                        break;

                    case 3:
                        System.out.println("Load temp files");
                        if (pdfDownloader != null) {
                            oldPDFChapters = convert.load(pdfDownloader.getTemporaryPathOld());
                            newPDFChapters = convert.load(pdfDownloader.getTemporaryPathNew());
                        }else {
                            System.out.println("Write path to temp files");
                            tempPath = sc.next();
                            oldPDFChapters = convert.load(tempPath+"oldChapters/");
                            newPDFChapters = convert.load(tempPath+"newChapters/");
                        }
                        break;

                    case 4:
                        System.out.println("Only Hard compare");
                        if (oldPDFChapters != null && newPDFChapters != null) {
                            convert.hardCompare(newPDFChapters, oldPDFChapters, tempPath);
                        }else{
                            System.out.println("You need to load");
                        }
                        break;

                    case 5:
                        System.out.println("Only Normal compare");
                        System.out.println("Write temporary files with diff chapters");
                        if (oldPDFChapters != null && newPDFChapters != null) {
                            HashMap<String, HashMap<Integer, Document>> chapters = new HashMap<>();
                            chapters.put("old", oldPDFChapters);
                            chapters.put("new", newPDFChapters);
                            convert.normalCompare(chapters, tempPath);
                        }else{
                            System.out.println("You need to load");
                        }
                        break;

                    case 6:
                        System.out.println("Only Soft compare");
                        System.out.println("Write temporary files with diff chapters");
                        if (oldPDFChapters != null && newPDFChapters != null) {
                            HashMap<String, HashMap<Integer, Document>> chapters = new HashMap<>();
                            chapters.put("old", oldPDFChapters);
                            chapters.put("new", newPDFChapters);
                            convert.softCompare(chapters, tempPath);
                        }else{
                            System.out.println("You need to load");
                        }
                        break;

                    case 7:
                        System.out.println("Only Create diff");
                        if(tempPath == null) {
                            System.out.println("Write path with diff chapters");
                            tempPath = sc.next();
                        }
                        System.out.println("Write folder to save diff pdf");
                        convert.createDiff(tempPath, sc.next());
                        break;

                    case 8:
                        System.out.println("Delete temp folder");
                        if(tempPath == null) {
                            System.out.println("Write path to temp folder");
                            tempPath = sc.next();
                        }
                        convert.deleteFile(tempPath);
                        break;
                }

            } catch (Exception e) {
                System.out.println("Write number \n");
                e.printStackTrace();
            }
        } while (wish(sc));

        System.out.println("Ending test \n");
    }

    private boolean wish(Scanner sc) {
        System.out.println("Do you wish to continue testing? [y/N]:");
        return sc.next().equals("y");
    }
}