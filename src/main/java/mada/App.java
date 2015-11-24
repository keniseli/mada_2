package mada;

import java.io.File;
import java.util.List;

public class App {
	private static final String INPUT_FILE = "/test.txt";
	private static final String DECODE_TABLE_FILE_NAME = "dec_tab";
	private static final String DECODE_TABLE_FILE_EXTENSION = ".txt";
	private static final String ENCODED_FILE_NAME = "output";
	private static final String ENCODED_FILE_EXTENSION = ".dat";
	private static final int BITS_PER_BYTE = 8;

	private HuffmanUtils huffmanUtils;
	private FileUtils fileUtils;

	public static void main(String[] args) {
		App app = new App();
		app.doTheHuffman();
	}

	public App() {
		huffmanUtils = new HuffmanUtils();
		fileUtils = new FileUtils();
	}

	/**
	 * Reads the input file,<br />
	 * <br />
	 * 
	 * then determines the probability of each symbol to occur<br />
	 * and determines the tree and therefore the codes <br />
	 * and writes the codes to the temp file with the name
	 * {@link #DECODE_TABLE_FILE_NAME},<br />
	 * <br />
	 * 
	 * then encodes the read content <br />
	 * and writes the encoded content into a {@link byte byte[]} <br />
	 * and writes the {@link byte byte[]} to the temp file with the name
	 * {@link #ENCODED_FILE_NAME}, <br />
	 * <br />
	 * 
	 */
	private void doTheHuffman() {
		File inputFile = fileUtils.getFileFromClassPath(INPUT_FILE);
		String content = fileUtils.readContentFromFile(inputFile);

		int[] probabilityTable = huffmanUtils.determineProbabilityTable(content);
		List<Node> nodes = huffmanUtils.createFrequencyTree(probabilityTable);
		String encodingTable = huffmanUtils.generateEncodingTable(content, nodes);
		String decodingTableFileName = fileUtils.writeToTemporaryFile(DECODE_TABLE_FILE_NAME,
				DECODE_TABLE_FILE_EXTENSION, encodingTable);
		String decodingTableFileWrittenMessage = String.format("wrote decode table to file [%s].",
				decodingTableFileName);
		notify(decodingTableFileWrittenMessage);

		String encodedContent = huffmanUtils.encode(content, nodes);
		byte[] bytes = toByteArray(encodedContent);
		String outputFileName = fileUtils.writeByteArrayToTemporaryFile(bytes, ENCODED_FILE_NAME,
				ENCODED_FILE_EXTENSION);
		String encodedFileWrittenMessage = String.format("wrote encoded output to file [%s].", outputFileName);
		notify(encodedFileWrittenMessage);
	}

	private void notify(String message) {
		System.out.println(message);
	}

	private byte[] toByteArray(String text) {
		int arrayLength = text.length() / BITS_PER_BYTE;
		byte[] bytes = new byte[arrayLength];
		for (int i = 0; i < arrayLength; i += BITS_PER_BYTE) {
			byte b = Byte.parseByte(text, 2);
			bytes[i / BITS_PER_BYTE] = b;
		}
		return bytes;
	}

}
