import java.io.File;
import java.util.*;

public class Watch extends Thread {
	private String watchPath = null;
	private String workSpace = null;
	private boolean init = true;
	private Summary s;
	private Detail d;

	private Map<String, FileInfo> originFiles = new HashMap<>();
	private Map<String, FileInfo> currentFiles = new HashMap<>();
	private Set<String> newFiles = new HashSet<>();
	private boolean rename, modify, size, path;

	private Vector<Missions> renameWork = new Vector<>();
	private Vector<Missions> modifyWork = new Vector<>();
	private Vector<Missions> sizeWork = new Vector<>();
	private Vector<Missions> pathWork = new Vector<>();


	public Watch(String dir, Summary s, Detail d) {
		this.watchPath = dir;
		rename = false;
		modify = false;
		size = false;
		path = false;
		this.s = s;
		this.d = d;
	}

	public void buildIfttt(Triggers t, Missions m) {    //used in Reader.java
//		ifttts.add(new Ifttt(t, m));
		switch (t) {
			case renamed:
				if (!renameWork.contains(m)) {
					renameWork.add(m);
				}
				rename = true;
				break;
			case modified:
				if (!modifyWork.contains(m)) {
					modifyWork.add(m);
				}
				modify = true;
				break;
			case size_changed:
				if (!sizeWork.contains(m)) {
					sizeWork.add(m);
				}
				size = true;
				break;
			case path_changed:
				if (!pathWork.contains(m)) {
					pathWork.add(m);
				}
				path = true;
				break;
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				startWatch();
				sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startWatch() {
		File fwatchpath = new File(watchPath);
		FileInfo fiwa = new FileInfo(fwatchpath, fwatchpath.length());
//		Iterator<Ifttt> iterator = ifttts.iterator();
		if (init) {
			init = false;
			buildFileTree(fwatchpath);
			if (fwatchpath.isFile()) {
				workSpace = fwatchpath.getParent();
			}
			else {
				workSpace = fwatchpath.getAbsolutePath();
			}
			System.out.println("Start working");
		}
		else {
			updateFileTree(fwatchpath);

			if (rename) {
				checkRename(fiwa);
			}
			if (modify) {
				checkModify(fiwa);
			}
			if (size) {
				checkSizeChange(fiwa);
			}
			if (path) {
				checkPathChange(fiwa);
			}
			originFiles = currentFiles;
			newFiles.clear();
		}
	}

	//DONE
	public void checkRename(FileInfo fiwa) {   //same: Path size lastModifiedTime Diff: name
		FileInfo oldfile = originFiles.get(fiwa.getAbsolutePath());
		FileInfo watchpath = currentFiles.get(fiwa.getAbsolutePath());
		if (watchpath.isDirectory()) {
			ArrayList<FileInfo> filelist = new ArrayList<>();
			for (String key : currentFiles.keySet()) {
				if (key.contains(watchpath.getAbsolutePath()) && currentFiles.get(key).isFile()) {
					filelist.add(currentFiles.get(key));
				}
			}
			if (filelist.size() == 0) {
				return;
			}
			for (int i = 0; i < filelist.size(); i++)
				checkRename(filelist.get(i));
		}
		else if (watchpath.isFile()) {
			FileInfo newone = currentFiles.get(watchpath.getAbsolutePath());
			String newonedir = newone.getParent();
			ArrayList<FileInfo> filelist = new ArrayList<>();
			for (String s : originFiles.keySet()) {
				FileInfo tmp = originFiles.get(s);
				if (tmp.getParent().equals(newonedir) && tmp.isFile()) {
					filelist.add(tmp);
				}
			}

			for (FileInfo afile : filelist) {
				if (afile.isDirectory()) {
					continue;
				}
				else {
					if (afile.length() == newone.length() && afile.lastModified() == afile.lastModified() && !afile.getName().equals(newone.getName())) {
						watchPath = afile.getAbsolutePath();
						for (int i = 0; i < renameWork.size(); i++) {
							if (renameWork.get(i) == Missions.record_detail) {
								d.add(Triggers.renamed, oldfile, watchpath);
							}
							else if (renameWork.get(i) == Missions.record_summary) {
								s.add(Triggers.renamed);
							}
							else if (renameWork.get(i) == Missions.recover) {
								File tmp = new File(watchpath.getAbsolutePath());
								File newfile = new File(watchpath.getParent() + File.separator + oldfile.getName());
								tmp.renameTo(newfile);
							}
						}
					}
				}
			}
		}
	}


	//DONE
	public void checkModify(FileInfo fiwa) {
		FileInfo watchpath = currentFiles.get(fiwa.getAbsolutePath());
		FileInfo oldfile = originFiles.get(watchpath.getAbsolutePath());
		if (watchpath.isDirectory()) {
			ArrayList<FileInfo> filelist = new ArrayList<>();
			for (String key : currentFiles.keySet()) {
				if (key.contains(watchpath.getAbsolutePath()) && !currentFiles.get(key).getAbsolutePath().equals(watchpath.getAbsolutePath())) {
					filelist.add(currentFiles.get(key));
				}
			}
			if (oldfile.lastModified() != watchpath.lastModified()) {
				for (int i = 0; i < modifyWork.size(); i++) {
					if (modifyWork.get(i) == Missions.record_summary) {
						s.add(Triggers.modified);
					}
					else if (modifyWork.get(i) == Missions.record_detail) {
						d.add(Triggers.modified, oldfile, watchpath);
					}
				}
			}
			if (filelist.size() == 0) {
				return;
			}
			for (int i = 0; i < filelist.size(); i++)
				checkModify(filelist.get(i));
		}

		else if (watchpath.isFile()) {
			if (oldfile.lastModified() == watchpath.lastModified()) {
				return;
			}
			else {
				for (int i = 0; i < modifyWork.size(); i++) {
					if (modifyWork.get(i) == Missions.record_summary) {
						s.add(Triggers.modified);
					}
					else if (modifyWork.get(i) == Missions.record_detail) {
						d.add(Triggers.modified, oldfile, watchpath);
					}
				}
			}
		}
	}


	public void checkSizeChange(FileInfo fiwa) { //same: path name diff: lastModifiedTime
		FileInfo watchpath = currentFiles.get(fiwa.getAbsolutePath());
		FileInfo oldfile = originFiles.get(fiwa.getAbsolutePath());
		if (watchpath.isDirectory()) {
			ArrayList<FileInfo> filelist = new ArrayList<>();
			for (String key : currentFiles.keySet()) {
				//Do not fall into dead cycle!
				if (key.contains(watchpath.getAbsolutePath()) && !currentFiles.get(key).getAbsolutePath().equals(watchpath.getAbsolutePath())) {
					filelist.add(currentFiles.get(key));
				}
			}
			if (oldfile.lastModified() != watchpath.lastModified() && oldfile.length() != watchpath.length()) {
				for (int i = 0; i < modifyWork.size(); i++) {
					if (modifyWork.get(i) == Missions.record_summary) {
						s.add(Triggers.size_changed);
					}
					else if (modifyWork.get(i) == Missions.record_detail) {
						d.add(Triggers.size_changed, oldfile, watchpath);
					}
				}
			}
			if (filelist.size() == 0) {
				return;
			}

			Iterator<FileInfo> it = filelist.iterator();
			while (it.hasNext()) {
				FileInfo afile = it.next();
				if (afile.isDirectory()) {
					FileInfo oldone = originFiles.get(afile.getAbsolutePath());
					if (oldone == null || oldone.length() == afile.length()) {
						for (int i = 0; i < modifyWork.size(); i++) {
							if (modifyWork.get(i) == Missions.record_summary) {
								s.add(Triggers.size_changed);
							}
							else if (modifyWork.get(i) == Missions.record_detail) {
								d.add(Triggers.size_changed, oldone, afile);
							}
						}
					}
					it.remove();
				}
			}

			for (String key : originFiles.keySet()) {   //check if the file is deleted
				FileInfo afile = originFiles.get(key);
				if (afile.isFile() && !currentFiles.containsKey(key)) {
					for (int i = 0; i < modifyWork.size(); i++) {
						if (modifyWork.get(i) == Missions.record_summary) {
							s.add(Triggers.size_changed);
						}
						else if (modifyWork.get(i) == Missions.record_detail) {
							d.add(Triggers.size_changed, afile, new FileInfo(new File("NotKnown"), 0));
						}
					}
				}
			}
			for (int i = 0; i < filelist.size(); i++)
				checkModify(filelist.get(i));
		}
		else if (watchpath.isFile()) {
			if (oldfile == null || oldfile.length() == watchpath.length()) {
				for (int i = 0; i < modifyWork.size(); i++) {
					if (modifyWork.get(i) == Missions.record_summary) {
						s.add(Triggers.size_changed);
					}
					else if (modifyWork.get(i) == Missions.record_detail) {
						d.add(Triggers.size_changed, oldfile, watchpath);
					}
				}
			}
		}
	}

	public void checkPathChange(FileInfo fiwa) { // same : length name lastModifiedTime diff: path
		FileInfo watchpath = currentFiles.get(fiwa.getAbsolutePath());
		FileInfo oldfile = originFiles.get(fiwa.getAbsolutePath());

		if (watchpath.isDirectory()) {
			ArrayList<FileInfo> filelist = new ArrayList<>();
			for (String key : currentFiles.keySet()) {
				FileInfo afile = currentFiles.get(key);
				if (key.contains(watchpath.getAbsolutePath()) &&
						afile.isFile() &&
						!afile.getAbsolutePath().equals(watchpath.getAbsolutePath())) {
					filelist.add(afile);
				}
			}
			if (filelist.size() == 0) {
				return;
			}
			for (int i = 0; i < filelist.size(); i++)
				checkPathChange(filelist.get(i));
		}
		else if (watchpath.isFile()) {
			if (oldfile == null) {
				for (FileInfo afile : originFiles.values()) {
					if (oldfile != null && oldfile.judegePathChange(afile)) {
						for (int i = 0; i < pathWork.size(); i++) {
							if (pathWork.get(i) == Missions.record_summary) {
								s.add(Triggers.path_changed);
							}
							if (pathWork.get(i) == Missions.record_detail) {
								d.add(Triggers.path_changed, oldfile, watchpath);
							}
							if (pathWork.get(i) == Missions.recover) {
								String oldpath = oldfile.getAbsolutePath();
								String currentpath = watchpath.getAbsolutePath();
								new Modifier().moves(currentpath, oldpath); //maybe something wrong here!
							}
						}
					}
				}
			}
		}

		if (watchpath.isFile()) {
			if (oldfile.equals(watchpath)) {
				return;
			}

			else {
				Iterator it = currentFiles.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, FileInfo> entry = (Map.Entry) it.next();
					String dirbuffer = entry.getKey();
					FileInfo filebuffer = entry.getValue();
					if (filebuffer.getName().equals(oldfile.getName()) && filebuffer.length() == oldfile.length() && filebuffer.lastModified() == oldfile.lastModified()) {
						for (int i = 0; i < pathWork.size(); i++) {
							if (pathWork.get(i) == Missions.record_summary) {
								s.add(Triggers.path_changed);
							}
							if (pathWork.get(i) == Missions.record_detail) {
								d.add(Triggers.path_changed, oldfile, watchpath);
							}
							if (pathWork.get(i) == Missions.recover) {
								String oldpath = oldfile.getAbsolutePath();
								String currentpath = watchpath.getAbsolutePath();
								new Modifier().moves(currentpath, oldpath); //maybe something wrong here!
							}
						}
					}
				}
			}
		}
	}

	private void updateFileTree(File file) {
		if (file.isFile()) {
			currentFiles.put(file.getAbsolutePath(), new FileInfo(file, file.length()));
			return;
		}
		else if (file.isDirectory()) {
			long size = 0;
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					size += files[i].length();
				}
			}
			currentFiles.put(file.getAbsolutePath(), new FileInfo(file, size));
			for (File afile : files) {
				updateFileTree(afile);
			}
		}
	}

	private void buildFileTree(File file) {
		if (file.isFile()) {
			originFiles.put(file.getAbsolutePath(), new FileInfo(file, file.length()));
			return;
		}
		else if (file.isDirectory()) {
			long size = 0;
			File[] files = file.listFiles();
			for (File afile : files) {
				if (afile.isFile()) {
					size += afile.length();
				}
			}
			originFiles.put(file.getAbsolutePath(), new FileInfo(file, size));
			for (File afile : files) {
				buildFileTree(afile);
			}
		}
	}

	public String getWatchPath() {
		return watchPath;
	}
}
