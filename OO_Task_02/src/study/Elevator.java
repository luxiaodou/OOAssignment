package study;

public class Elevator {
    private int floor = 1;
    private State state = State.UP;

    enum State {UP, DOWN, STAY}

    public Elevator() {
        this.floor = 1;
    }

    //考虑是否删除
    public Elevator(int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }

    public State getState() {
        return state;
    }

    public double move(Request rq) {
        int destnation = rq.getDfloor();
        double timeneed = 0;
        if (floor > destnation) {
            timeneed = (floor - destnation) * 0.5 + 1;
            state = State.DOWN;
        } else if (floor < destnation) {
            timeneed = (destnation - floor) * 0.5 + 1;
            state = State.UP;
        } else {
            timeneed = 1;
        }
        floor = destnation;
        return timeneed;
    }
}
