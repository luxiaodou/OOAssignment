package study;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by luxia on 2016/4/20.
 */
public class Main {
	public static final Taxi[] taxis = new Taxi[100];
	public static final long Starttime = System.currentTimeMillis();

	public static void main(String[] args) {
		Map map;
		ArrayBlockingQueue<Order> orders = new ArrayBlockingQueue<Order>(300);
		Customer customer = new Customer(orders);


		///////////////////////////Please input your filepath below/////////////////////
		String dir = "G:\\QMDownload\\1.txt";

		map = new Map(dir); //build map from file
		map.buildPointSet();
		map.buildMatrix();


		for (int i = 0; i < 100; i++) {
			taxis[i] = new Taxi(i);
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
