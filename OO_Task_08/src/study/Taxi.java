package study;


import java.util.ArrayDeque;
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
 */
public class Taxi extends Thread {
	private int id;
	private Node location;
	private Node destination;
	private DriverState driverState;
	private int credit;
	private Order way2Customer;
	private Order realOrder;
	private int count = 0;
	private Boolean tracked;

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
	// Modified: id,location,driverState,destination,credit,way2Customer,realOrder,tracked
	public Taxi(int id) {
		this.id = id;
		location = Map.getMatrix()[new Random().nextInt(MAXCOUNT)][new Random().nextInt(MAXCOUNT)];
		driverState = waiting;
		destination = null;
		credit = 0;
		way2Customer = null;
		realOrder = null;
		tracked = false;
	}

	/**
	 * 用来查看当前车辆的id
	 *
	 * @return 当前车辆的id，类型为int
	 */
	// Modified: null
	public int getcarid() {
		return id;
	}

	/**
	 * 用来设置请求
	 *
	 * @param way2Customer 从location到order起点的请求
	 * @param realOrder    实际的用户请求
	 */
	// Modified: way2Customer,realOrder,driverState,count,tracked
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
	//Modified: null
	public DriverState getDriverState() {
		return driverState;
	}

	/**
	 * 用来打印车辆的信息，格式为“时间+车号+车辆位置+车辆信用”
	 */
	//Modified: null
	public void showstate() {
		System.out.println("Time :" + (System.currentTimeMillis() - Starttime) + "ms. " + "Taxi " + id + " : location " + location.toString() + " state:" + driverState + " credit: " + credit);
	}

	/**
	 * 通过状态机来实现状态切换，这函数这么长我够不知道从哪儿开始说
	 * 详细的状态转换说明请看Readme，谢谢
	 *
	 * @throws InterruptedException 大概不会遇到问题吧=w=
	 */
	//Modified: wait serv driverState count nei moveTo min samedis finished dis RoadCounter
	public void serve() throws InterruptedException {

		boolean wait = false;
		boolean serv = false;
		while (true) {
			if (driverState == waiting) {
				if (way2Customer != null) {
					driverState = DriverState.onTheWay;
					count = 0;
					sleep(100);
				} else {
					randommove();
					count += 1;
					if (count == 200) {
						driverState = stopped;
						count = 0;
						wait = true;
					}
					/*if (id == 10 && count < 100){
						System.out.print(count);
						showstate();
					}*/
					/*if (id == 10 & count == 100){
						System.out.println(System.currentTimeMillis()/1000);
					}*/
					sleep(100);
				}
			} else if (driverState == DriverState.onTheWay) {
				System.out.println("Taxi " + id + ": coming to passenger " + way2Customer.toString());
				destination = way2Customer.getDestination();

				while (true) {
					ArrayList<Node> nei = new ArrayList<>();
					nei = location.getNeighbor();
					Node moveTo = null;
					int min = 99999;
					Boolean samedis = false;
					Boolean finished = false;
					for (Node n : nei) {
						int dis = findSp(n, destination).size();
						if (n.equals(destination)) {
							RoadCounter.gainTraffic(location, n);
							location = n;
							finished = true;
						}
						if (dis < min) {
							min = dis;
							moveTo = n;
						} else if (dis == min) {
							samedis = true;
						}
					}
					if (finished) {
						break;
					}
					if (!samedis) {
						RoadCounter.gainTraffic(location, moveTo);
						location = moveTo;  //无相同距离的边
					} else {                //有相同距离的边判断车流量
						int minflow = 9999;
						for (Node n : nei) {
							if (findSp(n, destination).size() == min) {
								int flow = RoadCounter.getTraffic(location, n);
								if (flow < minflow) {
									moveTo = n;
								}
							}
						}
						RoadCounter.gainTraffic(location, moveTo);
						location = moveTo;
					}
					if (tracked) {
						System.out.println("Taxi " + id + ": moving " + location.toString());
					}
					sleep(100);
				}

				driverState = stopped;
				way2Customer = null;
				serv = true;
			} else if (driverState == DriverState.serving) {
				System.out.println("Taxi " + id + ": serving " + realOrder.toString());
				destination = realOrder.getDestination();
				while (true) {
					ArrayList<Node> nei = new ArrayList<>();
					nei = location.getNeighbor();
					Node moveTo = null;
					int min = 99999;
					Boolean samedis = false;
					Boolean finished = false;
					for (Node n : nei) {
						int dis = findSp(n, destination).size();
						if (n.equals(destination)) {
							RoadCounter.gainTraffic(location, n);
							location = n;
							finished = true;
						}
						if (dis < min) {
							min = dis;
							moveTo = n;
						} else if (dis == min) {
							samedis = true;
						}
					}
					if (finished) {
						break;
					}
					if (!samedis) {
						RoadCounter.gainTraffic(location, moveTo);
						location = moveTo;  //无相同距离的边
					} else {                //有相同距离的边判断车流量
						int minflow = 9999;
						for (Node n : nei) {
							if (findSp(n, destination).size() == min) {
								int flow = RoadCounter.getTraffic(location, n);
								if (flow < minflow) {
									moveTo = n;
								}
							}
						}
						RoadCounter.gainTraffic(location, moveTo);
						location = moveTo;
					}
					if (tracked) {
						System.out.println("Taxi " + id + ": moving " + location.toString());
					}
					sleep(100);
				}
				driverState = stopped;
				credit += 3;
				System.out.println("Taxi " + id + ": moving " + destination.toString());
				System.out.println("Taxi " + id + ": serving finished");
				this.showstate();
				tracked = false;
				realOrder = null;
				wait = true;
			} else if (driverState == DriverState.stopped) {
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
	 * 修改版加权的随机移动函数
	 * 出租车的闲逛状态，随机移动，会自动选择车流量较小的边行走，若有多条最小边则随机行走
	 */
	//Modified: neibor moveTo minflow flow RoadCounter
	private void randommove() {
		ArrayList<Node> neibor = location.getNeighbor();
		ArrayList<Node> moveTo = new ArrayList<>();
		Node another = null;
		int minflow = 9999;
		for (Node nei : neibor) {
			if (RoadCounter.getTraffic(location, nei) == 0) {
				moveTo.add(nei);
			}
		}
		if (!moveTo.isEmpty()) {
			another = moveTo.get((int) (Math.random() * moveTo.size()));
		} else {
			for (Node nei : neibor) {
				int flow = RoadCounter.getTraffic(location, nei);
				if (flow < minflow) {
					minflow = flow;
					another = nei;
				}
			}
		}
		RoadCounter.gainTraffic(location, another);
		location = another;
	}

	/**
	 * 当前车辆的信誉值+1，用于车辆存在于扫描队列中时
	 */
	// Modified : credit
	public synchronized void gainCredit() {
		credit++;
	}

	/**
	 * 获取当前车辆的信誉
	 *
	 * @return 当前车辆的信誉，类型为int
	 */
	// Modified : null
	public int getCredit() {
		return credit;
	}

	/**
	 * 获取当前车辆的位置
	 *
	 * @return 当前车辆的位置，类型为Node
	 */
	// Modified : null
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
