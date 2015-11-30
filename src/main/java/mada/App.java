package mada;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		notify("encodedContent: " + encodedContent);
		byte[] bytes = toByteArray(encodedContent);
		String outputFileName = fileUtils.writeByteArrayToTemporaryFile(bytes, ENCODED_FILE_NAME,
				ENCODED_FILE_EXTENSION);
		String encodedFileWrittenMessage = String.format("wrote encoded output to file [%s].", outputFileName);
		notify(encodedFileWrittenMessage);
		
		// part 9 by PL
		// content to decode from byte-array
		
		// TEST FILE OF VOGT
		byte[] byteArrayFromFile = fileUtils.readByteArrayFromFile("output-mada.dat");
		
		//byte[] byteArrayFromFile = fileUtils.readByteArrayFromFile("output.dat");
		String bitStringFromArray = stringFromByteArray(byteArrayFromFile);
		String encodedBitText = cleanStringFromBits(bitStringFromArray);
		
		// get decode table from file and save in hashMap
		
		// TEST FILE OF VOGT
		File decTab = new File("/dec_tab-mada.txt");
		
		// File decTab = new File(DECODE_TABLE_FILE_NAME +  DECODE_TABLE_FILE_EXTENSION);
		String decTabContent = fileUtils.readContentFromFile(decTab);
		HashMap<String, Integer> encTableMap = huffmanUtils.getHashMapFromDecTab(decTabContent);
		
		// decode text with hashmap
		String decodedContent = huffmanUtils.decode(encodedBitText, encTableMap);
		notify(decodedContent);
		
		File decompressFile = new File(DECODED_TEXT_FILE_NAME);
		fileUtils.writeToFile(decompressFile, decodedContent);
		
		notify("END");
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
	    for( int i = 0; i < Byte.SIZE * bytes.length; i++ ) {
	    	sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
	    }
	    
	    String bites = sb.toString();
	    
	    return bites;
	}
	
	private String cleanStringFromBits(String bitText) {
		int index = bitText.lastIndexOf("1");
		return bitText.substring(0, index);
	}

}
