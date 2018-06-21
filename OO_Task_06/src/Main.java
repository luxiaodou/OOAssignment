import java.util.ArrayList;

/**
 * Created by luxia on 2016/4/13.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<String> directorys = new ArrayList<>();
        ArrayList<Triggers> triggers = new ArrayList<>();
        ArrayList<Missions> missions = new ArrayList<>();
        Summary summary = new Summary();
        Detail detail = new Detail();
        ArrayList<Watch> watches = new ArrayList<>();

	    try {
		    Reader reader = new Reader(directorys,triggers,missions,summary,detail);
		    reader.readIn();
		    reader.buildWatch(watches);
		    Watch watch1 = watches.get(0);
		    watch1.run();
	    }catch (Exception e) {
		    e.printStackTrace();
	    }


    }
}