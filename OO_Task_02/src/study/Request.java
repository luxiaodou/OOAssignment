package study;

public class Request {
    private int time;
    private int dfloor;
    private Direction direction;
    private int type;

    private enum Direction {UP, DOWN}

    public Request() {
        this.time = 0;
        this.dfloor = 1;
        this.type = 1;
        this.direction = Direction.UP;
    }

    public Request(int dfloor,int time,int type,int dir) {
        this.time = time;
        this.dfloor = dfloor;
        this.type = type;
        if(type == 0) direction = null;
        else if(dir == 1) direction = Direction.UP;
        else direction = Direction.DOWN;
    }

    public Direction getDirection() {
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
}
