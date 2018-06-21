package study;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 出租车状态的枚举类，包括了等待中(waiting),准备服务(onTheWay),正在服务(serving)和停止服务(stopped)四个状态
 */
public enum DriverState {
	serving,    //passenger in car
	onTheWay,   //no passenger in car, but have orders
	waiting,    //no passenger, no order
	stopped    //not working
}
