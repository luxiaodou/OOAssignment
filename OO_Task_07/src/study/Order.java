package study;

import java.util.*;

import static study.Main.taxis;

/**
 * Created by luxia on 2016/4/20.
 */
public class Order extends Thread {
	private Node location;
	private Node destination;
	private Boolean tracked;
	private ArrayDeque<Node> sp = new ArrayDeque<>();
	private Set<Integer> carnum = new HashSet<>();

	public Set<Integer> getCarnum() {
		return carnum;
	}

	public Node getLocation() {
		return location;
	}

	public Boolean getTracked() {
		return tracked;
	}

	@Override
	public String toString() {
		return "Order : " + location.toString() + " to " + destination.toString() + " ";
	}

	public Order(int x, int y, int endx, int endy, Boolean tracked) {
		location = Map.getMatrix()[x][y];
		destination = Map.getMatrix()[endx][endy];
		this.tracked = tracked;
		sp = Map.findSp(location, destination);
	}

	public Order(Node location, Node destination, Boolean tracked) {
		this.destination = destination;
		this.location = location;
		this.tracked = tracked;
		sp = Map.findSp(location, destination);
	}

	public ArrayDeque<Node> getway() {
		return sp;
	}

	public Node getDestination() {
		return destination;
	}

	public boolean inWindow(Taxi t) {
		return Math.abs(t.getLocation().getPos().x - location.getPos().x) <= 2 && Math.abs(t.getLocation().getPos().y - location.getPos().y) <= 2;
	}

	public void addcar(int i) {
		carnum.add(i);
	}

	@Override
	public void run() {
		long starttime = System.currentTimeMillis();

		while (System.currentTimeMillis() - starttime < 3000) {
			for (Taxi t : taxis) {
				if (t.getDriverState() == DriverState.waiting && this.inWindow(t) && !carnum.contains(t.getcarid())) {
					t.gainCredit();
					this.addcar(t.getcarid());
				}
			}
		}

		if (carnum.size() == 0) {
			System.out.println("No Response : " + this.toString());
		}
		else {
			synchronized (taxis) {
				Iterator<Integer> itr = carnum.iterator();
				while (itr.hasNext()){
					if (taxis[itr.next()].getDriverState() != DriverState.waiting)
						itr.remove();
				}
				if (carnum.size() == 0){
					System.out.println("No Response : " + this.toString());
				}
				else {
					Iterator<Integer> it = carnum.iterator();
					int highestCreditCar = it.next();
					int highestCredit = taxis[highestCreditCar].getCredit();
					ArrayList<Integer> topcars = new ArrayList<>();
					if (tracked) {
						System.out.print("In " + this.toString() + "list: ");
						taxis[highestCreditCar].showstate();
					}
					while (it.hasNext()) {
						int temp = it.next();
						if (tracked) {
							System.out.print("In " + this.toString() + "list: ");
							taxis[temp].showstate();
						}
						int tempCredit = taxis[temp].getCredit();
						if (highestCredit < tempCredit) {
							highestCreditCar = temp;
							highestCredit = tempCredit;
						}
						else {
							it.remove();
						}
					}
					Iterator<Integer> it2 = carnum.iterator();
					while (it2.hasNext()) {
						int top = it2.next();
						if (taxis[top].getCredit() == highestCredit) {
							topcars.add(top);
						}
					}

					if (topcars.size() != 1) {
						int mincost = 9999;
						for (int i : topcars) {
							Taxi t = taxis[i];
							Node car = t.getLocation();
							Node custom = location;
							int cost = new Order(car, custom, false).getway().size();
							if (cost < mincost) {
								highestCreditCar = i;
							}
						}
					}
					Taxi t = taxis[highestCreditCar];
					Node car = t.getLocation();
					Node custom = location;
					Order way2C = new Order(car, custom, false);
					t.setOrder(way2C, this);
					System.out.println("Taxi : ID " + t.getcarid() + " take " + this.toString());

				}
				}
		}
	}
}



