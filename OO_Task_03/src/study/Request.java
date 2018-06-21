package study;

public class Request {
    private int time;
    private int dfloor;
    private Moveable.State direction;
    private int type;
    private Boolean inQueue;
    private Boolean isDone;

    public Request() {
        this.isDone = false;
        this.inQueue = false;
        this.time = 0;
        this.dfloor = 1;
        this.type = 1;
        this.direction = Moveable.State.UP;
    }

    public Request(int dfloor, int time, int type, int dir) {
        this.isDone = false;
        this.time = time;
        this.dfloor = dfloor;
        this.inQueue = false;
        this.type = type;
        if (type == 0) direction = null;
        else if (dir == 1) direction = Moveable.State.UP;
        else direction = Moveable.State.DOWN;
    }

    public void Done() {
        isDone = true;
    }

    public Boolean isDone() {
        return isDone;
    }

    public Boolean inQueue() {
        return inQueue;
    }

    public void setInQueue() {
        inQueue = true;
    }

    public Moveable.State getDirection() {
        return direction;
    }

    public int getType() {
        return type;
    }

    public int getTime() {
        return time;
    }

    public int getDfloor() {
        return dfloor;
    }

    @Override
    public String toString() {
        String a = new String();
        if (type == 0) a = "(ER," + dfloor + "," + time + ")";
        else if (type == 1) a = "(FR," + dfloor + ',' + direction + "," + time + ")";
        return a;
    }
}
