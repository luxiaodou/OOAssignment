package study;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by luxia on 2016/4/21.
 */

/**
 * 结点类，用来保存地图信息
 */
public class Node {
	private Point pos;
	private Boolean up, down, left, right;
	private Node parent;
	private int F, G, H;
	private final static int STEP = 10;
	private final static int OBLIQUE = 14;

	/**
	 * 返回结点的位置信息
	 *
	 * @return 当前结点的位置信息，
	 */
	// Modified: null
	public Point getPos() {
		return pos;
	}

	/**
	 * 返回当前结点的信息
	 *
	 * @return 当前结点的信息，类型为String
	 */
	// Modified: null
	@Override
	public String toString() {
		return "(" + this.pos.x + "," + this.pos.y + ")";
	}

	public Node(int x, int y) {
		this.pos = new Point(x, y);
		up = false;
		down = false;
		left = false;
		right = false;
	}

	/*******
	 * new Added in OOTask8
	 ********/
	//used in Map.java

	/**
	 * 判定该结点是否有向上延伸的道路
	 *
	 * @return 如果有向上延伸的道路则返回true，否则返回false
	 */
	// Modified: null
	public Boolean WayUp() {
		return up;
	}

	/**
	 * 判定该结点是否有向下延伸的道路
	 *
	 * @return 如果有向下延伸的道路则返回true，否则返回false
	 */
	// Modified: null
	public Boolean WayDown() {
		return down;
	}

	/**
	 * 判定该结点是否有向左延伸的道路
	 *
	 * @return 如果有向左延伸的道路则返回true，否则返回false
	 */
	// Modified: null
	public Boolean WayLeft() {
		return left;
	}

	/**
	 * 判定该结点是否有向右延伸的道路
	 *
	 * @return 如果有向右延伸的道路则返回true，否则返回false
	 */
	// Modified: null
	public Boolean WayRight() {
		return right;
	}

	/**
	 * 将向上连接道路的标示标记为false，即表示道路被阻断
	 */
	// Modified: up
	void blockUp() {
		up = false;
	}

	/**
	 * 将向下连接道路的标示标记为false，即表示道路被阻断
	 */
	// Modified: down
	void blockDown() {
		down = false;
	}

	/**
	 * 将向左连接道路的标示标记为false，即表示道路被阻断
	 */
	// Modified: left
	void blockLeft() {
		left = false;
	}

	/**
	 * 将向右连接道路的标示标记为false，即表示道路被阻断
	 */
	// Modified: right
	void blockRight() {
		right = false;
	}

	/*******Added End******************/

	/**
	 * 获得结点的父亲结点，用于A-star寻路算法
	 *
	 * @return this的父亲节点，返回类型为Node
	 */
	// Modified: null
	public Node getParent() {
		return parent;
	}

	/**
	 * 将向上有道路连接的标示标记为true，即表示与向上的点之间存在通路
	 */
	// Modified: up
	public void linkup() {
		up = true;
	}

	/**
	 * 将向左有道路连接的标示标记为true，即表示与左边的点之间存在通路
	 */
	// Modified: left
	public void linkleft() {
		left = true;
	}

	/**
	 * 将向下有道路连接的标示标记为true，即表示与下边的点之间存在通路
	 */
	// Modified: down
	public void linkdown() {
		down = true;
	}

	/**
	 * 将向右有道路连接的标示标记为true，即表示与右边的点之间存在通路
	 */
	// Modified: right
	public void linkright() {
		right = true;
	}

	/**
	 * 获得该点现在的F权值，用于A-star寻路算法
	 *
	 * @return 返回this的F权值，类型为int
	 */
	// Modified: null
	public int getF() {
		return F;
	}

	/**
	 * 获得该点现在的G估计值，用于A-star寻路算法
	 *
	 * @return 返回this的G预估值，类型为int
	 */
	// Modified: null
	public int getG() {
		return G;
	}

	/**
	 * 计算更新所有与A-star算法相关的值，共涉及G,H,F三个变量
	 *
	 * @param end 终点结点，类型为Node
	 */
	// Modified: G,H,F
	public void Cal(Node end) {
		CalcG(this.getParent());
		CalcH(end);
		F = G + H;
	}

	/**
	 * 用来计算A-star中G的值
	 *
	 * @param node 应为与当前结点邻接的结点
	 * @return 返回该点的G预估值，类型为int
	 */
	// Modified: temp,dis,parentG,G
	public int CalcG(Node node) {
		int temp = 0;
		int dis = Math.abs(this.pos.x - node.pos.x) + Math.abs(this.pos.y - node.pos.y);
		switch (dis) {
			case 0:
				temp = 0;
				break;
			case 1:
				temp = STEP;
				break;
			default:
				temp = OBLIQUE;
				break;
		}
		int parentG = node.getG();
		G = temp + parentG;
		return G;
	}

	/**
	 * 计算A-star中H的值
	 *
	 * @param end 终点的结点
	 * @return 返回该点距离终点的曼哈顿距离长度H
	 */
	// Modified: step,H
	public int CalcH(Node end) {
		int step = Math.abs(end.pos.x - this.pos.x) + Math.abs(end.pos.y - this.pos.y);
		H = step * STEP;
		return H;
	}

	/**
	 * 将当前结点的父节点设置为v
	 *
	 * @param v 需要设置的父节点
	 */
	// Modified: this.parent
	public void setParent(Node v) {
		parent = v;
	}

	/**
	 * 用来获知当前结点有几个邻接结点，用于A-star算法
	 *
	 * @return 所有与当前结点相连接的结点，类型为ArrayList
	 */
	// Modified: neighbor
	public ArrayList<Node> getNeighbor() {
		ArrayList<Node> neighbor = new ArrayList<>();
		if (up) {
			neighbor.add(Map.getMatrix()[pos.x][pos.y - 1]);
		}
		if (down) {
			neighbor.add(Map.getMatrix()[pos.x][pos.y + 1]);
		}
		if (left) {
			neighbor.add(Map.getMatrix()[pos.x - 1][pos.y]);
		}
		if (right) {
			neighbor.add(Map.getMatrix()[pos.x + 1][pos.y]);
		}
		return neighbor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Node node = (Node) o;
		return pos != null ? pos.equals(node.pos) : node.pos == null;
	}

	@Override
	public int hashCode() {
		return pos != null ? pos.hashCode() : 0;
	}
}
