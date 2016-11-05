package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import centralprocessor.CommunicationsInterface;

public class ConnectGUI {
	private Stage pstage;
	
	private CommunicationsInterface comint;

    //takes in communicationsinterface to call functions later
    public ConnectGUI(CommunicationsInterface comint){
    	this.comint = comint;
    }
//	@Override
    public void start(Stage primaryStage) {
        this.pstage = primaryStage;
        this.pstage.setTitle("Connect");
        VBox root = new VBox(20);
        populateRoot(root, this.pstage);

        Scene scene = new Scene(root, 600, 250);
        this.pstage.setScene(scene);
    }
	
    public Stage getStage(){
    	return this.pstage;
    }
    
    private void populateRoot(VBox root, Stage stage){
    	
//    	root.set
    	
    	root.setAlignment(Pos.CENTER);
    	
    	Text title = new Text("PERSYST Login");
    	title.setFont(new Font(32));
    	
    	HBox userHbox = new HBox();
    	userHbox.setAlignment(Pos.CENTER);
    	
    	Text userText = new Text("username:\t\t");
    	userText.setFont(new Font(20));
    	
    	TextField userField = new TextField();
    	userHbox.getChildren().addAll(userText, userField);
    	userField.setPromptText("Your username");
    	
    	HBox passHbox = new HBox();
    	passHbox.setAlignment(Pos.CENTER);
    	
    	Text passText = new Text("password:\t\t");
    	passText.setFont(new Font(20));
    	    	
    	PasswordField passField = new PasswordField();
    	passField.setPromptText("Your password");
    	passHbox.getChildren().addAll(passText, passField);
    	

    	root.setAlignment(Pos.CENTER);
    	Text passHint = new Text("");
    	passHint.setFont(new Font(20));
    	passHint.setFill(Color.RED);
    	
    	root.setAlignment(Pos.CENTER);

    	Button loginBtn = new Button("Login");
    	loginBtn.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {

    	    }
    	});
    	
    	root.getChildren().addAll(title, userHbox, passHbox, passHint, loginBtn);
    }
}
