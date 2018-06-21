package study;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by luxia on 2016/6/3.
 */
public class ElevatorTest {
	Elevator e = new Elevator();
	@Test
	public void testmove() throws Exception {
		assertEquals(e.move(Moveable.State.UP),true);
		assertEquals(e.move(Moveable.State.STAY),true);
		assertEquals(e.move(Moveable.State.DOWN),true);
	}

	@Test
	public void testtoString() throws Exception {
		e.toString();
	}

	@Test
	public void testgetE_floor() throws Exception {
		e.getE_floor();
	}

	@Test
	public void testgetState() throws Exception {
		e.getState();
	}

	@Test
	public void testtimeSpent() throws Exception {
		e = new Elevator();
		e.timeSpent(new Request(e.getE_floor(),1,1,0));
		e.move(Moveable.State.UP);
		e.move(Moveable.State.UP);
		e.move(Moveable.State.UP);
		e.timeSpent(new Request(1,1,1,0));
		e.timeSpent(new Request(10,1,1,0));
	}

}