package study;

/**
 * Created by luxiaodou on 2016/5/11.
 */

/**
 * 红绿灯类。负责整个地图的红绿灯控制
 * dir用来定义现在能够通行的方向
 * time记录上次变灯的时间
 * 包含repOk方法，获得时间和方向的方法，变换方法的方法
 */
public class TrafficLight extends Thread {
	private static Direction dir = Direction.UP;
	private static long time = 0;

	/**
	 * 测试TrafficLight类的不变式的值
	 * @return 测试对象不变式的值
	 */
	//Require : null
	//Modified : null
	//Effect : 测试对象不变式的值
	public boolean tlRepOk() {
		if (dir == null || dir == Direction.RIGHT || dir == Direction.DOWN) return false;
		if (time < 0 || time > Integer.MAX_VALUE) return false;
		return true;
	}

	/**
	 * 用来改变通行方向的方法
	 */
	// Requrie ： null
	// Modified ： this.dir
	// Effect :　用来改变通行的方向
	private static void changeDir() {
		if (dir == Direction.UP) {
			dir = Direction.LEFT;
		}
		else {
			dir = Direction.UP;
		}
	}

	/**
	 * 获得下次变灯时间的方法
	 * @return 距离下次变灯的时间
	 */
	// Require :　null
	// Modified : null
	// Effect :　获得下次变灯时间的方法
	public static long getTime(){
		return System.currentTimeMillis() - time;
	}

	/**
	 * 获得当前允许同行方向的方法
	 * @return 当前允许同性的方向
	 */
	//Require : null
	// Modified : null
	// Effect : 获得当前允许同行方向
	public static Direction getDir() {
		return dir;
	}

	@Override
	public void run() {
		while (true) {
			try {
				time = System.currentTimeMillis();
				sleep(300);
				changeDir();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//////////////////just for test///////////////
	public static void main(String[] args) {
		TrafficLight tl = new TrafficLight();
		System.out.println(tl.tlRepOk());
	}
}
