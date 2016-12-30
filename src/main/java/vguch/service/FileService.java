package vguch.service;

import java.io.FileNotFoundException;

public interface FileService {
	void write(String fileName, String text);
	String read(String fileName) throws FileNotFoundException;
	void update(String fileName, String newText) throws FileNotFoundException;
	void delete(String nameFile) throws FileNotFoundException;
	public String findTextToChar(String text, char charBegin, char charEnd);
	public String readUsers(String fileName) throws FileNotFoundException;
}
