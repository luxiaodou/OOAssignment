package study;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

import static study.DriverState.serving;
import static study.DriverState.stopped;
import static study.DriverState.waiting;
import static study.Main.Starttime;
import static study.Map.*;

/**
 * Created by luxia on 2016/5/20.
 */

/**
 * 超级出租车类，可以无视关闭道路行驶
 * 继承自Taxi类，拥有Taxi类所有的成员变量和方法
 * 定义了traceList用来保存每次接到请求的行驶轨迹
 * 包括了构造方法和RepOk方法
 * 包括了用来获得迭代器的getIt方法
 * 重写了父类的serve，desmove,randommove,showstate方法
 */
public class SuperTaxi extends Taxi {
	private ArrayList<ArrayList> traceList;

	/**
	 * SuperTaxi的构造方法，构造出的车辆id为传入值
	 *
	 * @param id 车辆id
	 */
	//Require: id != null
	//Modified: this
	//Effect : 构造方法
	public SuperTaxi(int id) {
		super(id);
		location = Map.getOrimap()[new Random().nextInt(MAXCOUNT)][new Random().nextInt(MAXCOUNT)];
		traceList = new ArrayList<>();
	}

	/**
	 * 测试SuperTaxi类对象的不变式的值的方法
	 *
	 * @return 当前SuperTaxi对象的不变式的值
	 */
	// Require : SuperTaxi is initialized
	// Modified : null
	// Effect : 获得当前对象的不变式的值
	public boolean stRepOK() {
		if (!super.taxiRepOk()) {
			return false;
		}
		else if (traceList == null) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * 获取当前出租车第num个请求的迭代器的方法
	 *
	 * @param num 所要访问的请求编号
	 * @return 包含有num请求的行驶路径
	 */
	//Require : 0<num<traceList.size()
	//Modified : null
	//Effect : 获取当前出租车第num个请求的迭代器的方法
	//          当num < 0 || num >= traceList.size()时将会报错并退出程序
	public ListIterator getIt(int num) {
		if (num < 0 || num >= traceList.size()) {
			System.out.println("There is no suitable iterator!");
			System.exit(0);
		}
		return traceList.get(num).listIterator();
	}

	/**
	 * 总的来说和父类几乎是一模一样的，只是修改了一些关键信息的输出
	 * 详细的状态转换说明请看Readme，谢谢
	 *
	 * @throws InterruptedException 象征性Catch，大概不会遇到问题吧=w=
	 */
	//Require : null
	//Modified: wait serv driverState count nei moveTo min samedis finished dis RoadCounter
	//Effect： 出租车的运行函数。通过参数来实现状态切换
	@Override
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
				System.out.println("Super-Taxi " + id + ": coming to passenger " + way2Customer.toString());
				destination = way2Customer.getDestination();
				desmove();
				driverState = stopped;
				way2Customer = null;
				serv = true;
			}
			else if (driverState == DriverState.serving) {
				System.out.println("Super-Taxi " + id + ": serving " + realOrder.toString());
				destination = realOrder.getDestination();
				desmove();
				driverState = stopped;
				credit += 3;
				System.out.println("Super-Taxi " + id + ": moving " + destination.toString() + " @" + (System.currentTimeMillis() - Starttime) + "ms");
				System.out.println("Super-Taxi " + id + ": serving finished");
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
	 * 有目标地移动方式
	 */
	// Require : location != null deastination != null
	// Modified : nei,min,finished,samedis,flow,RoadCounter
	// Effect : 有目标地移动方式
	protected void desmove() {
		while (true) {
			ArrayList<Node> nei = new ArrayList<>();
			nei = location.getOriNei();
			int min = 99999;
			Boolean samedis = false;
			Boolean finished = false;

			for (Node n : nei) {
				int dis = findSSp(n, destination).size();
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
					if (findSSp(n, destination).size() == min) {
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
				System.out.println("Super-Taxi " + id + ": moving " + location.toString() + " @" + (System.currentTimeMillis() - Starttime) + "ms");
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
	 * 与父类不同的是可以在封闭道路行驶，
	 */
	// Require : location != null
	//Modified: neibor moveTo minflow flow RoadCounter
	// Effect :　实现车辆的随机移动，出租车的闲逛状态，随机移动，会自动选择车流量较小的边行走，若有多条最小边则随机行走
	@Override
	protected void randommove() {
		ArrayList<Node> neibor = location.getOriNei();
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
	 * 用来打印车辆的信息，格式为“时间+车号+车辆位置+车辆信用”
	 */
	// Require : null
	// Modified: null
	// Effect ：  用来打印车辆的信息，格式为“时间+车号+车辆位置+车辆信用”
	@Override
	protected void showstate() {
		System.out.println("Time :" + (System.currentTimeMillis() - Starttime) + "ms. " + "Super-Taxi " + id + " : location " + location.toString() + " state:" + driverState + " credit: " + credit);
	}
}
