package mada;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class HuffmanTest {
	private static final String DECODE_TABLE_FILE_NAME = "dec_tab";
	private static final String DECODE_TABLE_FILE_EXTENSION = ".txt";
	private static final String ENCODED_FILE_NAME = "output";
	private static final String ENCODED_FILE_EXTENSION = ".dat";

	@Test
	public void testHuffmanWithOwnFiles1() throws IOException {
		Huffman app = new Huffman();
		FileUtils fileUtils = new FileUtils();
		File inputFile = fileUtils.getFileFromClassPath("/test.txt");
		File decodeTableFile = File.createTempFile(DECODE_TABLE_FILE_NAME, DECODE_TABLE_FILE_EXTENSION);
		File encodedFile = File.createTempFile(ENCODED_FILE_NAME, ENCODED_FILE_EXTENSION);
		app.encode(inputFile, decodeTableFile, encodedFile);

		String decodedContent = app.decode(encodedFile, decodeTableFile);

		Assert.assertEquals("test", decodedContent);
	}

	@Test
	public void testHuffmanWithOwnFiles2() throws IOException {
		Huffman app = new Huffman();
		FileUtils fileUtils = new FileUtils();
		File inputFile = File.createTempFile("fox", ".txt");
		fileUtils.writeToFile(inputFile, "the quick brown fox");
		File decodeTableFile = File.createTempFile("fox-decode-table", ".txt");
		File encodedFile = File.createTempFile("fox", ".dat");
		app.encode(inputFile, decodeTableFile, encodedFile);

		String decodedContent = app.decode(encodedFile, decodeTableFile);
		Assert.assertEquals("the quick brown fox", fileUtils.readContentFromFile(inputFile));
		Assert.assertEquals("the quick brown fox", decodedContent);
	}

	@Test
	public void testDecodeWithMadaFiles() throws IOException {
		Huffman app = new Huffman();
		FileUtils fileUtils = new FileUtils();
		File decodeTableFile = fileUtils.getFileFromClassPath("/dec_tab-mada.txt");
		File encodedFile = fileUtils.getFileFromClassPath("/output-mada.dat");

		String decodedContent = app.decode(encodedFile, decodeTableFile);

		Assert.assertEquals(
				"Das Verfahren lohnt sich in der vorgestellten Form nur fuer Texte mit einer gewissen Laenge. Ansonsten ist der Overhead fuer die Erstellung der Tabelle zu gross.\r\n\r\nMan koennte sich aber auch ueberlegen, die Tabelle geschickter abzuspeichern. Ansonsten: Gut gemacht!",
				decodedContent);
	}
}
