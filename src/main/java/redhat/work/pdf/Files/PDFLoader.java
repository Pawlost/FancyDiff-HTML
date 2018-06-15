package redhat.work.pdf.Files;

import java.io.*;

import redhat.work.pdf.Core.Objects.FilePDF;

public class PDFLoader {

	private FilePDF[] pdfs;
	private String oldFilePath;
	private String newFilePath;

	public PDFLoader(String oldFileName, String newFileName) throws IOException {
		oldFilePath = "/PDFFiles/" + oldFileName + ".pdf";
		newFilePath = "/PDFFiles/" + newFileName + ".pdf";
	}

	public String loadFileString() {
		System.out.println("Vypisuji celý soubor:");
		String loadedText = "";
		try (BufferedReader br = new BufferedReader(new FileReader(oldFilePath))) {
			String s;
			while ((s = br.readLine()) != null) {
				loadedText = loadedText + s + "\n";
				System.out.println(s);
			}
		} catch (Exception e) {

			System.err.println("Chyba při četení ze souboru.");
		}
		return loadedText;
	}
}
