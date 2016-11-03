package gui;

import centralprocessor.CommunicationsInterface;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import centralprocessor.CommunicationsInterface;
import centralprocessor.PERSYSTSession;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConfigGUI {
	private Stage pstage;

	private CommunicationsInterface comint;

	// takes in communicationsinterface to call functions later
	public ConfigGUI(CommunicationsInterface comint) {
		this.comint = comint;
	}

	public void start(Stage primaryStage) {
		pstage = primaryStage;
		this.pstage.setTitle("Settings");
		VBox root = new VBox(20);
		populateRoot(root, this.pstage);

		Scene scene = new Scene(root, 600, 250);
		this.pstage.setScene(scene);
		pstage.sizeToScene();
		// this.pstage.show();
	}

	public Stage getStage() {
		return this.pstage;
	}
    	Text passHint = new Text("");
    	passHint.setFont(new Font(20));
    	passHint.setFill(Color.RED);

    	HBox bHbox = new HBox();
    	bHbox.setAlignment(Pos.CENTER);
    	Button save = new Button("save");
    	save.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override 
    	    public void handle(ActionEvent e) {

    	    }
    	});
    	
    	Button cancel = new Button("cancel");
    	cancel.setOnAction(new EventHandler<ActionEvent>() {
    		@Override 
    	    public void handle(ActionEvent e) {
    			getStage().close();
    		}
    	});
    	bHbox.getChildren().addAll(save, cancel);
    	root.getChildren().addAll(title, userHbox, passHbox, passHint, bHbox);
    }
    
    
    private boolean login(String username, String password){
    	//TODO 
    	// Pass the username and password to the correct method
    	return false;
    }
    
    private boolean validateUsername(String s){
    	return s.length() > 3;
    }
    
    private boolean validatePassword(String s){
    	return s.length() > 3;
    }
	private void populateRoot(VBox root, Stage stage) {
		Pane spacer = new Pane();

		// Root folder
		HBox rootBox = new HBox();
		Label rootLabel = new Label("RootFolder: " + comint.getRootFolder().toString());
		Button rootBtn = new Button("Choose Root Folder");

		rootBox.getChildren().addAll(rootLabel, spacer, rootBtn);
		rootBox.setHgrow(spacer, Priority.ALWAYS);

		// Port
		HBox portBox = new HBox();
		Label portLable = new Label("Port: ");
		TextField portField = new TextField();
		portField.setPromptText("The current port goes here");
		// portField.setPromptText(PERSYSTSession.usr.getConfiguration("port").toString());

		spacer = new Pane();
		portBox.getChildren().addAll(portLable, spacer, portField);
		portBox.setHgrow(spacer, Priority.ALWAYS);

		// maxfilesize
		HBox maxSizeBox = new HBox();
		Label maxSizeLabel = new Label("Max file size: ");
		TextField maxSizeField = new TextField();
		maxSizeField.setPromptText("The current size goes here");
		// portField.setPromptText(PERSYSTSession.usr.getConfiguration("maxfilesize").toString());
		maxSizeField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d+")) {
					int value = Integer.parseInt(newValue);
				} else {
					maxSizeField.setText(oldValue);
				}
			}
		});

		spacer = new Pane();
		maxSizeBox.getChildren().addAll(maxSizeLabel, spacer, maxSizeField);
		maxSizeBox.setHgrow(spacer, Priority.ALWAYS);

		HBox btnBox = new HBox(20);
		
		Button saveBtn = new Button("Save");
		Button cancelBtn = new Button("Cancel");

		cancelBtn.setOnAction((event) -> {
			pstage.close();
		});

		saveBtn.setOnAction((event) -> {
			// Save the configs
			PERSYSTSession.usr.setConfiguration("maxfilesize", maxSizeField.getText());
			PERSYSTSession.usr.setConfiguration("port", portField.getText());
		});

		btnBox.getChildren().addAll(saveBtn, cancelBtn);
		btnBox.setAlignment(Pos.BOTTOM_RIGHT);
		
		root.getChildren().addAll(rootBox, portBox, maxSizeBox, btnBox);
	}
}
