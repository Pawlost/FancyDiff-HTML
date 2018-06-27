package pawlost.work.pdf.Files;

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


import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.commons.io.FileUtils;
import java.io.*;

public class PDFCreater {

    public void writeFile(String filePath, String text) throws IOException {
        File file = new File(filePath);
        FileUtils.writeStringToFile(file, text + "\n", "UTF-8");
    }

    public void createPDF(String filePath, String html) throws IOException {
        OutputStream file = new FileOutputStream(new File(filePath));
        HtmlConverter.convertToPdf(html, file);
        file.close();
    }
}