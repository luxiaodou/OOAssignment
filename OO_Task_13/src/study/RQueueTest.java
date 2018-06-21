package study;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by luxia on 2016/6/3.
 */
public class RQueueTest {
	private static RQueue q = new RQueue();

	@Before
	public void before() throws Exception {
		q.addRequest(new Request(1, 1, 1, 1));
	}

	@Test
	public void testgetRequest() throws Exception {
		q.getRequest(0);
	}

	@Test
	public void testaddRequest() throws Exception {
		assertEquals(q.addRequest(new Request(1, 1, 1, 1)), true);
	}

	@Test
	public void testdelRequest() throws Exception {
		assertEquals(q.delRequest(1), true);
		try {
			assertEquals(q.delRequest(5), false);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Test
	public void testgetRindex() throws Exception {
		q.getRindex();
	}

	@Test
	public void testsetDone() throws Exception {
		assertEquals(q.setDone(0), true);
	}

	@Test
	public void testsetQueue() throws Exception {
		assertEquals(q.setQueue(0), true);
	}

	@Test
	public void testinfo() throws Exception {
		q.addRequest(new Request(2, 2, 2, 1));
		assertEquals(q.info(),true);
	}

}