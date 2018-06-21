package study;

/**
 * Created by luxia on 2016/5/3.
 */

/**
 * 点对类型，用来保存两个点，主要用于RoadCounter类
 */
public class NodePair {
	Node n1, n2;

	public NodePair(Node n1, Node n2) {
		this.n1 = n1;
		this.n2 = n2;
	}

	/**
	 * 用来判定此点对和另一个点对是否相同
	 * @param o 另一个点对
	 * @return 如果相同则返回true，如果不同则返回false。注意这里的相同指的是有两个相同的点即判定为相同，与输入的顺序无关
	 */
	// Modified: null
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NodePair nodePair = (NodePair) o;

		if (!n1.equals(nodePair.n1) && !n1.equals(nodePair.n2)) return false;
		return n2.equals(nodePair.n2) || n2.equals(nodePair.n1);

	}

	@Override
	public int hashCode() {
		int small = n1.hashCode() < n2.hashCode() ? n1.hashCode() : n2.hashCode();
		int big = small == n1.hashCode()? n2.hashCode() : n1.hashCode();
		int result = small;
		result = 31 * result + big;
		return result;
	}
}
