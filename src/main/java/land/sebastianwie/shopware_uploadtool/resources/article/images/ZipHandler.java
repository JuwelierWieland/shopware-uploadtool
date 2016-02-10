package land.sebastianwie.shopware_uploadtool.resources.article.images;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ZipHandler {
	private static final String homeDir = System.getProperty("user.home");
	private static final String zipDest = "/.shopware";

	File destDir;
	Set<File> files;

	public ZipHandler(String zipFileName) {
		for (int i = 0; !(destDir = new File(homeDir + zipDest + (i != 0 ? i : ""))).mkdir(); i++)
			;
		if (zipFileName == null || zipFileName.length() == 0)
			throw new IllegalArgumentException();

		try {
			ZipFile zipFile = new ZipFile(zipFileName);
			zipFile.extractAll(destDir.getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
			cleanup();
			return;
		}

		files = new HashSet<>();
		File[] fileArray = destDir.listFiles();
		if (fileArray != null)
			for (File file : fileArray)
				files.add(file);
	}

	public File[] getFileArray() {
		return files.toArray(new File[files.size()]);
	}

	public Set<File> getFileSet() {
		return files;
	}

	public boolean hasFile(File file) {
		return files.contains(file);
	}

	public boolean deleteFile(File file) {
		if (hasFile(file)) {
			files.remove(file);
			if (!file.delete()) {
				files.add(file);
				return false;
			} else
				return true;
		} else
			return false;
	}

	public void cleanup() {
		deleteDirectory(destDir);
	}

	private static boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	public static void main(String[] args) {
		new ZipHandler("asdf");
	}

}
