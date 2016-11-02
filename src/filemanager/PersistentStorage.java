package filemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.hive2hive.core.security.EncryptionUtil;

import encryption.PersystEncryption;

/**
 * This class provides methods for reading and writing
 * AES-encrypted files to and from the local file system.
 * 
 * Max certified file size is 10MB. Tested and fails for at least 300MB.
 * 
 * @author Andrew
 *
 */
public class PersistentStorage {
	private String pin;
	private String password;
	
	/**
	 * This creates a new PersistentStorage with the specified password and pin.
	 * 
	 * @param password The user's password
	 * @param pin The user's pin
	 */
	public PersistentStorage(String password, String pin) {
		this.pin = pin;
		this.password = password;
	}
	
	/**
	 * This method stores the data at the location in
	 * AES encrypted form using the PersystEncryption class.
	 * 
	 * @param data Data to be stored
	 * @param location Location to store the data
	 */
	public void store(byte[] data, File location) {
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(location));
			
			byte[] initVector = EncryptionUtil.generateIV();
			
			byte[] encryptedData = PersystEncryption.encryptAES(data, initVector, password, pin);
			ByteBuffer intToByte = ByteBuffer.allocate(4);
			intToByte.putInt(initVector.length);
			byte[] initVectorLength = intToByte.array();
			
			out.write(initVectorLength);
			out.write(initVector);
			out.write(encryptedData);
			
			out.close();
		} catch(IOException e) {
			System.out.println("Failed to write file.");
		}
	}
	
	/**
	 * This method converts a serializable object to a byte array before storing it.
	 * 
	 * @param object A serializable object to store
	 * @param location The location to store the object
	 */
	public void store(Serializable object, File location) {
		store(toBytes(object), location);
	}
	
	/**
	 * This method reads an encrypted file from to disk.
	 * 
	 * @param file Location to read from
	 * @return A byte array of the read file's contents
	 */
	public byte[] read(File file) {
		
		byte[] fileBytes = readPlainFile(file);
			
		int initVectorLengthRead = ByteBuffer.wrap(Arrays.copyOfRange(fileBytes, 0, 4)).getInt();
		byte[] initVectorRead = Arrays.copyOfRange(fileBytes, 4, 4 + initVectorLengthRead);
		byte[] encryptedRead = Arrays.copyOfRange(fileBytes, 4 + initVectorLengthRead, fileBytes.length);
		byte[] decrypted = PersystEncryption.decryptAES(encryptedRead, initVectorRead, password, pin);
			
		return decrypted;
		
	}
	
	/**
	 * This method converts a serializable object into bytes.
	 * This makes it possible to store using this class.
	 * 
	 * @param object A serializable object
	 * @return The byte array corresponding to the object
	 */
	public static byte[] toBytes(Serializable object) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream objects = null;
		try {
			objects = new ObjectOutputStream(bytes);
		} catch (IOException e) {}
        try {
        	objects.writeObject(object);
        } catch(IOException e) {}
          
        return bytes.toByteArray();
	}
	
	/**
	 * This method converts a byte array into an object.
	 * 
	 * @param bytes A byte array corresponding to an object
	 * @return An object corresponding to the input byte array
	 */
	public static Serializable fromBytes(byte[] bytes) {
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
	    ObjectInputStream output = null;
		try {
			output = new ObjectInputStream(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    try {
			return (Serializable) output.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Not an object");
		} catch (IOException e) {
			e.printStackTrace();
		} catch(ClassCastException e) {
			System.out.println("Not an object");
		}
	    return null;
	}

	/**
	 * Returns a byte[] from the plain bytes of a file (does not handle decryption)
	 * 
	 * @param file A file to read a byte[] from
	 * @return byte[] version of file
	 */
	protected static byte[] readPlainFile(File file) {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
