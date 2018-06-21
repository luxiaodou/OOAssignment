package study;

/**
 * Created by luxia on 2016/4/4.
 */
public class Time {
    private static long start = System.currentTimeMillis(); //record the start time, no need to be public

    public static double getTime() {
        long now = System.currentTimeMillis() - start;  //return the time when the method is called
        double temp;
        temp = now / 100;
        temp = Math.round(temp);
        temp = temp / 10;
        return temp;
    }
}
