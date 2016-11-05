package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import centralprocessor.CommunicationsInterface;

public class LoadScreen {
		private Stage pstage;
		private CommunicationsInterface comint;
		
		/**
		 * This label will show text under the loading spinner
		 */
		private Label loadingLabel = new Label("Hello World");

	    //takes in communicationsinterface to call functions later
	    public LoadScreen(CommunicationsInterface comint){
	    	this.comint = comint;
	    }

	    public Stage getStage(){
	    	return this.pstage;
	    }

		/**
		 * Use this function to change the text in this window
		 */
	    public void setLabelText(String text) {
			loadingLabel.setText(text);
		}

		public void setTitle(String text){
	    	this.pstage.setTitle(text);
	    }

		public void start(Stage primaryStage) {
					this.pstage = primaryStage;
					this.pstage.setTitle("Loading");
			        VBox root = new VBox(20);
		
					root.setAlignment(Pos.CENTER);
		
					loadingLabel.setFont(new Font(24));
		
					ProgressIndicator progress = new ProgressIndicator(-1.0);
		
					progress.setMinHeight(200);
					progress.setMinWidth(200);
		
					root.getChildren().addAll(progress, loadingLabel);
		
					Scene scene = new Scene(root, 600, 400);
		
			        this.pstage.setScene(scene);
		//	        this.pstage.show();
			    }
}
