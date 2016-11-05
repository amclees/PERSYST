package centralprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;

import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IFileConfiguration;
import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.core.security.UserCredentials;
import org.hive2hive.processframework.exceptions.InvalidProcessStateException;
import org.hive2hive.processframework.exceptions.ProcessExecutionException;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import configurations.PersystConfiguration;
import filemanager.ConsoleFileAgent;
import filemanager.FileUtils;
import filemanager.PersistentStorage;
import gui.ConfigGUI;
import gui.ConnectGUI;
import gui.LoadScreen;
import gui.LoginGUI;
import gui.PersystGUI;
import javafx.application.Application;
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

		
		
		File rootFolder = new File(System.getProperty("user.home") + "/Desktop");
		
		try {
			File initFile = new File("root.conf");
			BufferedReader rd = new BufferedReader(new FileReader(initFile));
			String path = rd.readLine();
			rootFolder = new File(path);
		} catch(Exception e) {}
		
		// default root folder desktop
		PERSYSTSession.rootFolder = rootFolder;
		
		
		

		PERSYSTSession.config = new PersystConfiguration(fconfig);
		//PERSYSTSession.config.rootFolder = PERSYSTSession.rootFolder;

		// gui setup
		this.lgui = new LoginGUI(this);
		this.lgui.start(new Stage());

		this.pgui = new PersystGUI(this);
		this.pgui.start(primaryStage);

		this.pgui.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				System.out.println("Disconnecting node");
				if (conNode.node.isConnected())
					conNode.node.disconnect();
				System.out.println("Saving config and shutting down");
				sendShutdownSignal();
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

	public void uploadOwnFiles() {
		for (File file : FileUtils.getFiles(PERSYSTSession.rootFolder)) {
			IProcessComponent<Void> process;
			try {
				process = this.conNode.getNode().getFileManager().createAddProcess(file);
				process.execute();
			} catch (NoPeerConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSessionException e) {
				System.out.println("No Session " + e.toString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidProcessStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProcessExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	@Override
	/**
	 * This method initializes the User Profile object for use by other modules
	 * then signals to verify them
	 */
	public boolean login(String username, String password) {
		try {
			System.out.println("The pre-login root folder is " + PERSYSTSession.rootFolder);
			File file = new File(PERSYSTSession.rootFolder + "/.persystconf");
			if (file.exists()) {
				PersistentStorage storage = new PersistentStorage(password, "Default PIN");
				byte[] config = storage.read(file);
				PersystConfiguration configObj;
				if (config.length == 0) {
					System.out.println("Could not read config data");
					configObj = PERSYSTSession.config;
				}
				else {
					configObj = (PersystConfiguration) (PersistentStorage.fromBytes(config));
					if(configObj == null) System.out.println("Failed to read config obj from data");
					System.out.println("The max file size conf is " + configObj.getFileConfig().getMaxFileSize());
					conNode.Disconnect();
					conNode.buildNode(configObj.getFileConfig());
					//System.out.println(this.netconfig.getNodeID());
					//NetworkConfiguration nconf = NetworkConfiguration.createInitial();
					conNode.Connect(this.netconfig);
				}
				PERSYSTSession.usr = new UserProfile(username, password, configObj);
				this.conNode.getNode().getUserManager().createLoginProcess(
						new UserCredentials(username, password, "Default PIN"),
						new ConsoleFileAgent(PERSYSTSession.rootFolder)).execute();
				System.out.println("The post-login root folder is " + PERSYSTSession.rootFolder);
				this.uploadOwnFiles();
				return true;
			} else {
				
				PERSYSTSession.usr = new UserProfile(username, password, PERSYSTSession.config);
				this.conNode.getNode().getUserManager().createLoginProcess(
						new UserCredentials(username, password, "Default PIN"),
						new ConsoleFileAgent(PERSYSTSession.rootFolder)).execute();
				System.out.println("This is the first login. The post-login root folder is " + PERSYSTSession.rootFolder);
				this.uploadOwnFiles();
				return true;
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
		try {
			File initFile = new File("root.conf");
			BufferedWriter out = new BufferedWriter(new FileWriter(initFile));
			out.write(PERSYSTSession.rootFolder.getAbsolutePath());
			out.close();
			
			PersistentStorage storage = new PersistentStorage(this.getPassword(), this.getPIN());
			File file = new File(this.getRootFolder().getAbsolutePath() + "/.persystconf");
			file.delete();
			file = new File(this.getRootFolder().getAbsolutePath() + "/.persystconf");
			storage.store(this.getConfigurationsData(), file);
		} catch(Exception e) {
			System.out.println("Failed to save config due to " + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * This method updates the current configurations from the file system
	 */
	public void updateConfigurations() {
		try {
			PersistentStorage storage = new PersistentStorage(this.getPassword(), this.getPIN());
			byte[] file = storage.read(new File(this.getRootFolder().getAbsolutePath() + "/.persystconf"));
			if (file.length == 0)
				return;
			PersystConfiguration configObj = (PersystConfiguration) (PersistentStorage.fromBytes(file));
			PERSYSTSession.usr.setConfigurations(configObj);
		} catch(NullPointerException e) {
			//This means the user hasn't logged in
			return;
		}
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
		this.saveConfigurations();
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
	public Object fromBytes(byte[] data) {
		return PersistentStorage.fromBytes(data);
	}

	@Override
	public byte[] toBytes(Serializable object) {
		return PersistentStorage.toBytes(object);
	}

	@Override
	public boolean setRootFolder(File rootFolder) {
		System.out.println("Trying to set root folder to " + rootFolder.toString());
		if(!rootFolder.isDirectory()) return false;
		try {
			// this.rootFolder = rootFolder;
			PERSYSTSession.rootFolder = rootFolder;
			//PERSYSTSession.config.rootFolder = rootFolder;
			this.setConfiguration("rootfolder", rootFolder);
			System.out.println("Set root folder to " + rootFolder.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public File getRootFolder() {
		return PERSYSTSession.rootFolder;
		// return (File) this.getConfiguration("rootfolder");
	}
}
