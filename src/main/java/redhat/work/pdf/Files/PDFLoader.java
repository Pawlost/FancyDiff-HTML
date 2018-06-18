package redhat.work.pdf.Files;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import redhat.work.pdf.Core.Objects.FilePDF;

public class PDFLoader {

	private String fileName;

	public PDFLoader(String fileName, String suffix) {
		this.fileName = fileName + "." + suffix;
	}

	public ArrayList<String> loadFileString() throws IOException {
		ArrayList<String> loadedText = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String s = br.readLine();
		while (s != null) {
			loadedText.add(s);
            s = br.readLine();
		}
		return (ArrayList<String>) loadedText.clone();
	}
}
