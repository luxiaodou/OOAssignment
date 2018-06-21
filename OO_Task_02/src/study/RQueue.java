package study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RQueue {
    private Request[] requests = new Request[100000];
    private static int rindex=0;

    public Request getRequest(int i) {
        return requests[i];
    }

    public void addRequest(Request request){
        requests[rindex++] = request;
    }

    public static int getRindex() {
        return rindex;
    }
}
