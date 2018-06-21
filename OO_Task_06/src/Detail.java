import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by luxia on 2016/4/13.
 */
public class Detail {
	private HashMap<Triggers, Change> detailList = new HashMap<>();
	private Change change;

	public Detail() {

	}



	private class Change {
		private FileInfo before;
		private FileInfo now;

		private Change(FileInfo before, FileInfo now) {
			this.before = before;
			this.now = now;
		}
	}

	public void add(Triggers t, FileInfo before, FileInfo now) {
		change = new Change(before, now);
		detailList.put(t, change);
		//System.out.println("Detail Changed : " + t + "\n" + before.toString() + " to " + now.toString());
	}

	public synchronized void write2File() {

	}
}


