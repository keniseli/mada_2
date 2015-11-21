package mada;

public class Node {
	private Node child0;
	private Node child1;
	private String value;
	private Node parent;
	private int probability;

	public Node(Node child0, Node child1) {
		this.child0 = child0;
		this.child1 = child1;
		this.child0.setParent(this);
		this.child1.setParent(this);
		this.value = String
				.format("%s%s", child0.getValue(), child1.getValue());
		this.probability = child0.getProbability() + child1.getProbability();
	}

	public Node(int probability, String value) {
		this.probability = probability;
		this.value = value;
	}

	public String getCode(Node child) {
		String code = "";
		if (parent != null) {
			code = parent.getCode(this);
		}
		if (child != null) {
			if (child.equals(child0)) {
				code = String.format("%s%s", code, "0");
			} else if (child.equals(child1)) {
				code = String.format("%s%s", code, "1");
			} else {
				code = "";
			}

		}
		return code;
	}

	@Override
	public String toString() {
		return String.format("%s : %d", value, probability);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((child0 == null) ? 0 : child0.hashCode());
		result = prime * result + ((child1 == null) ? 0 : child1.hashCode());
		result = prime * result + probability;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (child0 == null) {
			if (other.child0 != null)
				return false;
		} else if (!child0.equals(other.child0))
			return false;
		if (child1 == null) {
			if (other.child1 != null)
				return false;
		} else if (!child1.equals(other.child1))
			return false;
		if (probability != other.probability)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public Node getChild0() {
		return child0;
	}

	public void setChild0(Node child0) {
		this.child0 = child0;
		this.child0.setParent(this);
	}

	public Node getChild1() {
		return child1;
	}

	public void setChild1(Node child1) {
		this.child1 = child1;
		this.child1.setParent(this);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getProbability() {
		return probability;
	}

	public void setProbability(int probability) {
		this.probability = probability;
	}
}
