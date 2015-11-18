package mada;

import org.junit.Assert;
import org.junit.Test;

public class HuffmannUtilsTest {

	@Test
	public void testDetermineProbabilityTable() {
		int[] probability = HuffmanUtils.determineProbabilityTable("test");
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

		Node frequencyTree = HuffmanUtils.createFrequencyTree(probability);

		Node e = new Node(1, "e");
		Node s = new Node(1, "s");
		Node es = new Node(e, s);
		Node t = new Node(2, "t");
		Node root = new Node(t, es);

		Assert.assertEquals(root, frequencyTree);
		Assert.assertEquals(root.getChild0(), frequencyTree.getChild0());
		Assert.assertEquals(root.getChild1(), frequencyTree.getChild1());
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

		Node frequencyTree = HuffmanUtils.createFrequencyTree(probability);

		Assert.assertEquals(reops, frequencyTree);
		Assert.assertEquals(reops.getChild0(), frequencyTree.getChild0());
		Assert.assertEquals(reops.getChild1(), frequencyTree.getChild1());
		
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
