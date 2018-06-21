package study;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Dispatcher {
    private RQueue rQueue = new RQueue();
    private static int MAX_FLOOR = 10;
    private double timenow = 0;

    public void dispatch() {
        Elevator eva1 = new Elevator();
        int reqnum = 0;
        double timepassed;
        timenow = 0;
        System.out.println("调度结果如下：");
        for (reqnum = 0; reqnum < RQueue.getRindex(); reqnum++) {
            Request reqnow = rQueue.getRequest(reqnum);
            if (timenow < reqnow.getTime()) {
                timepassed = eva1.move(reqnow);
                timenow = reqnow.getTime() + timepassed;
            } else if (timenow > reqnow.getTime()) {
                timepassed = eva1.move(reqnow);
                timenow += timepassed;
            } else {
                timepassed = eva1.move(reqnow);
                timenow += timepassed;
            }
            System.out.println("(" + eva1.getFloor() + ", " + eva1.getState() + ", " + timenow + ")");
        }
    }


    public void bulidQueue() throws IOException, FormatException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int ch;
        int floor = 1;
        int time = 0;
        int dir = 1;
        int type = 1;   //0 - ER;1 - FR
        int needenter = 0;


        ch = bf.read();
        while (ch != -1) {
            if (ch == ' ' || ch == '\t') {
                ch = bf.read();
                continue;
            }
            if (ch == '(' && needenter == 0) {
                needenter = 1;
                Boolean knowntype = false;  //完成类型输入的标识符
                Boolean floorerror = false;  //楼层输入是否有错的表示符
                Boolean finishdir = false;  //完成方向输入的标识符
                Boolean finishnum = false;
                while ((ch = bf.read()) != ')') {
                    if (ch == ' ' || ch == '\t') continue;
                    if (ch == 'F' || ch == 'E') {
                        if (ch == 'E') {
                            finishdir = true;
                            type = 0;
                        } else {
                            finishdir = false;
                            type = 1;
                        }
                        while((ch = bf.read())== ' ' || ch == '\t') continue;
                        if (ch == 'R') {
                            knowntype = true;
                            while ((ch = bf.read()) == ' ' || ch == '\t') continue;
                            if (ch == ',') {
                                Boolean kong = true;
                                int sign = 1;
                                int signnum = 0;
                                Boolean moresign = true;
                                Boolean err = false;
                                int temp = 0;
                                while ((ch = bf.read()) != ',') {
                                    if (ch == ' ' || ch == '\t') continue;
                                    else if (ch == '+' && moresign) {
                                        moresign = false;
                                    } else if (ch == '-') {
                                        //sign = -sign;
                                        //moresign = false;
                                        signnum++;
                                        err = true;
                                    } else if (ch >= '0' || ch <= '9') {
                                        moresign = false;
                                        kong = false;
                                        temp = temp * 10 + ch - '0';
                                        if (temp > 10) err = true;
                                    } else {
                                        err = true;
                                        break;
                                    } //throw new FormatException("楼层部分输入错误！");
                                }
                                floorerror = (signnum > 1) || kong || (temp > MAX_FLOOR) || moresign || err || (temp == 0);
                                if (floorerror) break; // throw new FormatException("楼层部分输入错误！");
                                else {
                                    floor = temp;
                                    temp = 0;
                                    if (!finishdir) {
                                        while ((ch = bf.read()) != ',') {
                                            if (ch == ' ' || ch == '\t') continue;
                                            if (finishdir) break;
                                            if (ch == 'U') {
                                                while ((ch = bf.read()) == ' ' || ch == '\t') continue;
                                                if (ch == 'P') {
                                                    if (floor == 10) break;
                                                    dir = 1;    //1 means up
                                                    finishdir = true;
                                                } else break; // throw new FormatException("方向输入错误！");
                                            } else if (ch == 'D') {
                                                while ((ch = bf.read()) == ' ' || ch == '\t') continue;
                                                if (ch == 'O') {
                                                    while ((ch = bf.read()) == ' ' || ch == '\t') continue;
                                                    if (ch == 'W') {
                                                        while ((ch = bf.read()) == ' ' || ch == '\t') continue;
                                                        if (ch == 'N') {
                                                            if (floor == 1) break;
                                                            dir = -1;   //-1 means down
                                                            finishdir = true;
                                                        } else break; //throw new FormatException("方向输入错误！");
                                                    } else break; //throw new FormatException("方向输入错误！");
                                                } else break; //throw new FormatException("方向输入错误！");
                                            } else break; //throw new FormatException("方向输入错误！");
                                        }
                                    }
                                    if (!finishdir) break;

                                    while ((ch = bf.read()) != ')') {
                                        if (ch == ' ' || ch == '\t') continue;
                                        else if (ch >= '0' && ch <= '9') {
                                            temp = temp * 10 + ch - '0';
                                            time = temp;
                                            finishnum = true;
                                            if (temp > 100000000) break;
                                        } else break; // throw new FormatException("时间输入错误！");
                                    }
                                    if (finishnum) break;
                                    else throw new FormatException("时间输入错误！");

                                }
                            } else break; //throw new FormatException("请求类型错误！");
                        } else break; //throw new FormatException("请求类型错误！");
                    } else break; //throw new FormatException("请求类型错误！");
                }
                if (ch == ')' && knowntype && !floorerror && finishdir && finishnum) {
                    if (time < timenow) throw new FormatException("请输入按时间排序的请求序列！");
                    if (time != 0 && RQueue.getRindex() == 0) throw new FormatException("第一个请求的时间要求为0！");

                    timenow = time;
                    Request rq = new Request(floor, time,type, dir);
                    rQueue.addRequest(rq);


                }
            } else if (ch == 10) {
                needenter = 0;
                if ((ch = bf.read()) == 'r') if ((ch = bf.read()) == 'u') if ((ch = bf.read()) == 'n') break;
            } else {
                ch = bf.read();
            }
        }
    }
}
