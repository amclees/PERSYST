package centralprocessor;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.processframework.interfaces.IProcessComponent;

/**
 * IMPORTANT: If you need to modify this once it is in GitHub, do so in the
 * Github editor
 * 
 * This is the interface for the Communication Interface submodule. This should
 * contain all public methods for communication between modules.
 * 
 * TODO Write a concrete implementation TODO Write a static register for this
 */
public interface ICommunicationsInterface {
	
	public boolean login(String username, String password);
	
	public void saveConfigurations();

	public void updateConfigurations();

	public void sendShutdownSignal();

	public Serializable getConfiguration(String configuration);

	public void setConfiguration(String configuration, Serializable value);

	public byte[] getConfigurationsData();

	public String getPassword();

	public String getUsername();

	public String getPIN();

	/**
	 * This method is needed if we want to wrap download processes. It needs the
	 * IFileManager (H2H) if this interface does not have a field for it. If we
	 * use this, we need to modify the wrapper in FileEventListener
	 */
	public IProcessComponent<Void> createDownloadProcess(File file);

	public IProcessComponent<Void> createDownloadProcess(File file,
			IFileManager filemanager);

	/**
	 * This method sends data to the File Manaager to be encrypted and stored
	 *
	 * @param data
	 *            Data to be stored
	 * @param location
	 *            Location to store the data
	 */
	public void storeData(byte[] data, File location);

	/**
	 * This method sends a file path to the File Manager to be decrypted and
	 * read
	 *
	 * @param location
	 *            Location to read
	 * @return Data read, or empty if the file is not stored using
	 *         PersistentStorage
	 */
	public byte[] readData(File location);

	/**
	 * These method reads an object from a byte array.
	 *
	 * @param data
	 *            The byte array containing the object
	 * @return The object from the data. Null if the data is not a valid object.
	 */
	public Serializable fromBytes(byte[] data);

	/**
	 * These method creates a byte array from an object.
	 *
	 * @param object
	 *            A serializable object to be converted to bytes
	 * @return The byte array corresponding to the object
	 */
	public byte[] toBytes(Serializable object);

	/*
	 * 
	 * GUI SECTION
	 */

	/**
	 * These method creates and sets the root folder.
	 *
	 * @param File
	 *            with with the path of the root folder
	 * @return true if the File is a directory or the directory was created
	 */
	public boolean setRootFolder(File rootFolder);

	/**
	 * These method returns the root folder as a File.
	 *
	 * @return the root folder as a File
	 */
	public File getRootFolder();

}
