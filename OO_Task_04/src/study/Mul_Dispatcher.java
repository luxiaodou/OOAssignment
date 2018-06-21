package study;

import static java.lang.Thread.sleep;

/**
 * Created by luxia on 2016/4/4.
 */
public class Mul_Dispatcher implements Runnable {
    private RQueue rQueue = null;
    private Mul_Elevator[] eleArray = new Mul_Elevator[3];
    private RQueue[] eleRQ = new RQueue[3];

    @Override
    public void run() {
        try {
            while (true) {
                dispatch();
                sleep(50);
            }
        } catch (Exception e) {
            System.out.println("Mul_Dispatcher : Exception!");
            e.printStackTrace();
        }
    }

    public Mul_Dispatcher(RQueue rQueue, Mul_Elevator ele1, Mul_Elevator ele2, Mul_Elevator ele3, RQueue[] eleRQ) {
        this.rQueue = rQueue;
        this.eleArray[0] = ele1;
        this.eleArray[1] = ele2;
        this.eleArray[2] = ele3;
        this.eleRQ[0] = eleRQ[0];
        this.eleRQ[1] = eleRQ[1];
        this.eleRQ[2] = eleRQ[2];
    }

    public void dispatch() throws InterruptedException, FormatException {
        int mvamt1 = eleArray[0].getMvAmount();
        int mvamt2 = eleArray[1].getMvAmount();
        int mvamt3 = eleArray[2].getMvAmount();
        int i;
        int carryablenum;
        int mark;
        int idlesum = 0;
        int idlemark = -1;  //

        for (i = 0; i < 3; i++) {
            if (eleRQ[i].getRindex() == 0) {
                idlesum++;
                idlemark = i;
            }
        }

        for (i = 0; i < rQueue.getRindex(); i++) {
            mark = -1;  //mark = eleID-1
            carryablenum = 0;
            Request rq = rQueue.getRequest(i);
            int[] cancarry = {0,0,0};

            for (int j = 0; j < 3; j++) {
                Mul_Elevator e = eleArray[j];
                if (rq.getType() == 1) {
                    if (rq.getDirection() == e.getState()) {
                        if (rq.getDirection() == Moveable.State.UP && rq.getDestination() <= e.getDestination() && rq.getDestination() > e.getFloor() || rq.getDirection() == Moveable.State.DOWN && rq.getDestination() >= e.getDestination() && rq.getDestination() < e.getFloor()) {
                            mark = j;
                            cancarry[j] = 1;
                            carryablenum += 1;      //record how many elevator can carry this request
                        }
                    }
                }
                if (rq.getType() == 0) {
                    if (e.getId() == rq.getElenum() && e.getState() == Moveable.State.UP && rq.getDestination() > e.getFloor() || e.getId() == rq.getElenum() && e.getState() == Moveable.State.DOWN && rq.getDestination() < e.getFloor()) {
                        mark = j;
                        carryablenum += 1;
                    }
                }
            }

            if (carryablenum > 1) {    //when two or more elevators are carryable, the request will be pushed to the elevator with the min moveamount
                int minamt = 99999999;
                int k;
                for(k = 0;k<3;k++){
                    if(eleArray[k].getMvAmount() < minamt && cancarry[k] == 1){
                        minamt = eleArray[k].getMvAmount();
                    }
                }

                for (k = 0; k < 3; k++) {
                    if (eleArray[k].getMvAmount() == minamt) {
                        break;
                    }
                }
                eleRQ[k].addRequest(rq);
                rQueue.delRequest(i--);
                //System.out.println("Carryable > 1 : Dispatcher success : eleRQ #" + (k + 1) + rq.toString());
                //System.out.println(eleArray[k].toString());
            }

            if (carryablenum == 1 && mark != -1) {  //if only one elevator can carry this request, push to his queue
                eleRQ[mark].addRequest(rq);
                rQueue.delRequest(i--);
                //System.out.println("Carryable == 1 : Dispatcher success : eleRQ #" + (1 + mark) + rq.toString());
                //System.out.println(eleArray[mark].toString());
            }

            if (carryablenum == 0) { //no one can carry
                if (idlesum == 0) {
                    continue;      //no idle elevator, so go on to next request and wait
                }
                else if (idlesum == 1) {
                    if (rq.getType() == 0 && rq.getElenum() != idlemark + 1) {
                        continue;
                    }
                    eleRQ[idlemark].addRequest(rq);
                    rQueue.delRequest(i--);
                    //System.out.println("Idlesum == 1 : Dispatcher success : eleRQ #" + (idlemark + 1) + rq.toString());
                    //System.out.println(eleArray[idlemark].toString());
                    break;
                }
                else if (idlesum > 1) {
                    if (rq.getType() == 0 && eleArray[rq.getElenum() - 1].getState() == Moveable.State.IDLE) {
                        eleRQ[rq.getElenum() - 1].addRequest(rq);
                        rQueue.delRequest(i--);
                        //System.out.println("Idlesum > 1 ER : Dispatcher success : eleRQ #" + rq.getElenum() + rq.toString());
                        //System.out.println(eleArray[rq.getElenum() - 1].toString());
                    }
                    else if (rq.getType() == 1) {
                        int minmvamt = 99999999;
                        for (int j = 0; j < 3; j++) {
                            minmvamt = Math.min(minmvamt, eleArray[j].getMvAmount());
                        }
                        for (int j = 0; j < 3; j++) {
                            if (minmvamt == eleArray[j].getMvAmount() && eleArray[j].getState() == Moveable.State.IDLE) {
                                idlemark = j;
                                break;
                            }
                        }
                        //System.out.println(rq);
                        eleRQ[idlemark].addRequest(rq);
                        rQueue.delRequest(i--);
                        //System.out.println("Idlesum > 1 FR : Dispatcher success : eleRQ #" + (idlemark + 1) + rq.toString());
                        //System.out.println(eleArray[idlemark].toString());
                        break;
                    }
                }

            }
        }
    }
}

