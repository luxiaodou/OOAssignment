package study;

import java.io.IOException;

/**
 * Created by luxia on 2016/4/7.
 */
public class Fool_Main {
    public static void main(String[] args){
        FoolSchedule foolSchedule = new FoolSchedule();

        try {
            foolSchedule.bulidQueue();
            System.out.println("Elevator start working!");
            foolSchedule.dispatch();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }


}
