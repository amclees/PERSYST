package gui;

import centralprocessor.CommunicationsInterface;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

public class ChooseRootFolder {

    private File selectedDirectory;
    
    private Boolean directorySelected = false;
	
	private Stage pstage;
	private ArrayList<String> pears;

	private CommunicationsInterface comint;

	// takes in communicationsinterface to call functions later
	public ChooseRootFolder(CommunicationsInterface comint) {
		this.comint = comint;
	}

	public void start(Stage primaryStage) {
		this.pstage = primaryStage;
		comint.getRootFolder();
		chooseRoot(primaryStage);
	}

	public Stage getStage() {
		return this.pstage;
	}

	public File getSelectedDirectory(){
		return selectedDirectory;
	}
	
	public Boolean isFileSelected(){
		return directorySelected;
	}
	
    private void chooseRoot(Stage primaryStage) {
        File userDirectory;
        File persystRoot = comint.getRootFolder();
        
    	if(persystRoot != null || persystRoot.isDirectory()){
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
        File selectedFile = dirChooser.showDialog(primaryStage);

        selectedDirectory = selectedFile;
        comint.setRootFolder(selectedFile);
        
//      FileChooser fileChooser = new FileChooser();
//      fileChooser.setTitle("Open Resource File");
//      fileChooser.getExtensionFilters().addAll(
//      new ExtensionFilter("Text Files", "*.txt"),
//      new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
//      new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
//      new ExtensionFilter("All Files", "*.*"));
//		File selectedFile = fileChooser.showOpenDialog(primaryStage);

    }
}
