package mada;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

public class Huffman {
	private HuffmanUtils huffmanUtils;
	private FileUtils fileUtils;

	public Huffman() {
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
	 * at the end compares the file sizes of the input file and the encoded
	 * file.
	 */
	public void encode(File inputFile, File decodeTableFile, File encodedFile) {
		String content = fileUtils.readContentFromFile(inputFile);
		notify("plain content to be encoded: " + content);

		int[] probabilityTable = huffmanUtils.determineProbabilityTable(content);
		List<Node> nodes = huffmanUtils.createFrequencyTree(probabilityTable);
		String encodingTable = huffmanUtils.generateEncodingTable(content, nodes);
		fileUtils.writeToFile(decodeTableFile, encodingTable);
		notify("decoding table: " + decodeTableFile.getAbsolutePath());

		String encodedContent = huffmanUtils.encode(content, nodes);
		byte[] bytes = toByteArray(encodedContent);
		fileUtils.writeToFile(bytes, encodedFile);
		notify("encoded content: " + encodedFile.getAbsolutePath());
		compareFileSizes(inputFile, encodedFile);
	}

	private byte[] toByteArray(String text) {
		byte[] bigIntegerByteArray = new BigInteger(text, 2).toByteArray();
		if (bigIntegerByteArray.length != 0 && bigIntegerByteArray[0] == 0) {
			byte[] bytes = new byte[bigIntegerByteArray.length - 1];
			for (int i = 1; i < bigIntegerByteArray.length; i++) {
				bytes[i - 1] = bigIntegerByteArray[i];
			}
			return bytes;
		} else {
			return bigIntegerByteArray;
		}
	}

	private void compareFileSizes(File inputFile, File encodedFile) {
		long inputFileSize = inputFile.length();
		long encodedFileSize = encodedFile.length();
		long sizeDiff = inputFileSize - encodedFileSize;
		String message = String.format("encoding saved [%s] bytes.", sizeDiff);
		notify(message);
	}

	/**
	 * Reads given decoding table<br />
	 * then reads encoded content as byte array<br />
	 * and decodes content using decoding table codes
	 */
	public String decode(File encodedFile, File decodeTableFile) {
		byte[] byteArrayFromFile = fileUtils.readByteArrayFromFile(encodedFile);
		String bitStringFromArray = stringFromByteArray(byteArrayFromFile);
		String encodedBitText = cleanStringFromBits(bitStringFromArray);

		String decTabContent = fileUtils.readContentFromFile(decodeTableFile);
		HashMap<String, Integer> encTableMap = huffmanUtils.getHashMapFromDecTab(decTabContent);

		String decodedContent = huffmanUtils.decode(encodedBitText, encTableMap);
		notify("decoded content: " + decodedContent);
		return decodedContent;
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

	private void notify(String message) {
		System.out.println(message);
	}
}
