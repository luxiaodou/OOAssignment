package study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 请求队列类
 * 主要用于存储请求及完成请求队列的相关操作
 * 定义了一个Request类型的数组来保存请求
 * 定义了rindex来作为当前队列的指针
 * 包括ReqpOK方法
 * 包括从请求队列中获得、添加、删除请求的方法
 * 包括获得请求队列请求个数的方法
 * 包括设置请求done和inQueue属性的方法
 * 包括显示请求队列信息的方法
 */
public class RQueue {
	private Request[] requests = new Request[100000];
	private int rindex = 0;

	/**
	 * 电梯类的RepOK方法，用来获取当前对象不变式的值
	 *
	 * @return 当前对象不变式的值
	 */
	//Requires: this is initialized
	//Modifies: null
	//Effects : 获取当前对象不变式的值
	/*public boolean RepOK() {
		if (!(requests instanceof Request[])) {
			return false;
		}
		if (rindex < 0) {
			return false;
		}
		return true;
	}

	/**
	 * 获取第i个队列用的方法
	 *
	 * @param i 要取的第i个队列
	 * @return 请求中的第i个队列
	 */
	//Requires: 需要取出的请求的序号i
	//Modifies: null
	//Effects : 获取第i个队列用的方法
	public Request getRequest(int i) {
		return requests[i];
	}

	/**
	 * 向请求队列中添加请求的方法
	 *
	 * @param request 需要添加的请求
	 * @return 方法成功执行则返回true
	 */
	//Requires: 需要添加的请求request
	//Modifies: request rindex
	//Effects : 向队列中添加请求request
	public boolean addRequest(Request request) {
		boolean ret = false;
		requests[rindex++] = request;
		ret = true;
		return ret;
	}

	/**
	 * 删除请求队列中第index个请求的方法
	 *
	 * @param index 需要删除的请求序号
	 *              @return 方法成功执行则返回true
	 * @throws FormatException 格式错误
	 */
	//Requires: 需要删除的请求序号
	//Modifies: rindex request
	//Effects : 删除请求队列中第index个请求
	public boolean delRequest(int index) throws FormatException {
		boolean ret = false;
		if (index > rindex) {
			throw new FormatException("index larger than rindex!");
		}
		for (int i = index; i < rindex; i++) {
			requests[i] = requests[i + 1];
		}
		rindex -= 1;
		ret = true;
		return ret;
	}

	/**
	 * 得到rindex的方法
	 *
	 * @return rindex的值
	 */
	//Requires: null
	//Modifies: null
	//Effects : 得到rindex的方法
	public int getRindex() {
		return rindex;
	}

	/**
	 * 设置请求done属性的方法
	 *
	 * @param index 需要设置的请求的编号
	 * @return 方法成功执行则返回true
	 */
	//Requires: 需要设置的请求的编号
	//Modifies: request[index]
	//Effects : 将request中的第index个请求设置为已完成
	public boolean setDone(int index) {
		boolean ret = false;
		requests[index].Done();
		ret = true;
		return ret;
	}

	/**
	 * 设置请求inqueue属性的方法
	 *
	 * @param index 需要设置的请求的编号
	 *              @return 方法成功执行则返回true
	 */
	//Requires: 需要设置的请求的编号
	//Modifies: request[index]
	//Effects : 将request中的第index个请求设置为已入队
	public boolean setQueue(int index) {
		boolean ret = false;
		requests[index].setInQueue();
		ret = true;
		return ret;
	}

	/**
	 * 打印队列中的请求的详细信息
	 * @return 方法成功执行则返回true
	 */
	//Requires: null
	//Modifies: null
	//Effects : 打印队列中的请求的详细信息
	public boolean info() {
		boolean ret = false;
		System.out.print(requests[0].toString() + " (");
		for (int i = 1; i < rindex; i++) {
			System.out.print(requests[i].toString());
		}
		System.out.println(")");
		ret = true;
		return ret;
	}
}
