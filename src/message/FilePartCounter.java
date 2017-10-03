package message;

import java.io.File;
import java.io.Serializable;

public class FilePartCounter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5605774750599712223L;

	protected File file;
	
	protected long start;
	
	protected int size;
	
	protected char c;
	
	public FilePartCounter() {
		
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}

}
