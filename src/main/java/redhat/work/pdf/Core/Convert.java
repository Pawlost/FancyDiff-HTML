package redhat.work.pdf.Core;

import java.util.Scanner;

public class Convert {
	public static void main(String[] args) {
		try {

			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("--file")) {
					System.out.println("lol " + args[i]);
					System.out.println("lol " + args[i + 1]);
				}
			}
		} catch (Exception e) {
			System.out.println("Write correct arguments");
		}
	}
}
