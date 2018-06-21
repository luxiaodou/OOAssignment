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

    public void addRequest(Request request) {
        requests[rindex++] = request;
    }

    public void delRequest(int index) throws FormatException {
        if(index > rindex) throw new FormatException("index larger than rindex!");
        for (int i = index; i < rindex; i++){
            requests[i] = requests[i+1];
        }
        rindex -= 1;
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

    public void info(){
        System.out.print(requests[0].toString() + " (");
        for(int i = 1; i< rindex ; i++)
        {
            System.out.print(requests[i].toString());
        }
        System.out.println(")");
    }
}
