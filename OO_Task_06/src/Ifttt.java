import java.io.File;
import java.util.*;

/**
 * Created by luxia on 2016/4/13.
 */
public class Ifttt {
	private Triggers trigger;
	private Missions mission;

	public Ifttt(Triggers triggers, Missions missions) {
		this.trigger = triggers;
		this.mission = missions;
	}

	private boolean checkmodified(File file, Map<String,File> watchedFiles, Set<String> newFiles) {
		long lastModifiedTime = file.lastModified();

		if (file.isFile()) {
			if (lastModifiedTime == watchedFiles.get(file.getPath()).lastModified()) {
				return false;
			}
			else {
				watchedFiles.put(file.getPath(), file);
				return true;
			}
		}
		else if (file.isDirectory()) {


		}
		return false;
	}

	public Triggers getTrigger() {
		return trigger;
	}

	public Missions getMission() {
		return mission;
	}

	@Override
	public String toString() {
		return "Ifttt{" +
				"trigger=" + trigger +
				", mission=" + mission +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Ifttt ifttt = (Ifttt) o;

		if (trigger != ifttt.trigger) {
			return false;
		}
		return mission == ifttt.mission;

	}

	@Override
	public int hashCode() {
		int result = trigger != null ? trigger.hashCode() : 0;
		result = 31 * result + (mission != null ? mission.hashCode() : 0);
		return result;
	}

}
