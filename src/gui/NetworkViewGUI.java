package gui;

import java.util.ArrayList;

import centralprocessor.CommunicationsInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NetworkViewGUI {
	private Stage pstage;
	private ArrayList<String> pears;

	private CommunicationsInterface comint;

	// takes in communicationsinterface to call functions later
	public NetworkViewGUI(CommunicationsInterface comint) {
		this.comint = comint;
	}

	// @Override
	public void start(Stage primaryStage) {
		this.pstage = primaryStage;
		this.pstage.setTitle("Available LAN PERSYST");

		ArrayList<String> list = new ArrayList<>();

		list.add("130.182.24.170");
		list.add("130.182.24.172");
		list.add("130.182.24.174");
		list.add("130.182.24.176");
		list.add("130.182.24.178");

		NetworkView root = new NetworkView(list);

		Scene scene = new Scene(root, 600, 250);
		this.pstage.setScene(scene);
	}

	public Stage getStage() {
		return this.pstage;
	}
}