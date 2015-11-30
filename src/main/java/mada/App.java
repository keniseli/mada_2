package mada;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class App {
	private static final String INPUT_FILE = "/test.txt";
	private static final String DECODE_TABLE_FILE_NAME = "dec_tab";
	private static final String DECODE_TABLE_FILE_EXTENSION = ".txt";
	private static final String ENCODED_FILE_NAME = "output";
	private static final String ENCODED_FILE_EXTENSION = ".dat";
	private static final String DECODED_TEXT_FILE_NAME = "decompress.txt";	
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
	 * then reads given decoding table<br />
	 * then reads encoded content as byte array<br />
	 * and decodes content using decoding table codes
	 */
	private void doTheHuffman() {
		File inputFile = fileUtils.getFileFromClassPath(INPUT_FILE);
		String content = fileUtils.readContentFromFile(inputFile);

		int[] probabilityTable = huffmanUtils.determineProbabilityTable(content);
		List<Node> nodes = huffmanUtils.createFrequencyTree(probabilityTable);
		String encodingTable = huffmanUtils.generateEncodingTable(content, nodes);
		String decodingTableFileName = fileUtils.writeToTemporaryFile(DECODE_TABLE_FILE_NAME,
				DECODE_TABLE_FILE_EXTENSION, encodingTable);
		notify("decoding table: " + decodingTableFileName);

		String encodedContent = huffmanUtils.encode(content, nodes);
		byte[] bytes = toByteArray(encodedContent);
		String outputFileName = fileUtils.writeByteArrayToTemporaryFile(bytes, ENCODED_FILE_NAME,
				ENCODED_FILE_EXTENSION);
		notify("encoded content: " + outputFileName);

		byte[] byteArrayFromFile = fileUtils.readByteArrayFromFile("output-mada.dat");
		String bitStringFromArray = stringFromByteArray(byteArrayFromFile);
		String encodedBitText = cleanStringFromBits(bitStringFromArray);

		File decTab = fileUtils.getFileFromClassPath("/dec_tab-mada.txt");

		String decTabContent = fileUtils.readContentFromFile(decTab);
		HashMap<String, Integer> encTableMap = huffmanUtils.getHashMapFromDecTab(decTabContent);

		String decodedContent = huffmanUtils.decode(encodedBitText, encTableMap);
		notify(decodedContent);
		
		File decompressFile = new File(DECODED_TEXT_FILE_NAME);
		fileUtils.writeToFile(decompressFile, decodedContent);
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

	private String stringFromByteArray(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
		for (Byte b : bytes) {
			String stringFromByte = convertByteToString(b);
			sb.append(stringFromByte);
		}
		String bites = sb.toString();
		return bites;
	}

	private String convertByteToString(Byte b) {
		int x = b;
		if (x < 0)
			x = x + 256;
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < Byte.SIZE; i++) {
			if ((x % 2) == 1)
				s.insert(0, '1');
			else
				s.insert(0, '0');
			x = x / 2;
		}
		return s.toString();
	}

	private String cleanStringFromBits(String bitText) {
		int index = bitText.lastIndexOf("1");
		return bitText.substring(0, index);
	}

}
