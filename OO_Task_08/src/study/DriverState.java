package study;

/**
 * Created by luxia on 2016/4/20.
 */

/**
 * 出租车状态的枚举类
 */
public enum DriverState {
	serving,    //passenger in car
	onTheWay,   //no passenger in car, but have orders
	waiting,    //no passenger, no order
	stopped    //not working
}
