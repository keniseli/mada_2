package mada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HuffmanUtils {

	private static final int PROBABILITY_TABLE_SIZE = 128;

	/**
	 * returns an array containing the probability of the first 128 ASCII
	 * letters of the given content. example: test -> t: 2, e: 1, s: 1
	 * 
	 * @return a table containing the probability of occurrence of the first 128
	 *         ASCII letters.
	 */
	public int[] determineProbabilityTable(String content) {
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
	public List<Node> createFrequencyTree(int[] probabilityTable) {
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

	private void createTreeFromNodes(List<Node> nodes) {
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

	private Node getSmallestNode(List<Node> nodes) {
		Node smallestNode = nodes.stream().min((n1, n2) -> {
			return Integer.compare(n1.getProbability(), n2.getProbability());
		}).get();
		return smallestNode;
	}

	public String generateEncodingTable(String content, List<Node> nodes) {
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

	public String encode(String content, List<Node> nodes) {
		String bitStream = encodeSimple(content, nodes);
		if (bitStream.length() % 8 != 0) {
			bitStream = bitStream + "1";
		}
		while (bitStream.length() % 8 != 0) {
			bitStream = bitStream + "0";
		}
		return bitStream;
	}

	public String encodeSimple(String content, List<Node> nodes) {
		String code = "";
		for (int i = 0; i < content.length(); i++) {
			char character = content.charAt(i);
			code = code + findNode(String.valueOf(character), nodes).getCode(null);
		}
		return code;
	}

	private Node findNode(String value, List<Node> nodes) {
		return nodes.stream().filter(node -> {
			return node.getValue().equals(value);
		}).findFirst().get();
	}

	public HashMap<String, Integer> getHashMapFromDecTab(String decTabContent) {
		String[] tab = decTabContent.split("-");
		HashMap<String,Integer> decMap = new HashMap<>();
		
		for(int i = 0; i < tab.length; i++) {
			String[] tmp = tab[i].split(":");
			decMap.put(tmp[1], Integer.valueOf(tmp[0]));
		}
		
		return decMap;
	}

	public String decode(String encodedBitText, HashMap<String, Integer> encTableMap) {
		String encodedText = "";
		int signStart = 0;
		int signLength = 1;
		while(signLength <= encodedBitText.length()) {
			String tmpSign = encodedBitText.substring(signStart, signLength);
			if (encTableMap.containsKey(tmpSign)) {
				int x = encTableMap.get(tmpSign);
				char c = (char) x;
				encodedText += String.valueOf(c);
				signStart = signLength;
				signLength = signStart + 1;
			} else {
				signLength++;
			}
		}
		
		return encodedText;
	}

}
