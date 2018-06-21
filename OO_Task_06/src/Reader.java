import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by luxia on 2016/4/13.
 */
public class Reader {
    private ArrayList<String> directorys = new ArrayList<>();
    private ArrayList<Triggers> triggers = new ArrayList<>();
    private ArrayList<Missions> missions = new ArrayList<>();
    private Summary s;
    private Detail d;


    private static int directorycounter = 0;

    public Reader() {
    }

    public Reader(ArrayList<String> directorys, ArrayList<Triggers> triggers, ArrayList<Missions> missions,Summary s, Detail d) {
        this.directorys = directorys;
        this.triggers = triggers;
        this.missions = missions;
        this.s = s;
        this.d = d;
    }

    public void readIn() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String buffer = scanner.nextLine();
            buffer.trim();
            buffer = buffer.replace("\t", "");

            if (buffer.equals("END")) {
                break;
            }

            String[] iff = buffer.split(" ");

            if (iff.length == 5 && iff[0].equals("IF")) {
                if (iff[2].equals("modified") || iff[2].equals("renamed") || iff[2].equals("path-changed") || iff[2].equals("size-changed")) {
                    if (iff[3].equals("THEN")) {
                        if (iff[4].equals("record-summary") || iff[4].equals("record-detail") || iff[4].equals("recover")) {
                            if (!directorys.contains(iff[1])) {
                                directorycounter++;    //record how many different directories are watched
                            }
                            directorys.add(iff[1]);
                            switch (iff[2]) {
                                case "modified":
                                    triggers.add(Triggers.modified);
                                    break;
                                case "renamed":
                                    triggers.add(Triggers.renamed);
                                    break;
                                case "path-changed":
                                    triggers.add(Triggers.path_changed);
                                    break;
                                case "size-changed":
                                    triggers.add(Triggers.size_changed);
                                    break;
                            }
                            switch (iff[4]) {
                                case "record-summary":
                                    missions.add(Missions.record_summary);
                                    break;
                                case "record-detail":
                                    missions.add(Missions.record_detail);
                                    break;
                                case "recover":
                                    missions.add(Missions.recover);
                                    break;
                            }
                            System.out.println("success");
                        }
                    }
                }
            }

        }

        if (directorycounter < 5 || directorycounter > 8) {
            System.out.println("Error diecroty number!");
            System.exit(0);
        }

        for (int i = 0; i < directorys.size(); i++) {
            System.out.println(directorys.get(i) + " " + triggers.get(i) + " " + missions.get(i));
        }
    }

    public void buildWatch(ArrayList<Watch> watches) {    //考虑与ReadIn合并
        int watchIndex = 0;  //mark how many watch are created, each one point to one workspace
        int addedmark;
        for (int i =0 ;i<directorys.size();i++)
        {
            String dir = directorys.get(i);
            boolean added = false;
            for(addedmark = 0; addedmark<watches.size();addedmark ++)
            {
                if (watches.size() != 0 && dir.equals(watches.get(addedmark).getWatchPath())) {
                    added = true;
                    break;
                }
            }
            if (added)
            {
                watches.get(addedmark).buildIfttt(triggers.get(i),missions.get(i));
            }
            else {
                watches.add(new Watch(dir,s,d)) ;
                watches.get(watchIndex++).buildIfttt(triggers.get(i),missions.get(i));
            }
        }
    }

    public int getDirectorycounter() {
        return directorycounter;
    }
}
