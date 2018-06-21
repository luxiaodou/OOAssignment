package study;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

/**
 * Created by luxia on 2016/6/4.
 */
public class FoolScheduleTest {
	private static ByteArrayInputStream input;
	private static byte[] temp;
	private static FoolSchedule foolSchedule = new FoolSchedule();

	@Test
	public void dispatch() throws Exception {
		try {
			String s = "(ER,9,0) \n(ER, 5,0) \n(ER,4,1) \n run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			assertEquals(foolSchedule.bulidQueue(), true);
			assertEquals(foolSchedule.dispatch(), true);
			s = "(FR,5,UP,0)\n ( ER,5,3) \n(ER,4,4) \n(FR,4,DOWN,5) \n run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			foolSchedule = new FoolSchedule();
			assertEquals(foolSchedule.bulidQueue(), true);
			assertEquals(foolSchedule.dispatch(), true);
			s = " ( ER,4,0)\n" +
					"(ER,8,1)\n" +
					"(FR,6,UP,2)\n" +
					"(FR,4,DOWN,2)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			foolSchedule = new FoolSchedule();
			assertEquals(foolSchedule.bulidQueue(), true);
			assertEquals(foolSchedule.dispatch(), true);
			s = "(ER,4,0)\n" +
					"(ER,8,1)\n" +
					"(ER,6,12)\n" +
					"(ER,1,13)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			foolSchedule = new FoolSchedule();
			assertEquals(foolSchedule.bulidQueue(), true);
			assertEquals(foolSchedule.dispatch(), true);
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
			foolSchedule = new FoolSchedule();
			assertEquals(foolSchedule.bulidQueue(), true);
			assertEquals(foolSchedule.dispatch(), true);
			s = "(FR,1,UP,0)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			foolSchedule = new FoolSchedule();
			assertEquals(foolSchedule.bulidQueue(), true);
			assertEquals(foolSchedule.dispatch(), true);

			s = "(FR,1a,UP,0)\n" +
					"(FR,123?1asd,UP,0)\n" +
					"run";
			temp = s.getBytes();
			input = new ByteArrayInputStream(temp);
			System.setIn(input);
			foolSchedule = new FoolSchedule();
			assertEquals(foolSchedule.bulidQueue(), true);
			assertEquals(foolSchedule.dispatch(), true);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Test
	public void bulidQueue() throws Exception {

	}

}