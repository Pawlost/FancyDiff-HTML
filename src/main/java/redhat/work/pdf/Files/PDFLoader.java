package redhat.work.pdf.Files;

import java.io.*;
import java.util.ArrayList;

public class PDFLoader {

	public ArrayList<String> loadFile(String fileName, String absolutePath) throws IOException {
		ArrayList<String> loadedText = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String s;
		while ((s = br.readLine()) != null) {
			loadedText.add(s);
		}
		return (ArrayList<String>) loadedText.clone();
	}
}
