package filemanager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;

/**
 * This class provides a series of console-based tests for the File Manager.
 * 
 * @author Andrew
 *
 */
public class FileManagerTest {
	public static void main(String[] args) throws IOException {
		storageTest();
		encryptThisDirectory();
	}
	
	private static void encryptThisDirectory() throws IOException {
		File folder = new File("toEncrypt");
		folder.mkdir();
		File[] files = folder.listFiles();
		PersistentStorage store = new PersistentStorage("pass", "pin1");
		for(File file : files) {
			String newPath = file.getAbsolutePath() + ".aes";
			File encryptedFile = new File(newPath);
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			byte[] fileBytes = IOUtils.toByteArray(in);
			
			store.store(fileBytes, encryptedFile);
		}
	}
	
	private static void storageTest() {
		HashSet<Serializable> toStore = new HashSet<Serializable>();
		toStore.add("This is a message");
		for(int i = 0; i < 10; i++) {
			toStore.add(2 + Math.random() * 20);
		}
		toStore.add("This is another message");
		toStore.add(new byte[4]);
		
		PersistentStorage store = new PersistentStorage("pass1", "pin1");
		File file = new File("stringArray.txt");
		store.store(toStore, file);
		HashSet<Serializable> read = (HashSet<Serializable>)PersistentStorage.fromBytes(store.read(file));
		
		for(Serializable c : read) {
			System.out.println(c.toString());
		}
	}
}
