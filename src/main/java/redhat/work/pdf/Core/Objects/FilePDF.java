package redhat.work.pdf.Core.Objects;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import redhat.work.pdf.Files.PDFCreater;

public class FilePDF{
    public  FilePDF(Document document){
        Element body = document.body();

        PDFCreater pdfCreater = new PDFCreater("/home/Pawlost/RHWork/MyRedHatWork/kokot.pdf");
        try {
            pdfCreater.createPDF(document.outerHtml());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
