package study;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher();

        try {
            dispatcher.bulidQueue();
            dispatcher.dispatch();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("输入错误！");
        } catch (FormatException e) {
            System.out.println(e.toString());
            System.out.println("格式错误！");
        }

    }
}
