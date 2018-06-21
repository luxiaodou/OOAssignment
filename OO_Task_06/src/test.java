import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Created by luxia on 2016/4/13.
 */
public class test {
    public static void main(String[] args) {
        ArrayList<String> directorys = new ArrayList<>();
        ArrayList<Triggers> triggers = new ArrayList<>();
        ArrayList<Missions> missions = new ArrayList<>();
        ArrayList<Watch> watches = new ArrayList<>();

        Reader reader = new Reader(directorys,triggers,missions,new Summary(),new Detail());

	    long start = System.currentTimeMillis();
	    Modifier modifier = new Modifier();
	    modifier.readInfo("G:\\\\a.mkv");
	    modifier.moves("G:\\a.mkv","G:\\EVA");

	    long end = System.currentTimeMillis();
	    System.out.println(end - start);
    }
}

class test1 {
	private Map<String, Integer> watchedFiles = new HashMap<>();
	private Set<String> newFiles = new HashSet<>();

	public void d() {
		watchedFiles.put("a",1);
	}

	public void e() {
		test2 t2 = new test2();
		String s = "Hello";
		t2.f(s,newFiles);
		System.out.println(newFiles);
	}
}

class test2{
	private String s;

	public void f(String s,Set<String> newFiles) {
		newFiles.add(s);
		return;
	}
}
