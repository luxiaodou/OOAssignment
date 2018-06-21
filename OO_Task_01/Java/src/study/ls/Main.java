package study.ls;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, FormatException {
        // write your code here

        ComputePoly cp = new ComputePoly();

        try {
            cp.buildPoly();
            cp.compute();
        } catch (IOException e) {
            System.out.println("系统输入异常！");
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("系统输入异常！");
            e.printStackTrace();
        } catch (FormatException e) {
            System.out.println(e.toString());
            System.out.println("程序遇到异常啦~~~");
            System.out.println("请阅读用户手册以获取相关使用规范！");
        }
    }
}
