package centralprocessor;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import configurations.PersystConfiguration;
import userprofile.UserProfile;
import filemanager.PersistentStorage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import gui.*;
import javafx.stage.WindowEvent;

/**
 * Implementation of communications interface
 * 
 * @author Andrew, Jonathan Song
 *
 */
public class CommunicationsInterface extends Application implements ICommunicationsInterface {
	PersystGUI pgui;
	ConfigGUI cgui;
	LoadScreen lscreen;
	LoginGUI lgui;
	NetworkViewGUI nvgui;
	ConnectGUI congui;
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//prevent app from closing on all windows exit
		Platform.setImplicitExit(false);
		//gui setup

//		this.cgui = new ConfigGUI(this);
//		this.cgui.start(new Stage());
		//this.cgui.getStage().setOnCloseRequest(value);
//		this.cgui.getStage().setOnCloseRequest(value);
		
//		this.congui = new ConnectGUI(this);
//		this.congui.start(new Stage());
		
		this.lscreen = new LoadScreen(this);
		this.lscreen.start(new Stage());
		
//		this.lgui = new LoginGUI(this);
//		this.lgui.start(new Stage());
		
//		this.nvgui = new NetworkViewGUI(this);
//		this.nvgui.start(new Stage());
		
		this.pgui = new PersystGUI(this);
		this.pgui.start(primaryStage);
		
		this.pgui.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
//				System.out.println("goodbye world");
				//do stuff before exit
				Platform.exit();
			}
		});      
		
		//locks pgui while nvgui open
//		this.nvgui.getStage().initModality(Modality.WINDOW_MODAL);
//		this.nvgui.getStage().initOwner(this.pgui.getStage());
		
		//show initial display after here
//		this.cgui.getStage().show();
		this.pgui.getStage().show();
		this.pgui.getStage().sizeToScene();
//		this.nvgui.getStage().show();
		this.lscreen.getStage().show();
		lscreen.setLabelText("Hang in there!");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	/**
	 * This method initializes the User Profile object for use by other modules then signals to verify them
	 */
	public boolean login(String username, String password) {
		File file = new File(this.getRootFolder().getAbsolutePath()
				+ "/.persystconf");
		if(file.exists()) {
			PersistentStorage storage = new PersistentStorage(this.getPassword(),
					"Default PIN");
			byte[] config = storage.read(file);
			if(config.length == 0) return false;
			PersystConfiguration configObj = (PersystConfiguration)(PersistentStorage.fromBytes(config));
			PERSYSTSession.usr = new UserProfile(username, password, configObj);
			//Signal to verify with the network
			return true;
		} else {
			return false;
		}
	}

	@Override
	/**
	 * This method stores the current configurations in the file system
	 */
	public void saveConfigurations() {
		PersistentStorage storage = new PersistentStorage(this.getPassword(),
				this.getPIN());
		File file = new File(this.getRootFolder().getAbsolutePath()
				+ "/.persystconf");
		file.delete();
		storage.store(this.getConfigurationsData(), file);
	}

	@Override
	/**
	 * This method updates the current configurations from the file system
	 */
	public void updateConfigurations() {
		PersistentStorage storage = new PersistentStorage(this.getPassword(),
				this.getPIN());
		byte[] file = storage.read(new File(this.getRootFolder()
				.getAbsolutePath() + "/.persystconf"));
		if (file.length == 0)
			return;
		PersystConfiguration configObj = (PersystConfiguration)(PersistentStorage.fromBytes(file));
		PERSYSTSession.usr.setConfigurations(configObj);
	}

	@Override
	/**
	 * This method ends the program after saving the configurations
	 */
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
		return new File("C:\\Users\\Jpox\\Desktop\\Delete Me");
//		return (File) this.getConfiguration("rootfolder");
	}
}
