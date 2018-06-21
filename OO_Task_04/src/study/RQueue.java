package study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RQueue {
    private Request[] requests = new Request[100000];
    private int rindex = 0;

    public Request getRequest(int i) {
        return requests[i];
    }

    public synchronized void addRequest(Request request) {
        while (rindex == 100000) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.notify();
        requests[rindex++] = request;
        //System.out.println("Check : "this.toString() + "RQueue add finish" + request.toString());
    }

    public synchronized void delRequest(int index) throws FormatException {
        while (rindex == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.notify();
        if (index > rindex) {
            throw new FormatException("index larger than rindex!");
        }
        for (int i = index; i < rindex; i++) {
            requests[i] = requests[i + 1];
        }
        rindex -= 1;
    }


    public synchronized Request popRequest(int index) {
        while (rindex == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();
        if(index > rindex) {
            System.out.println("RQueue index out of bound!");
        }
        Request rq = requests[index];
        for (int i = index; i < rindex; i++) {  //delRequest
            requests[i] = requests[i + 1];
        }
        return rq;
    }

    public int getRindex() {
        return rindex;
    }

    public void setDone(int index) {
        requests[index].Done();
    }

    public void setQueue(int index) {
        requests[index].setInQueue();
    }

    public void info() {
        System.out.print(requests[0].toString() + " (");
        for (int i = 1; i < rindex; i++) {
            System.out.print(requests[i].toString());
        }
        System.out.println(")");
    }
}
