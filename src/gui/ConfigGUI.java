package gui;

import centralprocessor.CommunicationsInterface;
import centralprocessor.PERSYSTSession;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConfigGUI {
	private Stage pstage;

	private CommunicationsInterface comint;
	private ChooseRootFolder dialog;
	
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

	private void populateRoot(VBox root, Stage stage) {
		// Spacer add space between the label on the left and the button or text field on the right
		Pane spacer = new Pane();

		// Root folder
		HBox rootBox = new HBox();
		Label rootLabel = new Label("RootFolder: " + comint.getRootFolder().toString());
		Button rootBtn = new Button("Choose Root Folder");
		rootBtn.setOnAction((event) -> {
			dialog = new ChooseRootFolder(comint);
			dialog.start(new Stage());
			rootLabel.setText("RootFolder: " + comint.getRootFolder().toString());
		});

		rootBox.getChildren().addAll(rootLabel, spacer, rootBtn);
		rootBox.setHgrow(spacer, Priority.ALWAYS);

		// Port
		HBox portBox = new HBox();
		Label portLable = new Label("Port: ");
		TextField portField = new TextField();
		portField.setPromptText("The current port goes here");
		
		// Uncomment once PERSYSTSession.usr.getConfiguration() works
		// Check the cast
		
		// portField.setPromptText( (String) PERSYSTSession.usr.getConfiguration("port"));

		spacer = new Pane();
		portBox.getChildren().addAll(portLable, spacer, portField);
		portBox.setHgrow(spacer, Priority.ALWAYS);

		// maxfilesize
		HBox maxSizeBox = new HBox();
		Label maxSizeLabel = new Label("Max file size: ");
		TextField maxSizeField = new TextField();
		maxSizeField.setPromptText("The current size goes here");

		// Uncomment once PERSYSTSession.usr.getConfiguration() works
		// Check the cast
		
		// portField.setPromptText(PERSYSTSession.usr.getConfiguration("maxfilesize").toString());
		maxSizeField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO this block is causing exceptions when a letter is put first
				if (newValue.matches("\\d*")) {
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
			this.pstage.close();
		});

		saveBtn.setOnAction((event) -> {
			// Save the configs
//			 Check if the correct arguments are passed
			if(PERSYSTSession.usr != null){
				PERSYSTSession.usr.setConfiguration("maxfilesize", maxSizeField.getText());
			}
			this.pstage.close();
		});

		btnBox.getChildren().addAll(saveBtn, cancelBtn);
		btnBox.setAlignment(Pos.BOTTOM_RIGHT);
		
		root.getChildren().addAll(rootBox, portBox, maxSizeBox, btnBox);
	}
}