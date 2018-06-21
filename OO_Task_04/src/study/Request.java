package study;

public class Request {
    private double time;
    private int destination;
    private Moveable.State direction;
    private int type;
    private Boolean inQueue;
    private Boolean isDone;
    private int elenum;

    public int getElenum() {
        return elenum;
    }

    public Request() {
        this.isDone = false;
        this.inQueue = false;

        this.elenum = 0;
        this.time = 0;
        this.destination = 1;
        this.type = 1;
        this.direction = Moveable.State.UP;
    }

    public Request(int type, int elenum, int dfloor, int dir, double time) {
        this.isDone = false;
        this.type = type;
        this.elenum = elenum;
        this.destination = dfloor;
        this.direction = dir == 1 ? Moveable.State.UP : dir == 0 ? Moveable.State.DOWN : null;
        this.time = time;
    }

    public Request(int destination, long time, int type, int dir) {
        this.isDone = false;
        this.time = time;
        this.destination = destination;
        this.inQueue = false;
        this.type = type;
        if (type == 0) {
            direction = null;
        }
        else if (dir == 1) {
            direction = Moveable.State.UP;
        }
        else {
            direction = Moveable.State.DOWN;
        }
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

    public double getTime() {
        return time;
    }

    public int getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        String a = new String();
        if (type == 0) {
            a = "(ER,#" + elenum + "," + destination + "," + time + ")";
        }
        else if (type == 1) {
            a = "(FR," + destination + ',' + direction + "," + time + ")";
        }
        return a;
    }
}
