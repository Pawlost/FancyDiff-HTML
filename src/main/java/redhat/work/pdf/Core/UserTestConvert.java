package redhat.work.pdf.Core;

import redhat.work.pdf.Files.PDFDownloader;
import java.util.Scanner;

public class UserTestConvert {

    public UserTestConvert(String[] args) {

        Scanner sc = new Scanner(System.in);
        NormalConvert convert = new NormalConvert();

        System.out.println("Running test mode \n");

        do {
            System.out.println("Select one of following commands {\n 1) Continue to normal convert \n" +
                    " 2) Download and create temp folder \n 3) Only load tempFiles}");
            try {
                int decision = sc.nextInt();
                switch (decision) {
                    case 1:
                        new NormalConvert(args);
                        break;
                    case 2:
                        System.out.println("Write name of file with links");
                        String file = sc.next();
                        System.out.println("Write path to files with pdfs");
                        String absolutePath = sc.next();
                        PDFDownloader pdfDownloader = new PDFDownloader(file, absolutePath, "test", "404");
                        System.out.println("Write name of new bulk");
                        String bulk = sc.next();
                        System.out.println("Write name of previous bulk");
                        convert.download(bulk, sc.next(), pdfDownloader);
                        break;
                    case 3:
                        System.out.println("Write folder with temporary files");
                        convert.load(sc.next());
                        break;

                    case 4:
                        System.out.println("Write folder with temporary files");
                        convert.load("lol");
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
        System.out.println("Do you wish to continue testing [y/N] \n");
        return sc.next().equals("y");
    }
}