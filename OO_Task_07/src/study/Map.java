package study;

import java.awt.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by luxia on 2016/4/20.
 */
public class Map {
	private File file;
	private String dir;
	private BufferedReader bf = null;
	private String[] readinStr;
	private char[][] pointset;
	private static Node[][] matrix = new Node[80][80];
	private static final int MAXCOUNT = 80;

	/**
	 * 用来获得地图
	 * @return 地图，格式为Node的二维数组
	 */
	public static Node[][] getMatrix() {
		return matrix;
	}

	/***
	 *  Map的构造方法，用来从文件创建地图
	 * @param dir 文件的路径
	 */
	public Map(String dir) {
		this.dir = dir;
		file = new File(this.dir);
		readinStr = new String[MAXCOUNT];
		pointset = new char[MAXCOUNT][MAXCOUNT];
		for (int i = 0; i < MAXCOUNT; i++)
			for (int j = 0; j < MAXCOUNT; j++) {
				matrix[i][j] = new Node(i, j);
			}
	}

	/***
	 * 将传入的字符串转化为点阵
	 */
	public void buildPointSet() {   //build map by using point
		try {
			bf = new BufferedReader(new FileReader(file));
			String temp = null;
			int line = 0;

			while ((temp = bf.readLine()) != null) {
				//System.out.println("line" + line + ":" + temp);

				if (line >= 80 || temp.length() != 80){
					System.out.println("Error matrix length! The matrix should be 80 * 80");
					System.exit(0);
				}
				readinStr[line] = temp;
				pointset[line++] = temp.toCharArray();
				for (char c : pointset[line-1]){
					if (c == '0' || c == '1' || c=='2' || c=='3'){
						continue;
					}
					else {
						System.out.println("Error number in matrix! The number can only be 0,1,2,3");
						System.out.println("Error line : " + line);
						System.exit(0);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Cannot find mapfile! \nPlease check your filepath(String dir) in Main!");
			System.exit(0);
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将点阵转化为Node结点
	 */
	public void buildMatrix() {
		try {
			for (int i = 0; i < MAXCOUNT; i++)
				for (int j = 0; j < MAXCOUNT; j++) {
					switch (pointset[i][j]) {
						case '0':
							break;
						case '1':
							matrix[j][i].linkright();
							matrix[j + 1][i].linkleft();
							break;
						case '2':
							matrix[j][i].linkdown();
							matrix[j][i + 1].linkup();
							break;
						case '3':
							matrix[j][i].linkright();
							matrix[j + 1][i].linkleft();
							matrix[j][i].linkdown();
							matrix[j][i + 1].linkup();
							break;
					}
				}
		} catch (Exception e) {
			System.out.println("Something goes wrong when building matrix! Please check your input!");
			System.exit(0);
		}
	}

	/**
	 * 计算从start到end的最短路径
	 * @param start 请求的起点
	 * @param end 请求的终点
	 * @return 从start到end的最短路径
	 */
	public static synchronized ArrayDeque<Node> findSp(Node start, Node end) {
		ArrayDeque<Node> sp = new ArrayDeque<>();
		ArrayList<Node> openlist = new ArrayList<>();
		ArrayList<Node> closelist = new ArrayList<>();

		openlist.add(start);
		start.setParent(start);
		start.Cal(end);
		Node check = null;

		while (openlist.size() != 0) {
			if (start.equals(end)) {
				return sp;
			}
			check = openlist.get(0);
			check.Cal(end);
			for (Node n : openlist) {
				n.Cal(end);
				if (n.getF() < check.getF()) {
					check = n;
				}
			}
			openlist.remove(check);
			closelist.add(check);
			for (Node nei : check.getNeighbor()) {
				if (closelist.contains(nei)) {
					continue;
				}
				else {
					if (!openlist.contains(nei)) {
						nei.setParent(check);
						nei.Cal(end);
						openlist.add(nei);
					}
					else if (openlist.contains(nei)) {
						if (nei.CalcG(check) < nei.CalcG(nei.getParent())) {
							nei.Cal(end);
							nei.getF();
						}
					}
				}
			}
			if (openlist.contains(end)) {
				break;
			}
		}
		Node route = end.getParent();
		sp.addFirst(end);
		if (route == null) {
			System.out.println("The map is not connected! Please check your file!");
			System.exit(0);
		}
		while (!route.equals(start)) {
			sp.addFirst(route);
			route = route.getParent();
		}
		return sp;
	}

	/////////////////////////////////////just for test////////////////////////////////////
	/*public static void main(String[] args) {
		char[][] test = new char[80][80];
		Map map = new Map("G:\\QMDownload\\1.txt");
		map.buildPointSet();
		test = map.getPointset();
		map.buildMatrix();
		System.out.println();
		ArrayDeque<Node> suiyi = new ArrayDeque<>();
		Node st = matrix[0][0];
		Node ed = matrix[0][5];
		suiyi = findSp(st, ed);
		for (Node n : suiyi) {
			System.out.println(n.toString());
		}
	}*/


}
