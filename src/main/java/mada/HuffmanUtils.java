package mada;

import java.util.ArrayList;
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
	public static int[] determineProbabilityTable(String content) {
		int[] probabilityTable = new int[PROBABILITY_TABLE_SIZE];
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
	public static Node createFrequencyTree(int[] probabilities) {
		List<Node> nodes = getNodesFromProbabilityTable(probabilities);
		Node root = createTreeFromNodes(nodes);
		return root;
	}


	private static List<Node> getNodesFromProbabilityTable(int[] probabilities) {
		List<Node> nodes = new ArrayList<>();
		for (int i = 0; i < probabilities.length; i++) {
			int probability = probabilities[i];
			if (probability > 0) {
				Node node = new Node(probability, String.valueOf((char) i));
				nodes.add(node);
			}
		}
		return nodes;
	}
	private static Node createTreeFromNodes(List<Node> nodes) {
		Node parent = null;
		while (nodes.size() > 1) {
			Node node1 = getSmallestNode(nodes);
			nodes.remove(node1);
			Node node2 = getSmallestNode(nodes);
			nodes.remove(node2);
			parent = new Node(node1, node2);
			nodes.add(parent);
		}
		return parent;
	}

	private static Node getSmallestNode(List<Node> nodes) {
		Node smallestNode = nodes.stream().min((n1, n2) -> {
			return Integer.compare(n1.getProbability(), n2.getProbability());
		}).get();
		return smallestNode;
	}

}
