package gui;

import centralprocessor.CommunicationsInterface;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoadScreen extends Application {
		private Stage pstage;
		
		private CommunicationsInterface comint;

	    //takes in communicationsinterface to call functions later
	    public LoadScreen(CommunicationsInterface comint){
	    	this.comint = comint;
	    }
		@Override
	    public void start(Stage primaryStage) {
			this.pstage = primaryStage;
			this.pstage.setTitle("Loading");
	        VBox root = new VBox(20);

	        Scene scene = new Scene(root, 600, 250);
	        this.pstage.setScene(scene);
//	        this.pstage.show();
	    }
		
	    public Stage getStage(){
	    	return this.pstage;
	    }
	    
	    public void setText(String text){
	    	this.pstage.setTitle(text);
	    }
//	    public static void main(String[] args) {
//	        launch(args);
//	    }
	    
	    
}
