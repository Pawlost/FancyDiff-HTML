package redhat.work.pdf.Files;

import java.io.*;
import java.util.ArrayList;

public class PDFLoader {
	private String fileName;

	public PDFLoader(String fileName) {
		this.fileName = fileName;
	}
    public PDFLoader(String fileName, String absolutePath) {
        this.fileName = absolutePath + fileName;
    }

	public ArrayList<String> loadFileString() throws IOException {
		ArrayList<String> loadedText = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String s;
		while ((s = br.readLine()) != null) {
			loadedText.add(s);
		}
		return (ArrayList<String>) loadedText.clone();
	}
}
