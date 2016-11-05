package gui;

import java.io.File;
import java.util.ArrayList;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import centralprocessor.CommunicationsInterface;

public class ChooseRootFolder {

	/**
	 * the file that this class will select
	 */
    private File selectedDirectory;
    
	/**
	 * boolean to check if a file has been selected
	 */
    private Boolean directorySelected = false;
	
	private Stage pstage;
	private ArrayList<String> pears;

	private CommunicationsInterface comint;

	// takes in communicationsinterface to call functions later
	public ChooseRootFolder(CommunicationsInterface comint) {
		this.comint = comint;
	}

	/**
	 * Use this if you want to change the root folder. This class will update
	 * the root folder
	 */
	private void chooseRoot() {
	    File userDirectory;
	    File persystRoot = comint.getRootFolder();
	    
	    // Checks if a root has been selected otherwise it defaults to user.home or C:/
		if(persystRoot != null && persystRoot.isDirectory()){
			userDirectory = persystRoot;
		}
		else{
	        String userDirectoryString = System.getProperty("user.home");
	        userDirectory = new File(userDirectoryString);
	        if (!userDirectory.canRead()) {
	            userDirectory = new File("C:/");
	        }
		}
		
	    DirectoryChooser dirChooser = new DirectoryChooser();
	    dirChooser.setTitle("Choose Root Folder");
	    dirChooser.setInitialDirectory(userDirectory);
	    File selectedFile = dirChooser.showDialog(this.pstage);
	
	    selectedDirectory = selectedFile;
	    
	    // Updates the root folder in  PERSYST
	    if(selectedFile != null)
	    	comint.setRootFolder(selectedFile);
	    
//	    System.out.println("after set root comint: " + comint.getRootFolder().toString());
	}

	public Stage getStage() {
		return this.pstage;
	}

	/**
	 * Getter for the selected file
	 */
	public File getSelectedDirectory(){
		return selectedDirectory;
	}
	
	/**
	 * Returns true if a file has been selected by this class
	 */
	public Boolean isFileSelected(){
		return directorySelected;
	}

	public void start(Stage primaryStage) {
		this.pstage = primaryStage;
		comint.getRootFolder();
		chooseRoot();
	}
}
