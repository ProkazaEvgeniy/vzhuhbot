package vguch.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import vguch.service.FileService;

public class FileServiceImpl implements FileService {

	private File file;

	public FileServiceImpl() {}
	
	@Override
	public void write(String fileName, String text) {
		file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			PrintWriter out = new PrintWriter(file.getAbsoluteFile());
			try {
				out.println(text);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String read(String fileName) throws FileNotFoundException {
		file = new File(fileName);
		StringBuilder sb = new StringBuilder();
		isExists(fileName);
		try {
			BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
			try {
				String s;
				while ((s = in.readLine()) != null) {
					sb.append(s);
					sb.append("\n");
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

	@Override
	public void update(String fileName, String newText) throws FileNotFoundException {
		isExists(fileName);
		StringBuilder sb = new StringBuilder();
		String oldFile = read(fileName);
		sb.append(oldFile);
		sb.append(newText);
		write(fileName, sb.toString());
	}

	@Override
	public void delete(String nameFile) throws FileNotFoundException {
		isExists(nameFile);
		new File(nameFile).delete();
	}

	@Override
	public String findTextToChar(String text, char charBegin, char charEnd) {
		int[] indexBegin = new int[text.length()];
		int countBegin = 0;
		int[] indexEnd = new int[text.length()];
		int countEnd = 0;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == charBegin) {
				indexBegin[countBegin++] = i;
			}
			if (ch == charEnd) {
				indexEnd[countEnd++] = i;
			}
		}
		char[] charArrays = text.toCharArray();
		char[] charFinishWithSpace = new char[text.length()];
		for (int i = 0; i < charFinishWithSpace.length; i++) {
			charFinishWithSpace[i] = ' ';
		}
		for (int i = 0; i < countBegin; i++) {
			for (int j = indexBegin[i]; j < indexEnd[i] + 1; j++) {
				charFinishWithSpace[j] = charArrays[j];
			}
		}
		String sb = new String(charFinishWithSpace);
		String finishStr = sb.replaceAll(" ", "");
		return finishStr;
	}

	@Override
	public String readUsers(String fileName) throws FileNotFoundException {
		String readUsers = read(fileName);
		String countUsers = countUsers(readUsers, '{');
		return countUsers;
	}

	public String countUsers(String text, char charBegin) {
		int[] indexBegin = new int[text.length()];
		int countBegin = 0;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == charBegin) {
				indexBegin[countBegin++] = i;
			}
		}
		return countBegin + "";
	}

	private void isExists(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new FileNotFoundException(file.getName());
		}
	}
}
