package study;

import java.util.concurrent.ArrayBlockingQueue;

import static study.DriverState.onTheWay;
import static study.DriverState.waiting;
import static study.Main.taxis;

/**
 * Created by luxia on 2016/4/20.
 */
public class Customer extends Thread {
	private ArrayBlockingQueue<Order> orders;

	public Customer(ArrayBlockingQueue<Order> orders) {
		this.orders = orders;
	}

	/**
	 *用来输入请求
	 * @param x 请求起始点的 x 坐标
	 * @param y 请求起始点的 y 坐标
	 * @param endx 请求终点的 x 坐标
	 * @param endy 请求终点的 y 坐标
	 * @param track 是否追踪该请求
	 * @throws InterruptedException 只要少侠输入的请求合法就不会出错，嗯=w=
	 */
	public void inputOrder(int x, int y, int endx, int endy, boolean track) throws InterruptedException {
		if (x >= 0 && x <= 79 && y >= 0 && y <= 79 && endx >= 0 && endx <= 79 && endy >= 0 && endy <= 79) {
			Order order1 = new Order(x, y, endx, endy, track);
			orders.put(order1);
			System.out.println("Input " + order1.toString());
		}
		else {
			System.out.println("Error Order input!");
		}
	}

	/**
	 * show 用来查看现在所有出租车的状况，挨个输出，简单粗暴
	 */
	public void showAll() {
		for (Taxi t : taxis) {
			t.showstate();
		}
	}

	/**
	 * show 用来查看所有driverstate状态的车辆的状况
	 * @param driverState 输入想要查看的车辆的状态
	 * return null
	 */
	public void showAll(DriverState driverState) {
		for (Taxi t : taxis) {
			if (t.getDriverState() == driverState) {
				t.showstate();
			}
		}
	}

	/**
	 * show 用来打印某一辆出租车的信息
	 * @param id 输入想要查看车辆的id，注意id的值应在0~99之间，否则不会有输出
	 */
	public void showOne(int id) {
		if (id >= 0 && id < 100) {
			taxis[id].showstate();
		}
	}

	/**
	 * 测试的主线程
	 */
	@Override
	public void run() {
		/////////////////////First Method -- write the input thread by yourself///////////////////
		try {
			showOne(0);
			inputOrder(0, 0, 79, 79, true);
			inputOrder(15, 25, 34, 15, true);
			inputOrder(5, 8, 6, 7, true);
			inputOrder(2, 4, 7, 61, true);
			inputOrder(15, 7, 51, 3, true);
			inputOrder(12, 34, 41, 41, true);
			inputOrder(69, 64, 52, 74, true);
			sleep(3000);
			/*inputOrder(0, 0, 79, 79, true);
			inputOrder(15, 25, 34, 15, true);
			inputOrder(5, 8, 6, 7, false);
			inputOrder(2, 4, 7, 61, true);
			inputOrder(15, 7, 51, 3, false);
			inputOrder(12, 34, 41, 41, false);
			inputOrder(0, 0, 79, 79, false);
			inputOrder(69, 64, 52, 74, false);*/
			sleep(9000);
			showAll(DriverState.stopped);
//			showAll(waiting);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//////////////////Second Method(not safe) -- using console to input orders////////////
		////This is not an official way to test the program, but a little bit convenience/////

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
