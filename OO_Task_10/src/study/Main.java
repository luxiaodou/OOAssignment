package study;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 主类，也是程序的入口
 */
public class Main {
	public static final int taxiNum = 70;
	public static final Taxi[] taxis = new Taxi[100];
	public static final long Starttime = System.currentTimeMillis();

	public static void main(String[] args) {
		Map map;
		ArrayBlockingQueue<Order> orders = new ArrayBlockingQueue<Order>(300);
		Customer customer = new Customer(orders);


		///////////////////////////Please input your filepath below/////////////////////
		String dir = "G:\\QMDownload\\1.txt";
		String verticalDir = "G:\\QMDownload\\2.txt";

		map = new Map(dir,verticalDir); //build map from file
		map.buildPointSet();
		map.buildMatrix();
		map.buildVertical();

		Flow rc = new Flow();
		rc.start();

		TrafficLight tl = new TrafficLight();
		tl.start();

		/*System.out.println(customer.customerRepOk());
		System.out.println(rc.flowRepOk());
		System.out.println(map.mapRepOk());*/

		if (taxiNum > 100) {
			System.out.println("taxi number Error!");
			System.exit(0);
		}

		for (int i = 0; i < 100; i++) {
			if (i < taxiNum) {
				taxis[i] = new Taxi(i);
			}
			else {
				taxis[i] = new SuperTaxi(i);
			}
			taxis[i].start();
			taxis[i].showstate();   //comment to disable init-state output
		}

		customer.start();

		while (true) {
			if (!orders.isEmpty()) {
				Order or = orders.poll();
				or.start();
			}
			else {
				try {
					Thread.sleep((long) (Math.random() * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
