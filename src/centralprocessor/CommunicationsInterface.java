package centralprocessor;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import filemanager.PersistentStorage;

/**
 * Implementation of communications interface
 * 
 * @author Andrew
 *
 */
public class CommunicationsInterface implements ICommunicationsInterface {

	public CommunicationsInterface() {

	}

	@Override
	public boolean login(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveConfigurations() {
		PersistentStorage storage = new PersistentStorage(this.getPassword(),
				this.getPIN());
		File file = new File(this.getRootFolder().getAbsolutePath()
				+ "/.persystconf");
		file.delete();
		storage.store(this.getConfigurationsData(), file);
	}

	@Override
	public void updateConfigurations() {
		PersistentStorage storage = new PersistentStorage(this.getPassword(),
				this.getPIN());
		byte[] file = storage.read(new File(this.getRootFolder()
				.getAbsolutePath() + "/.persystconf"));
		if (file.length == 0)
			return;
		// Send signal to user profile
	}

	@Override
	public void sendShutdownSignal() {
		this.updateConfigurations();
		this.saveConfigurations();
		System.exit(0);
	}

	@Override
	public Serializable getConfiguration(String configuration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConfiguration(String configuration, Serializable value) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] getConfigurationsData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPIN() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProcessComponent<Void> createDownloadProcess(File file) {
		// Code for this session
		return null;
	}

	@Override
	public IProcessComponent<Void> createDownloadProcess(File file,
			IFileManager filemanager) {
		try {
			return filemanager.createDownloadProcess(file);
		} catch (NoPeerConnectionException e) {
			e.printStackTrace();
		} catch (NoSessionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void storeData(byte[] data, File location) {
		PersistentStorage storage = new PersistentStorage(this.getPassword(),
				this.getPIN());
		storage.store(data, location);
	}

	@Override
	public byte[] readData(File location) {
		PersistentStorage storage = new PersistentStorage(this.getPassword(),
				this.getPIN());
		return storage.read(location);
	}

	@Override
	public Serializable fromBytes(byte[] data) {
		return PersistentStorage.fromBytes(data);
	}

	@Override
	public byte[] toBytes(Serializable object) {
		return PersistentStorage.toBytes(object);
	}

	@Override
	public boolean setRootFolder(File rootFolder) {
		try {
			this.setConfiguration("rootfolder", rootFolder);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public File getRootFolder() {
		return (File) this.getConfiguration("rootfolder");
	}

}
