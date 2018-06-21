package study;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 用来储存地图信息的类，其中有修改地图以及计算最短路径的相关方法
 */
public class Map {
	private File file;
	private String dir;
	private BufferedReader bf = null;
	private String[] readinStr;
	private char[][] pointset;
	public static final int MAXCOUNT = 80;  //you can change the size of the map here
	private static Node[][] matrix = new Node[MAXCOUNT][MAXCOUNT];

	/**********
	 * new Added in OOTask8
	 ***************/
	private static int changes = 0;
	private static ArrayList<Pair> changelog = new ArrayList<>();


	/**
	 * 阻断道路的方法，输入想要阻断道路的一个端点和其对应的方向即可打断已有的道路
	 * 注意：如果原地图不存在此条边或者修改的边数超过5将会无法正常工作
	 *
	 * @param x 结点的x坐标
	 * @param y 结点的y坐标
	 * @param dir 道路相对于结点的方向
	 */
	//Modified：Map,n,another
	public static synchronized void blocktheWay(int x, int y, Direction dir) {
		Node n = matrix[x][y];
		String msg = "No way to block here! Please choose another road!";
		String toomany = "More than 5 roads are blocked, this operation will be ignored!";

		switch (dir) {
			case UP:
				if (!n.WayUp()) {
					System.out.println(msg);
				} else if (changelog.contains(new Pair(n, dir))) {
					n.blockUp();
					Node another = matrix[x][y - 1];
					another.blockDown();
				} else if (changes < 5) {
					n.blockUp();
					changelog.add(new Pair(n, Direction.UP));
					Node another = matrix[x][y - 1];
					another.blockDown();
					changelog.add(new Pair(another, Direction.DOWN));
					changes++;
				} else {
					System.out.println(toomany);
				}
				break;

			case DOWN:
				if (!n.WayDown())
					System.out.println(msg);
				else if (changelog.contains(new Pair(n, dir))) {
					n.blockDown();
					Node another = matrix[x][y + 1];
					another.blockUp();
				} else if (changes < 5) {
					n.blockDown();
					Node another = matrix[x][y + 1];
					another.blockUp();
					changelog.add(new Pair(n, Direction.DOWN));
					changelog.add(new Pair(another, Direction.UP));
					changes++;
				} else {
					System.out.println(toomany);
				}
				break;

			case LEFT:
				if (!n.WayLeft())
					System.out.println(msg);
				else if (changelog.contains(new Pair(n, dir))) {
					n.blockLeft();
					Node another = matrix[x - 1][y];
					another.blockRight();
				} else if (changes < 5) {
					n.blockLeft();
					Node another = matrix[x - 1][y];
					another.blockRight();
					changelog.add(new Pair(n, Direction.LEFT));
					changelog.add(new Pair(another, Direction.RIGHT));
					changes++;
				} else {
					System.out.println(toomany);
				}
				break;

			case RIGHT:
				if (!n.WayRight()) {
					System.out.println(msg);
				} else if (changelog.contains(new Pair(n, dir))) {
					n.blockRight();
					Node another = matrix[x + 1][y];
					another.blockLeft();
				} else if (changes < 5) {
					n.blockRight();
					Node another = matrix[x + 1][y];
					another.blockLeft();
					changelog.add(new Pair(n, Direction.RIGHT));
					changelog.add(new Pair(another, Direction.LEFT));
					changes++;
				} else {
					System.out.println(toomany);
				}
				break;
		}
	}

	/**
	 *
	 * Effect:重新修复已经被阻断的道路
	 * 如果该道路没有被blocktheWay方法截断，则无法正常工作并打印错误信息
	 *
	 * Require:
	 * @param x 修复结点的x坐标
	 * @param y 修复结点的y坐标
	 * @param dir 结点某方向的道路
	 */
	//Modified： Map, n , ano
	public static synchronized void rebuildTheWay(int x, int y, Direction dir) {
		Node n = matrix[x][y];
		Node ano = null;

		if (!changelog.contains(new Pair(n, dir))) {
			System.out.println("This way is not accessible in the origin map! Please try another road to rebuild!");
			return;
		}

		switch (dir) {
			case UP:
				n.linkup();
				ano = matrix[x][y + 1];
				ano.linkdown();
				break;
			case DOWN:
				n.linkdown();
				ano = matrix[x][y - 1];
				ano.linkup();
				break;
			case LEFT:
				n.linkleft();
				ano = matrix[x - 1][y];
				ano.linkright();
				break;
			case RIGHT:
				n.linkright();
				ano = matrix[x + 1][y];
				ano.linkleft();
				break;
		}
	}

	/***
	 * Added end
	 */

	/**
	 * Effect:用来获得地图
	 * Require: null
	 * Modified: 需要预先完成地图初始化
	 * @return 地图，格式为Node的二维数组
	 */
	//Modified：null
	public static Node[][] getMatrix() {
		return matrix;
	}

	/***
	 * Effect:Map的构造方法，用来从文件创建地图
	 * Require:
	 * @param dir 文件的路径
	 */
	//Modified：matrix,file,dir,pointset,readinStr
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
	//Modified：bf,temp,line,readinStr,pointset
	public void buildPointSet() {   //build map by using point
		try {
			bf = new BufferedReader(new FileReader(file));
			String temp = null;
			int line = 0;

			while ((temp = bf.readLine()) != null) {
				//System.out.println("line" + line + ":" + temp);

				if (line >= MAXCOUNT || temp.length() != MAXCOUNT) {
					System.out.println("Error matrix length! The matrix should be 80 * 80");
					System.exit(0);
				}
				readinStr[line] = temp;
				pointset[line++] = temp.toCharArray();
				for (char c : pointset[line - 1]) {
					if (c == '0' || c == '1' || c == '2' || c == '3') {
						continue;
					} else {
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
				bf.close(); //close the file reader
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将点阵转化为Node结点
	 */
	// Modified: matrix,related Nodes
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
	 * 计算从start到end的最短路径,使用A*算法
	 *
	 * @param start 请求的起点
	 * @param end   请求的终点
	 * @return 从start到end的最短路径
	 */
	// Modified: openlist,sp,closelist,check,route
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
				} else {
					if (!openlist.contains(nei)) {
						nei.setParent(check);
						nei.Cal(end);
						openlist.add(nei);
					} else if (openlist.contains(nei)) {
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
		map.buildMatrix();
		System.out.println();
		ArrayDeque<Node> suiyi = new ArrayDeque<>();
		Node st = matrix[0][0];
		Node ed = matrix[0][5];
		map.blocktheWay(10, 0, Direction.RIGHT);
		map.blocktheWay(3, 5, Direction.DOWN);
		map.blocktheWay(3, 5, Direction.LEFT);
		map.blocktheWay(3, 5, Direction.RIGHT);
		map.blocktheWay(3, 4, Direction.RIGHT);
		map.blocktheWay(0, 0, Direction.RIGHT);
		map.blocktheWay(0, 0, Direction.DOWN);
		suiyi = findSp(st, ed);
		for (Node n : suiyi) {
			System.out.println(n.toString());
		}
		map.rebuildTheWay(3,5,Direction.DOWN);
		System.out.println();
		suiyi = findSp(st,ed);
		for (Node n : suiyi) {
			System.out.println(n.toString());
		}
	}*/


}
