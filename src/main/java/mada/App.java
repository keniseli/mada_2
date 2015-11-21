package mada;

import java.io.File;
import java.util.List;

public class App {

	private static final String INPUT_FILE = "/test.txt";
	private static final String DEC_TAB = "/dec_tab.txt";

	public static void main(String[] args) {
		FileUtils fileUtils = new FileUtils();
		File inputFile = fileUtils.getFileFromClassPath(INPUT_FILE);
		String content = fileUtils.readContentFromFile(inputFile);
		
		int[] probabilityTable = HuffmanUtils.determineProbabilityTable(content);
		List<Node> nodes = HuffmanUtils.createFrequencyTree(probabilityTable);
		HuffmanUtils.encode(content, nodes);
		FileUtils.writeToFile(new File(DEC_TAB), HuffmanUtils.genEncodingTable(content, nodes));
	}

}
