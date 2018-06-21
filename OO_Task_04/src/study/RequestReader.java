package study;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by luxia on 2016/4/4.
 */
public class RequestReader implements Runnable {
    private RQueue rQueue = null;
    private final static int MAX_FLOOR = 20;
    private final static int ER = 0;
    private final static int FR = 1;

    public RequestReader(RQueue rQueue) {
        this.rQueue = rQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                buildQueue();
                Thread.sleep(20);
            } catch (IOException e) {
                System.out.println("Request Reader : IOException!");
                e.printStackTrace();
            } catch (FormatException e) {
                System.out.println("Request Reader : Format Error!");
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Request Reader : Interrputed Exception!");
                e.printStackTrace();
            }

        }
    }

    public void buildQueue() throws IOException, FormatException {
        Scanner scanner = new Scanner(System.in);
        String buffer;
        char[] c;

        while (true) {
            int type = ER;
            int elenum = 0;
            int floortemp = 0;
            int dir = -1;
            int i = 0;

            buffer = scanner.nextLine();
            buffer = buffer.replace(" ", "");
            buffer = buffer.replace("\t", "");
            //System.out.println(buffer);
            if (buffer.contains("FR")) {
                type = FR;
                if (buffer.contains("UP")) {
                    dir = 1;
                }
                if (buffer.contains("DOWN")) {
                    dir = 0;
                }
            }
            if (buffer.matches("\\(FR,\\x2b?\\d{1,2},((UP)|(DOWN))\\)") || buffer.matches("\\(ER,#\\d{1},\\x2b?\\d{1,2}\\)")) {

                floortemp = 0;

                c = buffer.toCharArray();

                while (c[i] < '0' || c[i] > '9') i++;

                if (type == FR) {
                    while (c[i] >= '0' && c[i] <= '9') {
                        floortemp = 10 * floortemp + c[i++] - '0';
                    }
                    if (floortemp > MAX_FLOOR) {
                        continue;
                    }
                    if (floortemp == 1 && dir == 0 || floortemp == 20 && dir == 1) {
                        continue;
                    }
                    rQueue.addRequest(new Request(FR, elenum, floortemp, dir, Time.getTime()));
                }

                if (type == ER) {
                    while (c[i] >= '0' && c[i] <= '9') {
                        elenum = c[i++] - '0';
                    }
                    if (elenum > 3) {
                        continue;
                    }

                    while (c[i] < '0' || c[i] > '9') i++;

                    while (c[i] >= '0' && c[i] <= '9') {
                        floortemp = 10 * floortemp + c[i++] - '0';
                    }

                    if (floortemp > MAX_FLOOR) {
                        continue;
                    }
                    rQueue.addRequest(new Request(ER, elenum, floortemp, dir, Time.getTime()));
                }
            }
            else {
                break;
            }
        }
    }
}


