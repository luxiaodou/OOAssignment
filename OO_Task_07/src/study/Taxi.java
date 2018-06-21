package study;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static study.DriverState.serving;
import static study.DriverState.stopped;
import static study.DriverState.waiting;
import static study.Main.Starttime;

/**
 * Created by luxia on 2016/4/20.
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
	 * @param id 车辆id
	 */
	public Taxi(int id) {
		this.id = id;
		location = Map.getMatrix()[new Random().nextInt(80)][new Random().nextInt(80)];
		driverState = waiting;
		destination = null;
		credit = 0;
		way2Customer = null;
		realOrder = null;
		tracked = false;
	}

	/**
	 * 用来查看当前车辆的id
	 * @return 当前车辆的id
	 */
	public int getcarid() {
		return id;
	}

	public synchronized void setOrder(Order way2Customer, Order realOrder) {
		if (driverState == waiting) {
			this.way2Customer = way2Customer;
			this.realOrder = realOrder;
			driverState = DriverState.onTheWay;
			count = 0;
			tracked = realOrder.getTracked();

		}
	}

	public DriverState getDriverState() {
		return driverState;
	}

	/**
	 * 用来打印车辆的信息，格式为“时间+车号+车辆位置+车辆信用”
	 */
	public void showstate() {
		System.out.println("Time :" + (System.currentTimeMillis()-Starttime) + "ms. " + "Taxi " + id + " : location " + location.toString() + " state:" + driverState + " credit: " + credit);
	}

	/**
	 * 通过状态机来实现状态切换
	 * @throws InterruptedException 大概不会遇到问题吧=w=
	 */
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
					/*if (id == 10 && count < 100){
						System.out.print(count);
						showstate();
					}*/
					/*if (id == 10 & count == 100){
						System.out.println(System.currentTimeMillis()/1000);
					}*/
					sleep(100);
				}
			}

			else if (driverState == DriverState.onTheWay) {
				System.out.println("Taxi " + id + ": coming to passenger " + way2Customer.toString());
				destination = way2Customer.getDestination();
				ArrayDeque<Node> way = way2Customer.getway();
				while (way.size() != 0) {
					if (tracked) {
						System.out.println("Taxi " + id + ": moving " + location.toString());
					}
					location = way.poll();  //get & delete
					sleep(100);
				}
				driverState = stopped;
				way2Customer = null;
				serv = true;
			}

			else if (driverState == DriverState.serving) {
				System.out.println("Taxi " + id + ": serving " + realOrder.toString());
				destination = realOrder.getDestination();
				ArrayDeque<Node> way = realOrder.getway();
				while (way.size()!=0) {
					if (tracked) {
						System.out.println("Taxi " + id + ": moving " + location.toString());
					}
					location = way.poll();
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
	 * 出租车的闲逛状态，随机移动
	 */
	private void randommove() {
		ArrayList<Node> neibor = location.getNeighbor();
		location = neibor.get((int) (Math.random() * neibor.size()));
	}

	/**
	 * 当前车辆的信誉值+1，用于车辆存在于扫描队列中时
	 */
	public synchronized void gainCredit() {
		credit++;
	}

	/**
	 * 获取当前车辆的信誉
	 * @return 当前车辆的信誉，类型为int
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * 获取当前车辆的位置
	 * @return 当前车辆的位置，类型为Node
	 */
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
