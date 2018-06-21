package study;

public class ALS_Main {
    public static void main(String[] args) {
        ALS_Schedule alsSchedule = new ALS_Schedule();

        try {
            alsSchedule.bulidQueue();
            System.out.println("电梯停靠信息及请求捎带信息如下：\n");
            alsSchedule.dispatch();
        } catch (FormatException e) {
            System.out.println(e.toString());
            System.out.println("格式错误！");
        }   catch (Exception e) {
            System.out.println("输入错误！");
        }
    }
}
