package study;

import static java.lang.Thread.sleep;

/**
 * Created by luxia on 2016/4/4.
 */
public class Mul_Elevator implements Runnable, Moveable {
    //private long sleeptime = 0;
    private int count = 0;
    private int id;
    private int floor = 1;
    private int mvAmount = 0;
    private int destination = 1;
    private double time;
    private State state = State.IDLE;
    private RQueue eleRQ = null;
    private RQueue carryRQ = null;
    private Boolean finished = true;

    @Override
    public void run() {
        try {
            while (true) {
                while (eleRQ == null) sleep(25);
                Schedule();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Schedule() throws FormatException, InterruptedException {
        Request primary = new Request();
        int primarymark = 0;    //mark where the primary req is
        Boolean done = false;   //mark whether ele is moved in this time section
        Boolean deleted = false;

        if (eleRQ.getRindex() == 0) {   //when eleRQ is empty don't do anything
            return;
        }
        else {
            primary = eleRQ.getRequest(0);
            primarymark = 0;
            if (state == State.UP) {
                for (int i = 0; i < eleRQ.getRindex(); i++) {
                    if (eleRQ.getRequest(i).getDestination() > primary.getDestination()) {
                        primary = eleRQ.getRequest(i);
                        primarymark = i;
                        destination = primary.getDestination();
                    }
                }
            }
            else if (state == State.DOWN) {
                for (int i = 0; i < eleRQ.getRindex(); i++) {
                    if (eleRQ.getRequest(i).getDestination() < primary.getDestination()) {
                        primary = eleRQ.getRequest(i);
                        primarymark = i;
                        destination = primary.getDestination();
                    }
                }
            }
        }

        //System.out.println("*****Check: #" + id + ", floor " + floor + ", primary " + primary.toString() + ", time " + time + "*****");
        if (finished) {
            time = Time.getTime();
            destination = primary.getDestination();
            //sleep(100);
        }

        if (state == State.IDLE) {
            if (destination > floor) {
                state = State.UP;
                finished = false;
                move(State.UP);
            }
            else if (destination < floor) {
                state = State.DOWN;
                finished = false;
                move(State.DOWN);
            }
            else if (destination == floor) {
                state = State.IDLE;
                for (int i = 0; i < eleRQ.getRindex(); i++) {
                    if (floor == eleRQ.getRequest(i).getDestination()) {
                        eleRQ.delRequest(i--);
                    }
                }
                finished = true;
                //eleRQ.delRequest(primarymark);
                System.out.println("Request Finished :\t(" + primary.toString() + ", #" + this.getId() + ")");
                move(State.IDLE);
            }
        }
        else if (state == State.UP) {
            for (int i = 0; i < eleRQ.getRindex(); i++) {
                if (floor == eleRQ.getRequest(i).getDestination()) {
                    if (floor == destination) {
                        finished = true;
                    }
                    move(State.IDLE);
                    done = true;
                    for (int j = 0; j < eleRQ.getRindex(); j++) {
                        if (floor == eleRQ.getRequest(j).getDestination()) {
                            System.out.println("Request Finished :\t(" + eleRQ.getRequest(j).toString() + ", #" + this.getId() + ")");
                            eleRQ.delRequest(j--);
                            deleted = true;
                        }
                    }
                    if (deleted) {
                        break;
                    }
                }
            }
            if (!done) {
                move(State.UP);
            }
        }
        else if (state == State.DOWN) {
            for (int i = 0; i < eleRQ.getRindex(); i++) {
                if (floor == eleRQ.getRequest(i).getDestination()) {
                    if (floor == destination) {
                        finished = true;
                    }
                    move(State.IDLE);
                    done = true;
                    for (int j = 0; j < eleRQ.getRindex(); j++) {
                        if (floor == eleRQ.getRequest(j).getDestination()) {
                            System.out.println("Request Finished :\t(" + eleRQ.getRequest(j).toString() + ", #" + this.getId() + ")");
                            eleRQ.delRequest(j--);
                            deleted = true;
                        }
                    }
                    if (deleted) {
                        break;
                    }
                }
            }
            if (!done) {
                move(State.DOWN);
            }
        }
    }

    public int getDestination() {
        return destination;
    }

    public int getMvAmount() {
        return mvAmount;
    }

    public Mul_Elevator(int id, RQueue rQ) {
        this.floor = 1;
        this.id = id;
        this.state = State.IDLE;
        this.mvAmount = 0;
        this.eleRQ = rQ;
        this.carryRQ = null;
        this.destination = 1;
        this.time = 0;
    }

    @Override
    public String toString() {
        return "Mul_Elevator{" +
                "floor=" + floor +
                ", id=" + id +
                ", state=" + state +
                ", mvAmount=" + mvAmount +
                ", time=" + time +
                '}';
    }

    public int getFloor() {
        return floor;
    }

    public State getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    @Override
    public double timeSpent(Request rq) {
        return 0;
    }

    @Override
    public void move(State st) throws InterruptedException {
        if (st == State.IDLE) {
            sleep(6000);
            if (finished) {
                state = State.IDLE;
            }
            time += 6;
            System.out.println("");
            System.out.println("Elevator Stopped :\t(#" + this.getId() + ",#" + this.getFloor() + "," + this.getState() + "," + this.getMvAmount() + "," + time + ")");
        }
        else if (st == State.UP) {
            sleep(3000);
            floor += 1;
            mvAmount += 1;
            time += 3;
        }
        else if (st == State.DOWN) {
            sleep(3000);
            floor -= 1;
            mvAmount += 1;
            time += 3;
        }
    }
}
