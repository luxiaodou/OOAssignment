package study;

/**
 * Created by luxia on 2016/5/3.
 */

/**
 * 通过节点和方向来记录边的工具类，比较简单
 * n表示结点，d表示该道路相对于结点的方向
 * 包含构造方法、repOK方法，判断是否相等的方法
 */
public class Pair {
	private Node n;
	private Direction d;

	/**
	 * 用来测试Pair类不变式的方法
	 * @return Pair类不变式的值
	 */
	// Require: Pair is initialized
	// Modified:null
	// Effect : 测试Pair对象的不变式的值
	public boolean pairRepOk() {
		if (!(n instanceof Node)) return false;
		if (!(d instanceof Direction)) return false;
		return true;
	}

	/**
	 * 工具类的构造方法，传入结点n和方向d来表示道路
	 * @param n 结点n ！= null
	 * @param d 方向d ！= null
	 */
	// Require :  n !=null d !=null
	// Modified :
	// Effect :  工具类的构造方法，传入结点n和方向d来表示道路
	public Pair(Node n, Direction d) {
		this.n = n;
		this.d = d;
	}

	/**
	 * 判断this和o是否相等的方法
	 * @param o 另一个对象o
	 * @return 二者的d和o相同则返回true，否则为false
	 */
	// Require : 另一个对象o
	// Modified : null
	// Effect : 判断this和o是否相等的方法
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Pair pair = (Pair) o;
		if (n != null ? !n.equals(pair.n) : pair.n != null) return false;
		return d == pair.d;

	}

	/**
	 * 获得this的hashcode的方法
	 * @return this的hashcode
	 */
	// Require : null
	// Modified : this.hashcode
	// Effect :　获得this的hashcode的方法
	@Override
	public int hashCode() {
		int result = n != null ? n.hashCode() : 0;
		result = 31 * result + (d != null ? d.hashCode() : 0);
		return result;
	}

	////////////////just for test/////////////
	/*public static void main(String[] args) {
		Pair p = new Pair(new Node(0,0),Direction.DOWN);
		System.out.println(p.pairRepOk());
	}*/
}
