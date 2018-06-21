package study;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by luxia on 2016/4/21.
 */

/**
 * 结点类，用来保存地图信息。定义了用于A-star计算的STEP和OBLIQUE静态常量。
 * 每个结点保存有其位置信息pos，其上下左右的邻接关系up, down, left, right
 * 记录其父亲结点parent, 记录了A-star F，G，H估算值
 * 记录是否有灯的属性light
 * 包含了构造方法和RepOK方法
 * 包含获得结点位置的方法，获得节点信息的方法，获得结点是否有灯，设置节点有灯属性，
 * 连接上下左右结点，封闭上下左右道路，是否与周围结点临街，获取父亲结点，获取或计算FGH的值，
 * 获得邻接节点，判断是否相等的方法
 */
public class Node{
	private final static int STEP = 10;
	private final static int OBLIQUE = 14;
	private Point pos;
	private Boolean up, down, left, right;
	private Node parent;
	private int F, G, H;
	private Boolean light;

	/**
	 * 测试Node类不变式的方法
	 *
	 * @return Node类不变式的值
	 */
	// Require : Node is initialized
	// Modified : null
	// Effect : 获得当前Node对象的不变式的值
	public boolean nodeRepOk() {
		if (STEP != 10) {
			return false;
		}
		if (OBLIQUE != 14) {
			return false;
		}
		if (pos == null) {
			return false;
		}
		if (up == null || down == null || left == null || right == null) {
			return false;
		}
		if (!(parent instanceof Node) && parent != null) {
			return false;
		}
		if (light == null) {
			return false;
		}
		return true;
	}

	/**
	 * 返回结点的位置信息
	 *
	 * @return 当前结点的位置信息，
	 */
	// Require : pos != null
	// Modified: null
	// Effect : 获得当前结点的位置
	public Point getPos() {
		return pos;
	}

	/**
	 * 返回当前结点的信息
	 *
	 * @return 当前结点的信息，类型为String
	 */
	// Require : pos != null
	// Modified: null
	// Effect : 返回当前结点的信息
	@Override
	public String toString() {
		return "(" + this.pos.x + "," + this.pos.y + ")";
	}

	/**
	 * Node的构造方法，定义了Node的位置
	 *
	 * @param x 结点的横坐标
	 * @param y 结点的纵坐标
	 */
	// Require : x,y != null
	// Modified : pos,up,down,left,right,light
	// Effect : 构造位置为(x,y)结点，并对相应的变量进行初始化
	public Node(int x, int y) {
		this.pos = new Point(x, y);
		up = false;
		down = false;
		left = false;
		right = false;
		light = false;
	}

	/**
	 * 决定该结点是否有灯,仅在vertical属性为1时才会被调用
	 */
	//Require : null
	//Modified : light
	//Effect : 若该结点有大于等于3个邻接节点，则将light置为true
	public void hasLight() {
		if (this.getNeighbor().size() >= 3) {
			light = true;
		}
	}

	/**
	 * 获得当前路口是否有灯的方法
	 *
	 * @return 该路口是否有红绿灯
	 */
	// Require : this != null
	// Modified : null
	// Effect : 获得当前路口是否有灯
	public boolean getLight() {
		return light;
	}

	/**
	 * 判定该结点是否有向上延伸的道路
	 *
	 * @return 如果有向上延伸的道路则返回true，否则返回false
	 */
	// Require : null
	// Effect : 判定该结点是否有向上延伸的道路
	// Modified: null
	public Boolean WayUp() {
		return up;
	}

	/**
	 * 判定该结点是否有向下延伸的道路
	 *
	 * @return 如果有向下延伸的道路则返回true，否则返回false
	 */
	// Effect : 判定该结点是否有向下延伸的道路
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
	// Effect : 判定该结点是否有向左延伸的道路
	public Boolean WayLeft() {
		return left;
	}

	/**
	 * 判定该结点是否有向右延伸的道路
	 *
	 * @return 如果有向右延伸的道路则返回true，否则返回false
	 */
	// Modified: null
	// Effect : 判定该结点是否有向右延伸的道路
	public Boolean WayRight() {
		return right;
	}

	/**
	 * 将向上连接道路的标示标记为false，即表示道路被阻断
	 */
	// Modified: up
	// Effect : 将向上连接道路的标示标记为false，即表示道路被阻断
	void blockUp() {
		up = false;
	}

	/**
	 * 将向下连接道路的标示标记为false，即表示道路被阻断
	 */
	// Modified: down
	// Effect : 将向下连接道路的标示标记为false，即表示道路被阻断
	void blockDown() {
		down = false;
	}

	/**
	 * 将向左连接道路的标示标记为false，即表示道路被阻断
	 */
	// Modified: left
	// Effect : 将向左连接道路的标示标记为false，即表示道路被阻断
	void blockLeft() {
		left = false;
	}

	/**
	 * 将向右连接道路的标示标记为false，即表示道路被阻断
	 */
	// Modified: right
	// Effect : 将向右连接道路的标示标记为false，即表示道路被阻断
	void blockRight() {
		right = false;
	}

	/**
	 * 获得结点的父亲结点，用于A-star寻路算法
	 *
	 * @return this的父亲节点，返回类型为Node
	 */
	// Modified: null
	// Effect : 获得结点的父亲结点
	public Node getParent() {
		return parent;
	}

	/**
	 * 将向上有道路连接的标示标记为true，即表示与向上的点之间存在通路
	 */
	// Modified: up
	// Effect : 将向上有道路连接的标示标记为true，即表示与向上的点之间存在通路
	public void linkup() {
		up = true;
	}

	/**
	 * 将向左有道路连接的标示标记为true，即表示与左边的点之间存在通路
	 */
	// Modified: left
	// Effect : 将向左有道路连接的标示标记为true，即表示与左边的点之间存在通路
	public void linkleft() {
		left = true;
	}

	/**
	 * 将向下有道路连接的标示标记为true，即表示与下边的点之间存在通路
	 */
	// Modified: down
	// Effect : 将向下有道路连接的标示标记为true，即表示与下边的点之间存在通路
	public void linkdown() {
		down = true;
	}

	/**
	 * 将向右有道路连接的标示标记为true，即表示与右边的点之间存在通路
	 */
	// Modified: right
	// Effect : 将向右有道路连接的标示标记为true，即表示与右边的点之间存在通路
	public void linkright() {
		right = true;
	}

	/**
	 * 获得该点现在的F权值，用于A-star寻路算法
	 *
	 * @return 返回this的F权值，类型为int
	 */
	// Modified: null
	// Effect :　获得该点现在的F权值，用于A-star寻路算法
	public int getF() {
		return F;
	}

	/**
	 * 获得该点现在的G估计值，用于A-star寻路算法
	 *
	 * @return 返回this的G预估值，类型为int
	 */
	// Modified: null
	// Effect : 获得该点现在的G估计值，用于A-star寻路算法
	public int getG() {
		return G;
	}

	/**
	 * 计算更新所有与A-star算法相关的值，共涉及G,H,F三个变量
	 *
	 * @param end 终点结点，类型为Node
	 */
	// Require : 终点节点end
	// Modified: G,H,F
	// Effect : 计算更新所有与A-star算法相关的值，共涉及G,H,F三个变量
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
	// Require : 结点node , node需要与this相邻
	// Modified: temp,dis,parentG,G
	// Effect : 用来计算A-star中G的值
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
	// Require : 终点Node
	// Modified: step,H
	// Effect : 计算A-star中H的值
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
	// Require : Node v， v需要是this的邻接节点
	// Modified: this.parent
	// Effect : 将this.parent 置为 v
	public void setParent(Node v) {
		parent = v;
	}

	/**
	 * 用来获知当前结点有几个邻接结点
	 *
	 * @return 所有与当前结点相连接的结点，类型为ArrayList
	 */
	// Require : null
	// Modified: neighbor
	// Effect : 来获知当前结点有几个邻接结点,返回包含有this邻接节点的Arraylist
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

	public ArrayList<Node> getOriNei() {
		ArrayList<Node> neighbor = new ArrayList<>();
		if (up) {
			neighbor.add(Map.getOrimap()[pos.x][pos.y - 1]);
		}
		if (down) {
			neighbor.add(Map.getOrimap()[pos.x][pos.y + 1]);
		}
		if (left) {
			neighbor.add(Map.getOrimap()[pos.x - 1][pos.y]);
		}
		if (right) {
			neighbor.add(Map.getOrimap()[pos.x + 1][pos.y]);
		}
		return neighbor;
	}


	/**
	 * 判定两个Node是否相等的方法
	 *
	 * @param o 传入参数
	 * @return this与o的位置相同则返回true，否则返回false
	 */
	// Require : 传入对象o
	// Modified : null
	// Effect : 用来判断this与o两个对象是否相同
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

	/**
	 * 简单地通过位置来计算Node和hashCode的方法
	 *
	 * @return this的hashcode
	 */
	// Require : null
	// Modified : this.hashcode
	// Effect : 简单地通过位置来计算this的ashCode
	@Override
	public int hashCode() {
		return pos != null ? pos.hashCode() : 0;
	}

	////////////////just for test///////////////
//	public static void main(String[] args) {
//		Node n = new Node(1, 1);
//		boolean e = n.nodeRepOk();
//		System.out.println(e);
//	}
}
