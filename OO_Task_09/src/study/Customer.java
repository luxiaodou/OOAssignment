package study;

import java.util.concurrent.ArrayBlockingQueue;

import static study.Main.taxis;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 测试线程类，包含有构造传入的请求队列orders，主要的修改方法和测试线程均在此类中包含构造方法和repok方法，包含了输入请求的方法、显示车辆信息的方法、阻塞和修复道路的方法，获得道路流量的方法
 */
public class Customer extends Thread {
	private ArrayBlockingQueue<Order> orders;

	/**
	 * customer类的不变式
	 * @return 正常情况下应始终未true
	 */
	// Require : null
	// Modified : null
	// Effect ： 不变式的值
	public boolean customerRepOk() {
		return (orders instanceof ArrayBlockingQueue);
	}

	/**
	 * 用来构造测试线程
	 * @param orders 传入的是请求队列
	 */
	//Require : ArrayBlockingQueue 格式的 orders
	//Modified : this.orders
	//Effect : 构造传入orders
	public Customer(ArrayBlockingQueue<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 用来输入请求，将以(x,y) 为起点，(endx,endy)为终点，跟踪状态为track的请求加入到请求队列中
	 * 当输入不合法时将不会正常工作
	 *
	 * @param x     请求起始点的横坐标
	 * @param y     请求起始点的纵坐标
	 * @param endx  请求终点的横坐标
	 * @param endy  请求终点的纵坐标
	 * @param track 是否追踪该请求
	 * @throws InterruptedException 只要少侠输入的请求合法就不会出错，嗯=w=
	 */
	//Require : 0 <= x,y,endx,endy <= 79, tracked = false or true
	//Modified： orders
	//Effect : 用来输入请求,将以(x,y) 为起点，(endx,endy)为终点，跟踪状态为track的请求加入到请求队列中
				//当输入不合法时将不会正常工作并抛出异常
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
	//Require: null
	//Modified： null
	//Effect : 用来查看现在所有出租车的状况，挨个输出，简单粗暴
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
	//Require : 想要查询的出租车状态 driverState,类型是DriverState
	//Modified：null
	//Effect : 用来打印所有driverstate状态的车辆的状况
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
	//Require : 想要查询车辆的id号, 0<=id <= 100
	//Modified：null
	//Effect : 查询车辆号为id的车辆的运动状态
	public void showOne(int id) {
		if (id >= 0 && id < 100) {
			taxis[id].showstate();
		}
	}

	/**
	 * 用来阻塞道路，可以实现道路的实时阻塞，当阻塞道路的总数超过5的时候将不会执行操作并提示错误。
	 * 注：如果输入的点的坐标不合法将会输出错误信息，不执行操作
	 *
	 * @param x   想要阻塞点的横坐标，应在0~79之间
	 * @param y   需要阻塞的点的纵坐标，应在0~79之间
	 * @param dir 需要阻塞的道路相对于点的坐标，详细类型请参见Direction.java
	 * @see Direction
	 */
	//Require : 0<=x,y<=79 , Direction 类型的dir
	//Modified：Map
	//Effect : 用来实时阻塞道路，阻塞的道路为以(x,y)为端点，指向dir方向的道路
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
	 * @param x   需要恢复道路的一个端点的横坐标
	 * @param y   需要恢复道路的同一个端点的纵坐标
	 * @param dir 需要恢复的道路相对于这个端点的位置，详细类型请参见Direction.java
	 * @see Direction
	 */
	//Require : 0<=x,y<=79 , Direction 类型的dir
	//Modified：Map
	//Effect : 用来实时恢复被阻塞道路，恢复的道路为以(x,y)为端点，指向dir方向的道路
	public void rebuild(int x, int y, Direction dir) {
		if (x >= 0 && x <= 79 && y >= 0 && y <= 79) {
			Map.rebuildTheWay(x, y, dir);
		} else {
			System.out.println("Illegal point! Please try again!");
		}
	}

	/**
	 * 用来打印道路车流量的方法，输入道路的两个端点，返回道路的车流量
	 * @param x 道路的一个端点的横坐标
	 * @param y 道路的同一个端点的纵坐标
	 * @param anox 道路的另一个端点的横坐标
	 * @param anoy 道理的另一个端点的纵坐标
	 */
	//Require : 0<=x,y,anox,anoy<=79
	//Modified: n1 n2
	//Effect : 用来打印以(x,y),(anox,anoy)为端点的道路的车流量，非正确坐标将会输出错误提示
	public void getFlow(int x, int y, int anox,int anoy) {
		if (x >= 0 && x <= 79 && y >= 0 && y <= 79 && anox >= 0 && anox <= 79 && anoy >= 0 && anoy <= 79) {
			Node n1 = Map.getMatrix()[x][y];
			Node n2 = Map.getMatrix()[anox][anoy];
			System.out.println("Traffic flow: from" + n1.toString()+ " to " + n2.toString() + " is " + Flow.getTraffic(n1, n2));
		} else {
			System.out.println("Illegal point! Please try again!");
		}
	}

	/**
	 * 测试的主线程
	 */
	@Override
	public void run() {
		/////////////////////write the test thread by yourself///////////////////
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
	}
}
