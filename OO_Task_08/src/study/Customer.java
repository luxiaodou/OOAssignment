package study;

import java.util.concurrent.ArrayBlockingQueue;

import static study.Main.taxis;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 测试线程类，主要的修改方法和测试线程都在此类中
 */
public class Customer extends Thread {
	private ArrayBlockingQueue<Order> orders;

	/**
	 * 用来构造测试线程
	 * @param orders 传入的是请求队列
	 */
	public Customer(ArrayBlockingQueue<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 用来输入请求，输入请求的起始地和终点并将其加入请求队列中
	 *
	 * @param x     请求起始点的 x 坐标
	 * @param y     请求起始点的 y 坐标
	 * @param endx  请求终点的 x 坐标
	 * @param endy  请求终点的 y 坐标
	 * @param track 是否追踪该请求
	 * @throws InterruptedException 只要少侠输入的请求合法就不会出错，嗯=w=
	 */
	//Modified： orders
	public void inputOrder(int x, int y, int endx, int endy, boolean track) throws InterruptedException {
		if (x >= 0 && x <= 79 && y >= 0 && y <= 79 && endx >= 0 && endx <= 79 && endy >= 0 && endy <= 79) {
			Order order1 = new Order(x, y, endx, endy, track);
			orders.put(order1);
			System.out.println("Input " + order1.toString());
		} else {
			System.out.println("Error Order input!");
		}
	}

	/**
	 * 用来查看现在所有出租车的状况，挨个输出，简单粗暴
	 */
	//Modified：null
	public void showAll() {
		for (Taxi t : taxis) {
			t.showstate();
		}
	}

	/**
	 * 用来打印所有driverstate状态的车辆的状况
	 *
	 * @param driverState 输入想要查看的车辆的状态，详细的类型请参见DriverState.java
	 */
	//Modified：null
	public void showAll(DriverState driverState) {
		for (Taxi t : taxis) {
			if (t.getDriverState() == driverState) {
				t.showstate();
			}
		}
	}

	/**
	 * 用来打印某一辆出租车的信息，注意出租车编号的合法性
	 *
	 * @param id 输入想要查看车辆的id，注意id的值应在0~99之间，否则不会有输出
	 */
	//Modified：null
	public void showOne(int id) {
		if (id >= 0 && id < 100) {
			taxis[id].showstate();
		}
	}

	/**
	 * 用来阻塞道路，可以实现道路的实时阻塞，当阻塞道路的总数超过5的时候将不会执行操作并提示错误。
	 * 注：如果输入的点的坐标不合法将会输出错误信息，不执行操作
	 *
	 * @param x   想要阻塞点的x坐标，应在0~79之间
	 * @param y   需要阻塞的点的y坐标，应在0~79之间
	 * @param dir 需要阻塞的道路相对于点的坐标，详细类型请参见Direction.java
	 * @see Direction
	 */
	//Modified：Map
	public void block(int x, int y, Direction dir) {
		if (x >= 0 && x <= 79 && y >= 0 && y <= 79) {
			Map.blocktheWay(x, y, dir);
		} else {
			System.out.println("Illegal point！ Please try again!");
		}
	}

	/**
	 * 用来实时恢复被阻塞的道路，如果原地图中并不含有该道路，即道路并不是因为block()方法阻断的
	 * 那么将不会执行操作并提示错误信息。
	 *
	 * @param x   需要恢复道路的一个端点的x坐标
	 * @param y   需要恢复道路的同一个端点的y坐标
	 * @param dir 需要恢复的道路相对于这个端点的位置，详细类型请参见Direction.java
	 * @see Direction
	 */
	//Modified：Map
	public void rebuild(int x, int y, Direction dir) {
		if (x >= 0 && x <= 79 && y >= 0 && y <= 79) {
			Map.rebuildTheWay(x, y, dir);
		} else {
			System.out.println("Illegal point! Please try again!");
		}
	}

	/**
	 * 用来打印道路车流量的方法，输入道路的两个端点，返回道路的车流量
	 * @param x 道路的一个端点的x坐标
	 * @param y 道路的同一个端点的y坐标
	 * @param anox 道路的另一个端点的x坐标
	 * @param anoy 道理的另一个端点的y坐标
	 */
	//Modified: n1 n2
	public void getFlow(int x, int y, int anox,int anoy) {
		if (x >= 0 && x <= 79 && y >= 0 && y <= 79 && anox >= 0 && anox <= 79 && anoy >= 0 && anoy <= 79) {
			Node n1 = Map.getMatrix()[x][y];
			Node n2 = Map.getMatrix()[anox][anoy];
			System.out.println("Traffic flow: from" + n1.toString()+ " to " + n2.toString() + " is " + RoadCounter.getTraffic(n1, n2));
		} else {
			System.out.println("Illegal point! Please try again!");
		}
	}

	/**
	 * 测试的主线程
	 */
	@Override
	public void run() {
		/////////////////////First Method -- write the input thread by yourself///////////////////
		try {
			/*showOne(0);*/
			block(15, 25, Direction.RIGHT);
			inputOrder(15, 25, 8, 15, true);
			inputOrder(0, 5, 0, 0, true);
			inputOrder(60, 60, 8, 15, true);
			inputOrder(15, 52, 65, 15, true);
			/*for (int i = 0; i < 30; i++) {
				showAll();
				sleep(1000);
			}
			sleep(9000);
			showAll(DriverState.waiting);*/
			/*for (int i = 0;i<1000;i++) {
				getFlow(0,0,0,1);
				sleep(50);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		//////////////////Second Method(not safe) -- using console to input orders////////////
		//////////////////////This is not the official way to test the program////////////////

		/*Scanner scanner = new Scanner(System.in);
		while (true) {
			String or = scanner.nextLine();
			String[] or4 = or.split(" ");
			int[] param = new int[4];
			param[0] = Integer.parseInt(or4[0]);
			param[1] = Integer.parseInt(or4[1]);
			param[2] = Integer.parseInt(or4[2]);
			param[3] = Integer.parseInt(or4[3]);
			Order order1 = new Order(Map.getMatrix()[param[0]][param[1]], Map.getMatrix()[param[2]][param[3]],true);
			try {
				orders.put(order1);
				System.out.println("Input Order " + order1.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}

}
