package centralprocessor;

import java.io.File;
import java.io.Serializable;

import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IFileConfiguration;
import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.core.security.UserCredentials;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import configurations.PersystConfiguration;
import filemanager.ConsoleFileAgent;
import filemanager.PersistentStorage;
import gui.ConfigGUI;
import gui.ConnectGUI;
import gui.LoadScreen;
import gui.LoginGUI;
import gui.PersystGUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import networking.Connection;
import userprofile.UserProfile;

/**
 * Implementation of communications interface
 * 
 * @author Andrew, Jonathan Song
 *
 */
public class CommunicationsInterface extends Application implements ICommunicationsInterface {
	public PersystGUI pgui;
	public ConfigGUI cgui;
	public LoadScreen lscreen;
	public LoginGUI lgui;
	// public NetworkViewGUI nvgui;
	public ConnectGUI congui;

	public NetworkConfiguration netconfig;
	public IFileConfiguration fconfig;
	public Connection conNode;

	@Override
	public void start(Stage primaryStage) throws Exception {
		PERSYSTSession.comm = this;
		// prevent app from closing on all windows exit
		// Platform.setImplicitExit(false);
		fconfig = FileConfiguration.createDefault();

		this.conNode = new Connection(fconfig);

		// default root folder desktop
		PERSYSTSession.rootFolder = new File(System.getProperty("user.home") + "/Desktop");

		PERSYSTSession.config = new PersystConfiguration(fconfig);

		// gui setup
		this.lgui = new LoginGUI(this);
		this.lgui.start(new Stage());

		this.pgui = new PersystGUI(this);
		this.pgui.start(primaryStage);

		this.pgui.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				System.out.println("goodbye world");
				// do stuff before exit
				if (conNode.node.isConnected())
					conNode.node.disconnect();
				Platform.exit();
			}
		});

		this.lgui.getStage().initModality(Modality.WINDOW_MODAL);
		this.lgui.getStage().initOwner(this.pgui.getStage());
		// this.cgui = new ConfigGUI(this);
		// this.cgui.start(new Stage());
		// this.cgui.getStage().setOnCloseRequest(value);
		// this.cgui.getStage().setOnCloseRequest(value);

		// this.congui = new ConnectGUI(this);
		// this.congui.start(new Stage());

		this.lscreen = new LoadScreen(this);
		this.lscreen.start(new Stage());

		// this.nvgui = new NetworkViewGUI(this);
		// this.nvgui.start(new Stage());

		// locks pgui while nvgui open
		// this.nvgui.getStage().initModality(Modality.WINDOW_MODAL);
		// this.nvgui.getStage().initOwner(this.pgui.getStage());

		// show initial display after here
		// this.cgui.getStage().show();
		this.pgui.getStage().show();
		this.pgui.getStage().sizeToScene();
		// this.nvgui.getStage().show();
		// this.lscreen.getStage().show();
		// lscreen.setLabelText("Hang in there!");
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	/**
	 * This method initializes the User Profile object for use by other modules
	 * then signals to verify them
	 */
	public boolean login(String username, String password) {
		try {
			File file = new File(this.getRootFolder().getAbsolutePath() + "/.persystconf");
			if (file.exists()) {
				PersistentStorage storage = new PersistentStorage(this.getPassword(), "Default PIN");
				byte[] config = storage.read(file);
				if (config.length == 0)
					return false;
				PersystConfiguration configObj = (PersystConfiguration) (PersistentStorage.fromBytes(config));
				PERSYSTSession.usr = new UserProfile(username, password, configObj);
				this.conNode.getNode().getUserManager().createLoginProcess(
						new UserCredentials(username, password, "Default PIN"),
						new ConsoleFileAgent(PERSYSTSession.rootFolder));
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	/**
	 * This method stores the current configurations in the file system
	 */
	public void saveConfigurations() {
		PersistentStorage storage = new PersistentStorage(this.getPassword(), this.getPIN());
		File file = new File(this.getRootFolder().getAbsolutePath() + "/.persystconf");
		file.delete();
		storage.store(this.getConfigurationsData(), file);
	}

	@Override
	/**
	 * This method updates the current configurations from the file system
	 */
	public void updateConfigurations() {
		PersistentStorage storage = new PersistentStorage(this.getPassword(), this.getPIN());
		byte[] file = storage.read(new File(this.getRootFolder().getAbsolutePath() + "/.persystconf"));
		if (file.length == 0)
			return;
		PersystConfiguration configObj = (PersystConfiguration) (PersistentStorage.fromBytes(file));
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
	public IProcessComponent<Void> createDownloadProcess(File file, IFileManager filemanager) {
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
		PersistentStorage storage = new PersistentStorage(this.getPassword(), this.getPIN());
		storage.store(data, location);
	}

	@Override
	public byte[] readData(File location) {
		PersistentStorage storage = new PersistentStorage(this.getPassword(), this.getPIN());
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
			// this.rootFolder = rootFolder;
			this.setConfiguration("rootfolder", rootFolder);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public File getRootFolder() {
		return PERSYSTSession.rootFolder;
		// return (File) this.getConfiguration("rootfolder");
	}
}
