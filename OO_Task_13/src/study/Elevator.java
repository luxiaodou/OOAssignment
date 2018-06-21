package study;

/**
 * Overview:
 * 电梯类，主要负责电梯相关数据的存储与获取
 * 定义了e_floor 变量用来储存当前电梯所在的楼层
 * 定义了state 变量用来储存当前电梯的运行状态
 * 创建了构造方法和RepOK方法
 * 创建了获得电梯所在楼层和电梯运动状态的方法
 * 创建了电梯根据运动状态进行运动的方法
 * 创建了计算电梯完成相应请求所需要的时间的方法
 */
public class Elevator implements Moveable {
    private int e_floor = 1;
    private State state = State.STAY;

	/**
     * 电梯的运动方法，实现自Moveable接口
     * 主要用来根据电梯当前的运动状态实现楼层的转移和运动状态的变化
     * @param st 电梯的运动状态
     */
    //Requires: st!= null  st类型为State
    //Modifies: this.e_floor this.state
    //Effects : 电梯的运动方法，实现自Moveable接口,
    //          主要用来根据电梯当前的运动状态实现楼层的转移和运动状态的变化
    @Override
    public boolean move(State st) {    //change the state
        boolean ret = false;
        if(st == State.STAY){
            state = State.STAY;
        }
        if(st == State.UP){
            state = State.UP;
            e_floor += 1;
        }
        if(st == State.DOWN){
            state = State.DOWN;
            e_floor -= 1;
        }
        ret = true;
        return ret;
    }

	/**
     * 电梯类的RepOK方法，用来获取当前对象不变式的值
     * @return 当前对象不变式的值
     */
    //Requires: this is initialized
    //Modifies: null
    //Effects : 获取当前对象不变式的值
    /*public Boolean EleRepOK() {
        if (this.e_floor <= 0 || this.e_floor > 10)
            return false;
        if (!((state == State.DOWN) || (state == State.STAY) || (state == State.UP)))
            return false;
        return true;
    }

	/**
     * 电梯类的构造方法
     */
    //Requires: null
    //Modifies: this.e_floor, this.state
    //Effects : 构造新的Elevator类，将楼层设置为1，状态设置为静止
    public Elevator() {
        this.e_floor = 1;
        this.state = State.STAY;
    }

	/**
     * 获取当前电梯状态的toString方法，重写自Object类的toString方法
     * @return 包含电梯状态信息的字符串
     */
    //Requires: this is initialized
    //Modifies: null
    //Effects : 获取当前电梯状态
    @Override
    public String toString() {
        String a = "(" + this.e_floor + "," + this.state + ",";
        return a;
    }

	/**
     * 获得电梯当前的楼层
     * @return 电梯当前楼层
     */
    //Requires: null
    //Modifies: null
    //Effects : 获得电梯当前的楼层
    public int getE_floor() {
        return e_floor;
    }

    /**
     * 获得电梯当前的运行状态
     * @return 电梯当前的运行状态
     */
    //Requires: null
    //Modifies: null
    //Effects : 获得电梯当前的运行状态
    public State getState() {
        return state;
    }

	/**
     * 结合电梯当前的楼层，计算运动所花费时间的方法
     * @param rq 需要完成的请求
     * @return 完成请求所需要的时间
     */
    //Requires: 需要完成的请求rq
    //Modifies: timeneed
    //Effects : 完成请求所需要的时间
    public double timeSpent(Request rq) {
        int destnation = rq.getDfloor();
        double timeneed = 0;
        if (e_floor > destnation) {
            timeneed = (e_floor - destnation) * 0.5 + 1;
            state = State.DOWN;
        } else if (e_floor < destnation) {
            timeneed = (destnation - e_floor) * 0.5 + 1;
            state = State.UP;
        } else {
            timeneed = 1;
        }
        e_floor = destnation;
        return timeneed;
    }
}
