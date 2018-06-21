package study;

/**
 * Created by luxia on 2016/5/3.
 */

/**
 * 点对类型，用来保存两个点，主要用于RoadCounter类
 * 点对通过保存两个端点来实现，n1, n2均为Node类型
 * 包含构造方法，RepOk方法，判断相等的方法
 */
public class NodePair {
	Node n1, n2;

	/**
	 * 测试NodePair类不变式的值的方法
	 * @return NodePair类的不变式的值
	 */
	// Require : NodePair is initialized
	// Modified : null
	// Effect : 获得当前NodePair对象的不变式的值
	public boolean NPRepOk() {
		return (n1 != null && n2 != null);
	}

	/**
	 * NodePair的构造方法
	 * @param n1 点对的其中一个结点
	 * @param n2 点对的另一个结点
	 */
	// Require ： n1 != null n2 != null
	// Modified : this.n1 this.n2
	// Effect : 构造NodePair
	public NodePair(Node n1, Node n2) {
		this.n1 = n1;
		this.n2 = n2;
	}

	/**
	 * 用来判定此点对和另一个对象是否相同
	 * @param o 另一个对象
	 * @return 如果相同则返回true，如果不同则返回false。注意这里的相同指的是有两个相同的点即判定为相同，与输入的顺序无关
	 */
	// Require ： 另一个对象O
	// Modified: null
	// Effect ： 判断this和o是否相同,其中只需要满足包含的结点相同即可判定为相等,与结点的顺序无关。
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NodePair nodePair = (NodePair) o;
		if (!n1.equals(nodePair.n1) && !n1.equals(nodePair.n2)) return false;
		return n2.equals(nodePair.n2) || n2.equals(nodePair.n1);

	}

	/**
	 * 计算点对HashCode的方法，保证拥有相同结点的点对有相同的hashcode
	 * @return this的hashcode
	 */
	// Require : this!=null;
	// Modified : small,big,result
	// Effect : 计算当前点对的hashcode
	@Override
	public int hashCode() {
		int small = n1.hashCode() < n2.hashCode() ? n1.hashCode() : n2.hashCode();
		int big = small == n1.hashCode()? n2.hashCode() : n1.hashCode();
		int result = small;
		result = 31 * result + big;
		return result;
	}

	/////////////////just for test////////////////////////
	/*public static void main(String[] args) {
		NodePair np = new NodePair(new Node(0,0),new Node(1,1));
		System.out.println(np.NPRepOk());
	}*/
}
