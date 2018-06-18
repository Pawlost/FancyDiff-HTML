package redhat.work.pdf.Core;

import redhat.work.pdf.Files.PDFDownloader;
import java.util.HashMap;

public class Convert {
	private static HashMap<String, String> information = new HashMap<>();
	private static boolean isHelp= false;

	public static void main(String[] args) {
		try {
			for (int i = 0; i < args.length; i++) {
				switch (args[i]) {
					case "--file":
						information.put("file", args[i + 1]);
						break;
					case "--revision_id":
						information.put("r_id", args[i + 1]);
						break;
					case "--bulk":
						information.put("bulk", args[i + 1]);
						break;

					case "--path":
						information.put("path", args[i + 1]);
						break;

					case "--help":
						System.out.println("Place paramaters in correct order <--file paramater --revision_id parameter --bulk parameter>");
						isHelp = true;
						break;
				}
			}
			if(!isHelp && information.get("file") != null && information.get("r_id") != null && information.get("bulk") != null){
                    PDFDownloader pdfDownloader = new PDFDownloader(information.get("file"), information.get("path"));

			} else{
				System.out.println("Write all neccesary arguments or type help to see correct order");
			}
		} catch (NullPointerException e) {
			System.out.println("Write correct parameters, obligatory parameters are <--file --revision_id --bulk> ");
		}
		isHelp = false;
	}
}
