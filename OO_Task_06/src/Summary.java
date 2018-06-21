import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by luxia on 2016/4/13.
 */
public class Summary {
	private HashMap<Triggers, Long> Summarylist = new HashMap<>();
	private Summary instance;
	private FileWriter fw;
	private Long temp;
	private long begin;

	public Summary() {
		begin = System.currentTimeMillis();
		Summarylist.put(Triggers.size_changed, (long) 0);
		Summarylist.put(Triggers.path_changed, (long) 0);
		Summarylist.put(Triggers.modified, (long) 0);
		Summarylist.put(Triggers.renamed, (long) 0);
	}

	public synchronized void add(Triggers t) {
		temp = (long)Summarylist.get(t);
		temp += 1;
		Summarylist.put(t, temp);
		System.out.println("SummaryInfo : Trigger \""+ t + "\" " + temp + " time(s)");
	}

	public synchronized void write2File() {
		String dir = ".";

		try {
			this.fw = new FileWriter(dir,true);
		} catch (Exception e) {
			e.printStackTrace();
			this.fw = null;
		}
		if (fw != null && temp != 0 ) {
			String info = "";
			long curTime = System.currentTimeMillis();
			info += "###Log time : " + (curTime - this.begin) + " ms \r\n";
		}
	}
}
