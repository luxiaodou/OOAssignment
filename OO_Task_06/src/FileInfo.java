import java.io.File;

/**
 * Created by luxia on 2016/4/16.
 */
public class FileInfo {
	private String name;
	private long lastModifiedTime;
	private long length;
	private String parent;
	private String path;
	private boolean isFile;
	private boolean isDirectory;
	private boolean isExist;

	public boolean isFile() {
		return isFile;
	}

	public boolean exists() {
		return isExist;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public FileInfo(File file,long length) {
		this.name = file.getName();
		this.lastModifiedTime = file.lastModified();
		this.length = length;
		this.parent = file.getParent();
		this.path = file.getAbsolutePath();
		this.isFile = file.isFile();
		this.isExist = file.exists();
		this.isDirectory = file.isDirectory();
	}

	public String getName() {
		return name;
	}

	public long lastModified() {
		return lastModifiedTime;
	}

	public long length() {
		return length;
	}

	public String getParent() {
		return parent;
	}

	public String getAbsolutePath() {
		return path;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		FileInfo fileInfo = (FileInfo) o;

		if (lastModifiedTime != fileInfo.lastModifiedTime) {
			return false;
		}
		if (length != fileInfo.length) {
			return false;
		}
		if (isFile != fileInfo.isFile) {
			return false;
		}
		if (isDirectory != fileInfo.isDirectory) {
			return false;
		}
		if (isExist != fileInfo.isExist) {
			return false;
		}
		if (name != null ? !name.equals(fileInfo.name) : fileInfo.name != null) {
			return false;
		}
		if (parent != null ? !parent.equals(fileInfo.parent) : fileInfo.parent != null) {
			return false;
		}
		return path != null ? path.equals(fileInfo.path) : fileInfo.path == null;

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (int) (lastModifiedTime ^ (lastModifiedTime >>> 32));
		result = 31 * result + (int) (length ^ (length >>> 32));
		result = 31 * result + (parent != null ? parent.hashCode() : 0);
		result = 31 * result + (path != null ? path.hashCode() : 0);
		result = 31 * result + (isFile ? 1 : 0);
		result = 31 * result + (isDirectory ? 1 : 0);
		result = 31 * result + (isExist ? 1 : 0);
		return result;
	}

	public boolean judegePathChange (FileInfo another){
		if (another.isFile() && this.isFile() && another.getName().equals(name) && another.length() == length() && !another.getAbsolutePath().equals(path)) {
			return true;
		}
		else return false;
	}

	@Override
	public String toString() {
		return "FileInfo{" +
				"name='" + name + '\'' +
				", lastModifiedTime=" + lastModifiedTime +
				", length=" + length +
				", path='" + path + '\'' +
				'}';
	}
}
