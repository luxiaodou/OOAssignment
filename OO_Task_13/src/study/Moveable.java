package study;

/**
 * Created by luxia on 2016/3/16.
 */
public interface Moveable {
    enum State {UP, DOWN, STAY}
    public double timeSpent(Request rq);
    public boolean move(State state);
}
