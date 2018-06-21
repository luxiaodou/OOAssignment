package study;

/**
 * Created by luxia on 2016/5/3.
 */

/**
 * 用来保存结点和其相关编号的工具类，比较简单，因此注释较少
 */
public class Pair {
	private Node n;
	private Direction d;

	public Pair(Node n, Direction d) {
		this.n = n;
		this.d = d;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Pair pair = (Pair) o;

		if (n != null ? !n.equals(pair.n) : pair.n != null) return false;
		return d == pair.d;

	}

	@Override
	public int hashCode() {
		int result = n != null ? n.hashCode() : 0;
		result = 31 * result + (d != null ? d.hashCode() : 0);
		return result;
	}
}
