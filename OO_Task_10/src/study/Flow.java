package study;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by luxia on 2016/5/3.
 */

/**
 * 用来统计道路流量相关的类。定义了流量统计时间窗的长度refreshTime，
 * 通过保存以点对信息为key，流量为value的Hashmap traffic来保存当前的流量信息
 * 包含了构造方法和repOK方法
 * 包含了刷新，增加，获得流量的方法
 */
public class Flow extends Thread {
	/**
	 * 可以在此处自定义时间窗长度
	 */
	private static final int refreshTime = 100;
	private static HashMap<NodePair, AtomicInteger> traffic = new HashMap<>();

	/**
	 * 用来测试Flow类不变式的方法
	 * @return Flow类不变式的值
	 */
	// Require : null
	// Modified : null
	// Effect : 获得当前Flow对象不变式的值
	public boolean flowRepOk() {
		if (refreshTime < 0 || refreshTime > 100) return false;
		if (! (traffic instanceof HashMap)) return false;
		return true;
	}
	/**
	 * 用来清空所有在上一个时间窗口内被记录的数据
	 */
	// Require : null
	// Modified: traffic
	// Effect : 用来清空所有在上一个时间窗口内被记录的数据, traffic将被清空
	public static synchronized void refresh() {
		traffic.clear();
	}

	/**
	 * 用来获得交通流量的方法，输入为某条道路的两个端点，返回值为该条路的流量
	 *
	 * @param n1 道路的其中一个端点
	 * @param n2 道路的另一个端点
	 * @return 该道路此窗口内的流量
	 */
	// Require : n1 != null && n2 != null
	// Modified : np
	// Effect : 用来获得以n1 n2 为端点的边的交通流量
	public static synchronized int getTraffic(Node n1, Node n2) {
		NodePair np = new NodePair(n1, n2);
		if (!traffic.keySet().contains(np)) {
			return 0;
		} else return traffic.get(new NodePair(n1, n2)).get();
	}

	/**
	 * 用来增加某条道路的流量，输入为道路的两个端点
	 * @param n1 道路的其中一个端点
	 * @param n2 道路的另一个端点
	 */
	// Require : n1 != null && n2 != null
	// Modified : traffic
	// Effect : 用来增加以n1 n2 为端点的道路的流量
	public static synchronized void gainTraffic(Node n1, Node n2) {
		NodePair np = new NodePair(n1, n2);
		if (!traffic.keySet().contains(np)) {
			traffic.put(np, new AtomicInteger(1));
		} else {
			traffic.get(np).incrementAndGet();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				sleep(refreshTime);
				refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	////////////just for test//////////////.
//	public static void main(String[] args) {
//		Flow f = new Flow();
//		System.out.println(f.flowRepOk());
//	}
}

