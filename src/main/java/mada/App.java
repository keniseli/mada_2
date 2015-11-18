package mada;

import java.io.File;

public class App {

	private static final String INPUT_FILE = "/test.txt";

	public static void main(String[] args) {
		FileUtils fileUtils = new FileUtils();
		File inputFile = fileUtils.getFileFromClassPath(INPUT_FILE);
		String content = fileUtils.readContentFromFile(inputFile);
		
		int[] probability = HuffmanUtils.determineProbabilityTable(content);
		Node frequencyTree = HuffmanUtils.createFrequencyTree(probability);
	}

}
