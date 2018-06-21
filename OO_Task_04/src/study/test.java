package study;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by luxia on 2016/3/9.
 */
public class test {
    public static void main(String[] args) throws IOException, FormatException, InterruptedException {
        //Timer timer = new Timer();
        //timer.schedule(new Mytask(), 1000, 2000);
        Time time = new Time();
        System.out.println(Time.getTime());
        Thread.sleep(100);
        System.out.println(Time.getTime());
    }

}


/*
class que {
    int[] a = new int[10]    ;
    public void init() {
        for(int i =0; i< 10; i++)
            a[i] = i;
    }

    public void info() {
        for (int i=0;i<10;i++)
            System.out.println(a[i]);
        System.out.println("que's info finished!");
    }

}

class quet {
    int [] b;

    public quet(que q) {
        this.b = q.a;
    }

    public void clear() {
        for (int i =0 ;i < 10 ;i++){
            b[i] = 0;
        }
    }

    public void info() {
        for (int i=0;i<10;i++)
            System.out.println(b[i]);
        System.out.println("quet's info finished!");
    }
}
*/
