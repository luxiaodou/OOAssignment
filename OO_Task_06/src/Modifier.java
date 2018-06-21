import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Created by luxia on 2016/4/13.
 */
public class Modifier {
	private String separator = File.separator;

	public void readInfo(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			System.out.println("ReadInfo-error : File is not exist!");
			return;
		}
		if (file.isDirectory()) {
			System.out.println("ReadInfo-error : readInfo can not read Directory's info!");
			return;
		}
		else {
			System.out.println(file.getName() + " " + file.length() + " " +
					new Date(file.lastModified()));
		}
	}

	public void renames(String dir, String newName) {
		File file = new File(dir);
		if (!file.exists()) {
			System.out.println("Rename-error : File is not exist!");
		}
		else if (file.isDirectory()) {
			System.out.println("Rename-error : rename mothod can not rename directory!");
		}
		else {
			File newfile = new File(file.getParent() + separator + newName);
			file.renameTo(newfile);
		}
	}

	public void moves(String oridir, String currentdir) {
		File orifile = new File(oridir);

		if (new File(currentdir).isFile()) {
			System.out.println("Move-error : The second parameter must be directory!");
			return;
		}
		if (!orifile.exists()) {
			System.out.println("Move-error : Origin file is not exist!");
		}
		else if (orifile.isDirectory()) {
			System.out.println("Move-error : Move mothod can note move directory!");
		}
		else {
			String filename = orifile.getName();
			File newFile = new File(currentdir + separator + filename);
			orifile.renameTo(newFile);
		}
	}

	public void createsFile(String filepath) {
		File file = new File(filepath);

		if (file.isDirectory()) {
			System.out.println("Please use createsDir to create a directory!");
			return;
		}
		if (file.exists()) {
			System.out.println("CreatesFile-error : The file you want to create is already exist!");
			return;
		}
		else {
			try {
				if (!file.createNewFile()) {
					System.out.println("CreatesFile-error : cannot create this file, please check the directory you entered and your permissino!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createsDir(String dirpath) {
		File file = new File(dirpath);
		if (!file.isDirectory()) {
			System.out.println("CreatesDir-error : Please use createsFile to create a file.");
			return;
		}
		if (file.exists()) {
			System.out.println("CreatesDir-error : the dir you want to create is already exist!");
		}
		else {
			file.mkdir();
		}
	}

	public void deleteFile(String filepath) {
		File file = new File(filepath);

		if (file.isDirectory()) {
			System.out.println("DeleteFile-error : Please use deleteDir to delete a directory!");
			return;
		}
		if (!file.exists()) {
			System.out.println("DeleteFile-error : The file you want to deleteFile is not exist!");
		}
		else {
			file.delete();
		}
	}

	public void deleteDir(String dirpath) {
		File file = new File(dirpath);

		if (file.exists()) {
			File[] temp = file.listFiles();
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].isDirectory()) {
					deleteDir(dirpath + separator + temp[i].getName());
				}
				else {
					temp[i].delete();
				}
			}
			file.delete();
		}
	}

	public void modifier(String filepath,String content) {
		File file2write = new File(filepath);
		if (file2write.exists()) {
			try {
				FileWriter writer = new FileWriter(filepath,true);
				writer.write(content);
				writer.close();
			} catch (IOException e) {
				System.out.println("Modifier-error : Cannot touch this file");
				e.printStackTrace();
			}
		}
		else {
			System.out.println("The file you want to modify is not exist! Please Create it first!");
		}
	}


}
