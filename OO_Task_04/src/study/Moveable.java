package study;

/**
 * Created by luxia on 2016/3/16.
 */
public interface Moveable {
    enum State {UP, DOWN, IDLE}
    public double timeSpent(Request rq);
    public void move(State state) throws InterruptedException;
}
