package mada;

import org.junit.Assert;
import org.junit.Test;

import mada.Node;

public class NodeTest {

	@Test
	public void testCodeOfChildren() {
		Node node = new Node(1, "a");
		Node child0 = new Node(1, "b");
		Node child1 = new Node(1, "c");
		node.setChild0(child0);
		node.setChild1(child1);
		Assert.assertEquals("0", child0.getCode(null));
		Assert.assertEquals("1", child1.getCode(null));
		Assert.assertEquals("", node.getCode(null));
	}
	
	@Test
	public void testCodeOfChildOfChild() {
		Node node = new Node(1, "a");
		Node child0 = new Node(1, "a");
		Node child1 = new Node(1, "a");
		Node child2 = new Node(1, "a");
		node.setChild0(child0);
		node.setChild1(child1);
		child1.setChild0(child2);
		Assert.assertEquals("0", child0.getCode(null));
		Assert.assertEquals("1", child1.getCode(null));
		Assert.assertEquals("", node.getCode(null));
		Assert.assertEquals("10", child2.getCode(null));
	}
	
	@Test
	public void testCodeOfComplexStructure() {
		Node node = new Node(1, "a");
		Node child0 = new Node(1, "b");
		Node child00 = new Node(1, "c");
		Node child01 = new Node(1, "d");
		Node child010 = new Node(1, "e");
		Node child011 = new Node(1, "f");

		node.setChild0(child0);
		child0.setChild0(child00);
		child0.setChild1(child01);
		child01.setChild0(child010);
		child01.setChild1(child011);
		
		Node child1 = new Node(1, "a");
		Node child10 = new Node(1, "b");
		Node child100 = new Node(1, "c");
		Node child101 = new Node(1, "d");
		Node child11 = new Node(1, "e");
		Node child110 = new Node(1, "f");
		Node child111 = new Node(1, "g");
		
		node.setChild1(child1);
		child1.setChild0(child10);
		child10.setChild0(child100);
		child10.setChild1(child101);
		child1.setChild1(child11);
		child11.setChild0(child110);
		child11.setChild1(child111);
		
		Assert.assertEquals("1", child1.getCode(null));
		Assert.assertEquals("01", child01.getCode(null));
		Assert.assertEquals("010", child010.getCode(null));
		Assert.assertEquals("011", child011.getCode(null));
		Assert.assertEquals("10", child10.getCode(null));
		Assert.assertEquals("100", child100.getCode(null));
		Assert.assertEquals("101", child101.getCode(null));
		Assert.assertEquals("110", child110.getCode(null));
		Assert.assertEquals("111", child111.getCode(null));
	}
}
