package mada;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Does the reading/writing from and to files.
 *
 */
public class FileUtils {

	public List<String> readLinesFromFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			file = getFileFromClassPath(fileName);
		}
		List<String> lines = readFromFile(file);
		return lines;
	}

	public File getFileFromClassPath(String fileName) {
		return new File(getClass().getResource(fileName).getFile());
	}

	public List<String> readFromFile(File file) {
		if (file == null) {
			throw new RuntimeException("A file must be provided but was [null].");
		}
		try {
			List<String> lines = Files.lines(file.toPath()).collect(Collectors.toList());
			return lines;
		} catch (IOException e) {
			e.printStackTrace();
			String message = String.format("Could not read from [%s]", file.getName());
			throw new RuntimeException(message);
		}
	}

	public String readContentFromFile(File file) {
		String absolutePath = file.getAbsolutePath();
		List<String> readLinesFromFile = readLinesFromFile(absolutePath);
		String content = "";
		for (String line : readLinesFromFile) {
			content = String.format("%s%s", content, line);
		}
		return content;
	}

	public void writeToFile(File file, String text) {
		Path path = file.toPath();
		OpenOption[] options = new OpenOption[0];
		List<String> lines = new ArrayList<>();
		lines.add(text);
		try {
			Files.write(path, lines, options);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void writeToFile(byte[] bytes, File outputFile) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			fileOutputStream.write(bytes);
			fileOutputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String writeToTemporaryFile(String fileName, String fileExtension, String content) {
		try {
			File tempFile = File.createTempFile(fileName, fileExtension);
			writeToFile(tempFile, content);
			return tempFile.getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] readByteArrayFromFile(File file) {
		byte[] bytes = new byte[(int) file.length()];
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bytes;
	}

}
