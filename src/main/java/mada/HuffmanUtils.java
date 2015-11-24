package mada;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

public class HuffmanUtils {

	private static final int PROBABILITY_TABLE_SIZE = 128;

	/**
	 * returns an array containing the probability of the first 128 ASCII
	 * letters of the given content. example: test -> t: 2, e: 1, s: 1
	 * 
	 * @return a table containing the probability of occurrence of the first 128
	 *         ASCII letters.
	 */
	public static int[] determineProbabilityTable(String content) {
		int[] probabilityTable = new int[PROBABILITY_TABLE_SIZE];
		for (int i = 0; i < probabilityTable.length; i++) {
			probabilityTable[i] = 0;
		}
		for (char c : content.toCharArray()) {
			if (c < PROBABILITY_TABLE_SIZE) {
				probabilityTable[c]++;
			}
		}
		return probabilityTable;
	}

	/**
	 * Creates the frequency tree of all letters in the given {@link int[]}.
	 * 
	 * @param probability
	 *            an array of ints representing the amount of occurrences of the
	 *            according index as character.
	 * @return
	 */
	public static List<Node> createFrequencyTree(int[] probabilityTable) {
		List<Node> nodes = getNodesFromProbabilityTable(probabilityTable);
		createTreeFromNodes(nodes);
		return nodes;
	}

	private static List<Node> getNodesFromProbabilityTable(int[] probabilityTable) {
		List<Node> nodes = new ArrayList<>();
		for (int i = 0; i < probabilityTable.length; i++) {
			int probability = probabilityTable[i];
			if (probability > 0) {
				Node node = new Node(probability, String.valueOf((char) i));
				nodes.add(node);
			}
		}
		return nodes;
	}

	private static void createTreeFromNodes(List<Node> nodes) {
		List<Node> clone = new ArrayList<>(nodes);
		Node parent = null;
		while (clone.size() > 1) {
			Node node1 = getSmallestNode(clone);
			clone.remove(node1);
			Node node2 = getSmallestNode(clone);
			clone.remove(node2);
			parent = new Node(node1, node2);
			clone.add(parent);
		}
	}

	private static Node getSmallestNode(List<Node> nodes) {
		Node smallestNode = nodes.stream().min((n1, n2) -> {
			return Integer.compare(n1.getProbability(), n2.getProbability());
		}).get();
		return smallestNode;
	}

	public static String encode(String content, List<Node> nodes) {
		String code = "";
		for (int i = 0; i < content.length(); i++) {
			char character = content.charAt(i);
			code = code + findNode(String.valueOf(character), nodes).getCode(null);
		}

		return code;
	}

	public static String genEncodingTable(String content, List<Node> nodes) {
		String encodingEntry = "";
		String encodingTableEntry = "";
		int charASCII = 0;
		for (int i = 0; i < content.length(); i++) {
			char character = content.charAt(i);
			encodingEntry = findNode(String.valueOf(character), nodes).getCode(null);
			charASCII = (int) character;
			encodingTableEntry = encodingTableEntry + String.format("%s%s%s%s", charASCII, ":", encodingEntry, "-");
		}
		return encodingTableEntry;
	}

	public static String getBitstreamMultiple8(String content, List<Node> nodes) {
		String bitStream = encode(content, nodes);
		if (bitStream.length() % 8 != 0) {
			bitStream = bitStream + "1";
		}
		while (bitStream.length() % 8 != 0) {
			bitStream = bitStream + "0";
		}
		return bitStream;
	}

	private static Node findNode(String value, List<Node> nodes) {
		return nodes.stream().filter(node -> {
			return node.getValue().equals(value);
		}).findFirst().get();
	}

}
