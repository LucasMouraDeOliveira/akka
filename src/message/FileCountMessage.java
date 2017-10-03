package message;

import java.io.File;
import java.io.Serializable;

public class FileCountMessage implements Serializable {

	private static final long serialVersionUID = 5283119891665615583L;

	protected File file;
	
	protected char c;
	
	public FileCountMessage() {
		
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}
	
}
