package study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by luxia on 2016/3/16.
 */

/**
 * Overview: a little stupid 电梯调度类
 * 根据输入的时间对于请求进行合理排序后在进行电梯调度
 * 定义了一个用来保存请求的请求去队列
 * 定义了最大楼层数
 * 定义了当前时间和已经过去的时间
 * 包含了reoOK方法，调度方法和生成请求队列的方法
 */
public class ALS_Schedule extends FoolSchedule {
	private RQueue rQueue = new RQueue();
	private static int MAX_FLOOR = 10;
	private double t = 0;
	private double timenow = 0;

	/**
	 * 调度类的RepOK方法，用来获取当前对象不变式的值
	 *
	 * @return 当前对象不变式的值
	 */
	//Requires: this is initialized
	//Modifies: null
	//Effects : 获取当前对象不变式的值
	/*public boolean ALSRepOK() {
		if (t < 0 || timenow < 0) {
			return false;
		}
		if (MAX_FLOOR < 0) {
			return false;
		}
		if (!(rQueue instanceof RQueue)) {
			return false;
		}
		return true;
	}

	/**
	 * 实现有点傻调度的方法
	 *
	 * @throws FormatException 如果输入的格式错误可能会抛出自定义异常
	 */
	//Requires: rQueue is built correctly
	//Modifies: t timenow
	//Effects : 对电梯进行A Little Stupid调度
	public boolean dispatch() throws FormatException {
		Elevator eva1 = new Elevator();
		RQueue follow = new RQueue();
		int headreq = 0;
		int timeBuffer = 0;
		int e_floor = 1;
		boolean ret = false;
		Elevator.State e_state = Elevator.State.STAY;
		Request primary = new Request();
		int portering = 0;

		for (t = 0; rQueue.getRindex() != 0 || follow.getRindex() != 0; t += 0.5) {
			if (portering > 0) {
				portering--;
				continue;
			}

			//build/update the "follow" queue, which contains primary and carrying request
			if (follow.getRindex() == 0) {
				if (rQueue.getRequest(headreq).getTime() > t) {
					continue;
				}
				follow.addRequest(rQueue.getRequest(headreq));  //when follow is empty, add the most recent one
				rQueue.setQueue(headreq);
				rQueue.delRequest(headreq);
			}
			primary = follow.getRequest(0);
			Request carry = new Request();  //choose proper one to add to the follow queue
			e_state = eva1.getState();  //获得电梯当前这0.5秒内的运动状态
			e_floor = eva1.getE_floor();


			for (int i = 0; i < rQueue.getRindex(); i++) {
				carry = rQueue.getRequest(i);
				if (carry.getTime() > t) {
					break; //ignore the "future" requests
				}
				if (!carry.inQueue()) {  //According to PPT, create the follow queue

					if (carry.getType() == 1) { //Type-FR
						if ((primary.getDfloor() >= e_floor && carry.getDirection() == Moveable.State.UP) || (primary.getDfloor() <= e_floor && carry.getDirection() == Moveable.State.DOWN)) {
							if (e_state != Moveable.State.STAY) {
								if ((carry.getDirection() == Moveable.State.UP) && (carry.getDfloor() <= primary.getDfloor() && (carry.getDfloor() > e_floor))) {
									follow.addRequest(carry);
									System.out.println("New carrying order:\t\t\t" + primary.toString() + " (" + carry.toString() + ")");
									rQueue.setQueue(i);
									rQueue.delRequest(i--);
								}
								else if ((carry.getDirection() == Moveable.State.DOWN) && (carry.getDfloor() >= primary.getDfloor()) && (carry.getDfloor() < e_floor)) {
									follow.addRequest(carry);
									System.out.println("New carrying order:\t\t\t" + primary.toString() + " (" + carry.toString() + ")");
									rQueue.setQueue(i);
									rQueue.delRequest(i--);
								}
							}
							else if (e_state == Moveable.State.STAY) {
								if ((carry.getDirection() == Moveable.State.UP) && (carry.getDfloor() <= primary.getDfloor()) && (primary.getDfloor() >= e_floor) || (carry.getDirection() == Moveable.State.DOWN) && (carry.getDfloor() >= primary.getDfloor()) && (primary.getDfloor() <= e_floor)) {
									follow.addRequest(carry);
									System.out.println("New carrying order:\t\t\t" + primary.toString() + " (" + carry.toString() + ")");
									rQueue.setQueue(i);
									rQueue.delRequest(i--);
								}
							}
						}

					}
					else if (carry.getType() == 0) {  //Type-ER
						if (e_state == Moveable.State.UP && carry.getDfloor() > e_floor) {
							follow.addRequest(carry);
							System.out.println("New carrying order:\t\t\t" + primary.toString() + " (" + carry.toString() + ")");
							rQueue.setQueue(i);
							rQueue.delRequest(i--);
						}
						if (e_state == Moveable.State.DOWN && carry.getDfloor() < e_floor) {
							follow.addRequest(carry);
							System.out.println("New carrying order:\t\t\t" + primary.toString() + " (" + carry.toString() + ")");
							rQueue.setQueue(i);
							rQueue.delRequest(i--);
						}
						if (e_state == Moveable.State.STAY) {
							if (primary.getDfloor() >= e_floor && carry.getDfloor() >= e_floor || primary.getDfloor() <= e_floor && carry.getDfloor() <= e_floor) {
								follow.addRequest(carry);
								System.out.println("New carrying order:\t\t\t" + primary.toString() + " (" + carry.toString() + ")");
								rQueue.setQueue(i);
								rQueue.delRequest(i--);
							}
						}
					}
				}
			}
			//choose the request to complete right now,whose name is primary
			primary = follow.getRequest(0);//follow[0] is always the primary request,finish it first

			//finish carryings
			for (int i = 1; i < follow.getRindex(); i++) {  //start from the first carry req
				Request carryBuffer = follow.getRequest(i);
				if (carryBuffer.isDone()) {
					continue;
				}
				if (eva1.getE_floor() == carryBuffer.getDfloor()) {
					if (carryBuffer.getDfloor() != primary.getDfloor()) {
						System.out.println("Carrying request complete:" + "\t" + "(" + eva1.getE_floor() + "," + eva1.getState() + "," + t + ")");
					}
					for (int j = 1; j < follow.getRindex(); j++) //delete the request in the same floor
						if (e_floor == follow.getRequest(j).getDfloor()) {
							follow.setDone(j);
						}
					portering = 2;
				}
			}

			//finish primary
			if (primary.getDfloor() > e_floor) {
				eva1.move(Moveable.State.UP);
			}
			else if (primary.getDfloor() < e_floor) {
				eva1.move(Moveable.State.DOWN);
			}
			else if (primary.getDfloor() == eva1.getE_floor()) { //need to open the door
				follow.setDone(0);
				follow.getRequest(0);
				System.out.print("Primary request complete : \t");
				System.out.println(eva1.toString() + t + ")");  //print state info
				System.out.print("Carrying status : \t\t\t");
				follow.info();                                  //print carry info
				System.out.println();
				for (int bf = follow.getRindex() - 1; bf >= 0; bf--) {
					if (follow.getRequest(bf).isDone()) {
						follow.delRequest(bf);
					}
				}
				eva1.move(Moveable.State.STAY);
				portering = 1;
			}

			//after finishing the operation in this time section, update the state of requests' queue, output the finished ones
			//redefine headreq,the first 'notDone' request in the rQueue
			for (int i = 0; i < rQueue.getRindex(); i++) {
				if (!rQueue.getRequest(i).inQueue()) {
					headreq = i;
					break;
				}
			}
		}
		ret = true;
		return ret;
	}

	/**
	 * 请求读入的方法
	 *
	 * @throws IOException     可能会抛出IO异常
	 * @throws FormatException 可能会爆出自定义的格式异常，如果出现此类型异常，则为输入格式错误
	 */
	//Requires: 控制台输入
	//Modifies: rQueue bf
	//Effects : 读入请求并将其放入请求队列
	@Override
	public boolean bulidQueue() throws IOException, FormatException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		int ch;
		int floor = 1;
		int time = 0;
		int dir = 1;
		int type = 1;   //0 - ER;1 - FR
		int needenter = 0;
		boolean ret = false;

		ch = bf.read();
		while (ch != -1) {
			if (ch == ' ' || ch == '\t') {
				ch = bf.read();
				continue;
			}
			if (ch == '(' && needenter == 0) {
				needenter = 1;
				Boolean knowntype = false;  //完成类型输入的标识符
				Boolean floorerror = false;  //楼层输入是否有错的表示符
				Boolean finishdir = false;  //完成方向输入的标识符
				Boolean finishnum = false;
				while ((ch = bf.read()) != ')') {
					if (ch == ' ' || ch == '\t') {
						continue;
					}
					if (ch == 'F' || ch == 'E') {
						if (ch == 'E') {
							finishdir = true;
							type = 0;
						}
						else {
							finishdir = false;
							type = 1;
						}
						while ((ch = bf.read()) == ' ' || ch == '\t') continue;
						if (ch == 'R') {
							knowntype = true;
							while ((ch = bf.read()) == ' ' || ch == '\t') continue;
							if (ch == ',') {
								Boolean kong = true;
								int sign = 1;
								int signnum = 0;
								Boolean moresign = true;
								Boolean err = false;
								int temp = 0;
								while ((ch = bf.read()) != ',') {
									if (ch == ' ' || ch == '\t') {
										continue;
									}
									else if (ch == '+' && moresign) {
										moresign = false;
									}
									else if (ch == '-') {
										signnum++;
										err = true;
									}
									else if (ch >= '0' || ch <= '9') {
										moresign = false;
										kong = false;
										temp = temp * 10 + ch - '0';
										if (temp > 10) {
											err = true;
										}
									}
									else {
										err = true;
										break;
									} //throw new FormatException("楼层部分输入错误！");
								}
								floorerror = (signnum > 1) || kong || (temp > MAX_FLOOR) || moresign || err || (temp == 0);
								if (floorerror) {
									break; // throw new FormatException("楼层部分输入错误！");
								}
								else {
									floor = temp;
									temp = 0;
									if (!finishdir) {
										while ((ch = bf.read()) != ',') {
											if (ch == ' ' || ch == '\t') {
												continue;
											}
											if (finishdir) {
												break;
											}
											if (ch == 'U') {
												while ((ch = bf.read()) == ' ' || ch == '\t') continue;
												if (ch == 'P') {
													if (floor == 10) {
														break;
													}
													dir = 1;    //1 means up
													finishdir = true;
												}
												else {
													break; // throw new FormatException("方向输入错误！");
												}
											}
											else if (ch == 'D') {
												while ((ch = bf.read()) == ' ' || ch == '\t') continue;
												if (ch == 'O') {
													while ((ch = bf.read()) == ' ' || ch == '\t') continue;
													if (ch == 'W') {
														while ((ch = bf.read()) == ' ' || ch == '\t') continue;
														if (ch == 'N') {
															if (floor == 1) {
																break;
															}
															dir = -1;   //-1 means down
															finishdir = true;
														}
														else {
															break; //throw new FormatException("方向输入错误！");
														}
													}
													else {
														break; //throw new FormatException("方向输入错误！");
													}
												}
												else {
													break; //throw new FormatException("方向输入错误！");
												}
											}
											else {
												break; //throw new FormatException("方向输入错误！");
											}
										}
									}
									if (!finishdir) {
										break;
									}

									while ((ch = bf.read()) != ')') {
										if (ch == ' ' || ch == '\t') {
											continue;
										}
										else if (ch >= '0' && ch <= '9') {
											temp = temp * 10 + ch - '0';
											time = temp;
											finishnum = true;
											if (temp > 100000000) {
												break;
											}
										}
										else {
											break; // throw new FormatException("时间输入错误！");
										}
									}
									if (finishnum) {
										break;
									}
									else {
										throw new FormatException("时间输入错误！");
									}

								}
							}
							else {
								break; //throw new FormatException("请求类型错误！");
							}
						}
						else break; //throw new FormatException("请求类型错误！");
					}
					else break; //throw new FormatException("请求类型错误！");
				}
				if (ch == ')' && knowntype && !floorerror && finishdir && finishnum) {
					if (time < timenow) throw new FormatException("请输入按时间排序的请求序列！");
					if (time != 0 && rQueue.getRindex() == 0) throw new FormatException("第一个请求的时间要求为0！");

					timenow = time;
					Request rq = new Request(floor, time, type, dir);
					rQueue.addRequest(rq);


				}
			}
			else if (ch == 10) {
				needenter = 0;
				if ((ch = bf.read()) == 'r') if ((ch = bf.read()) == 'u') if ((ch = bf.read()) == 'n') break;
			}
			else {
				ch = bf.read();
			}
		}
		ret = true;
		return ret;
	}

}
