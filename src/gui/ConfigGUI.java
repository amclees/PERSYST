package gui;

import javafx.application.Application;
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
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConfigGUI extends Application {
	private Stage pstage;
	@Override
    public void start(Stage primaryStage) {
		pstage = primaryStage;
		this.pstage.setTitle("Log In");
        VBox root = new VBox(20);
        populateRoot(root, this.pstage);

        Scene scene = new Scene(root, 600, 250);
        this.pstage.setScene(scene);
//        this.pstage.show();
    }
	
    public Stage getStage(){
    	return this.pstage;
    }
//    public static void main(String[] args) {
//        launch(args);
//    }
    
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
    	    	String username = userField.getText();
    	    	String password = passField.getText();
    	    	if(validateUsername(username) && validatePassword(password)){
    	    		
    	    		if(login(username, password)){
        	            stage.close();
    	    		}
    	    		else{
    	    			passHint.setText("couldn't connect");
    	    		}
//    	    		System.out.println("username " + username);
//    	    		System.out.println("password " + password);
    	    	}
    	    	else{
    	    		if ( ! validateUsername(username)){
    	    			passHint.setText("Username is too short.");
    	    		}
    	    		else{
    	    			passHint.setText("Password is too short");
    	    		}
    	    	}
    	    }
    	});
    	
    	root.getChildren().addAll(title, userHbox, passHbox, passHint, loginBtn);
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


}
