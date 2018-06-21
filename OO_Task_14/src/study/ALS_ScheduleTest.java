package study;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

/**
 * Created by luxia on 2016/6/3.
 */
public class ALS_ScheduleTest {
	private static ByteArrayInputStream input;
	private static byte[] temp;
	private static ALS_Schedule als_schedule = new ALS_Schedule();

	@org.junit.Before
	public void testsetUp() throws Exception {
		temp = new byte[2048];
		String s = "(ER,9,0) \n (FR, 5,UP,0)\n (ER,4,0)\nrun";
		temp = s.getBytes();
		input = new ByteArrayInputStream(temp);
	}

	@org.junit.Test
	public void testdispatch() throws Exception {
		try {
			String s = "(ER,9,0) \n(ER, 5,0) \n(ER,4,1) \n run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			assertEquals(als_schedule.bulidQueue(), true);
			assertEquals(als_schedule.dispatch(), true);
			s = "(FR,5,UP,0)\n ( ER,5,3) \n(ER,4,4) \n(FR,4,DOWN,5) \n run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			als_schedule = new ALS_Schedule();
			assertEquals(als_schedule.bulidQueue(), true);
			assertEquals(als_schedule.dispatch(), true);
			s = " ( ER,4,0)\n" +
					"(ER,8,1)\n" +
					"(FR,6,UP,2)\n" +
					"(FR,4,DOWN,2)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			als_schedule = new ALS_Schedule();
			assertEquals(als_schedule.bulidQueue(), true);
			assertEquals(als_schedule.dispatch(), true);
			s = "(ER,4,0)\n" +
					"(ER,8,1)\n" +
					"(ER,6,12)\n" +
					"(ER,1,13)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			als_schedule = new ALS_Schedule();
			assertEquals(als_schedule.bulidQueue(), true);
			assertEquals(als_schedule.dispatch(), true);
			s = "(FR,9,UP,0)\n" +
					"(\tFR,8,UP,1)\n" +
					"(FR,\t7,\tUP,2)\n" +
					"(FR,9,UP,2)\n" +
					"(FR,+9,UP,2)\n" +
					"(FR,4,DOWN,12)\n" +
					"(FR,-4,DOWN,12)\n" +
					"(FR,5,DOWN,13)\n" +
					"(FR,53,DOWN,13)\n" +
					"(FR,a,DOWN,13)\n" +
					"(FR,10,UP,13)\n" +
					"(FR,1,DOWN,1\t3)\n" +
					"(FR,5,DOWN\t,1000000001)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			als_schedule = new ALS_Schedule();
			assertEquals(als_schedule.bulidQueue(), true);
			assertEquals(als_schedule.dispatch(), true);
			s = "(FR,1,UP,0)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			als_schedule = new ALS_Schedule();
			assertEquals(als_schedule.bulidQueue(), true);
			assertEquals(als_schedule.dispatch(), true);

			s = "(FR,1a,UP,0)\n" +
					"(FR,123?1asd,UP,0)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			als_schedule = new ALS_Schedule();
			assertEquals(als_schedule.bulidQueue(), true);
			assertEquals(als_schedule.dispatch(), true);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@org.junit.Test
	public void testbulidQueue() throws Exception {
		input.reset();
		System.setIn(input);
		assertEquals(als_schedule.bulidQueue(), true);
		assertEquals(als_schedule.dispatch(), true);
	}

}