package gui;

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
import centralprocessor.PERSYSTSession;

public class LoginGUI {
	private Stage pstage;

	private CommunicationsInterface comint;

	// takes in communicationsinterface to call functions later
	public LoginGUI(CommunicationsInterface comint) {
		this.comint = comint;
	}

	public Stage getStage() {
		return this.pstage;
	}

	private void populateRoot(VBox root, Stage stage) {
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

		loginBtn.setOnAction((event) -> {
			String username = userField.getText();
			String password = passField.getText();
			if (validateUsername(username) && validatePassword(password)) {

				if (PERSYSTSession.comm.login(username, password)) {
					// Have Core start close this window and start PERSYST Gui
					stage.close();
				} else {
					passHint.setText("couldn't connect");
				}
			} else {
				if (!validateUsername(username)) {
					passHint.setText("Username is too short.");
				} else {
					passHint.setText("Password is too short");
				}
			}
		});

		root.getChildren().addAll(title, userHbox, passHbox, passHint, loginBtn);
	}

	public void start(Stage primaryStage) {
		this.pstage = primaryStage;
		this.pstage.setTitle("Log In");
		VBox root = new VBox(20);
		populateRoot(root, this.pstage);
	
		Scene scene = new Scene(root, 600, 250);
		this.pstage.setScene(scene);
	}

	private boolean validateUsername(String s) {
		// TODO check if H2H requires additional validation
		return s.length() > 3;
	}

	private boolean validatePassword(String s) {
		// TODO check if H2H requires additional validation
		return s.length() > 3;
	}

}
