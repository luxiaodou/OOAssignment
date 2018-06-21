package study;

import java.io.IOException;

/**
 * Created by luxia on 2016/4/4.
 */
public class Mul_Main {
    public static void main(String[] args) {
        System.out.println("Please input Requests:");
        Time time = new Time();
        RQueue rQueue = new RQueue();
        RQueue[] eleRQ = new RQueue[3];
        for(int i = 0;i < 3;i++) {
            eleRQ[i] = new RQueue();
        }
        Mul_Elevator ele1 = new Mul_Elevator(1,eleRQ[0]);
        Mul_Elevator ele2 = new Mul_Elevator(2,eleRQ[1]);
        Mul_Elevator ele3 = new Mul_Elevator(3,eleRQ[2]);
        Mul_Dispatcher mDispatcher = new Mul_Dispatcher(rQueue,ele1,ele2,ele3,eleRQ);
        RequestReader rr = new RequestReader(rQueue);

        Thread thread_ele1 = new Thread(ele1,"Elevator1");
        Thread thread_ele2 = new Thread(ele2,"Elevator2");
        Thread thread_ele3 = new Thread(ele3,"Elevator3");
        Thread thread_RequestReader = new Thread(rr,"RequestReader");
        Thread thread_dispatch = new Thread(mDispatcher,"Dispatcher");

        //rr.buildQueue();
        thread_RequestReader.start();
        thread_dispatch.start();
        thread_ele1.start();
        thread_ele2.start();
        thread_ele3.start();
    }
}
