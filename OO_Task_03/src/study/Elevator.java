package study;

public class Elevator implements Moveable {
    private int e_floor = 1;
    private State state = State.STAY;
    @Override
    public void move(State st) {    //change the state
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
    }

    public Elevator() {
        this.e_floor = 1;
        this.state = State.STAY;
    }

    @Override
    public String toString() {
        String a = "(" + this.e_floor + "," + this.state + ",";
        return a;
    }

    public int getE_floor() {
        return e_floor;
    }

    public State getState() {
        return state;
    }

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
