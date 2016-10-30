package centralprocessor;

import java.io.File;
import java.io.Serializable;

import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import userprofile.UserProfile;
import filemanager.PersistentStorage;
import javafx.application.Application;
import javafx.stage.Stage;
import gui.*;

/**
 * Implementation of communications interface
 * 
 * @author Andrew
 *
 */
public class CommunicationsInterface extends Application implements ICommunicationsInterface {
	PersystGUI pgui;
	ConfigGUI cgui;
	LoadScreen lscreen;
	LoginGUI lgui;
	NetworkViewGUI nvgui;
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.cgui = new ConfigGUI();
		this.cgui.start(new Stage());
		
		this.lscreen = new LoadScreen();
		this.lscreen.start(new Stage());
		
		this.lgui = new LoginGUI();
		this.lgui.start(new Stage());
		
		this.nvgui = new NetworkViewGUI();
		this.nvgui.start(new Stage());
		
		this.pgui = new PersystGUI();
		this.pgui.start(primaryStage);
		this.pgui.getStage().show();
		this.nvgui.getStage().show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public boolean login(String username, String password) {
		File file = new File(this.getRootFolder().getAbsolutePath()
				+ "/.persystconf");
		PersistentStorage storage = new PersistentStorage(this.getPassword(),
				"Default PIN");
		byte[] config = storage.read(file);
		PERSYSTSession.usr = new UserProfile(username, password);
		//Config configObj = (Config)config;
		//usr.setConfig(configObj)
		
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
		return PERSYSTSession.usr.getConfiguration(configuration);
	}

	@Override
	public void setConfiguration(String configuration, Serializable value) {
		PERSYSTSession.usr.setConfiguration(configuration, value);
	}

	@Override
	public byte[] getConfigurationsData() {
		return PERSYSTSession.usr.getConfigData();
	}

	@Override
	public String getPassword() {
		return PERSYSTSession.usr.getPassword();
	}

	@Override
	public String getUsername() {
		return PERSYSTSession.usr.getUsername();
	}

	@Override
	public String getPIN() {
		return PERSYSTSession.usr.getPIN();
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
