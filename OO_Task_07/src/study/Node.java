package study;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by luxia on 2016/4/21.
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
	 * @return 当前结点的位置信息，
	 */
	public Point getPos() {
		return pos;
	}

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

	public Node getParent() {
		return parent;
	}

	public void linkup() {
		up = true;
	}


	public void linkleft() {
		left = true;
	}

	public void linkdown() {
		down = true;
	}

	public void linkright() {
		right = true;
	}

	public int getF() {
		return F;
	}

	public int getG() {
		return G;
	}

	public int getH() {
		return H;
	}

	public void setG(int g) {
		G = g;
	}

	/**
	 * 计算所有与A-star算法相关的值
	 * @param end 终点
	 */
	public void Cal(Node end) {
		CalcG(this.getParent());
		CalcH(end);
		F = G + H;
	}

	/**
	 * 用来计算A-star中G的值
	 * @param node 应为与当前结点邻接的结点
	 * @return G
	 */
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
	 * @param end 终点
	 * @return H
	 */
	public int CalcH(Node end) {
		int step = Math.abs(end.pos.x - this.pos.x) + Math.abs(end.pos.y - this.pos.y);
		H = step * STEP;
		return H;
	}

	/**
	 * 将当前结点的父节点设置为v
	 * @param v 需要设置的父节点
	 */
	public void setParent(Node v) {
		parent = v;
	}

	/**
	 * 用来获知当前结点有几个邻接结点，用于A-star算法
	 * @return 所有与当前结点相连接的结点，类型为ArrayList
	 */
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
