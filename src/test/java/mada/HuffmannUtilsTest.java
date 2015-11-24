package mada;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HuffmannUtilsTest {

	private HuffmanUtils huffmanUtils;
	
	@Before
	public void setUp() {
		huffmanUtils = new HuffmanUtils();
	}
	
	@Test
	public void testDetermineProbabilityTable() {
		int[] probability = huffmanUtils.determineProbabilityTable("test");
		Assert.assertEquals(2, probability['t']);
		Assert.assertEquals(1, probability['e']);
		Assert.assertEquals(1, probability['s']);
		Assert.assertEquals(0, probability['z']);
		Assert.assertEquals(0, probability['b']);
	}

	@Test
	public void testCreateFrequencyTreeForTest() {
		int[] probability = new int[128];
		probability['t'] = 2;
		probability['e'] = 1;
		probability['s'] = 1;

		List<Node> nodes = huffmanUtils.createFrequencyTree(probability);

		Node e = new Node(1, "e");
		Node s = new Node(1, "s");
		Node es = new Node(e, s);
		Node t = new Node(2, "t");
		Node tes = new Node(t, es);

		Node eInList = findNode("e", nodes);
		Node sInList = findNode("s", nodes);
		Node tInList = findNode("t", nodes);
		Assert.assertEquals(e.getCode(null), eInList.getCode(null));
		Assert.assertEquals(s.getCode(null), sInList.getCode(null));
		Assert.assertEquals(t.getCode(null), tInList.getCode(null));
	}

	private Node findNode(String value, List<Node> nodes) {
		return nodes.stream().filter(node -> {
			return node.getValue().equals(value);
		}).findFirst().get();
	}

	@Test
	public void testCreateFrequencyTreeForEspresso() {
		int[] probability = new int[128];
		probability['e'] = 2;
		probability['s'] = 3;
		probability['p'] = 1;
		probability['r'] = 1;
		probability['o'] = 1;

		Node e = new Node(2, "e");
		Node o = new Node(1, "o");
		Node p = new Node(1, "p");
		Node r = new Node(1, "r");
		Node s = new Node(3, "s");
		Node op = new Node(o, p); // probability: 2
		Node re = new Node(r, e); // probability: 3
		Node ops = new Node(op, s); // first op:2 then s:3 (because re:3 comes
									// after s:3)
		Node reops = new Node(re, ops);

		huffmanUtils.createFrequencyTree(probability);

		// e - re = 1, re - reops = 0
		Assert.assertEquals("01", e.getCode(null));

		// s - ops = 1, ops - reops = 1
		Assert.assertEquals("11", s.getCode(null));

		// p - op = 1, op - ops = 0, ops - reops = 1
		Assert.assertEquals("101", p.getCode(null));

		// r - re = 0, re - reops = 0
		Assert.assertEquals("00", r.getCode(null));

		// o - op = 0, op - ops = 0, ops - reops = 1
		Assert.assertEquals("100", o.getCode(null));
	}

}
