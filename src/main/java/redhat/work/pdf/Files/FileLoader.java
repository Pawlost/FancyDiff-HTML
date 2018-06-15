package redhat.work.pdf.Files;

import java.io.File;
import java.io.IOException;

import redhat.work.pdf.Core.Objects.FilePDF;

public class FileLoader{
	
	private FilePDF pdf;
	
	public FileLoader(String path) throws Exception  {
		File myFile = new File(path);
	}
	
	public FilePDF getText() {
		return new FilePDF();
	}
}
