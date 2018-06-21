package study;


import java.util.ArrayList;
import java.util.Random;

import static study.DriverState.serving;
import static study.DriverState.stopped;
import static study.DriverState.waiting;
import static study.Main.Starttime;
import static study.Map.MAXCOUNT;
import static study.Map.findSp;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 出租车类，用来保存出租车的信息及相关方法
 * 其中id为出租车的编号
 * location和destination分别为当前出租车的位置和将要去的位置
 * moveTo为将要更新的location地址
 * driverState为当前出租车的位置
 * credit为当前出租车的信誉度
 * way2Customer和realOrder分别表示从接到请求到请求起点与从请求起点到请求终点的请求
 * count用来记录当前waiting状态维持时间的counter
 * tracked 表示自己有没有拿到跟踪的请求
 * headfor是现在车头的方向
 * 包括了构造方法和repOk方法
 * 包括了获得车辆id，运动状态，信誉，位置的方法
 * 包括了给车设定请求的方法
 * 包括车辆随机和有目的移动，等灯，增加信用，服务的方法
 */
public class Taxi extends Thread {
	private int id;
	private Node location;
	private Node destination;
	private Node moveTo = null;
	private DriverState driverState;
	private int credit;
	private Order way2Customer;
	private Order realOrder;
	private int count = 0;
	private Boolean tracked;
	private Direction headfor;

	/**
	 * 测试Taxi类对象的不变式的值
	 * @return 当前taxi对象的不变式的值
	 */
	// Require : Taxi is initialized
	// Modified : null
	// Effect : 获得当前对象的不变式的值
	public boolean taxiRepOk() {
		if (id < 0 || id >100) return false;
		if (!(location instanceof Node) || !(destination instanceof Node) || !(moveTo instanceof Node))
			return false;
		if (driverState == null) return false;
		if (credit < 0 || credit > Integer.MAX_VALUE) return false;
		if (tracked == null) return false;
		if (headfor == null) return false;
		return true;
	}

	/**
	 * 车辆开始运行，其实就是一个serve（）
	 */
	@Override
	public void run() { //waiting for finish
		try {
			serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Taxi的构造方法，构造出的车辆id为传入值
	 *
	 * @param id 车辆id
	 */
	// Require : 传入车辆的id
	// Modified: id,location,driverState,destination,credit,way2Customer,realOrder,tracked
	// Effect : 构造编号为id的车辆
	public Taxi(int id) {
		this.id = id;
		location = Map.getMatrix()[new Random().nextInt(MAXCOUNT)][new Random().nextInt(MAXCOUNT)];
		driverState = waiting;
		destination = null;
		credit = 0;
		way2Customer = null;
		realOrder = null;
		tracked = false;
		headfor = Direction.UP;
	}

	/**
	 * 用来查看当前车辆的id
	 *
	 * @return 当前车辆的id，类型为int
	 */
	// Require : null
	// Modified: null
	// Effect : 用来查看当前车辆的id
	public int getcarid() {
		return id;
	}

	/**
	 * 用来设置请求
	 *
	 * @param way2Customer 从location到order起点的请求
	 * @param realOrder    实际的用户请求
	 */
	// Require : way2Customer！=null  realOrder != null
	// Modified: way2Customer,realOrder,driverState,count,tracked
	// Effect : 用来设置请求
	public synchronized void setOrder(Order way2Customer, Order realOrder) {
		if (driverState == waiting) {
			this.way2Customer = way2Customer;
			this.realOrder = realOrder;
			driverState = DriverState.onTheWay;
			count = 0;
			tracked = realOrder.getTracked();
		}
	}

	/**
	 * 用来获得当前车辆的行驶状况
	 *
	 * @return 当前车辆的行驶状况，类型为DriverState
	 */
	// Require : null
	//Modified: null
	// Effect : 用来获得当前车辆的行驶状况
	public DriverState getDriverState() {
		return driverState;
	}

	/**
	 * 用来打印车辆的信息，格式为“时间+车号+车辆位置+车辆信用”
	 */
	// Require : null
	//Modified: null
	// Effect ：  用来打印车辆的信息，格式为“时间+车号+车辆位置+车辆信用”
	public void showstate() {
		System.out.println("Time :" + (System.currentTimeMillis() - Starttime) + "ms. " + "Taxi " + id + " : location " + location.toString() + " state:" + driverState + " credit: " + credit);
	}

	/**
	 * 通过状态机来实现状态切换，这函数这么长我够不知道从哪儿开始说
	 * 详细的状态转换说明请看Readme，谢谢
	 *
	 * @throws InterruptedException 大概不会遇到问题吧=w=
	 */
	//Require : null
	//Modified: wait serv driverState count nei moveTo min samedis finished dis RoadCounter
	//Effect： 出租车的运行函数。通过参数来实现状态切换
	public void serve() throws InterruptedException {

		boolean wait = false;
		boolean serv = false;
		while (true) {
			if (driverState == waiting) {
				if (way2Customer != null) {
					driverState = DriverState.onTheWay;
					count = 0;
					sleep(100);
				}
				else {
					randommove();
					count += 1;
					if (count == 200) {
						driverState = stopped;
						count = 0;
						wait = true;
					}
					sleep(100);
				}
			}
			else if (driverState == DriverState.onTheWay) {
				System.out.println("Taxi " + id + ": coming to passenger " + way2Customer.toString());
				destination = way2Customer.getDestination();
				desmove();
				driverState = stopped;
				way2Customer = null;
				serv = true;
			}
			else if (driverState == DriverState.serving) {
				System.out.println("Taxi " + id + ": serving " + realOrder.toString());
				destination = realOrder.getDestination();
				desmove();
				driverState = stopped;
				credit += 3;
				System.out.println("Taxi " + id + ": moving " + destination.toString() + " @" + (System.currentTimeMillis()-Starttime) + "ms");
				System.out.println("Taxi " + id + ": serving finished");
				this.showstate();
				tracked = false;
				realOrder = null;
				wait = true;
			}
			else if (driverState == DriverState.stopped) {
				//System.out.println("Taxi " + id + ": stopped");
				sleep(1000);
				if (wait) {
					driverState = waiting;
					wait = false;
				}
				if (serv) {
					driverState = serving;
					serv = false;
				}
			}
		}
	}

	/**
	 *有目标地移动方式
	 */
	// Require : location != null deastination != null
	// Modified : nei,min,finished,samedis,flow,RoadCounter
	// Effect : 有目标地移动方式
	private void desmove() {
		while (true) {
			ArrayList<Node> nei = new ArrayList<>();
			nei = location.getNeighbor();
			int min = 99999;
			Boolean samedis = false;
			Boolean finished = false;

			for (Node n : nei) {
				int dis = findSp(n, destination).size();
				if (n.equals(destination)) {
					Flow.gainTraffic(location, n);
					location = n;
					finished = true;
				}
				if (dis < min) {
					min = dis;
					moveTo = n;
				}
				else if (dis == min) {
					samedis = true;
				}
			}
			if (finished) {
				break;
			}
			if (samedis) {             //有相同距离的边判断车流量
				int minflow = 9999;
				for (Node n : nei) {
					if (findSp(n, destination).size() == min) {
						int flow = Flow.getTraffic(location, n);
						if (flow < minflow) {
							moveTo = n;
						}
					}
				}
			}
			try {
				seeLight();
			} catch (InterruptedException e) {
				System.out.println("Sleeping Error！");
			}
			Flow.gainTraffic(location, moveTo);
			location = moveTo;
			if (tracked) {
				System.out.println("Taxi " + id + ": moving " + location.toString() + " @" + (System.currentTimeMillis()-Starttime) + "ms");
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修改版加权的随机移动函数
	 * 出租车的闲逛状态，随机移动，会自动选择车流量较小的边行走，若有多条最小边则随机行走
	 */
	// Require : location != null
	//Modified: neibor moveTo minflow flow RoadCounter
	// Effect :　实现车辆的随机移动
	private void randommove() {
		ArrayList<Node> neibor = location.getNeighbor();
		ArrayList<Node> moveto = new ArrayList<>();
		int minflow = 9999;
		for (Node nei : neibor) {
			if (Flow.getTraffic(location, nei) == 0) {
				moveto.add(nei);
			}
		}
		if (!moveto.isEmpty()) {
			moveTo = moveto.get((int) (Math.random() * moveto.size()));
		}
		else {
			for (Node nei : neibor) {
				int flow = Flow.getTraffic(location, nei);
				if (flow < minflow) {
					minflow = flow;
					moveTo = nei;
				}
			}
		}
		try {
			seeLight();
		} catch (Exception e) {
			System.out.println("Sleeping Error");
		}
		Flow.gainTraffic(location, moveTo);
		location = moveTo;
	}

	/**
	 * 车辆根据红绿灯判断形式的方法
	 * @throws InterruptedException 中断异常
	 */
	// Require : null
	// Modified : dx dy
	// Effect : 根据车车辆的前进方向和红绿灯状态来决定如何行进
	private void seeLight() throws InterruptedException {
		if (location.getLight()) {
			double dx = moveTo.getPos().getX() - location.getPos().getX();
			double dy = moveTo.getPos().getY() - location.getPos().getY();

			if (headfor == Direction.UP && (dy !=0 || dx == -1) && (TrafficLight.getDir() == Direction.LEFT)) {
				sleep(TrafficLight.getTime());
			}
			else if (headfor == Direction.DOWN && (dy != 0 || dx == 1) && (TrafficLight.getDir() == Direction.LEFT)) {
				sleep(TrafficLight.getTime());
			}
			else if (headfor == Direction.LEFT && (dy == -1 || dx != 0) && (TrafficLight.getDir() == Direction.UP)) {
				sleep(TrafficLight.getTime());
			}
			else if (headfor == Direction.RIGHT && (dy == 1 || dx != 0) && (TrafficLight.getDir() == Direction.UP)) {
				sleep(TrafficLight.getTime());
			}
		}
	}

	/**
	 * 当前车辆的信誉值+1，用于车辆存在于扫描队列中时
	 */
	// Require : null
	// Modified : credit
	// Effect :  当前车辆的信誉值+1，用于车辆存在于扫描队列中时
	public synchronized void gainCredit() {
		credit++;
	}

	/**
	 * 获取当前车辆的信誉
	 *
	 * @return 当前车辆的信誉，类型为int
	 */
	// Require : null
	// Modified : null
	// Effect : 获取当前车辆的信誉
	public int getCredit() {
		return credit;
	}

	/**
	 * 获取当前车辆的位置
	 *
	 * @return 当前车辆的位置，类型为Node
	 */
	// Require : null
	// Modified : null
	// Effect : 获取当前车辆的位置
	public Node getLocation() {
		return location;
	}

	/////////////////////////////////just for test//////////////////////////////////////
	/*public static void main(String[] args) {
		Map map = new Map("A");
		map.buildPointSet();
		map.buildMatrix();

		Order realOrder = new Order(Map.getMatrix()[15][7], Map.getMatrix()[50][53],true);
		Taxi test = new Taxi(1);
		Node car = test.getLocation();
		Node custom = realOrder.getLocation();
		Order way2C = new Order(car, custom,false);
		test.setOrder(way2C,realOrder);
		try {
			test.serve();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}*/
}
