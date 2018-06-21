package study;

import java.util.*;

import static study.Main.taxis;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 请求类，用来储存请求信息
 */
public class Order extends Thread {
	private Node location;
	private Node destination;
	private Boolean tracked;
	private ArrayDeque<Node> sp = new ArrayDeque<>();
	private Set<Integer> carnum = new HashSet<>();

	/**
	 * 用来获得改指令的跟踪状态
	 * @return 若被跟踪则返回True，否则返回false
	 */
	// Modified: null
	public Boolean getTracked() {
		return tracked;
	}

	/**
	 * 用来返回指令的打印信息
	 * @return 指令的信息，类型为String
	 */
	// Modified: null
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
	// Modified: location,destination,tracked,sp
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
	// Modified: destination,location,tracked,sp
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
	// Modified: null
	public ArrayDeque<Node> getway() {
		return sp;
	}

	/**
	 * 获得该请求的终点
	 * @return 该请求的终点，类型为Node
	 */
	// Modified: null
	public Node getDestination() {
		return destination;
	}

	/**
	 * 判断出租车t是否在响应窗口里
	 * @param t 出租车t
	 * @return 如果t在该请求的窗口中则返回true，否则返回false
	 */
	// Modified: null
	public boolean inWindow(Taxi t) {
		return Math.abs(t.getLocation().getPos().x - location.getPos().x) <= 2 && Math.abs(t.getLocation().getPos().y - location.getPos().y) <= 2;
	}

	/**
	 * 将车辆的编号添加到carnum中
	 * @param i 车辆的编号
	 */
	// Modified: carnum
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
}



