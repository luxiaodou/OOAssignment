package study;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 用来储存地图信息的类，其中有修改地图以及计算最短路径的相关方法
 * file和dir分别为保存地图结点连接关系的地图的文件及其路径
 * verticalFile和verticaldir分别为保存了交叉关系的文件及其路径
 * bf用来读取文件，并将读取后的信息存入readinStr中，并分割为char的二维数组存入pointset中
 * 定义了地图的规格，应为MAXCOUNT*MAXCOUNT的方阵
 * matrix保存了结点的矩阵
 * changes记录了当前共有几次不重复的道路阻断
 * changelog记录了经过改变的结点对
 * 包含了构造方法和RepOk方法
 * 包含了阻塞及修复道路的方法
 * 包含了从文件构造地图的方法
 * 包含了使用A*算法寻找最短路的算法
 */
public class Map {
	private File file;
	private String dir;
	private File verticalFile;
	private String verticaldir;
	private BufferedReader bf = null;
	private String[] readinStr;
	private char[][] pointset;
	public static final int MAXCOUNT = 80;  //you can change the size of the map here
	private static Node[][] matrix = new Node[MAXCOUNT][MAXCOUNT];
	private static int changes = 0;
	private static ArrayList<Pair> changelog = new ArrayList<>();

	/**
	 * 测试Map类不变式的方法
	 * @return Map类不变式的值
	 */
	// Require : Map is initialized
	// Modified : null
	// Effect : 获得Map不变式的值
	public boolean mapRepOk() {
		if (!file.exists() || !verticalFile.exists()) {
			return false;
		}
		if (dir == null || verticaldir == null) {
			return false;
		}
		if (matrix == null) {
			return false;
		}
		if (changes > 5 ||changes<0) {
			return false;
		}
		if (changelog == null) {
			return false;
		}
		if (readinStr.length != MAXCOUNT || pointset.length!=MAXCOUNT || MAXCOUNT < 0 || MAXCOUNT > Integer.MAX_VALUE) {
			return false;
		}
		return true;
	}

	/***
	 * Effect:Map的构造方法，用来从文件创建地图
	 * Require:
	 *
	 * @param dir         文件的路径
	 * @param verticaldir 交叉文件的路径
	 */
	// Require : dir,verticaldir != null
	// Modified：matrix,file,verticalFile,this.verticaldir,this.dir,pointset,readinStr
	// Effect : 构造以dir路径的file为地图文件，verticaldir路径的file文件为交叉地图文件的地图，并对一些值进行初始化
	public Map(String dir, String verticaldir) {
		this.dir = dir;
		this.verticaldir = verticaldir;
		file = new File(this.dir);
		verticalFile = new File(this.verticaldir);
		readinStr = new String[MAXCOUNT];
		pointset = new char[MAXCOUNT][MAXCOUNT];
		for (int i = 0; i < MAXCOUNT; i++)
			for (int j = 0; j < MAXCOUNT; j++) {
				matrix[i][j] = new Node(i, j);
			}
	}


	/**
	 * 阻断道路的方法，输入想要阻断道路的一个端点和其对应的方向即可打断已有的道路
	 * 注意：如果原地图不存在此条边或者修改的边数超过5将会无法正常工作
	 *
	 * @param x   结点的x坐标
	 * @param y   结点的y坐标
	 * @param dir 道路相对于结点的方向
	 */
	//Require : 0<=x,y<MAXCOUNT dir类型为DIrection且不为null
	//Modified：Map,n,another
	// Effect : 阻断道路的方法，输入想要阻断道路的一个端点和其对应的方向即可打断已有的道路
	public static synchronized void blocktheWay(int x, int y, Direction dir) {
		Node n = matrix[x][y];
		String msg = "No way to block here! Please choose another road!";
		String toomany = "More than 5 roads are blocked, this operation will be ignored!";

		switch (dir) {
			case UP:
				if (!n.WayUp()) {
					System.out.println(msg);
				}
				else if (changelog.contains(new Pair(n, dir))) {
					n.blockUp();
					Node another = matrix[x][y - 1];
					another.blockDown();
				}
				else if (changes < 5) {
					n.blockUp();
					changelog.add(new Pair(n, Direction.UP));
					Node another = matrix[x][y - 1];
					another.blockDown();
					changelog.add(new Pair(another, Direction.DOWN));
					changes++;
				}
				else {
					System.out.println(toomany);
				}
				break;

			case DOWN:
				if (!n.WayDown()) {
					System.out.println(msg);
				}
				else if (changelog.contains(new Pair(n, dir))) {
					n.blockDown();
					Node another = matrix[x][y + 1];
					another.blockUp();
				}
				else if (changes < 5) {
					n.blockDown();
					Node another = matrix[x][y + 1];
					another.blockUp();
					changelog.add(new Pair(n, Direction.DOWN));
					changelog.add(new Pair(another, Direction.UP));
					changes++;
				}
				else {
					System.out.println(toomany);
				}
				break;

			case LEFT:
				if (!n.WayLeft()) {
					System.out.println(msg);
				}
				else if (changelog.contains(new Pair(n, dir))) {
					n.blockLeft();
					Node another = matrix[x - 1][y];
					another.blockRight();
				}
				else if (changes < 5) {
					n.blockLeft();
					Node another = matrix[x - 1][y];
					another.blockRight();
					changelog.add(new Pair(n, Direction.LEFT));
					changelog.add(new Pair(another, Direction.RIGHT));
					changes++;
				}
				else {
					System.out.println(toomany);
				}
				break;

			case RIGHT:
				if (!n.WayRight()) {
					System.out.println(msg);
				}
				else if (changelog.contains(new Pair(n, dir))) {
					n.blockRight();
					Node another = matrix[x + 1][y];
					another.blockLeft();
				}
				else if (changes < 5) {
					n.blockRight();
					Node another = matrix[x + 1][y];
					another.blockLeft();
					changelog.add(new Pair(n, Direction.RIGHT));
					changelog.add(new Pair(another, Direction.LEFT));
					changes++;
				}
				else {
					System.out.println(toomany);
				}
				break;
		}
	}

	/**
	 * 重新修复已经被阻断的道路
	 * 如果该道路没有被blocktheWay方法截断，则无法正常工作并打印错误信息
	 *
	 * @param x   修复结点的x坐标
	 * @param y   修复结点的y坐标
	 * @param dir 结点某方向的道路
	 */
	// Require : 0<=x,y<MAXCOUNT dir类型为DIrection且不为null
	// Modified： Map, n , ano
	// Effect : 重新修复已经被阻断的道路,如果该道路没有被blocktheWay方法截断，则无法正常工作并打印错误信息
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

	/**
	 * Effect:用来获得地图
	 * Require: null
	 * Modified: 需要预先完成地图初始化
	 *
	 * @return 地图，格式为Node的二维数组
	 */
	// Require : null
	//Modified：null
	// Effect : 获得matrix
	public static Node[][] getMatrix() {
		return matrix;
	}

	/**
	 * 通过交叉文件来建设红绿灯
	 */
	// Require ： null
	// Modified : matrix
	// Effect : 通过交叉文件来建设红绿灯
	public void buildVertical() {
		try {
			bf = new BufferedReader(new FileReader(verticalFile));
			String temp = null;
			int line = 0;

			while ((temp = bf.readLine()) != null) {
				if (line >= MAXCOUNT || temp.length() != MAXCOUNT) {
					System.out.println("Vertical input error : error matrix length! The vertical matrix should be 80 * 80!");
					System.exit(0);
				}
				readinStr[line] = temp;
				pointset[line++] = temp.toCharArray();
				for (char c : pointset[line - 1]) {
					if (c == '0' || c == '1') {
						continue;
					}
					else {
						System.out.println("Error number in vertical matrix. The number can only be 0 or 1!");
						System.out.println("Error line : " + line);
						System.exit(0);
					}
				}
			}
			if (line == 0) {
				System.out.println("error Vertical Map! Please check your file and try again!");
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Cannot find vertical map file! \n Please check your filepath (String verticalFile) in Main!");
			System.exit(0);
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			for (int i = 0; i < MAXCOUNT; i++) {
				for (int j = 0; j < MAXCOUNT; j++) {
					switch (pointset[i][j]) {
						case '1':
							matrix[j][i].hasLight();
						case '0':
							break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Something goes wrong when building vertical matrix! Please check your input!");
			System.exit(0);
		}
	}

	/***
	 * 将传入的字符串转化为点阵
	 */
	// Require : null
	//Modified：bf,temp,line,readinStr,pointset
	// Effect : 将传入的字符串转化为点阵
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
					}
					else {
						System.out.println("Error number in matrix! The number can only be 0,1,2,3");
						System.out.println("Error line : " + line);
						System.exit(0);
					}
				}
			}

			if (line == 0 || line != 80) {
				System.out.println("Your Map is error! Please check your map file and try again! ");
				System.exit(0);
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
	// Require : null
	// Modified: matrix,related Nodes
	// effect : 将点阵转化为Node结点
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
	// Require : 起点和终点结点start和end
	// Modified: openlist,sp,closelist,check,route
	// Effect :  计算从start到end的最短路径,使用A*算法
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
		Map map = new Map("G:\\QMDownload\\1.txt", "G:\\QMDownload\\2.txt");
		map.buildPointSet();
		map.buildMatrix();
		map.buildVertical();
//		System.out.println();
//		ArrayDeque<Node> suiyi = new ArrayDeque<>();
//		Node st = matrix[0][0];
//		Node ed = matrix[0][5];
//		map.blocktheWay(10, 0, Direction.RIGHT);
//		map.blocktheWay(3, 5, Direction.DOWN);
//		map.blocktheWay(3, 5, Direction.LEFT);
//		map.blocktheWay(3, 5, Direction.RIGHT);
//		map.blocktheWay(3, 4, Direction.RIGHT);
//		map.blocktheWay(0, 0, Direction.RIGHT);
//		map.blocktheWay(0, 0, Direction.DOWN);
//		suiyi = findSp(st, ed);
//		for (Node n : suiyi) {
//			System.out.println(n.toString());
//		}
//		map.rebuildTheWay(3,5,Direction.DOWN);
//		System.out.println();
//		suiyi = findSp(st,ed);
//		for (Node n : suiyi) {
//			System.out.println(n.toString());
//		}
	}*/
}
