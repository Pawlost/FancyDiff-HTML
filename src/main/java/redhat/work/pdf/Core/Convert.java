package redhat.work.pdf.Core;

import redhat.work.pdf.Files.PDFCreater;
import redhat.work.pdf.Files.PDFDownloader;
import redhat.work.pdf.Files.PDFLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Convert {
	private static HashMap<Integer, String> information = new HashMap<>();
	private static boolean isHelp= false;

	public static void main(String[] args) {
		try {
			for (int i = 0; i < args.length; i++) {
				switch (args[i]) {
					case "file:":
						information.put(1, args[i + 1]);
						break;
					case "revision_id:":
						information.put(2, args[i + 1]);
						break;
					case "bulk:":
						information.put(3, args[i + 1]);
						break;
					case "help":
						System.out.println("Place paramaters in correct order <file: paramater revision_id: parameter bulk: parameter>");
						isHelp = true;
						break;
				}
			}
			if(!isHelp && information.get(1) != null && information.get(2) != null && information.get(3) != null){
				PDFDownloader pdfDownloader = new PDFDownloader(information.get(1));

			} else{
				System.out.println("Write all neccesary arguments or type help to see correct order");
			}
		} catch (NullPointerException e) {
			System.out.println("Write correct parameters, obligatory parameters are <file: revision_id: bulk:>");
		}
		isHelp = false;
	}
}
