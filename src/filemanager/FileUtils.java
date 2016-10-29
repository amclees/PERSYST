package filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

/**
 * This class provides methods to get a list of files recursively from a folder/collection of folders.
 * It also provides a checksum function for files.
 * 
 * @author Andrew
 *
 */
public class FileUtils {
	
	/**
	 * Gets a SHA-256 checksum of a file
	 * 
	 * @param file A file to get a checksum of
	 * @return A SHA-256 checksum of the file
	 */
	public static String fileChecksum(File file) {
		byte[] buf = new byte[8192];
		if(!file.isFile()) return null;
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		try {
			DigestInputStream hashStream = new DigestInputStream(new FileInputStream(file), digest);
			while(hashStream.read(buf) != -1);
			hashStream.close();
		} catch(Exception e) {
			return null;
		}
		return DatatypeConverter.printHexBinary(digest.digest());
	}
	
	/**
	 * This method recursively gets a collection of all the files in a collection of folders/files.
	 * 
	 * @param folders A collection of folders to get all files from
	 * @return A list of all files in a folder
	 */
	public static Collection<File> getFiles(Collection<File> folders) {
		List<File> fileList = new LinkedList<File>();
		for(File folder : folders) {
			if(folder.isDirectory()) {
				for(File f : folder.listFiles()) {
					if(f.isDirectory()) fileList.addAll(getFiles(f));
					else fileList.add(f);
				}
			} else {
				fileList.add(folder);
			}
		}
		return fileList;
	}
	
	/**
	 * This method recursively gets a collection of all the files in a single folder
	 * 
	 * @param folder A folder to get all files from
	 * @return A list of all files in a folder (null if input is not a folder)
	 */
	public static Collection<File> getFiles(File folder) {
		if(!folder.isDirectory()) return null;
		List<File> folderList = new ArrayList<File>(1);
		folderList.add(folder);
		return getFiles(folderList);
	}
}
