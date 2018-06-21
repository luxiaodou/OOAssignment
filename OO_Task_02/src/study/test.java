package study;

import java.io.IOException;

/**
 * Created by luxia on 2016/3/9.
 */
public class test {
    public static void main(String[] args) throws IOException, FormatException {
        int ch;
        char c;/*
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        while((ch = bf.read())!= -1){
            c = (char) ch;
            System.out.println(ch + " " + c);*/
        Dispatcher rq = new Dispatcher();
        try {
            rq.bulidQueue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

}
