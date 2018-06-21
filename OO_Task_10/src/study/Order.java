package study;

import java.util.*;

import static study.Main.taxis;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 请求类，用来储存请求信息
 * location和destination变量用来保存请求的起点和终点
 * tracked用来保存该请求的跟踪状态
 * sp用来记录起点到终点的最短路径
 * carnum用来记录在3s内经过窗口的车辆的车牌号
 * 包括了构造和RepOK方法
 * 包括了设定追踪状态的方法，判断窗口内车辆以及添加车辆的方法
 * 包括了获得请求信息的方法，获得最短路径的方法，获得目的地的方法
 */
public class Order extends Thread {
	private Node location;
	private Node destination;
	private Boolean tracked;
	private ArrayDeque<Node> sp = new ArrayDeque<>();
	private Set<Integer> carnum = new HashSet<>();

	/**
	 * 测试当前Order类不变式的方法
	 * @return 当前对象的不变式的值
	 */
	// Require : Order is initialized, Map is initialized
	// Modified : null
	// Effect : 获得当前对象的不变式的值
	public boolean orderRepOk() {
		if (!(location instanceof Node) || !(destination instanceof  Node))
			return false;
		if (!(tracked != null)) return false;
		if (!(sp instanceof ArrayDeque)) return false;
		if (!(carnum instanceof Set)) return false;
		return true;
	}

	/**
	 * 用来获得改指令的跟踪状态
	 * @return 若被跟踪则返回True，否则返回false
	 */
	// Require : null
	// Modified: null
	// Effect : 用来获得改指令的跟踪状态
	public Boolean getTracked() {
		return tracked;
	}

	/**
	 * 用来返回指令的打印信息
	 * @return 指令的信息，类型为String
	 */
	// Require : null
	// Modified: null
	// Effect :用来返回指令的打印信息
	@Override
	public String toString() {
		return "Order : " + location.toString() + " to " + destination.toString() + " ";
	}

	/**
	 * 请求的构造方法，传入起点和终点的坐标和是否被跟踪来创建请求
	 *
	 * @param x 请求起点的x坐标
	 * @param y 请求起点的y坐标
	 * @param endx 请求终点的x坐标
	 * @param endy 请求终点的y坐标
	 * @param tracked 请求的跟踪状态
	 */
	// Require : 0<=x,y,enx,enxy <MAXCOUNT , tracked != null
	// Modified: location,destination,tracked,sp
	// Effect : 请求的构造方法，传入起点和终点的坐标和是否被跟踪来创建请求
	public Order(int x, int y, int endx, int endy, Boolean tracked) {
		location = Map.getMatrix()[x][y];
		destination = Map.getMatrix()[endx][endy];
		this.tracked = tracked;
		sp = Map.findSp(location, destination);
	}

	/**
	 * 请求的另一个种构造方法，通过直接传入起点和终点的结点来创建请求
	 * @param location 起点的结点
	 * @param destination 终点的结点
	 * @param tracked 请求的跟踪状态
	 */
	// Require : 起点和终点结点location和destination 跟踪状态tracked
	// Modified: destination,location,tracked,sp
	// Effect : 请求的另一个种构造方法，通过直接传入起点和终点的结点来创建请求
	public Order(Node location, Node destination, Boolean tracked) {
		this.destination = destination;
		this.location = location;
		this.tracked = tracked;
		sp = Map.findSp(location, destination);
	}

	/**
	 * 获得最短路径
	 * @return 该请求从起点到终点的最短路径，类型为ArrayDeque
	 */
	// Require : null
	// Modified: null
	// Effect : 获得最短路径sp
	public ArrayDeque<Node> getway() {
		return sp;
	}

	/**
	 * 获得该请求的终点
	 * @return 该请求的终点，类型为Node
	 */
	// Require: null
	// Modified: null
	// Effect : 获得该请求的终点
	public Node getDestination() {
		return destination;
	}

	/**
	 * 判断出租车t是否在响应窗口里
	 * @param t 出租车t
	 * @return 如果t在该请求的窗口中则返回true，否则返回false
	 */
	// Require : 出租者t ！= null
	// Modified: null
	// Effect : 判断出租车t是否在响应窗口里
	public boolean inWindow(Taxi t) {
		return Math.abs(t.getLocation().getPos().x - location.getPos().x) <= 2 && Math.abs(t.getLocation().getPos().y - location.getPos().y) <= 2;
	}

	/**
	 * 将车辆的编号添加到carnum中
	 * @param i 车辆的编号
	 */
	// Require : 0<=i<100
	// Modified: carnum
	// Effect : 将车辆的编号添加到carnum中
	public void addcar(int i) {
		carnum.add(i);
	}

	@Override
	public void run() {
		long starttime = System.currentTimeMillis();

		while (System.currentTimeMillis() - starttime < 3000) {
			for (Taxi t : taxis) {
				if (t.getDriverState() == DriverState.waiting && this.inWindow(t) && !carnum.contains(t.getcarid())) {
					t.gainCredit();
					this.addcar(t.getcarid());
				}
			}
		}

		if (carnum.size() == 0) {
			System.out.println("No Response : " + this.toString());
		}
		else {
			synchronized (taxis) {
				Iterator<Integer> itr = carnum.iterator();
				while (itr.hasNext()){
					if (taxis[itr.next()].getDriverState() != DriverState.waiting)
						itr.remove();
				}
				if (carnum.size() == 0){
					System.out.println("No Response : " + this.toString());
				}
				else {
					Iterator<Integer> it = carnum.iterator();
					int highestCreditCar = it.next();
					int highestCredit = taxis[highestCreditCar].getCredit();
					ArrayList<Integer> topcars = new ArrayList<>();
					if (tracked) {
						System.out.print("In " + this.toString() + "list: ");
						taxis[highestCreditCar].showstate();
					}
					while (it.hasNext()) {
						int temp = it.next();
						if (tracked) {
							System.out.print("In " + this.toString() + "list: ");
							taxis[temp].showstate();
						}
						int tempCredit = taxis[temp].getCredit();
						if (highestCredit < tempCredit) {
							highestCreditCar = temp;
							highestCredit = tempCredit;
						}
						else {
							it.remove();
						}
					}
					Iterator<Integer> it2 = carnum.iterator();
					while (it2.hasNext()) {
						int top = it2.next();
						if (taxis[top].getCredit() == highestCredit) {
							topcars.add(top);
						}
					}

					if (topcars.size() != 1) {
						int mincost = 9999;
						for (int i : topcars) {
							Taxi t = taxis[i];
							Node car = t.getLocation();
							Node custom = location;
							int cost = new Order(car, custom, false).getway().size();
							if (cost < mincost) {
								highestCreditCar = i;
							}
						}
					}
					Taxi t = taxis[highestCreditCar];
					Node car = t.getLocation();
					Node custom = location;
					Order way2C = new Order(car, custom, false);
					t.setOrder(way2C, this);
					System.out.println("Taxi : ID " + t.getcarid() + " take " + this.toString());

				}
				}
		}
	}

	///////////////just for test////////////////
//	public static void main(String[] args) {
//		Map m = new Map("G:\\QMDownload\\1.txt","G:\\QMDownload\\2.txt");
//		m.buildPointSet();
//		m.buildMatrix();
//		m.buildVertical();
//		Order o = new Order(1,2,3,4,false);
//		System.out.println(o.orderRepOk());
//	}
}



