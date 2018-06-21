package study;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by luxia on 2016/5/3.
 */

/**
 * 用来统计道路流量相关的类
 */
public class RoadCounter extends Thread {
	private static HashMap<NodePair, AtomicInteger> traffic = new HashMap<>();
	private static final int refreshTime = 100;


	/**
	 * 用来清空所有在上一个时间窗口内被记录的数据
	 */
	// Modified: traffic
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
	// Modified: np
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
	// Modified: traffic
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
}

